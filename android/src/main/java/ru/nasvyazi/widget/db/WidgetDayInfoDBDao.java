package ru.nasvyazi.widget.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WidgetDayInfoDBDao {

  @Insert
  Long insert(WidgetDayInfoDB item);


  @Query("SELECT * FROM WidgetDayInfoDB")
  LiveData<List<WidgetDayInfoDB>> fetchAll();

  @Query("DELETE FROM WidgetDayInfoDB")
  void clearDB();


  @Query("SELECT * FROM WidgetDayInfoDB WHERE id =:id")
  LiveData<WidgetDayInfoDB> get(int id);


  @Update
  void update(WidgetDayInfoDB item);


  @Delete
  void delete(WidgetDayInfoDB item);
}