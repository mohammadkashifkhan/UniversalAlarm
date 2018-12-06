package com.mdkashif.alarm.utils.db;

import android.content.Context;

import com.mdkashif.alarm.alarm.miscellaneous.DaoAccess;
import com.mdkashif.alarm.alarm.miscellaneous.TimingsModel;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TimingsModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mINSTANCE;

    public abstract DaoAccess userDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (mINSTANCE == null) {
            mINSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "universal-alarm-db")
                            .allowMainThreadQueries()
                            .build();
        }
        return mINSTANCE;
    }

    public static void destroyInstance() {
        mINSTANCE = null;
    }
}
