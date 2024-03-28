package ru.nasvyazi.widget.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.util.List;

import ru.nasvyazi.widget.DBInsertCallback;

public class DayOffInfoDBRepository {

  private String DB_NAME = "db_day_off_info";

  private DayOffInfoDBDatabase dayOffInfoDBDatabase;
  public DayOffInfoDBRepository(Context context) {
    dayOffInfoDBDatabase = Room.databaseBuilder(context, DayOffInfoDBDatabase.class, DB_NAME).build();

  }

  public void deleteAllTables(){
    dayOffInfoDBDatabase.clearAllTables();
  }

  public void insert(String title,
                      String subtitle,
                      int parentId) {

    DayOffInfoDB item = new DayOffInfoDB();
    item.setTitle(title);
    item.setSubtitle(subtitle);
    item.setParentId(parentId);
    

    insert(item);
  }

  public void insert(final DayOffInfoDB dayOffInfoDB) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        dayOffInfoDBDatabase.dayOffInfoDBDao().insert(dayOffInfoDB);
        return null;
      }
    }.execute();
  }

  public void insert(final DayOffInfoDB item, DBInsertCallback callback) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        callback.onInsert(dayOffInfoDBDatabase.dayOffInfoDBDao().insert(item));
        return null;
      }
    }.execute();
  }

  public void update(final DayOffInfoDB item) {

    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        dayOffInfoDBDatabase.dayOffInfoDBDao().update(item);
        return null;
      }
    }.execute();
  }

  public void delete(final int id) {
    final LiveData<DayOffInfoDB> item = get(id);
    if(item != null) {
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
          dayOffInfoDBDatabase.dayOffInfoDBDao().delete(item.getValue());
          return null;
        }
      }.execute();
    }
  }

  public void delete(final DayOffInfoDB item) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        dayOffInfoDBDatabase.dayOffInfoDBDao().delete(item);
        return null;
      }
    }.execute();
  }


  public void clearDB() {
    dayOffInfoDBDatabase.dayOffInfoDBDao().clearDB();
  }

  public LiveData<DayOffInfoDB> get(int id) {
    return dayOffInfoDBDatabase.dayOffInfoDBDao().get(id);
  }

  public LiveData<DayOffInfoDB> getByParentId(int id) {
    return dayOffInfoDBDatabase.dayOffInfoDBDao().getByParentId(id);
  }

  public LiveData<List<DayOffInfoDB>> getAll() {
    return dayOffInfoDBDatabase.dayOffInfoDBDao().fetchAll();
  }
}