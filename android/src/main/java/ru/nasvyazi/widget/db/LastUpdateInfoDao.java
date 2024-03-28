package ru.nasvyazi.widget.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LastUpdateInfoDao {

  @Insert
  Long insert(LastUpdateInfo item);


  @Query("SELECT * FROM LastUpdateInfo")
  LiveData<List<LastUpdateInfo>> fetchAll();

  @Query("DELETE FROM LastUpdateInfo")
  void clearDB();


  @Query("SELECT * FROM LastUpdateInfo WHERE id =:id")
  LiveData<LastUpdateInfo> getLastUpdateInfo(int id);


  @Update
  void update(LastUpdateInfo item);


  @Delete
  void delete(LastUpdateInfo item);
}