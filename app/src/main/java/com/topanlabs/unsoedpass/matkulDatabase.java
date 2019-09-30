package com.topanlabs.unsoedpass;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {matkuldb.class}, version = 1)
public abstract class  matkulDatabase extends RoomDatabase {

    public abstract matkulDAO matkulDao();

    private static volatile matkulDatabase INSTANCE;

    static matkulDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (matkulDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            matkulDatabase.class, "matkul_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
