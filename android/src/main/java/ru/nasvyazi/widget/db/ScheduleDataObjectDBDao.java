package ru.nasvyazi.widget.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScheduleDataObjectDBDao {

  @Insert
  Long insert(ScheduleDataObjectDB item);


  @Query("SELECT * FROM ScheduleDataObjectDB")
  LiveData<List<ScheduleDataObjectDB>> fetchAll();

  @Query("DELETE FROM ScheduleDataObjectDB")
  void clearDB();


  @Query("SELECT * FROM ScheduleDataObjectDB WHERE id =:id")
  LiveData<ScheduleDataObjectDB> get(int id);

  @Query("SELECT * FROM ScheduleDataObjectDB WHERE parentId =:parentId")
  LiveData<List<ScheduleDataObjectDB>> getByParentId(int parentId);


  @Update
  void update(ScheduleDataObjectDB item);


  @Delete
  void delete(ScheduleDataObjectDB item);
}