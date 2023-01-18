package ru.nasvyazi.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class TeleoptiWidget extends AppWidgetProvider {
    private final int ALARM_ID = 12312;
    public static String ACTION_AUTO_UPDATE_WIDGET = "ACTION_AUTO_UPDATE_WIDGET";
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public TeleoptiWidget() {
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Helper.setStateForMetrics(context, new StateForMetrics(true, false));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        
        Helper.setStateForMetrics(context, new StateForMetrics(false, true));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        if (appWidgetIds.length == 0) {
            // stop alarm
            AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
            appWidgetAlarm.stopAlarm(ALARM_ID);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetRootView);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lvList);

        onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);      

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // start alarm
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm(ALARM_ID);
        // update widgets
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);

        }

;
    }

    void updateWidget(Context context, AppWidgetManager appWidgetManager,
                      int appWidgetId) {
        SharedPreferences sharedPref = context.getSharedPreferences("TELEOPTI_storage", Context.MODE_PRIVATE);
        String json = sharedPref.getString("teleoptiData", null);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.teleopti_widget);
        java.util.Date todayDate = new Date();
        DateFormat todayDateFormatter = new SimpleDateFormat("EEEE, d MMM");
        String srtTodayDate = todayDateFormatter.format(todayDate);
        rv.setTextViewText(R.id.todayDate, srtTodayDate);
        if(json == null){
            rv.setViewVisibility(R.id.updateDate, View.INVISIBLE);
            rv.setViewVisibility(R.id.listContent, View.INVISIBLE);
            rv.setViewVisibility(R.id.dayoff, View.INVISIBLE );
            rv.setViewVisibility(R.id.noShedule, View.INVISIBLE);
            rv.setViewVisibility(R.id.noData, View.VISIBLE);
        } else{
            Gson g = new Gson();
            Data newData = g.fromJson(json, Data.class);
            String updateDate = newData.updateDate;
            Boolean hasTeleopti = newData.hasTeleopti;
            if(!hasTeleopti){
                rv.setViewVisibility(R.id.updateDate, View.INVISIBLE);
                rv.setViewVisibility(R.id.listContent, View.INVISIBLE);
                rv.setViewVisibility(R.id.dayoff, View.INVISIBLE );
                rv.setViewVisibility(R.id.noShedule, View.INVISIBLE);
                rv.setViewVisibility(R.id.noData, View.VISIBLE);
            } else{
                rv.setViewVisibility(R.id.updateDate, View.VISIBLE);
                WidgetDayInfo currentDay = Helper.getCurrentDay(newData.json);
                if(currentDay!=null){
                    rv.setTextViewText(R.id.updateDate, "Данные на "+updateDate);
                    rv.setTextViewText(R.id.todayDuration, " "+currentDay.dayDuration);
                    rv.setTextColor(R.id.updateDate, Color.parseColor("#73767A"));
                    DayOffInfo dayOffInfo = currentDay.personDayOff;

                    if(dayOffInfo != null){
                        if(currentDay.secondDayShedule != null){
                            List<DataObject> currentShedule = Helper.getCurrentShedule(currentDay.secondDayShedule, currentDay.twoDaysWorkDay, currentDay.conflictEventIndex);
                            if(currentShedule.size() > 0){
                                rv.setViewVisibility(R.id.dayoff, View.INVISIBLE );
                                rv.setViewVisibility(R.id.noShedule, View.INVISIBLE);
                                rv.setViewVisibility(R.id.listContent, View.VISIBLE);
                                setList(rv, context, appWidgetId);
                            }else {
                                rv.setViewVisibility(R.id.dayoff, View.VISIBLE);
                                rv.setViewVisibility(R.id.listContent, View.INVISIBLE);
                                rv.setViewVisibility(R.id.noShedule, View.INVISIBLE);
                                rv.setTextViewText(R.id.dayoffTitle, "Сегодня – "+dayOffInfo.title);
                                rv.setTextViewText(R.id.dayoffSubtitle, dayOffInfo.subtitle);
                            }
                        }else {
                            rv.setViewVisibility(R.id.dayoff, View.VISIBLE);
                            rv.setViewVisibility(R.id.listContent, View.INVISIBLE);
                            rv.setViewVisibility(R.id.noShedule, View.INVISIBLE);
                            rv.setTextViewText(R.id.dayoffTitle, "Сегодня – "+dayOffInfo.title);
                            rv.setTextViewText(R.id.dayoffSubtitle, dayOffInfo.subtitle);
                        }
                    } else {
                        rv.setViewVisibility(R.id.dayoff, View.INVISIBLE );
                        rv.setViewVisibility(R.id.noShedule, View.INVISIBLE);
                        rv.setViewVisibility(R.id.listContent, View.VISIBLE);
                        setList(rv, context, appWidgetId);
                    }
                } else {
                    rv.setTextViewText(R.id.updateDate, "Расписание не загрузилось");
                    rv.setTextColor(R.id.updateDate, Color.parseColor("#FF455C"));
                    rv.setViewVisibility(R.id.dayoff, View.INVISIBLE );
                    rv.setViewVisibility(R.id.listContent, View.INVISIBLE);
                    rv.setViewVisibility(R.id.noShedule, View.VISIBLE);
                }
            }
        }
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

//   

    void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent adapter = new Intent(context, WidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        rv.setRemoteAdapter(R.id.lvList, adapter);
    }

    void setListClick(RemoteViews rv, Context context, int appWidgetId) {

    }

}