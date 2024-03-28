package ru.nasvyazi.widget.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DayOffInfoDB.class}, version = 1, exportSchema = false)
public abstract class DayOffInfoDBDatabase extends RoomDatabase {

  public abstract DayOffInfoDBDao dayOffInfoDBDao();
}