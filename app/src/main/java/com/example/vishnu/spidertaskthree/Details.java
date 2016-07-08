package com.example.vishnu.spidertaskthree;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class Details extends AppCompatActivity {

    public static final String LOG_TAG = "Details";
    Movie movie;

    ImageView imageView;

    private boolean isFoundInDB = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        int id = (int) getIntent().getLongExtra(MainActivity.ID, 0);

        if(id == 0){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(id == SearchResults.NOT_ADDED){
            Log.d(LOG_TAG, "Inside searchresults.notadded");
            movie =new Movie();
            Gson gson = new Gson();

            movie = gson.fromJson(
                    getIntent().getStringExtra(SearchResults.MOVIE),
                    Movie.class
            );

            if (movie.getTitle() == null) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                finish();
            }


            Log.d(LOG_TAG, movie.getTitle());

            isFoundInDB = false;
        }

        else {

            Log.d(LOG_TAG, "Inside default");

            DatabaseHelper dbHelper = OpenHelperManager.getHelper(
                    this,
                    DatabaseHelper.class
            );

            RuntimeExceptionDao<Movie, Integer> runtimeExceptionDao = dbHelper.getExceptionDao();

            movie = runtimeExceptionDao.queryForId((int) id);

            if (movie.getTitle() == null) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                finish();
            }

            isFoundInDB = true;
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int heightPixels = displayMetrics.heightPixels;
        int widthPixels = displayMetrics.widthPixels;

        imageView = (ImageView) findViewById(R.id.imageViewDetails);
        imageView.getLayoutParams().height = (int) (heightPixels * 0.3);
        imageView.setScaleType(ImageView.ScaleType.CENTER);

        Picasso.with(this)
                .load(movie.getPoster())
                .error(R.mipmap.ic_default_poster)
                .placeholder(R.mipmap.ic_placeholder)
                .into(imageView);

        TextView title = (TextView) findViewById(R.id.titleDetails);
        TextView year = (TextView) findViewById(R.id.yearDetails);
        TextView released = (TextView) findViewById(R.id.releaseDetails);
        TextView actors = (TextView) findViewById(R.id.actorsDetails);
        TextView director = (TextView) findViewById(R.id.directorDetails);
        TextView writer = (TextView) findViewById(R.id.writerDetails);
        TextView ratings = (TextView) findViewById(R.id.ratingsDetails);
        TextView rated = (TextView) findViewById(R.id.ratedDetails);
        TextView votes = (TextView) findViewById(R.id.votesDetails);
        TextView plot = (TextView) findViewById(R.id.plotDetails);
        TextView type = (TextView) findViewById(R.id.typeDetails);
        TextView genre = (TextView) findViewById(R.id.genreDetails);
        TextView length = (TextView) findViewById(R.id.lengthDetails);
        TextView country = (TextView) findViewById(R.id.countryDetails);
        TextView language = (TextView) findViewById(R.id.languageDetails);
        TextView awards = (TextView) findViewById(R.id.awardsDetails);
        TextView metascore = (TextView) findViewById(R.id.metascoreDetails);


        title.setText("Title: " + movie.getTitle());
        year.setText("Year: " +movie.getYear());
        released.setText("Released: " + movie.getReleased());
        actors.setText("Actors: " + movie.getActors());
        director.setText("Director: " + movie.getDirector());
        writer.setText("Writer: " + movie.getWriter());
        ratings.setText("IMDb Ratings: " + movie.getImdbRating());
        rated.setText("Rated: " + movie.getRated());
        votes.setText("IMDb Votes: " + movie.getImdbVotes());
        plot.setText("Plot: " + movie.getPlot());
        type.setText("Type: " + movie.getType());
        genre.setText("Genre: " + movie.getGenre());
        length.setText("Runtime: " + movie.getRuntime());
        country.setText("County: " + movie.getCountry());
        language.setText("Languages: " + movie.getLanguage());
        awards.setText("Awards: " + movie.getAwards());
        metascore.setText("Metascore: " + movie.getMetascore());



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.menu_details,
                menu
        );

        MenuItem item = menu.findItem(R.id.addOrDelete);

        if(isFoundInDB){
            item.setTitle("Delete");
        }

        else{
            item.setTitle("Add");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.addOrDelete)
        {
            if(isFoundInDB){
                new AlertDialog.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete " + movie.getTitle() + " ?")
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

                                DatabaseHelper dbHelper = OpenHelperManager.getHelper(
                                        Details.this,
                                        DatabaseHelper.class
                                );

                                RuntimeExceptionDao<Movie, Integer> runtimeExceptionDao =
                                        dbHelper.getExceptionDao();

                                int op = runtimeExceptionDao.deleteById((int) movie.getId());

                                Log.d(LOG_TAG, "Deleted " + movie.getId() + "\nO/P: " + op);

                                Intent intent = new Intent();
                                intent.putExtra(SearchResults.DELETED, true);

                                setResult(RESULT_OK, intent);

                                OpenHelperManager.releaseHelper();

                                finish();

                            }
                        })
                        .show();
            }

            else{

                new AlertDialog.Builder(Details.this)
                        .setTitle("Add movie")
                        .setMessage("Add movie to database?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseHelper dbHelper = OpenHelperManager.getHelper(
                                        Details.this,
                                        DatabaseHelper.class
                                );

                                RuntimeExceptionDao<Movie, Integer> runtimeExceptionDao =
                                        dbHelper.getExceptionDao();

                                runtimeExceptionDao.createIfNotExists(movie);

                                Intent intent = new Intent();
                                intent.putExtra(SearchResults.ADDED, true);
                                setResult(RESULT_OK);

                                OpenHelperManager.releaseHelper();

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
        }

        return super.onOptionsItemSelected(item);
    }
}
