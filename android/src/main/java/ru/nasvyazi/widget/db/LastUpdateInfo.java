package ru.nasvyazi.widget.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;

@Entity
public class LastUpdateInfo implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private String updateDate;
  private Boolean hasTeleopti;

  

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(String updateDate) {
    this.updateDate = updateDate;
  }

  public Boolean getHasTeleopti() {
    return hasTeleopti;
  }

  public void setHasTeleopti(Boolean hasTeleopti) {
    this.hasTeleopti = hasTeleopti;
  }

}