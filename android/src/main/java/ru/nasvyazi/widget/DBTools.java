package ru.nasvyazi.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import ru.nasvyazi.widget.db.DayOffInfoDB;
import ru.nasvyazi.widget.db.DayOffInfoDBDao;
import ru.nasvyazi.widget.db.DayOffInfoDBRepository;
import ru.nasvyazi.widget.db.LastUpdateInfo;
import ru.nasvyazi.widget.db.LastUpdateInfoRepository;
import ru.nasvyazi.widget.db.ScheduleDataObjectDB;
import ru.nasvyazi.widget.db.ScheduleDataObjectDBRepository;
import ru.nasvyazi.widget.db.ScheduleSecondDataObjectDBRepository;
import ru.nasvyazi.widget.db.WidgetDayInfoDB;
import ru.nasvyazi.widget.db.WidgetDayInfoDBRepository;

interface DBToolsCallback{
    void onSuccess();
}

interface DBToolsPutCallback{
    void onSuccess(List<WidgetDayInfoDB> insertedData );
}

interface DBToolsGetCallback{
    void onSuccess(Data data );
}

interface DBToolsGetInfoCallback{
    void onSuccess(List<WidgetDayInfo> data);
}

interface DBToolsGetDayOffCallback{
    void onSuccess(DayOffInfo data);
}

interface DBToolsGetScheduleCallback{
    void onSuccess(List<DataObject> data);
}

interface DBToolsPutScheduleItemCallback{
    void onSuccess(List<ScheduleDataObjectDB> insertedData );
}

public class DBTools {

    public static void updateInfo(Context context, Boolean hasTeleopti, String updateDate, DBToolsCallback callback){
        final LastUpdateInfoRepository lastUpdateInfoRepository = new LastUpdateInfoRepository(context);

        lastUpdateInfoRepository.clearDB();

        LastUpdateInfo currentInfo = new LastUpdateInfo();
        currentInfo.setHasTeleopti(hasTeleopti);
        currentInfo.setUpdateDate(updateDate);

        lastUpdateInfoRepository.insert(currentInfo);

        callback.onSuccess();
    }

    public static void putWidgetSchedule(int parentId, ScheduleDataObjectDBRepository repository, List<DataObject> scheduleItems, DBToolsPutScheduleItemCallback callback){
        if (scheduleItems == null || scheduleItems.size() == 0){
            callback.onSuccess(new ArrayList<>());
        } else {
            DataObject scheduleItem = scheduleItems.get(0);

            ScheduleDataObjectDB item = new ScheduleDataObjectDB();
            item.setEventTimeStart(scheduleItem.eventTimeStart);
            item.setEventTimeEnd(scheduleItem.eventTimeEnd);
            item.setEventDuration(scheduleItem.eventDuration);
            item.setHexContainer(scheduleItem.hexContainer);
            item.setHexDivider(scheduleItem.hexDivider);
            item.setDescription(scheduleItem.description);
            item.setParentId(parentId);

            repository.insert(item, new DBInsertCallback() {
                @Override
                public void onInsert(long id) {
                    item.setId((int)id);

                    scheduleItems.remove(0);
                    if (scheduleItems.size() > 0) {
                        putWidgetSchedule(parentId, repository, scheduleItems, new DBToolsPutScheduleItemCallback() {
                            @Override
                            public void onSuccess(List<ScheduleDataObjectDB> insertedData) {
                                insertedData.add(item);
                                callback.onSuccess(insertedData);
                            }
                        });
                    } else {
                        ArrayList<ScheduleDataObjectDB> returnValue = new ArrayList<ScheduleDataObjectDB>();
                        returnValue.add(item);
                        callback.onSuccess(returnValue);
                    }
                }
            });
        }
    }

