package com.example.vishnu.spidertaskthree;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.hardware.Camera;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vishnu on 6/7/16.
 */
public class MovieAdapter extends BaseAdapter {

    private Context context;
    private List<Movie> movies;


    public MovieAdapter(Context context, List<Movie> objects) {
        this.context = context;
        this.movies = objects;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View converterView, ViewGroup parent){

        Movie movie = movies.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );

        View view = inflater.inflate(R.layout.custom_gridview_layout, null);

        TextView title = (TextView) view.findViewById(R.id.title);

        title.setText(movie.getTitle());

        TextView genre = (TextView) view.findViewById(R.id.genre);

        genre.setText("Genre: " + movie.getGenre());

        TextView ratings = (TextView) view.findViewById(R.id.ratings);

        ratings.setText("Ratings: " + movie.getImdbRating());

        ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        Picasso.with(context)
                .load(movie.getPoster())
                .placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_default_poster)
                .into(thumbnail);

        return view;

    }
}
