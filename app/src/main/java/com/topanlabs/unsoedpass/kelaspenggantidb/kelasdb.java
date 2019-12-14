package com.topanlabs.unsoedpass.kelaspenggantidb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {kelaspengganti.class}, version = 1)
public abstract class  kelasdb extends RoomDatabase {

    public abstract kelaspengDAO kelaspengDAO();

    private static volatile kelasdb INSTANCE;

    static kelasdb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (kelasdb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            kelasdb.class, "kelas_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}