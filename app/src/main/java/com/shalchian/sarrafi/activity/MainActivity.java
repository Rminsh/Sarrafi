
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

package com.shalchian.sarrafi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.adapter.PriceAdapter;
import com.shalchian.sarrafi.model.PriceModel;
import com.shalchian.sarrafi.utils.ActivityHelper;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

  RecyclerView recycler_view;
  PriceAdapter adapter;
  ArrayList<PriceModel> list;
  View status_layout;
  LottieAnimationView status_animation;
  TextView status_text;
  Button status_button;
  SwipeRefreshLayout swipeRefreshLayout;
  Toolbar toolbar;
  private Parcelable recyclerViewState;

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  String mainUrl = "https://call.tgju.org/ajax.json";
  String checkedFilter = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    ViewPump.init(ViewPump.builder().addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Shabnam-FD.ttf").setFontAttrId(R.attr.fontPath).build())).build());
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AndroidNetworking.initialize(getApplicationContext());
    list = new ArrayList<>();

    toolbar = findViewById(R.id.toolbar);
    toolbar.inflateMenu(R.menu.main_menu);

    recycler_view = findViewById(R.id.classroom_news_rcv);
    status_layout = findViewById(R.id.status_layout);
    status_animation = findViewById(R.id.status_animation);
    status_text = findViewById(R.id.status_text);
    status_button = findViewById(R.id.status_button);

    swipeRefreshLayout = findViewById(R.id.main_page_refresh);
    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setColorSchemeResources(
            R.color.purple,
            R.color.blue,
            R.color.light_green);


    adapter = new PriceAdapter(list, this);
    recycler_view.setHasFixedSize(true);

    status_button.setOnClickListener(view -> {
      status_button.setVisibility(View.GONE);
      status_animation.setAnimation("loading_animation.json");
      status_animation.playAnimation();
      status_text.setText("");
      checkConnection();
    });

    toolbar.setOnMenuItemClickListener(item -> {

      switch (item.getItemId()) {

        case R.id.menu_about:
          Intent i = new Intent(getApplicationContext(), AboutActivity.class);
          startActivity(i);
          return true;

        case R.id.menu_filter:
          LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          View bottomSheetView = Objects.requireNonNull(layoutInflater).inflate(R.layout.bottom_sheet_filter, null);
          BottomSheetDialog dialog = new BottomSheetDialog(this,R.style.AppBottomSheetDialogTheme);

          ChipGroup chipGroup = bottomSheetView.findViewById(R.id.filter_chip_group);

          switch (checkedFilter) {
            case "currency":
              chipGroup.check(R.id.currency_chip);
              break;
            case "gold":
              chipGroup.check(R.id.gold_chip);
              break;
            case "oil":
              chipGroup.check(R.id.oil_chip);
              break;
            case "digital_currency":
              chipGroup.check(R.id.digital_currency_chip);
              break;
            default:
              chipGroup.check(R.id.all_chip);
              break;
          }

          chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
              case R.id.currency_chip:
                checkedFilter = "currency";
                break;
              case R.id.gold_chip:
                checkedFilter = "gold";
                break;
              case R.id.oil_chip:
                checkedFilter = "oil";
                break;
              case R.id.digital_currency_chip:
                checkedFilter = "digital_currency";
                break;
              default:
                checkedFilter = "";
                break;
            }
            getData();
          });
          dialog.setContentView(bottomSheetView);
          dialog.show();
          return true;

        default:
          return false;
      }
    });

  }

  public void checkConnection() {
    if (ActivityHelper.checkConnection(getBaseContext())) {
      getData();
    } else {
      showProblem(getResources().getString(R.string.no_network));
    }
  }

  public void getData() {
    AndroidNetworking
            .get(mainUrl)
            .setPriority(Priority.HIGH)
            .doNotCacheResponse()
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                status_layout.setVisibility(View.GONE);
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

                    // Save recyclerview state
                    recyclerViewState = Objects.requireNonNull(recycler_view.getLayoutManager()).onSaveInstanceState();
                    list.clear();

                    if (checkedFilter.equals("currency") || checkedFilter.equals("")) {
                      addObject("price_dollar_rl","دلار", price_dollar_rl, "ریال" );
                      addObject("price_dollar_soleymani","دلار سلیمانیه", price_dollar_soleymani, "ریال" );
                      addObject("price_eur","یورو", price_eur, "ریال" );
                      addObject("price_cad","دلار کانادا", price_cad, "ریال" );
                      addObject("price_gbp","پوند انگلیس", price_gbp, "ریال" );
                      addObject("price_aed","درهم امارات", price_aed, "ریال" );
                      addObject("price_try","لیر ترکیه", price_try, "ریال" );
                      addObject("price_cny","یوان چین", price_cny, "ریال" );
                      addObject("price_jpy","ین ژاپن", price_jpy, "ریال" );
                      addObject("price_afn","افغانی", price_afn, "ریال" );
                      addObject("price_iqd","دینار عراق", price_iqd, "ریال" );
                      addObject("price_myr","رینگت مالزی", price_myr, "ریال" );
                      addObject("price_rub","روبل روسیه", price_rub, "ریال" );
                    }

                    if (checkedFilter.equals("gold") || checkedFilter.equals("")) {
                      addObject("sekee","سکه امامی", sekee, "ریال" );
                      addObject("sekeb","سکه بهار آزادی", sekeb, "ریال" );
                      addObject("nim","نیم سکه", nim, "ریال" );
                      addObject("rob","ربع سکه", rob, "ریال" );
                      addObject("geram24","طلای ۲۴ عیار", geram24, "ریال" );
                      addObject("geram18","طلای ۱۸ عیار", geram18, "ریال" );
                      addObject("mesghal","مثقال طلا", mesghal, "ریال" );
                      addObject("gerami","سکه گرمی", gerami, "ریال" );
                      addObject("ons","انس طلا", ons, "ریال" );
                      addObject("silver","انس نقره", silver, "ریال" );
                      addObject("gold_mini_size","طلای دست دوم", gold_mini_size, "ریال" );
                    }
                    
                    if (checkedFilter.equals("oil") || checkedFilter.equals("")) {
                      addObject("oil","نفت سبک", oil, "دلار" );
                      addObject("oil_brent","نفت برنت", oil_brent, "دلار" );
                      addObject("oil_opec","نفت اوپک", oil_opec, "دلار" );
                    }

                    if (checkedFilter.equals("digital_currency") || checkedFilter.equals("")) {
                      addObject("crypto-bitcoin","بیت کوین / Bitcoin", crypto_bitcoin, "دلار" );
                      addObject("crypto-ethereum","اتریوم / Ethereum", crypto_ethereum, "دلار" );
                      addObject("crypto-ripple","ریپل / Ripple", crypto_ripple,  "دلار" );
                      addObject("crypto-dash","دش / Dash", crypto_dash,  "دلار" );
                      addObject("crypto-litecoin","لایت کوین / Litecoin", crypto_litecoin,  "دلار" );
                      addObject("crypto-stellar","استلار / Stellar", crypto_stellar,  "دلار" );
                    }

                  }

                } catch (JSONException e) {
                  showProblem(getResources().getString(R.string.error_parsing));
                }

                recycler_view.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                // Restore recyclerview state
                Objects.requireNonNull(recycler_view.getLayoutManager()).onRestoreInstanceState(recyclerViewState);

                if (list.isEmpty()) {
                  status_layout.setVisibility(View.VISIBLE);
                  status_animation.setAnimation("empty_box.json");
                  status_animation.playAnimation();
                  status_text.setText(getResources().getString(R.string.empty_list));
                }
              }
              @Override
              public void onError(ANError error) {
                Log.e("🔴ERROR" , String.valueOf(error));
                showProblem(getResources().getString(R.string.error_loading));
              }
            });
  }

  public void addObject(String objectName, String name, JSONObject object, String toCurrency) {
    PriceModel priceModel = null;

    try {
      priceModel = new PriceModel(
              objectName,
              name,
              toCurrency,
              object.getString("p"),
              object.getString("h"),
              object.getString("l"),
              object.getString("d"),
              object.getDouble("dp"),
              object.getString("dt"),
              object.getString("t"));
    } catch (JSONException e) {
      Log.e("ERROR EXCEPTION", String.valueOf(e));
    }
    list.add(priceModel);
  }

  public void showProblem(String error) {
    list.clear();
    adapter.notifyDataSetChanged();
    swipeRefreshLayout.setRefreshing(false);
    status_layout.setVisibility(View.VISIBLE);
    status_animation.setAnimation("no_internet_connection.json");
    status_animation.playAnimation();
    status_text.setText(error);
    status_button.setVisibility(View.VISIBLE);
  }

  @Override
  protected void onResume() {
    super.onResume();
    checkConnection();
  }

  @Override
  public void onRefresh() {
    swipeRefreshLayout.setRefreshing(true);
    checkConnection();
  }
}
