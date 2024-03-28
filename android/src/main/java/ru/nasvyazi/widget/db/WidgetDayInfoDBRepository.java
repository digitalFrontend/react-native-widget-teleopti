package ru.nasvyazi.widget.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.util.List;

import ru.nasvyazi.widget.DBInsertCallback;

public class WidgetDayInfoDBRepository {

  private String DB_NAME = "db_widget_day_info";

  private WidgetDayInfoDBDatabase widgetDayInfoDBDatabase;
  public WidgetDayInfoDBRepository(Context context) {
    widgetDayInfoDBDatabase = Room.databaseBuilder(context, WidgetDayInfoDBDatabase.class, DB_NAME).build();

  }

  public void deleteAllTables(){
    widgetDayInfoDBDatabase.clearAllTables();
  }

  public void insert(String dayDate,
                      String dayDuration,
                      boolean twoDaysWorkDay,
                      int conflictEventIndex) {

    WidgetDayInfoDB item = new WidgetDayInfoDB();
    item.setConflictEventIndex(conflictEventIndex);
    item.setTwoDaysWorkDay(twoDaysWorkDay);
    item.setDayDate(dayDate);
    item.setDayDuration(dayDuration);
    

    insert(item);
  }

  public void insert(final WidgetDayInfoDB widgetDayInfo) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        widgetDayInfoDBDatabase.widgetDayInfoDBDao().insert(widgetDayInfo);
        return null;
      }
    }.execute();
  }

  public void insert(final WidgetDayInfoDB item, DBInsertCallback callback) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        callback.onInsert(widgetDayInfoDBDatabase.widgetDayInfoDBDao().insert(item));
        return null;
      }
    }.execute();
  }

  public void update(final WidgetDayInfoDB item) {

    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        widgetDayInfoDBDatabase.widgetDayInfoDBDao().update(item);
        return null;
      }
    }.execute();
  }

  public void delete(final int id) {
    final LiveData<WidgetDayInfoDB> item = get(id);
    if(item != null) {
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
          widgetDayInfoDBDatabase.widgetDayInfoDBDao().delete(item.getValue());
          return null;
        }
      }.execute();
    }
  }

  public void delete(final WidgetDayInfoDB item) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        widgetDayInfoDBDatabase.widgetDayInfoDBDao().delete(item);
        return null;
      }
    }.execute();
  }


  public void clearDB() {
    widgetDayInfoDBDatabase.widgetDayInfoDBDao().clearDB();
  }

  public LiveData<WidgetDayInfoDB> get(int id) {
    return widgetDayInfoDBDatabase.widgetDayInfoDBDao().get(id);
  }

  public LiveData<List<WidgetDayInfoDB>> getAll() {
    return widgetDayInfoDBDatabase.widgetDayInfoDBDao().fetchAll();
  }
}