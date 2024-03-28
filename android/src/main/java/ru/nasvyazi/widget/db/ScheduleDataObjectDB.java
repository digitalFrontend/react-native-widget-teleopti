package ru.nasvyazi.widget.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.List;

@Entity
public class ScheduleDataObjectDB implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  public String eventDuration;
  public String description;
  public String eventTimeStart;
  public String eventTimeEnd;
  public String hexDivider;
  public String hexContainer;
  public int parentId;




  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEventDuration() {
    return eventDuration;
  }

  public void setEventDuration(String eventDuration) {
    this.eventDuration = eventDuration;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getEventTimeStart() {
    return eventTimeStart;
  }

  public void setEventTimeStart(String eventTimeStart) {
    this.eventTimeStart = eventTimeStart;
  }

  public String getEventTimeEnd() {
    return eventTimeEnd;
  }

  public void setEventTimeEnd(String eventTimeEnd) {
    this.eventTimeEnd = eventTimeEnd;
  }

  public String getHexDivider() {
    return hexDivider;
  }

  public void setHexDivider(String hexDivider) {
    this.hexDivider = hexDivider;
  }

  public String getHexContainer() {
    return hexContainer;
  }

  public void setHexContainer(String hexContainer) {
    this.hexContainer = hexContainer;
  }

  public int getParentId() {
    return parentId;
  }

  public void setParentId(int parentId) {
    this.parentId = parentId;
  }


}