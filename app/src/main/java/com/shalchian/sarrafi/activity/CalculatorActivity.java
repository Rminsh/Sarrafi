
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
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.adapter.UnitAdapter;
import com.shalchian.sarrafi.model.UnitItem;
import com.shalchian.sarrafi.utils.ActivityHelper;
import com.shalchian.sarrafi.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class CalculatorActivity extends AppCompatActivity {

  Toolbar toolbar;

  View status_layout;
  LottieAnimationView status_animation;
  TextView status_text;
  Button status_button;
  NestedScrollView detail_scrollview;

  ArrayList<UnitItem> unitItems;
  UnitAdapter unitAdapter;
  Spinner spinnerFirst;
  Spinner spinnerSecond;
  EditText editTextFirst;
  EditText editTextSecond;
  ExtendedFloatingActionButton reverseFab;
  Animatable animatedVectorDrawable;
  Drawable animationDrawable;
  Double firstValue;
  Double secondValue;
  String mainUrl = "https://call.tgju.org/ajax.json";

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    ViewPump.init(ViewPump.builder().addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Shabnam-FD.ttf").setFontAttrId(R.attr.fontPath).build())).build());
    super.onCreate(savedInstanceState);
    getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    setContentView(R.layout.activity_calculator);

    toolbar = findViewById(R.id.toolbar);
    toolbar.setTitle(getResources().getString(R.string.calculator));
    toolbar.setBackground(null);
    setSupportActionBar(toolbar);
    toolbar.inflateMenu(R.menu.detail_menu);
    toolbar.setNavigationIcon(R.drawable.ic_back);
    toolbar.setNavigationOnClickListener(view -> this.finish());

    status_layout = findViewById(R.id.status_layout);
    status_animation = findViewById(R.id.status_animation);
    status_text = findViewById(R.id.status_text);
    status_button = findViewById(R.id.status_button);
    detail_scrollview = findViewById(R.id.detail_scrollview);

    spinnerFirst = findViewById(R.id.spinner_first);
    spinnerSecond = findViewById(R.id.spinner_second);
    editTextFirst = findViewById(R.id.edit_text_first);
    editTextSecond = findViewById(R.id.edit_text_second);
    reverseFab = findViewById(R.id.fab_reverse);
    animationDrawable = reverseFab.getIcon();

    animatedVectorDrawable = (Animatable) animationDrawable;

    unitItems = new ArrayList<>();
    unitAdapter = new UnitAdapter(this, unitItems);

    checkConnection();

    status_button.setOnClickListener(view -> {
      status_button.setVisibility(View.GONE);
      status_animation.setAnimation("loading_animation.json");
      status_animation.playAnimation();
      status_text.setText("");
      checkConnection();
    });

    spinnerFirst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        UnitItem clickedItem = (UnitItem) parent.getItemAtPosition(position);
        firstValue = clickedItem.getUnitPrice();

        calculate();
      }
      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });

    spinnerSecond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        UnitItem clickedItem = (UnitItem) parent.getItemAtPosition(position);
        secondValue = clickedItem.getUnitPrice();

        calculate();
      }
      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });

    editTextFirst.addTextChangedListener(new TextWatcher() {

      @Override
      public void afterTextChanged(Editable s) {

      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length() != 0) {
          calculate();
        }
      }
    });

    reverseFab.setOnClickListener(view -> {
      int firstPos = spinnerFirst.getSelectedItemPosition();
      int secondPos = spinnerSecond.getSelectedItemPosition();
      spinnerFirst.setSelection(secondPos);
      spinnerSecond.setSelection(firstPos);

      //TODO: Bug at duplicated animation
      animatedVectorDrawable.start();
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
                  unitItems.clear();
                  unitItems.addAll(JSONParser.priceConverterList(response, getBaseContext()));

                  spinnerFirst.setAdapter(unitAdapter);
                  spinnerSecond.setAdapter(unitAdapter);
                  spinnerSecond.setSelection(1);
                  detail_scrollview.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                  showProblem(getResources().getString(R.string.error_parsing));
                }

              }
              @Override
              public void onError(ANError error) {
                Log.e("🔴ERROR" , String.valueOf(error));
                showProblem(getResources().getString(R.string.error_loading));
              }
            });
  }

  private void calculate() {
    if (editTextFirst.length() !=0 ) {
      Double value = (Double.parseDouble(editTextFirst.getText().toString()) * firstValue) / secondValue;

      NumberFormat numberFormat = NumberFormat.getInstance();
      numberFormat.setGroupingUsed(false);
      editTextSecond.setText(JSONParser.addComma(numberFormat.format(value)));
    }
  }

  private void showProblem(String error) {
    unitItems.clear();

    status_layout.setVisibility(View.VISIBLE);
    status_animation.setAnimation("no_internet_connection.json");
    status_animation.playAnimation();
    status_text.setText(error);
    status_button.setVisibility(View.VISIBLE);
  }
}