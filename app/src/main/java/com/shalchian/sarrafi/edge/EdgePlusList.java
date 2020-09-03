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

package com.shalchian.sarrafi.edge;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailProvider;
import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.db.DatabaseManager;
import com.shalchian.sarrafi.model.PriceModel;
import com.shalchian.sarrafi.utils.ActivityHelper;
import com.shalchian.sarrafi.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.shalchian.sarrafi.R.id.remote_list;

public class EdgePlusList extends SlookCocktailProvider {

  private static final String ACTION_PULL_TO_REFRESH = "com.shalchian.sarrafi.action.ACTION_PULL_TO_REFRESH";

  @Override
  public void onUpdate(Context context, SlookCocktailManager cocktailManager, int[] cocktailIds) {
    super.onUpdate(context, cocktailManager, cocktailIds);

    RemoteViews remoteViews = setupRemoteViews(context, false);
    if (cocktailIds != null) {
      for (int id : cocktailIds) {
        cocktailManager.updateCocktail(id, remoteViews);
        // set pull to refresh
        Intent refreshIntent = new Intent(ACTION_PULL_TO_REFRESH);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0xff, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        SlookCocktailManager.getInstance(context).setOnPullPendingIntent(cocktailIds[0], R.id.remote_list, pendingIntent);
      }
    }
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    String action = intent.getAction();
    switch(Objects.requireNonNull(action)) {
      case ACTION_PULL_TO_REFRESH:
        Log.e("♻️ ACTION_PULL", action);
        requestData(context, 0);
        break;
    }
  }

  @Override
  public void onVisibilityChanged(final Context context, final int cocktailId, int visibility) {
    super.onVisibilityChanged(context, cocktailId, visibility);

    if(ActivityHelper.checkConnection(context))
      requestData(context, cocktailId);
  }

  private void requestData(final Context context, final int cocktailId) {
    RemoteViews remoteViews = setupRemoteViews(context, false);
    SlookCocktailManager.getInstance(context).updateCocktail(cocktailId, remoteViews);
    ArrayList<PriceModel> list = new ArrayList<>();
    Log.e("♻️", "GETTING DATA");
    AndroidNetworking
            .get("https://call.tgju.org/ajax.json")
            .setPriority(Priority.HIGH)
            .doNotCacheResponse()
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  list.addAll(JSONParser.priceList(response, "", context));
                  savePriceListToDatabase(list);
                  updatePriceListAdapter(context, cocktailId);
                } catch (JSONException e) {
                  Log.e("🔴🔴 JSONException", String.valueOf(e));
                  deletePriceListFromDatabase();
                  updatePriceListAdapter(context, cocktailId);
                }

              }
              @Override
              public void onError(ANError error) {
                Log.e("🔴ERROR" , String.valueOf(error));
                deletePriceListFromDatabase();
                updatePriceListAdapter(context, cocktailId);
              }
            });
  }

  private void savePriceListToDatabase(List<PriceModel> priceModel) {
    DatabaseManager.getInstance().setPriceList(priceModel);
  }

  private void deletePriceListFromDatabase() {
    DatabaseManager.getInstance().deletePriceList();
  }

  private void updatePriceListAdapter(Context context, int cocktailId) {
    RemoteViews remoteViews = setupRemoteViews(context, true);
    SlookCocktailManager.getInstance(context).updateCocktail(cocktailId, remoteViews);
    SlookCocktailManager.getInstance(context).notifyCocktailViewDataChanged(cocktailId, R.id.remote_list);
  }

  private RemoteViews setupRemoteViews(Context context, boolean isContentShowing) {
    Intent intent = new Intent(context, PriceListAdapterService.class);
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.edge_plus_remote_list);
    remoteViews.setRemoteAdapter(remote_list, intent);
    remoteViews.setEmptyView(remote_list, R.id.tv_empty_list);
    remoteViews.setViewVisibility(R.id.layout_loading, isContentShowing ? View.INVISIBLE : View.VISIBLE);
    remoteViews.setViewVisibility(R.id.remote_list, isContentShowing ? View.VISIBLE : View.INVISIBLE);
    return remoteViews;
  }

}