    public static void putWidgetSecondSchedule(int parentId, ScheduleSecondDataObjectDBRepository repository, List<DataObject> scheduleItems, DBToolsPutScheduleItemCallback callback){
        if (scheduleItems == null || scheduleItems.size() == 0){
            callback.onSuccess(new ArrayList<>());
        } else {
            DataObject scheduleItem = scheduleItems.get(0);

            ScheduleDataObjectDB item = new ScheduleDataObjectDB();
            item.setEventTimeStart(scheduleItem.eventTimeStart);
            item.setEventTimeEnd(scheduleItem.eventTimeEnd);
            item.setEventDuration(scheduleItem.eventDuration);
            item.setHexContainer(scheduleItem.hexContainer);
            item.setHexDivider(scheduleItem.hexDivider);
            item.setDescription(scheduleItem.description);
            item.setParentId(parentId);

            repository.insert(item, new DBInsertCallback() {
                @Override
                public void onInsert(long id) {
                    item.setId((int) id);

                    scheduleItems.remove(0);
                    if (scheduleItems.size() > 0) {
                        putWidgetSecondSchedule(parentId, repository, scheduleItems, new DBToolsPutScheduleItemCallback() {
                            @Override
                            public void onSuccess(List<ScheduleDataObjectDB> insertedData) {
                                insertedData.add(item);
                                callback.onSuccess(insertedData);
                            }
                        });
                    } else {
                        ArrayList<ScheduleDataObjectDB> returnValue = new ArrayList<ScheduleDataObjectDB>();
                        returnValue.add(item);
                        callback.onSuccess(returnValue);
                    }
                }
            });
        }
    }

    public static void putDayOff(DayOffInfoDBRepository dayOffInfoDBRepository, int parentId, DayOffInfo personDayOff, DBToolsCallback callback) {
        if (personDayOff == null){
            callback.onSuccess();
        } else {
            DayOffInfoDB dayOffItem = new DayOffInfoDB();
            dayOffItem.setParentId(parentId);
            dayOffItem.setSubtitle(personDayOff.subtitle);
            dayOffItem.setTitle(personDayOff.title);

            dayOffInfoDBRepository.insert(dayOffItem, new DBInsertCallback() {
                @Override
                public void onInsert(long id) {
                    callback.onSuccess();


                }
            });
        }
    }

