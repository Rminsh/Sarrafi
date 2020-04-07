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
import com.shalchian.sarrafi.model.PriceModelLegacy;
import com.shalchian.sarrafi.utils.ActivityHelper;

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
    ArrayList<PriceModelLegacy> list = new ArrayList<>();
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
                  JSONObject jsonData = response.optJSONObject("current");
                  if (jsonData != null) {
                    JSONObject price_dollar_rl = jsonData.getJSONObject("price_dollar_rl");
                    JSONObject price_eur              = jsonData.getJSONObject("price_eur");
                    JSONObject price_dollar_soleymani = jsonData.getJSONObject("price_dollar_soleymani");
                    JSONObject price_cad              = jsonData.getJSONObject("price_cad");
                    JSONObject price_gbp              = jsonData.getJSONObject("price_gbp");
                    JSONObject price_aed              = jsonData.getJSONObject("price_aed");
                    JSONObject price_try              = jsonData.getJSONObject("price_try");
                    JSONObject price_cny              = jsonData.getJSONObject("price_cny");
                    JSONObject price_jpy              = jsonData.getJSONObject("price_jpy");
                    JSONObject price_afn              = jsonData.getJSONObject("price_afn");
                    JSONObject price_iqd              = jsonData.getJSONObject("price_iqd");
                    JSONObject price_myr              = jsonData.getJSONObject("price_myr");
                    JSONObject price_rub              = jsonData.getJSONObject("price_rub");

                    JSONObject sekee            = jsonData.getJSONObject("sekee");
                    JSONObject sekeb            = jsonData.getJSONObject("sekeb");
                    JSONObject nim              = jsonData.getJSONObject("nim");
                    JSONObject rob              = jsonData.getJSONObject("rob");
                    JSONObject geram24          = jsonData.getJSONObject("geram24");
                    JSONObject geram18          = jsonData.getJSONObject("geram18");
                    JSONObject mesghal          = jsonData.getJSONObject("mesghal");
                    JSONObject gerami           = jsonData.getJSONObject("gerami");
                    JSONObject ons              = jsonData.getJSONObject("ons");
                    JSONObject silver           = jsonData.getJSONObject("silver");
                    JSONObject gold_mini_size   = jsonData.getJSONObject("gold_mini_size");

                    JSONObject oil        = jsonData.getJSONObject("oil");
                    JSONObject oil_brent  = jsonData.getJSONObject("oil_brent");
                    JSONObject oil_opec   = jsonData.getJSONObject("oil_opec");

                    JSONObject crypto_bitcoin = jsonData.getJSONObject("crypto-bitcoin");
                    JSONObject crypto_ethereum = jsonData.getJSONObject("crypto-ethereum");
                    JSONObject crypto_ripple = jsonData.getJSONObject("crypto-ripple");
                    JSONObject crypto_dash = jsonData.getJSONObject("crypto-dash");
                    JSONObject crypto_litecoin = jsonData.getJSONObject("crypto-litecoin");
                    JSONObject crypto_stellar = jsonData.getJSONObject("crypto-stellar");

                    list.add(addObject("price_dollar_rl","دلار", price_dollar_rl, "ریال" ));
                    list.add(addObject("price_dollar_soleymani","دلار سلیمانیه", price_dollar_soleymani, "ریال" ));
                    list.add(addObject("price_eur","یورو", price_eur, "ریال" ));
                    list.add(addObject("price_cad","دلار کانادا", price_cad, "ریال" ));
                    list.add(addObject("price_gbp","پوند انگلیس", price_gbp, "ریال" ));
                    list.add(addObject("price_aed","درهم امارات", price_aed, "ریال" ));
                    list.add(addObject("price_try","لیر ترکیه", price_try, "ریال" ));
                    list.add(addObject("price_cny","یوان چین", price_cny, "ریال" ));
                    list.add(addObject("price_jpy","ین ژاپن", price_jpy, "ریال" ));
                    list.add(addObject("price_afn","افغانی", price_afn, "ریال" ));
                    list.add(addObject("price_iqd","دینار عراق", price_iqd, "ریال" ));
                    list.add(addObject("price_myr","رینگت مالزی", price_myr, "ریال" ));
                    list.add(addObject("price_rub","روبل روسیه", price_rub, "ریال" ));

                    list.add(addObject("sekee","سکه امامی", sekee, "ریال" ));
                    list.add(addObject("sekeb","سکه بهار آزادی", sekeb, "ریال" ));
                    list.add(addObject("nim","نیم سکه", nim, "ریال" ));
                    list.add(addObject("rob","ربع سکه", rob, "ریال" ));
                    list.add(addObject("geram24","طلای ۲۴ عیار", geram24, "ریال" ));
                    list.add(addObject("geram18","طلای ۱۸ عیار", geram18, "ریال" ));
                    list.add(addObject("mesghal","مثقال طلا", mesghal, "ریال" ));
                    list.add(addObject("gerami","سکه گرمی", gerami, "ریال" ));
                    list.add(addObject("ons","انس طلا", ons, "ریال" ));
                    list.add(addObject("silver","انس نقره", silver, "ریال" ));
                    list.add(addObject("gold_mini_size","طلای دست دوم", gold_mini_size, "ریال" ));

                    list.add(addObject("oil","نفت سبک", oil, "دلار" ));
                    list.add(addObject("oil_brent","نفت برنت", oil_brent, "دلار" ));
                    list.add(addObject("oil_opec","نفت اوپک", oil_opec, "دلار" ));

                    list.add(addObject("crypto-bitcoin","بیت کوین / Bitcoin", crypto_bitcoin, "دلار" ));
                    list.add(addObject("crypto-ethereum","اتریوم / Ethereum", crypto_ethereum, "دلار" ));
                    list.add(addObject("crypto-ripple","ریپل / Ripple", crypto_ripple,  "دلار" ));
                    list.add(addObject("crypto-dash","دش / Dash", crypto_dash,  "دلار" ));
                    list.add(addObject("crypto-litecoin","لایت کوین / Litecoin", crypto_litecoin,  "دلار" ));
                    list.add(addObject("crypto-stellar","استلار / Stellar", crypto_stellar,  "دلار" ));

                    savePriceListToDatabase(list);
                    updatePriceListAdapter(context, cocktailId);
                  }

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

  private PriceModelLegacy addObject(String objectName, String name, JSONObject object, String toCurrency) {
    PriceModelLegacy priceModel = null;

    try {
      priceModel = new PriceModelLegacy(
              objectName,
              name,
              toCurrency,
              object.getString("p"),
              object.getDouble("dp"),
              object.getString("dt"),
              object.getString("t"));
    } catch (JSONException e) {
      Log.e("ERROR EXCEPTION", String.valueOf(e));
    }
    return priceModel;
  }

  private void savePriceListToDatabase(List<PriceModelLegacy> priceModel) {
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
