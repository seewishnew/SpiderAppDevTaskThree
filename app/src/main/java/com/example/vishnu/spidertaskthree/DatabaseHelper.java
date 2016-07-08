package com.example.vishnu.spidertaskthree;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by vishnu on 7/7/16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    public static final String DATBASE_NAME = "movies.db";
    public static final int VERSION = 4;

    public static final String TABLE_NAME = "movie";

    private Dao<Movie, Integer> movieDao = null;
    private RuntimeExceptionDao<Movie, Integer> runtimeExceptionDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATBASE_NAME, null, VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Movie.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

        try {
            TableUtils.dropTable(connectionSource, Movie.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Dao<Movie, Integer> getMovieDao() throws SQLException {

        if(movieDao == null){

            movieDao = getDao(Movie.class);

        }

        return movieDao;

    }

    public RuntimeExceptionDao getExceptionDao(){
        if(runtimeExceptionDao == null){
            runtimeExceptionDao = getRuntimeExceptionDao(Movie.class);
        }

        return runtimeExceptionDao;
    }
}