    public static void putWidgetDayInfos(Context context, WidgetDayInfoDBRepository widgetDayInfoRepository, DayOffInfoDBRepository dayOffInfoDBRepository, List<WidgetDayInfo> days,DBToolsPutCallback callback){
        if (days == null || days.size() == 0){
            callback.onSuccess(new ArrayList<>());
        } else {
            WidgetDayInfo day = days.get(0);

            WidgetDayInfoDB item = new WidgetDayInfoDB();
            item.setDayDate(day.dayDate);
            item.setTwoDaysWorkDay(day.twoDaysWorkDay);
            item.setConflictEventIndex(day.conflictEventIndex);
            item.setDayDuration(day.dayDuration);
            widgetDayInfoRepository.insert(item, new DBInsertCallback() {
                @Override
                public void onInsert(long id) {
                    item.setId((int)id);

                    putDayOff(dayOffInfoDBRepository, item.getId(), day.personDayOff, new DBToolsCallback() {
                        @Override
                        public void onSuccess() {
                            final ScheduleDataObjectDBRepository scheduleDataObjectDBRepository = new ScheduleDataObjectDBRepository(context);
                            final ScheduleSecondDataObjectDBRepository scheduleSecondDataObjectDBRepository = new ScheduleSecondDataObjectDBRepository(context);

                            putWidgetSchedule(item.getId(), scheduleDataObjectDBRepository, day.shedule, new DBToolsPutScheduleItemCallback() {
                                @Override
                                public void onSuccess(List<ScheduleDataObjectDB> insertedData) {
                                    putWidgetSecondSchedule(item.getId(), scheduleSecondDataObjectDBRepository, day.secondDayShedule, new DBToolsPutScheduleItemCallback() {
                                        @Override
                                        public void onSuccess(List<ScheduleDataObjectDB> insertedData) {
                                            days.remove(0);
                                            if (days.size() > 0) {
                                                putWidgetDayInfos(context, widgetDayInfoRepository, dayOffInfoDBRepository, days, new DBToolsPutCallback() {
                                                    @Override
                                                    public void onSuccess(List<WidgetDayInfoDB> insertedData) {
                                                        insertedData.add(item);
                                                        callback.onSuccess(insertedData);
                                                    }
                                                });
                                            } else {
                                                ArrayList<WidgetDayInfoDB> returnValue = new ArrayList<WidgetDayInfoDB>();
                                                returnValue.add(item);
                                                callback.onSuccess(returnValue);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }

    }

    public static void putNewDays(Context context, List<WidgetDayInfo> days, DBToolsCallback callback){
        final WidgetDayInfoDBRepository widgetDayInfoRepository = new WidgetDayInfoDBRepository(context);
        final DayOffInfoDBRepository dayOffInfoDBRepository = new DayOffInfoDBRepository(context);
        final ScheduleDataObjectDBRepository scheduleDataObjectDBRepository = new ScheduleDataObjectDBRepository(context);
        final ScheduleSecondDataObjectDBRepository scheduleSecondDataObjectDBRepository = new ScheduleSecondDataObjectDBRepository(context);

        widgetDayInfoRepository.clearDB();
        dayOffInfoDBRepository.clearDB();
        scheduleDataObjectDBRepository.clearDB();
        scheduleSecondDataObjectDBRepository.clearDB();

        putWidgetDayInfos(context, widgetDayInfoRepository, dayOffInfoDBRepository, days, new DBToolsPutCallback() {
            @Override
            public void onSuccess(List<WidgetDayInfoDB> insertedData) {
                callback.onSuccess();
            }
        });
    }

    public static void getOriginalDayOff(Context context, WidgetDayInfoDB dayInfo, DBToolsGetDayOffCallback callback){
        final DayOffInfoDBRepository dayOffInfoDBRepository = new DayOffInfoDBRepository(context);
        final ScheduleDataObjectDBRepository scheduleDataObjectDBRepository = new ScheduleDataObjectDBRepository(context);
        final ScheduleSecondDataObjectDBRepository scheduleSecondDataObjectDBRepository = new ScheduleSecondDataObjectDBRepository(context);

        final LiveData<DayOffInfoDB> infosData = dayOffInfoDBRepository.getByParentId(dayInfo.getId());

        Observer<DayOffInfoDB> observer = new Observer<DayOffInfoDB>() {
            @Override
            public void onChanged(@Nullable final DayOffInfoDB item) {
                if (item == null){
                    callback.onSuccess(null);
                } else {
                    DayOffInfo returnValue = new DayOffInfo();

                    returnValue.title = item.getTitle();
                    returnValue.subtitle = item.getSubtitle();

                    callback.onSuccess(returnValue);
                }
            }
        };

        infosData.observeForever(observer);
    }

    public static void getOriginalSchedule(Context context, WidgetDayInfoDB dayInfo, DBToolsGetScheduleCallback callback){
        final ScheduleDataObjectDBRepository scheduleDataObjectDBRepository = new ScheduleDataObjectDBRepository(context);

        final LiveData<List<ScheduleDataObjectDB>> infosData = scheduleDataObjectDBRepository.getByParentId(dayInfo.getId());

        Observer<List<ScheduleDataObjectDB>> observer = new Observer<List<ScheduleDataObjectDB>>() {
            @Override
            public void onChanged(@Nullable final List<ScheduleDataObjectDB> itemArray) {
                List<DataObject> returnValue = new ArrayList<>();

                for (ScheduleDataObjectDB item : itemArray){
                    DataObject newReturnValue = new DataObject(item.getDescription());
                    newReturnValue.eventDuration = item.getEventDuration();
                    newReturnValue.eventTimeEnd = item.getEventTimeEnd();
                    newReturnValue.eventTimeStart = item.getEventTimeStart();
                    newReturnValue.hexContainer = item.getHexContainer();
                    newReturnValue.hexDivider = item.getHexDivider();

                    returnValue.add(newReturnValue);
                }

                callback.onSuccess(returnValue);
            }
        };

        infosData.observeForever(observer);
    }

    public static void getOriginalScheduleSecond(Context context, WidgetDayInfoDB dayInfo, DBToolsGetScheduleCallback callback){
        final ScheduleSecondDataObjectDBRepository scheduleSecondDataObjectDBRepository = new ScheduleSecondDataObjectDBRepository(context);

        final LiveData<List<ScheduleDataObjectDB>> infosData = scheduleSecondDataObjectDBRepository.getByParentId(dayInfo.getId());

        Observer<List<ScheduleDataObjectDB>> observer = new Observer<List<ScheduleDataObjectDB>>() {
            @Override
            public void onChanged(@Nullable final List<ScheduleDataObjectDB> itemArray) {
                List<DataObject> returnValue = new ArrayList<>();

                for (ScheduleDataObjectDB item : itemArray){
                    DataObject newReturnValue = new DataObject(item.getDescription());
                    newReturnValue.eventDuration = item.getEventDuration();
                    newReturnValue.eventTimeEnd = item.getEventTimeEnd();
                    newReturnValue.eventTimeStart = item.getEventTimeStart();
                    newReturnValue.hexContainer = item.getHexContainer();
                    newReturnValue.hexDivider = item.getHexDivider();

                    returnValue.add(newReturnValue);
                }

                callback.onSuccess(returnValue);
            }
        };

        infosData.observeForever(observer);
    }


    public static void convertToOriginal(Context context,  List<WidgetDayInfoDB> infos, DBToolsGetInfoCallback callback){
        if (infos.size() == 0){
            callback.onSuccess(new ArrayList<>());
        } else {

            WidgetDayInfoDB info = infos.get(0);
            WidgetDayInfo item = new WidgetDayInfo();
            item.dayDate = info.getDayDate();
            item.dayDuration = info.getDayDuration();
            item.conflictEventIndex = info.getConflictEventIndex();
            item.twoDaysWorkDay = info.getTwoDaysWorkDay();

            getOriginalSchedule(context, info, new DBToolsGetScheduleCallback() {
                @Override
                public void onSuccess(List<DataObject> data) {
                    item.shedule = data;
                    getOriginalScheduleSecond(context, info, new DBToolsGetScheduleCallback() {
                        @Override
                        public void onSuccess(List<DataObject> data) {
                            item.secondDayShedule = data;
                            getOriginalDayOff(context, info, new DBToolsGetDayOffCallback() {
                                @Override
                                public void onSuccess(DayOffInfo data) {
                                    item.personDayOff = data;
                                    infos.remove(0);
                                    if (infos.size() > 0) {
                                        convertToOriginal(context, infos, new DBToolsGetInfoCallback() {
                                            @Override
                                            public void onSuccess(List<WidgetDayInfo> data) {
                                                data.add(item);
                                                callback.onSuccess(data);
                                            }
                                        });
                                    } else {
                                        ArrayList<WidgetDayInfo> returnValue = new ArrayList<WidgetDayInfo>();
                                        returnValue.add(item);
                                        callback.onSuccess(returnValue);
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public static void getDataFromDB(Context context, DBToolsGetCallback callback){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                final LastUpdateInfoRepository lastUpdateInfoRepository = new LastUpdateInfoRepository(context);
                final WidgetDayInfoDBRepository widgetDayInfoRepository = new WidgetDayInfoDBRepository(context);

                Data returnValue = new Data();


                final LiveData<List<LastUpdateInfo>> infosData = lastUpdateInfoRepository.getAll();
                Observer<List<LastUpdateInfo>> observer = new Observer<List<LastUpdateInfo>>() {
                    @Override
                    public void onChanged(@Nullable final List<LastUpdateInfo> infos) {

                        if (infos.size() == 0){
                            callback.onSuccess(null);
                        } else {
                            returnValue.hasTeleopti = infos.get(0).getHasTeleopti();
                            returnValue.updateDate = infos.get(0).getUpdateDate();

                            final LiveData<List<WidgetDayInfoDB>> dayInfosData = widgetDayInfoRepository.getAll();
                            Observer<List<WidgetDayInfoDB>> observer1 = new Observer<List<WidgetDayInfoDB>>() {
                                @Override
                                public void onChanged(@Nullable final List<WidgetDayInfoDB> infos) {

                                    convertToOriginal(context, infos, new DBToolsGetInfoCallback() {
                                        @Override
                                        public void onSuccess(List<WidgetDayInfo> data) {
                                            returnValue.json = data;
                                            callback.onSuccess(returnValue);
                                        }
                                    });
                                }
                            };

                            dayInfosData.observeForever(observer1);
                        }
                    }
                };

                infosData.observeForever(observer);
            }
        });
    }
}
