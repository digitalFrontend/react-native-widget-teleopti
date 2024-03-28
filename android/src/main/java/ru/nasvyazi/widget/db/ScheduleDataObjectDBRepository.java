package ru.nasvyazi.widget.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.util.List;

import ru.nasvyazi.widget.DBInsertCallback;

public class ScheduleDataObjectDBRepository {

  private String DB_NAME = "db_schedule_data_objects";

  private ScheduleDataObjectDBDatabase scheduleDataObjectDBDatabase;
  public ScheduleDataObjectDBRepository(Context context) {
    scheduleDataObjectDBDatabase = Room.databaseBuilder(context, ScheduleDataObjectDBDatabase.class, DB_NAME).build();

  }

  public void deleteAllTables(){
    scheduleDataObjectDBDatabase.clearAllTables();
  }

  public void insert(String hexContainer,
                     String hexDivider,
                     String eventDuration,
                     String eventTimeStart,
                     String eventTimeEnd,
                     String description,
                     int parentId) {

    ScheduleDataObjectDB item = new ScheduleDataObjectDB();
    item.setParentId(parentId);
    item.setHexContainer(hexContainer);
    item.setHexDivider(hexDivider);
    item.setDescription(description);
    item.setEventDuration(eventDuration);
    item.setEventTimeEnd(eventTimeEnd);
    item.setEventTimeStart(eventTimeStart);
    

    insert(item);
  }

  public void insert(final ScheduleDataObjectDB scheduleDataObjectDB) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        scheduleDataObjectDBDatabase.scheduleDataObjectDBDao().insert(scheduleDataObjectDB);
        return null;
      }
    }.execute();
  }

  public void insert(final ScheduleDataObjectDB item, DBInsertCallback callback) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        callback.onInsert(scheduleDataObjectDBDatabase.scheduleDataObjectDBDao().insert(item));
        return null;
      }
    }.execute();
  }

  public void update(final ScheduleDataObjectDB item) {

    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        scheduleDataObjectDBDatabase.scheduleDataObjectDBDao().update(item);
        return null;
      }
    }.execute();
  }

  public void delete(final int id) {
    final LiveData<ScheduleDataObjectDB> item = get(id);
    if(item != null) {
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
          scheduleDataObjectDBDatabase.scheduleDataObjectDBDao().delete(item.getValue());
          return null;
        }
      }.execute();
    }
  }

  public void delete(final ScheduleDataObjectDB item) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        scheduleDataObjectDBDatabase.scheduleDataObjectDBDao().delete(item);
        return null;
      }
    }.execute();
  }


  public void clearDB() {
    scheduleDataObjectDBDatabase.scheduleDataObjectDBDao().clearDB();
  }

  public LiveData<ScheduleDataObjectDB> get(int id) {
    return scheduleDataObjectDBDatabase.scheduleDataObjectDBDao().get(id);
  }

  public LiveData<List<ScheduleDataObjectDB>> getByParentId(int parentId) {
    return scheduleDataObjectDBDatabase.scheduleDataObjectDBDao().getByParentId(parentId);
  }

  public LiveData<List<ScheduleDataObjectDB>> getAll() {
    return scheduleDataObjectDBDatabase.scheduleDataObjectDBDao().fetchAll();
  }
}