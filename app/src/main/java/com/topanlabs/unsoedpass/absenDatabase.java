package com.topanlabs.unsoedpass;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {absendb.class}, version = 1)
public abstract class  absenDatabase extends RoomDatabase {

    public abstract absenDAO absenDAO();

    private static volatile absenDatabase INSTANCE;

    static absenDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (matkulDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            absenDatabase.class, "absen_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}