package ru.nasvyazi.widget.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.util.List;

import ru.nasvyazi.widget.DBInsertCallback;

public class LastUpdateInfoRepository {

  private String DB_NAME = "db_last_update_info";

  private LastUpdateInfoDatabase lastUpdateInfoDatabase;
  public LastUpdateInfoRepository(Context context) {
    lastUpdateInfoDatabase = Room.databaseBuilder(context, LastUpdateInfoDatabase.class, DB_NAME).build();
  }

  public void deleteAllTables(){
    lastUpdateInfoDatabase.clearAllTables();
  }

  public void insert(String updateDate,
                         Boolean hasTeleopti) {

    LastUpdateInfo lastUpdateInfo = new LastUpdateInfo();
    lastUpdateInfo.setUpdateDate(updateDate);
    lastUpdateInfo.setHasTeleopti(hasTeleopti);
    

    insert(lastUpdateInfo);
  }

  public void insert(final LastUpdateInfo item) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        lastUpdateInfoDatabase.lastUpdateInfoDao().insert(item);
        return null;
      }
    }.execute();
  }

  public void insert(final LastUpdateInfo item, DBInsertCallback callback) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        callback.onInsert(lastUpdateInfoDatabase.lastUpdateInfoDao().insert(item));
        return null;
      }
    }.execute();
  }

  public void update(final LastUpdateInfo item) {

    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        lastUpdateInfoDatabase.lastUpdateInfoDao().update(item);
        return null;
      }
    }.execute();
  }

  public void delete(final int id) {
    final LiveData<LastUpdateInfo> item = get(id);
    if(item != null) {
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
          lastUpdateInfoDatabase.lastUpdateInfoDao().delete(item.getValue());
          return null;
        }
      }.execute();
    }
  }

  public void delete(final LastUpdateInfo item) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        lastUpdateInfoDatabase.lastUpdateInfoDao().delete(item);
        return null;
      }
    }.execute();
  }


  public void clearDB() {
    lastUpdateInfoDatabase.lastUpdateInfoDao().clearDB();
  }

  public LiveData<LastUpdateInfo> get(int id) {
    return lastUpdateInfoDatabase.lastUpdateInfoDao().getLastUpdateInfo(id);
  }

  public LiveData<List<LastUpdateInfo>> getAll() {
    return lastUpdateInfoDatabase.lastUpdateInfoDao().fetchAll();
  }
}