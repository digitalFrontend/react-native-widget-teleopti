package ru.nasvyazi.widget;

import static android.content.Context.MODE_PRIVATE;

import static com.facebook.react.bridge.ReadableType.Map;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


class DayOffInfo {
    public String title;
    public String subtitle;
        }
class WidgetDayInfo {
    public String dayDuration;
    public String dayDate;
    public List<DataObject> shedule;
    public DayOffInfo personDayOff;
    public boolean twoDaysWorkDay;
    public List<DataObject> secondDayShedule;
    public int conflictEventIndex;
    public String id ;
        }
class DataObject {
    public String eventDuration;
    public String description;
    public String eventTimeStart;
    public String eventTimeEnd;
    public String hexDivider;
    public String hexContainer;

    public  DataObject(String description){
        this.description = description;
    };

}
class StateForMetrics {
    public boolean create;
    public boolean destroy;

    public  StateForMetrics(boolean create, boolean destroy){
        this.create = create;
        this.destroy = destroy;
    };
}
class Data {
    public List<WidgetDayInfo> json;
    public String updateDate;
    public boolean hasTeleopti;
}
class Helper {

    public static void setStateForMetrics(Context context, StateForMetrics stateForMetrics) {
        SharedPreferences sharedPreferences =  context.getSharedPreferences("TELEOPTI_storage", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString("stateForMetrics", gson.toJson(stateForMetrics));
        editor.commit();
    }

    public static String getStateForMetrics(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("TELEOPTI_storage", Context.MODE_PRIVATE);
        String json = sharedPref.getString("stateForMetrics", null);

        if(json == null){
            StateForMetrics defaultState =  new StateForMetrics(false, false);
            Gson gson = new Gson();
            String jsonState = gson.toJson(defaultState);
            return jsonState;
        }else {
            return json;
        }


    }
    
   public static boolean compareTime(String time1, String time2) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);

            if(date1.before(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    public static WidgetDayInfo getCurrentDay(List<WidgetDayInfo> days) {
        Date currentDate = new Date();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String srtCurrDate = dateFormatter.format(currentDate);
        for (int i=0; i<days.size(); i++) {
            if (days.get(i).dayDate.equals(srtCurrDate)){ //srtCurrDate//"2022-12-07
             return days.get(i);
            }
        }
        return null;
    }

    public static List<DataObject> getCurrentShedule(List<DataObject> shedule, boolean twoDaysWorkDay, int conflictEventIndex) {
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        Date currentDate = new Date();
        String strCurrTime = timeFormatter.format(currentDate);
        List<DataObject> currenShedule = new ArrayList<>();
        if(twoDaysWorkDay){
            for (int i=0; i<conflictEventIndex; i++) {
                Boolean isBefore = Helper.compareTime(shedule.get(i).eventTimeEnd, strCurrTime); // "12-00"
                if (!isBefore){
                    currenShedule.add(shedule.get(i));
                }
            }
            for (int i=conflictEventIndex; i<shedule.size(); i++) {
                Boolean isBefore = Helper.compareTime(shedule.get(i).eventTimeEnd, strCurrTime); // "12-00"
                if (!isBefore){
                    currenShedule.add(shedule.get(i));
                }
            }
        } else{
            for (int i=0; i<shedule.size(); i++) {
                Boolean isBefore = Helper.compareTime(shedule.get(i).eventTimeEnd, strCurrTime); // "12-00"
                if (!isBefore){
                    currenShedule.add(shedule.get(i));
                }
            }
        }
        return currenShedule;
    }

}

public class WidgetFactory implements RemoteViewsFactory {
    ArrayList<Integer> colorList = new ArrayList<Integer>();
    List<DataObject> allData;
    List<DataObject> data;
    Context context;
    SimpleDateFormat sdf;
    String updateDate;
    Boolean hasTeleopti;
    int widgetID;

    WidgetFactory(Context ctx, Intent intent) {
        context = ctx;
        sdf = new SimpleDateFormat("HH:mm:ss");
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        data = new ArrayList<DataObject>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(context.getPackageName(), R.layout.item);
        Boolean noMoreView = false;
        if(data.get(position).eventTimeEnd == null && data.get(position).eventTimeStart == null && data.get(position).eventDuration == null && data.get(position).description != null) {
            noMoreView = true;
        }
        if(noMoreView){
            rView.setViewVisibility(R.id.noMoreActivity, View.VISIBLE);
            rView.setTextViewText(R.id.noMoreActivity, data.get(position).description);
            rView.setViewVisibility(R.id.sheduleElementContainer, View.INVISIBLE);
            rView.setViewVisibility(R.id.sheduleElementContent, View.INVISIBLE);
        } else {
            rView.setViewVisibility(R.id.sheduleElementContainer, View.VISIBLE);
            rView.setViewVisibility(R.id.sheduleElementContent, View.VISIBLE);
            rView.setViewVisibility(R.id.noMoreActivity, View.INVISIBLE);
            rView.setImageViewResource(R.id.sheduleElementContainer, R.drawable.rounded);
            rView.setInt(R.id.sheduleElementContainer, "setColorFilter",  Color.parseColor(  data.get(position).hexContainer));

            rView.setImageViewResource(R.id.sheduleElementDivider, R.drawable.rounded_inside_divider);
            rView.setInt(R.id.sheduleElementDivider, "setColorFilter", Color.parseColor(  data.get(position).hexDivider));

            rView.setTextViewText(R.id.description, data.get(position).description+", "+data.get(position).eventDuration);
            rView.setTextColor(R.id.description, Color.parseColor(  data.get(position).hexDivider));
            rView.setTextViewText(R.id.duration, data.get(position).eventTimeStart+"–"+data.get(position).eventTimeEnd);
            rView.setTextColor(R.id.duration, Color.parseColor(  data.get(position).hexDivider));
        }

        return rView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        data.clear();
        colorList.clear();

        SharedPreferences sharedPref = context.getSharedPreferences("TELEOPTI_storage", Context.MODE_PRIVATE);
        String json = sharedPref.getString("teleoptiData", null);

        Gson g = new Gson();
        Data newData = g.fromJson(json, Data.class);
        updateDate = newData.updateDate;
        hasTeleopti = newData.hasTeleopti;

        List<WidgetDayInfo> days = newData.json;

        WidgetDayInfo currentDay = Helper.getCurrentDay(days);
        if(currentDay != null){
            List<DataObject> currentShedule = new ArrayList<>();
            if(currentDay.secondDayShedule != null){
                currentShedule = Helper.getCurrentShedule(currentDay.secondDayShedule, currentDay.twoDaysWorkDay, currentDay.conflictEventIndex);
            } else{
                currentShedule = Helper.getCurrentShedule(currentDay.shedule, currentDay.twoDaysWorkDay, currentDay.conflictEventIndex);
            }

            data = currentShedule;
            if(data.size() < 4){
                DataObject noMoreActivity = new DataObject("Сегодня событий больше нет");
                data.add(noMoreActivity);
            }
        }
    }

    @Override
    public void onDestroy() {

    }

}