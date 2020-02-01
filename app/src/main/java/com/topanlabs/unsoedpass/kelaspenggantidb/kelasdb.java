package com.topanlabs.unsoedpass.kelaspenggantidb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.topanlabs.unsoedpass.memo.memodao;
import com.topanlabs.unsoedpass.memo.memoent;

@Database(entities = {kelaspengganti.class, memoent.class}, version = 2)
public abstract class  kelasdb extends RoomDatabase {

    public abstract kelaspengDAO kelaspengDAO();
    public abstract memodao memodao();

    private static volatile kelasdb INSTANCE;

    public static kelasdb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (kelasdb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            kelasdb.class, "kelas_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `memo` (`namakul` TEXT NOT NULL, `jam` TEXT,`jenis` TEXT,`catatan` TEXT, `ruangan` TEXT," +
                    "`id` INTEGER NOT NULL,`tanggal` TEXT,PRIMARY KEY(`id`))");
        }
    };

}