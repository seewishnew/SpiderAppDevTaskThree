package com.example.vishnu.spidertaskthree;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int INTERNET_PERMISSION_REQUEST_CODE = 1;
    public static final String LOG_TAG = "MainActivity";
    public static final String TITLE = "Title";
    public static final int REQUEST_CODE_SEARCH = 1;
    public static final int REQUEST_CODE_ADD = 2;
    public static final int REQUEST_CODE_DETAILS = 3;
    public static final String ID = "ID";
    private boolean isSearchOpen = false;

    private boolean permissionGiven = true;
    public static final String permissions[] = {
            Manifest.permission.INTERNET
    };

    public AutoCompleteTextView editSearch;
    public List<Movie> movies;
    GridView gridView;

    DatabaseHelper helper;
    RuntimeExceptionDao<Movie, Integer> runtimeExceptionDao;

    int movieBufPosition;
    Movie movieBuf;

    public static List<String> titles;

    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        runtimeExceptionDao = helper.getExceptionDao();

        gridView = (GridView) findViewById(R.id.gridView);

        registerForContextMenu(gridView);

        checkPermissions();

        if(permissionGiven){

            Log.d(LOG_TAG, "Permission was given");

            movies = new ArrayList<>();

            refreshDisplay();

            titles = getSuggestions();

        }


    }

    public List<String> getSuggestions(){

        List<String> suggestions = new ArrayList<>();

        GenericRawResults<String> rawResults = runtimeExceptionDao.queryRaw(
                "SELECT title FROM " + DatabaseHelper.TABLE_NAME,
                new RawRowMapper<String>() {
                    @Override
                    public String mapRow(String[] strings, String[] strings1) throws SQLException {

                        return strings1[0];
                    }
                }
        );

        try {
            suggestions = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suggestions;

    }

    private void refreshDisplay() {

        movies = runtimeExceptionDao.queryForAll();

        if(movies.size()>0)
        {
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(new MovieAdapter(this, movies));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    viewDetails(position);
                }
            });
        }

        else
        {
            Toast.makeText(this, "Database Empty", Toast.LENGTH_SHORT).show();
            gridView.setVisibility(View.GONE);
        }
    }

    private void viewDetails(int position) {
        Intent intent = new Intent(
                MainActivity.this,
                Details.class
        );

        intent.putExtra(ID, movies.get(position).getId());

        startActivityForResult(intent, REQUEST_CODE_DETAILS);
    }

    private void checkPermissions() {
        //if(Build.VERSION.SDK_INT>=23)
        {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                    PackageManager.PERMISSION_GRANTED) {

                permissionGiven = false;

                ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        INTERNET_PERMISSION_REQUEST_CODE
                );

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == INTERNET_PERMISSION_REQUEST_CODE){
            if(grantResults.length>0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                permissionGiven = false;



                    new AlertDialog.Builder(this)
                            .setTitle("Allow app to use internet?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("You need to allow this app to access internet" +
                                    " to use this app")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    exitApp();
                                }
                            })
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(
                                            MainActivity.this,
                                            permissions,
                                            INTERNET_PERMISSION_REQUEST_CODE

                                    );
                                }
                            });



            }

            else
                permissionGiven = true;
        }

    }

    public void exitApp(){
        finish();
        System.exit(0);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId() == R.id.gridView){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String menuOptions[] = getResources().getStringArray(R.array.options);

            movieBufPosition = info.position;
            movieBuf = runtimeExceptionDao.queryForAll().get(info.position);

            for(int i = 0; i<menuOptions.length; i++)
            {
                menu.add(Menu.NONE, i, i, menuOptions[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){

            case 0:
                viewDetails(movieBufPosition);
                break;

            case 1:

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
                                int op = runtimeExceptionDao.deleteById((int) movieBuf.getId());

                                Log.d(LOG_TAG, "Deleted " + movieBuf.getId() +"\nO/P: " +op);

                                refreshDisplay();
                            }
                        })
                        .show();
                break;

            default: break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        item = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

         if (id == R.id.action_search){

            final ActionBar actionBar = getSupportActionBar();
            final InputMethodManager imm = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);

            if(isSearchOpen){

                imm.hideSoftInputFromWindow(
                        editSearch.getWindowToken(),
                        InputMethodManager.HIDE_IMPLICIT_ONLY
                );

                Log.d(LOG_TAG, "hide implicit only");

                item.setIcon(R.mipmap.ic_search);

                actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);

                isSearchOpen = false;

            }

            else
            {

                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setCustomView(R.layout.search_bar);
                actionBar.setDisplayShowTitleEnabled(false);

                editSearch = (AutoCompleteTextView)
                        actionBar.getCustomView().findViewById(R.id.editSearch);

                editSearch.requestFocus();

                imm.showSoftInput(editSearch, InputMethodManager.SHOW_IMPLICIT);

                Log.d(LOG_TAG, "show implicit");

                Point point = new Point();
                getWindowManager().getDefaultDisplay().getSize(point);

                editSearch.setDropDownWidth((int) (point.x * 0.7));

                titles = getSuggestions();

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        R.layout.custom_autocomplete_item,
                        titles
                );

                editSearch.setAdapter(adapter);

                item.setIcon(R.mipmap.ic_close);

                editSearch.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);

                editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if(actionId == EditorInfo.IME_ACTION_SEARCH){

                            String title = editSearch.getText().toString();

                            Intent intent = new Intent(
                                    MainActivity.this,
                                    SearchResults.class);

                            intent.putExtra(TITLE, title);

                            imm.hideSoftInputFromWindow(
                                    editSearch.getWindowToken(),
                                    InputMethodManager.HIDE_IMPLICIT_ONLY);
                            actionBar.setDisplayShowCustomEnabled(false);
                            actionBar.setDisplayShowTitleEnabled(true);

                            item.setIcon(R.mipmap.ic_search);

                            isSearchOpen = false;

                            startActivityForResult(intent, REQUEST_CODE_SEARCH);

                            return true;
                        }

                        return false;
                    }
                });


                isSearchOpen = true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(isSearchOpen){

            Log.d(LOG_TAG, "Inside onBackPressed");

            ActionBar actionBar = getSupportActionBar();
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);

            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);

            imm.hideSoftInputFromWindow(
                    editSearch.getWindowToken(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY
            );

            isSearchOpen = false;

            item.setIcon(R.mipmap.ic_search);
        }

        else
            super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(helper!=null){
            OpenHelperManager.releaseHelper();
        }

        helper=null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_SEARCH && resultCode == RESULT_OK){
            refreshDisplay();
            titles = getSuggestions();
            if(data!=null){

                if(data.getBooleanExtra(SearchResults.DELETED, false)){
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                }

                else if(data.getBooleanExtra(SearchResults.ADDED, false)){
                    Toast.makeText(this, "Added to db", Toast.LENGTH_SHORT).show();
                }


            }

        }

        if(requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK){
            refreshDisplay();
            titles = getSuggestions();
            if(data!=null){
                if(data.getBooleanExtra(SearchResults.DELETED, false)){
                    Toast.makeText(this, "Delted", Toast.LENGTH_SHORT).show();
                }
                else if(data.getBooleanExtra(SearchResults.ADDED, false)){
                    Toast.makeText(this,"Added", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
