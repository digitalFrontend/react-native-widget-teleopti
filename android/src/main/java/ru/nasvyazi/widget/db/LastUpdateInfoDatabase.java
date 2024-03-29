package ru.nasvyazi.widget.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LastUpdateInfo.class}, version = 1, exportSchema = false)
public abstract class LastUpdateInfoDatabase extends RoomDatabase {

  public abstract LastUpdateInfoDao lastUpdateInfoDao();
}