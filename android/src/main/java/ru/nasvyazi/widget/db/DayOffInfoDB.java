package ru.nasvyazi.widget.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.List;

@Entity
public class DayOffInfoDB implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private String title;
  private String subtitle;
  private int parentId;



  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public int getParentId(){return parentId;}

  public void setParentId(int parentId){this.parentId = parentId;}
}