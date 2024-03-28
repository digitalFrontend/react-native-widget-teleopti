package ru.nasvyazi.widget.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WidgetDayInfoDB.class}, version = 1, exportSchema = false)
public abstract class WidgetDayInfoDBDatabase extends RoomDatabase {

  public abstract WidgetDayInfoDBDao widgetDayInfoDBDao();
}