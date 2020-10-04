
/*
 *     This file is part of Sarrafi.
 *
 *     Sarrafi is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Sarrafi is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Sarrafi.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.shalchian.sarrafi.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.preference.PreferenceManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.model.PriceModel;
import com.shalchian.sarrafi.utils.ActivityHelper;
import com.shalchian.sarrafi.utils.JSONParser;
import com.shalchian.sarrafi.utils.WidgetHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class SymbolWidget extends AppWidgetProvider {

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.symbol_widget);

      Intent refreshIntent = new Intent(context, SymbolWidget.class);
      refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
      refreshIntent.setAction("com.shalchian.sarrafi.widget.REFRESH");
      PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      views.setOnClickPendingIntent(R.id.symbol_refresh, refreshPendingIntent);

      if (ActivityHelper.checkConnection(context)) {
        requestData(context, views, appWidgetId, appWidgetManager);
      } else {
        views.setViewVisibility(R.id.symbol_refresh, View.VISIBLE);
        views.setImageViewBitmap(R.id.symbol_date, WidgetHelper.BuildUpdate(context.getResources().getString(R.string.no_network), "Shabnam-FD", (int) context.getResources().getDimension(R.dimen.symbol_details), context));
        views.setViewVisibility(R.id.symbol_progress, View.GONE);
        appWidgetManager.updateAppWidget(appWidgetId, views);
      }

    }
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);

    if (Objects.equals(intent.getAction(), "com.shalchian.sarrafi.widget.REFRESH")) {
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.symbol_widget);
      int appWidgetId = Objects.requireNonNull(intent.getExtras()).getInt("appWidgetId");
      if (ActivityHelper.checkConnection(context)) {
        requestData(context, views, appWidgetId, appWidgetManager);
      } else {
        views.setViewVisibility(R.id.symbol_refresh, View.VISIBLE);
        views.setImageViewBitmap(R.id.symbol_date, WidgetHelper.BuildUpdate(context.getResources().getString(R.string.no_network), "Shabnam-FD", (int) context.getResources().getDimension(R.dimen.symbol_details), context));
        views.setViewVisibility(R.id.symbol_progress, View.GONE);
        appWidgetManager.updateAppWidget(appWidgetId, views);
      }
    }
  }

  private void requestData(Context context,RemoteViews views, int appWidgetId, AppWidgetManager appWidgetManager) {
    Log.e("♻️", "GETTING DATA");
    views.setViewVisibility(R.id.symbol_refresh, View.GONE);
    views.setImageViewBitmap(R.id.symbol_date, WidgetHelper.BuildUpdate(context.getResources().getString(R.string.updating), "Shabnam-FD", (int) context.getResources().getDimension(R.dimen.symbol_details), context));
    views.setViewVisibility(R.id.symbol_progress, View.VISIBLE);
    appWidgetManager.updateAppWidget(appWidgetId, views);
    AndroidNetworking
            .get("https://call.tgju.org/ajax.json")
            .setPriority(Priority.HIGH)
            .doNotCacheResponse()
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                  boolean tomanConvert = preferences.getBoolean("toman", false);
                  String iran_currency = tomanConvert ? "تومان" : "ریال";

                  JSONObject jsonData = response.optJSONObject("current");
                  if (jsonData != null) {
                    JSONObject price_dollar_rl = jsonData.getJSONObject("price_dollar_rl");
                    PriceModel model = JSONParser.addObject("price_dollar_rl", context.getResources().getString(R.string.dollar), price_dollar_rl, iran_currency, tomanConvert);

                    views.setImageViewBitmap(R.id.symbol_type, WidgetHelper.BuildUpdate(model.getType(), "Shabnam-FD", (int) context.getResources().getDimension(R.dimen.symbol_title), context));
                    views.setImageViewBitmap(R.id.symbol_price, WidgetHelper.BuildUpdate(model.getPrice(), "Shabnam-Bold-FD", (int) context.getResources().getDimension(R.dimen.symbol_price), context));
                    views.setImageViewBitmap(R.id.symbol_date, WidgetHelper.BuildUpdate(model.getTime(), "Shabnam-FD", (int) context.getResources().getDimension(R.dimen.symbol_details), context));
                    views.setImageViewBitmap(R.id.symbol_percent, WidgetHelper.BuildUpdate(model.getPercent_change() + "٪", "Shabnam-FD", (int) context.getResources().getDimension(R.dimen.symbol_details), context));

                    switch (model.getStatus()) {
                      case "low":
                        views.setInt(R.id.symbol_layout, "setBackgroundResource",R.drawable.rounded_gradient_purple);
                        views.setImageViewResource(R.id.symbol_percent_image, R.drawable.ic_down);
                        break;
                      case "high":
                        views.setInt(R.id.symbol_layout, "setBackgroundResource",R.drawable.rounded_gradient_new_leaf);
                        views.setImageViewResource(R.id.symbol_percent_image, R.drawable.ic_up);
                        break;
                      default:
                        views.setInt(R.id.symbol_layout, "setBackgroundResource",R.drawable.rounded_gradient_ocean_blue);
                        views.setImageViewResource(R.id.symbol_percent_image, android.R.color.transparent);
                        break;
                    }

                    views.setViewVisibility(R.id.symbol_refresh, View.VISIBLE);
                    views.setViewVisibility(R.id.symbol_progress, View.GONE);
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                    Log.e("✅", "DONE DATA");
                  }
                } catch (JSONException e) {
                  Log.e("🔴🔴 JSONException", String.valueOf(e));
                  views.setViewVisibility(R.id.symbol_refresh, View.VISIBLE);
                  views.setImageViewBitmap(R.id.symbol_date, WidgetHelper.BuildUpdate(context.getResources().getString(R.string.error_parsing), "Shabnam-FD", (int) context.getResources().getDimension(R.dimen.symbol_details), context));
                  views.setViewVisibility(R.id.symbol_progress, View.GONE);
                  appWidgetManager.updateAppWidget(appWidgetId, views);
                }

              }
              @Override
              public void onError(ANError error) {
                Log.e("🔴ERROR" , String.valueOf(error));
                views.setViewVisibility(R.id.symbol_refresh, View.VISIBLE);
                views.setImageViewBitmap(R.id.symbol_date, WidgetHelper.BuildUpdate(context.getResources().getString(R.string.error_loading), "Shabnam-FD", (int) context.getResources().getDimension(R.dimen.symbol_details), context));
                views.setViewVisibility(R.id.symbol_progress, View.GONE);
                appWidgetManager.updateAppWidget(appWidgetId, views);
              }
            });
  }
}

