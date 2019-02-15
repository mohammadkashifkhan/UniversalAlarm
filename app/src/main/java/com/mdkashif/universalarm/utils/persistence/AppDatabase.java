package com.mdkashif.universalarm.utils.persistence;

import android.content.Context;

import com.mdkashif.universalarm.alarm.miscellaneous.db.DaoAccess;
import com.mdkashif.universalarm.alarm.miscellaneous.db.DaysModel;
import com.mdkashif.universalarm.alarm.miscellaneous.db.TimingsModel;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TimingsModel.class, DaysModel.class}, version = 1)
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