package ru.nasvyazi.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

class DataObject {
    public String contractTime;
    public String description;
    public int alpha;
    public int blue;
    public int green;
    public int red;
}

class Data {
    public List<DataObject> json;
}

public class WidgetFactory implements RemoteViewsFactory {
    ArrayList<Integer> colorList = new ArrayList<Integer>();
    List<DataObject> allData;
    ArrayList<String> data;
    Context context;
    SimpleDateFormat sdf;
    int widgetID;

    WidgetFactory(Context ctx, Intent intent) {
        context = ctx;
        sdf = new SimpleDateFormat("HH:mm:ss");
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        data = new ArrayList<String>();
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
        RemoteViews rView = new RemoteViews(context.getPackageName(),
                R.layout.item);
    rView.setInt(R.id.itemContainer, "setBackgroundColor", colorList.get(position)); 
        rView.setTextViewText(R.id.tvItemText, data.get(position));

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
        List<DataObject> dataItemsArr = newData.json;

        allData = newData.json;

        List<String> newArrayDataDescription = new ArrayList<String>();
        List<String> newArrayDataContractTime = new ArrayList<String>();
        for(int i=0; i<dataItemsArr.size(); i++){
            String description = dataItemsArr.get(i).description;
            String contractTime = dataItemsArr.get(i).contractTime;
            int intColor = Color.argb(dataItemsArr.get(i).alpha, dataItemsArr.get(i).red, dataItemsArr.get(i).green, dataItemsArr.get(i).blue);
            newArrayDataDescription.add(i,description );
            newArrayDataContractTime.add(i,contractTime);
            colorList.add(intColor);
        }

        for (int i = 0; i<newArrayDataDescription.size(); i++) {
            data.add(newArrayDataContractTime.get(i) + " - " + newArrayDataDescription.get(i));
        }
    }

    @Override
    public void onDestroy() {

    }

}