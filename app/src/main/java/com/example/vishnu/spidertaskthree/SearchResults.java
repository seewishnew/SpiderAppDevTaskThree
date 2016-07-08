package com.example.vishnu.spidertaskthree;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.RawResultsImpl;
import com.j256.ormlite.stmt.query.Raw;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchResults extends AppCompatActivity {

    public static final String LOG_TAG = "SearchResults";
    public static final long NOT_ADDED = -1;
    public static final String MOVIE = "Movie";
    public static final String DELETED = "Deleted";
    public static final String ADDED = "Added";
    public static final int REQUEST_CODE_DETAILS = 4;

    DatabaseHelper dbHelper;
    ProgressBar progressBar;
    String title;
    GridView gridView;

    int movieBufPosition;
    Movie movieBuf;

    ActionBar actionBar;
    InputMethodManager imm;
    MenuItem item;
    private boolean isDatabaseEmpty = false;
    private boolean isSearchOpen = false;
    private boolean isFoundInDb = true;
    public AutoCompleteTextView editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        actionBar = getSupportActionBar();

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        gridView = (GridView) findViewById(R.id.gridView2);
        gridView.setVisibility(View.GONE);

        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();

        title = intent.getStringExtra(MainActivity.TITLE);
        title = title.trim();
        actionBar.setTitle("Search results for " + title);

        searchDB();

        registerForContextMenu(gridView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if(v.getId() == R.id.gridView2){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            movieBufPosition = info.position;

            Log.d(LOG_TAG, "Inside onCreateContextMenu");

            if(isFoundInDb){

                Log.d(LOG_TAG, "Inside onCreateContextMenu -> if");

                String menuOptions[] = getResources().getStringArray(R.array.options);

                RuntimeExceptionDao<Movie, Integer> runtimeExceptionDao = dbHelper.getExceptionDao();

            movieBuf = runtimeExceptionDao.queryForAll().get(info.position);

            for(int i = 0; i<menuOptions.length; i++)
            {
                menu.add(Menu.NONE, i, i, menuOptions[i]);
            }
        }
            else{
                Log.d(LOG_TAG, "Inside onCreateContextMenu -> else");

                String menuOptions[] = getResources().getStringArray(R.array.options2);

                for(int i =  0; i<menuOptions.length; i++)
                    menu.add(Menu.NONE, i, i, menuOptions[i]);

            }

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case 0:
                if(isFoundInDb){
                    Intent intent = new Intent(
                            SearchResults.this,
                            Details.class
                    );

                    intent.putExtra(MainActivity.ID, movieBuf.getId());

                    startActivity(intent);

                    return true;
                }

                else{

                    String buf = new Gson().toJson(movieBuf);

                    Log.d(LOG_TAG, buf);

                    Intent intent = new Intent(
                            SearchResults.this,
                            Details.class
                    );

                    Log.d(Details.LOG_TAG, "Adding NOT_ADDED");

                    intent.putExtra(MainActivity.ID, NOT_ADDED);
                    intent.putExtra(MOVIE, buf);

                    startActivityForResult(intent, REQUEST_CODE_DETAILS);

                    return true;
                }


            case 1:
                if(isFoundInDb) {
                    new AlertDialog.Builder(this)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete " + movieBuf.getTitle() + " ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    RuntimeExceptionDao<Movie, Integer> runtimeExceptionDao =
                                            dbHelper.getExceptionDao();

                                    int op = runtimeExceptionDao.deleteById((int) movieBuf.getId());

                                    Log.d(LOG_TAG, "Deleted " + movieBuf.getId() + "\nO/P: " + op);

                                    Intent intent = new Intent();
                                    intent.putExtra(DELETED, true);

                                    setResult(RESULT_OK, intent);

                                    finish();

                                }
                            })
                            .show();
                }

                else{
                    addToDb(movieBuf);
                }

                break;

        }


        return super.onContextItemSelected(item);
    }

    private void searchDB() {

        isFoundInDb = false;

        RuntimeExceptionDao<Movie, Integer> runtimeExceptionDao =
                dbHelper.getExceptionDao();

        title = title.trim();

        String query =  "SELECT   title,   year,   rated,   released,   runtime,   genre,   director,   writer,   actors,   plot,   language,   country,   awards,   poster,   metascore,   imdbRating,   imdbVotes,   imdbID,   type,   response FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE title LIKE '%" + title + "%' COLLATE NOCASE";

        GenericRawResults<Movie> rawResults = runtimeExceptionDao.queryRaw(
                query,
                new RawRowMapper<Movie>() {
                    @Override
                    public Movie mapRow(String[] strings, String[] strings1) throws SQLException {
                        Movie movie = new Movie(
                                strings1[0], strings1[1], strings1[2], strings1[3], strings1[4],
                                strings1[5], strings1[6], strings1[7], strings1[8], strings1[9],
                                strings1[10], strings1[11], strings1[12], strings1[13], strings1[14],
                                strings1[15], strings1[16], strings1[17], strings1[18], strings1[19]
                        );

                        return movie;
                    }
                }
        );

        List<Movie> movies = null;
        try {
            movies = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(movies.size()>0)
            isFoundInDb=true;
        else
            isFoundInDb=false;

        refreshDisplay(movies);
    }


    public void refreshDisplay(final List<Movie> movies){

        progressBar.setVisibility(View.GONE);

        if(movies.size()>0)
        {
            Log.d(LOG_TAG, "Title: " + movies.get(0).getTitle() +
                    "\nID: " + movies.get(0).getId());
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(new MovieAdapter(this, movies));

        }

        else
        {
            isFoundInDb = false;
            TextView notFound = (TextView) findViewById(R.id.notFound);
            notFound.setVisibility(View.VISIBLE);
            ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
            progressBar1.setVisibility(View.VISIBLE);

            movieQuery(title);
        }
    }

    private void movieQuery(String title) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OMDbAPI.ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OMDbAPI omDbAPI = retrofit.create(OMDbAPI.class);

        Map<String, String> stringMap = new HashMap<>();

        title = title.trim();

        stringMap.put("t", title);
        stringMap.put("plot", "long");
        stringMap.put("r", "json");

        rx.Observable<Movie> movieObserver = omDbAPI.getMovie(stringMap);
        final ProgressBar[] progressBar1 = new ProgressBar[1];
        final TextView[] notFound = new TextView[1];
        final TextView add;
        add = (TextView) findViewById(R.id.tryAdding);
        notFound[0] = (TextView) findViewById(R.id.notFound);
        notFound[0].setVisibility(View.VISIBLE);
        progressBar1[0] = (ProgressBar) findViewById(R.id.progressBar1);

        Subscriber<Movie> subscriber = new Subscriber<Movie>() {
            @Override
            public void onCompleted() {
                Log.d(LOG_TAG, "Completed");

                progressBar1[0] = (ProgressBar) findViewById(R.id.progressBar1);
                progressBar1[0].setVisibility(View.GONE);

            }

            @Override
            public void onError(Throwable e) {

//                Log.d(LOG_TAG, e.getMessage());

//
                notFound[0].setText("Error, try again");
                notFound[0].setVisibility(View.VISIBLE);
//
                progressBar1[0].setVisibility(View.GONE);

            }

            @Override
            public void onNext(final Movie movie) {

                if(movie.getResponse()!="False" && movie.getTitle() != null){
                    Log.d(LOG_TAG, "Title: " + movie.getTitle() +
                            "\nIMDB Ratings: " + movie.getImdbRating() +
                            "\nResponse: " + movie.getResponse()
                    );

                    movieBuf = movie;

                    final List<Movie> movies = new ArrayList<>();
                    movies.add(movie);

                    add.setText("Try adding new entry: ");
                    add.setVisibility(View.VISIBLE);

                    notFound[0].setVisibility(View.GONE);


                    refreshDisplay(movies);


                }

                else
                {
                    Toast.makeText(SearchResults.this, "Not found online either!", Toast.LENGTH_SHORT).show();
                    add.setVisibility(View.VISIBLE);
                    add.setText("Not found online either");
                }
            }
        };

        movieObserver.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void addToDb(final Movie movie) {
        new AlertDialog.Builder(SearchResults.this)
                .setTitle("Add movie")
                .setMessage("Add movie to database?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RuntimeExceptionDao<Movie, Integer> runtimeExceptionDao =
                                dbHelper.getExceptionDao();

                        runtimeExceptionDao.createIfNotExists(movie);

                        Intent intent = new Intent();
                        intent.putExtra(ADDED, true);
                        setResult(RESULT_OK);
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
        .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        item = menu.findItem(R.id.action_search);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_search){

            if(isSearchOpen){

                imm.hideSoftInputFromWindow(
                        editSearch.getWindowToken(),
                        InputMethodManager.HIDE_IMPLICIT_ONLY
                );

                actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setTitle("Search results for " + title);

                item.setIcon(R.mipmap.ic_search);

                isSearchOpen = false;

            }

            else{

                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setCustomView(R.layout.search_bar);
                actionBar.setDisplayShowTitleEnabled(false);

                editSearch = (AutoCompleteTextView)
                        actionBar.getCustomView().findViewById(R.id.editSearch);

                editSearch.requestFocus();

                imm.showSoftInput(
                        editSearch,
                        InputMethodManager.SHOW_IMPLICIT
                );

                editSearch.setClickable(true);

                editSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        editSearch.requestFocus();

                        imm.showSoftInput(
                                editSearch,
                                InputMethodManager.SHOW_IMPLICIT
                        );
                    }
                });

                Point point = new Point();
                getWindowManager().getDefaultDisplay().getSize(point);

                editSearch.setDropDownWidth((int) (point.x * 0.7));

                editSearch.setText(title);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        SearchResults.this,
                        R.layout.custom_autocomplete_item,
                        MainActivity.titles
                );

                editSearch.setAdapter(adapter);

                editSearch.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);

                editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if(actionId == EditorInfo.IME_ACTION_SEARCH){

                            title = editSearch.getText().toString();

                            title = title.trim();

                            actionBar.setDisplayShowCustomEnabled(false);
                            actionBar.setDisplayShowTitleEnabled(true);
                            actionBar.setTitle("Search results for " + title);

                            item.setIcon(R.mipmap.ic_search);

                            isSearchOpen = false;

                            editSearch.requestFocus();

                            imm.hideSoftInputFromWindow(
                                    editSearch.getWindowToken(),
                                    InputMethodManager.HIDE_IMPLICIT_ONLY
                            );

                            gridView.setVisibility(View.GONE);

                            TextView notFound = (TextView) findViewById(R.id.notFound);
                            notFound.setVisibility(View.GONE);

                            TextView add = (TextView) findViewById(R.id.tryAdding);
                            add.setVisibility(View.GONE);

                            isFoundInDb = false;

                            searchDB();
                        }

                        return false;
                    }
                });

                item.setIcon(R.mipmap.ic_close);

                isSearchOpen = true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK){
            Intent intent = new Intent();

            intent.putExtra(ADDED, true);

            setResult(RESULT_OK, intent);

            finish();
        }

    }


}
