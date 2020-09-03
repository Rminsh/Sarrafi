
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
import com.shalchian.sarrafi.utils.JSONParser;

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

    list = new ArrayList<>();

    toolbar = findViewById(R.id.toolbar);
    toolbar.inflateMenu(R.menu.main_menu);

    recycler_view = findViewById(R.id.price_rcv);
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

    ActivityHelper.checkUpdate(this, getBaseContext());
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
                  recyclerViewState = Objects.requireNonNull(recycler_view.getLayoutManager()).onSaveInstanceState();
                  list.clear();
                  list.addAll(JSONParser.priceList(response, checkedFilter, getBaseContext()));

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
