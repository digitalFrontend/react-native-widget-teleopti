package ru.nasvyazi.widget.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.List;

@Entity
public class WidgetDayInfoDB implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private String dayDuration;
  private String dayDate;
  private boolean twoDaysWorkDay;
  private int conflictEventIndex;




  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDayDuration() {
    return dayDuration;
  }

  public void setDayDuration(String dayDuration) {
    this.dayDuration = dayDuration;
  }

  public String getDayDate() {
    return dayDate;
  }

  public void setDayDate(String dayDate) {
    this.dayDate = dayDate;
  }

  public boolean getTwoDaysWorkDay() {
    return twoDaysWorkDay;
  }

  public void setTwoDaysWorkDay(boolean twoDaysWorkDay) {
    this.twoDaysWorkDay = twoDaysWorkDay;
  }

  public int getConflictEventIndex() {
    return conflictEventIndex;
  }

  public void setConflictEventIndex(int conflictEventIndex) {
    this.conflictEventIndex = conflictEventIndex;
  }
}