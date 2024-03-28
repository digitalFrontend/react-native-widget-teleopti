package ru.nasvyazi.widget.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ScheduleDataObjectDB.class}, version = 1, exportSchema = false)
public abstract class ScheduleSecondDataObjectDBDatabase extends RoomDatabase {

  public abstract ScheduleDataObjectDBDao scheduleDataObjectDBDao();
}