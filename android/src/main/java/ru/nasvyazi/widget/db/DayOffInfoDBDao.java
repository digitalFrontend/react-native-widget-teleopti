package ru.nasvyazi.widget.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DayOffInfoDBDao {

  @Insert
  Long insert(DayOffInfoDB item);


  @Query("SELECT * FROM DayOffInfoDB")
  LiveData<List<DayOffInfoDB>> fetchAll();

  @Query("DELETE FROM DayOffInfoDB")
  void clearDB();


  @Query("SELECT * FROM DayOffInfoDB WHERE id =:id")
  LiveData<DayOffInfoDB> get(int id);

  @Query("SELECT * FROM DayOffInfoDB WHERE parentId =:parentId")
  LiveData<DayOffInfoDB> getByParentId(int parentId);


  @Update
  void update(DayOffInfoDB item);


  @Delete
  void delete(DayOffInfoDB item);
}