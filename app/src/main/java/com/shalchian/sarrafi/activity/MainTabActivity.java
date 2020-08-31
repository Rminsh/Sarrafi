
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.iammert.library.AnimatedTabLayout;
import com.shalchian.sarrafi.BuildConfig;
import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.fragment.CurrencyListFragment;
import com.shalchian.sarrafi.fragment.DigitalCurrencyListFragment;
import com.shalchian.sarrafi.fragment.GoldListFragment;
import com.shalchian.sarrafi.fragment.OilListFragment;
import com.shalchian.sarrafi.fragment.PricePagerAdapter;
import com.shalchian.sarrafi.utils.ActivityHelper;

import org.json.JSONObject;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainTabActivity extends AppCompatActivity {

  View status_layout;
  LottieAnimationView status_animation;
  TextView status_text;
  Button status_button;
  Toolbar toolbar;
  ViewPager viewPager;
  AnimatedTabLayout tabs;

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  String mainUrl = "https://call.tgju.org/ajax.json";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    ViewPump.init(ViewPump.builder().addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Shabnam-FD.ttf").setFontAttrId(R.attr.fontPath).build())).build());
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_tab);

    viewPager = findViewById(R.id.view_pager);
    tabs = findViewById(R.id.tabs);

    toolbar = findViewById(R.id.toolbar);
    toolbar.inflateMenu(R.menu.tab_menu);
    toolbar.getMenu().findItem(R.id.menu_rate).setVisible(!BuildConfig.showUpdater);

    status_layout = findViewById(R.id.status_layout);
    status_animation = findViewById(R.id.status_animation);
    status_text = findViewById(R.id.status_text);
    status_button = findViewById(R.id.status_button);

    checkConnection();

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

        case R.id.menu_rate:
          ActivityHelper.rateUS(this, getBaseContext());
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

                Bundle bundle = new Bundle();
                bundle.putString("data_key", response.toString());

                PricePagerAdapter pricePagerAdapter = new PricePagerAdapter(getBaseContext(), getSupportFragmentManager());
                pricePagerAdapter.addFragment(CurrencyListFragment.newInstance(bundle));
                pricePagerAdapter.addFragment(GoldListFragment.newInstance(bundle));
                pricePagerAdapter.addFragment(OilListFragment.newInstance(bundle));
                pricePagerAdapter.addFragment(DigitalCurrencyListFragment.newInstance(bundle));
                viewPager.setAdapter(pricePagerAdapter);
                tabs.setupViewPager(viewPager);
                tabs.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);

                if (response.toString().equals("")) {
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
    viewPager.setVisibility(View.GONE);
    status_layout.setVisibility(View.VISIBLE);
    status_animation.setAnimation("no_internet_connection.json");
    status_animation.playAnimation();
    status_text.setText(error);
    status_button.setVisibility(View.VISIBLE);
  }

}