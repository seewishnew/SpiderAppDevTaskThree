package com.example.vishnu.spidertaskthree;

import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by vishnu on 7/7/16.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{Movie.class};

    public static void main(String args[]) throws IOException, SQLException {
//        writeConfigFile(
//                new File("/home/vishnu/AndroidStudioProjects/SpiderTaskThree/app/src/main/res/raw/ormlite_config.txt"),
//                classes);

        System.out.println(DatabaseTableConfig.extractTableName(Movie.class));
    }

}
