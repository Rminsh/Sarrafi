
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

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.utils.ActivityHelper;
import com.shalchian.sarrafi.utils.TableMarker;

public class DetailActivity extends AppCompatActivity {

  ScrollView scrollView;
  View parent_chart_frame;
  LineChart chart;
  ProgressBar table_progressbar;
  Toolbar toolbar;
  TextView detail_price;
  TextView detail_price_change;
  CircularProgressIndicator detail_percent_change_circular;
  TextView detail_update;
  TextView detail_price_up;
  TextView detail_price_down;

  ChipGroup tableChipGroup;
  Chip detail_daily_chip;
  Chip detail_monthly_chip;

  String checkedFilter = "MONTHLY";

  String OBJECT;
  String CURRENCY_TYPE;
  String PERCENT_CHANGE;
  String PERCENT_CHANGE_RAW;

  List<String> dateMonthly;
  List<Float> pricesMonthly;

  List<String> dateDaily;
  List<Float> pricesDaily;

  List<String> dateThreeMonths;
  List<Float> pricesThreeMonths;

  List<String> dateSixMonths;
  List<Float> pricesSixMonths;

  List<String> dateAllMonths;
  List<Float> pricesAllMonths;

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
    setContentView(R.layout.activity_detail);

    String TYPE = Objects.requireNonNull(getIntent().getExtras()).getString("TYPE");
    CURRENCY_TYPE = getIntent().getExtras().getString("TOCURRENCY");
    String PRICE = getIntent().getExtras().getString("PRICE") + " " + CURRENCY_TYPE;
    String PRICE_UP = getIntent().getExtras().getString("PRICE_UP") + " " + CURRENCY_TYPE;
    String PRICE_DOWN = getIntent().getExtras().getString("PRICE_DOWN") + " " + CURRENCY_TYPE;
    String PRICE_CHANGE = getIntent().getExtras().getString("PRICE_CHANGE") + " " + getIntent().getExtras().getString("TOCURRENCY");
    String UPDATE = getResources().getString(R.string.last_update)+ " " + getIntent().getExtras().getString("UPDATE");
    String STATUS = getIntent().getExtras().getString("STATUS");
    PERCENT_CHANGE = getIntent().getExtras().getString("PERCENT_CHANGE");
    PERCENT_CHANGE_RAW = PERCENT_CHANGE;
    OBJECT = getIntent().getExtras().getString("OBJECT");

    toolbar = findViewById(R.id.toolbar);
    toolbar.setTitle(TYPE);
    toolbar.setBackground(null);
    setSupportActionBar(toolbar);
    toolbar.inflateMenu(R.menu.detail_menu);
    toolbar.setNavigationIcon(R.drawable.ic_back);
    toolbar.setNavigationOnClickListener(view -> this.finish());

    scrollView = findViewById(R.id.detail_scrollview);
    parent_chart_frame = findViewById(R.id.parent_chart_frame);
    detail_price = findViewById(R.id.detail_price);
    detail_price_change = findViewById(R.id.detail_price_change);
    detail_percent_change_circular = findViewById(R.id.detail_percent_change_circular);
    detail_update = findViewById(R.id.detail_update);
    detail_price_up = findViewById(R.id.detail_price_up);
    detail_price_down = findViewById(R.id.detail_price_down);

    tableChipGroup = findViewById(R.id.detail_chip_group);
    detail_daily_chip = findViewById(R.id.detail_daily_chip);
    detail_monthly_chip = findViewById(R.id.detail_monthly_chip);
    tableChipGroup.setSelectionRequired(true);

    detail_price.setText(PRICE);
    detail_update.setText(UPDATE);
    detail_price_up.setText(PRICE_UP);
    detail_price_down.setText(PRICE_DOWN);

    parent_chart_frame.setOnTouchListener(this::onTouchActionHandler);

    switch (Objects.requireNonNull(STATUS)) {
      case "low":
        PERCENT_CHANGE = "-" + PERCENT_CHANGE;
        detail_percent_change_circular.setProgressColor(getBaseContext().getResources().getColor(R.color.price_down));
        detail_percent_change_circular.setDotColor(getBaseContext().getResources().getColor(R.color.price_down));
        detail_price_change.setTextColor(getBaseContext().getResources().getColor(R.color.price_down));
        detail_price_change.setText("کاهش " + PRICE_CHANGE);
        break;

      case "high":
        PERCENT_CHANGE = "+" + PERCENT_CHANGE;
        detail_percent_change_circular.setProgressColor(getBaseContext().getResources().getColor(R.color.price_up));
        detail_percent_change_circular.setDotColor(getBaseContext().getResources().getColor(R.color.price_up));
        detail_price_change.setTextColor(getBaseContext().getResources().getColor(R.color.price_up));
        detail_price_change.setText("افزایش " + PRICE_CHANGE);
        break;

      default:
        detail_price_change.setText(PRICE_CHANGE);
        break;
    }

    detail_percent_change_circular.setCurrentProgress(Double.parseDouble(PERCENT_CHANGE != null ? PERCENT_CHANGE : "0"));

    toolbar.setOnMenuItemClickListener(item -> {

      switch (item.getItemId()) {

        case R.id.menu_share:
          String status = "";
          if (STATUS.equals("low"))
            status = " کاهش ";
          else if (STATUS.equals("high"))
            status = " افزایش ";
          String header = "🏷 " + TYPE + " " + PRICE;
          String priceUp = "📈 " + getBaseContext().getResources().getString(R.string.price_high) + " " + PRICE_UP;
          String priceDown = "📉 " + getBaseContext().getResources().getString(R.string.price_low) + " " + PRICE_DOWN;
          String priceChange = "🧮 " + getBaseContext().getResources().getString(R.string.price_change) + status + PRICE_CHANGE;
          String pricePercentChange = "📊 " + getBaseContext().getResources().getString(R.string.price_percent_change) + status + PERCENT_CHANGE_RAW + "٪";
          String time = "🕰 " + UPDATE;
          ActivityHelper.shareText(getBaseContext(), header, priceUp + "\n" + priceDown + "\n" + priceChange + "\n" + pricePercentChange + "\n" + time);
          return true;

        default:
          return false;
      }
    });

    chart = findViewById(R.id.lineChart1);
    table_progressbar = findViewById(R.id.table_progressbar);

    chart.animateX(400);

    chart.setDrawBorders(false);
    chart.setPinchZoom(true);
    chart.setDrawGridBackground(false);
    chart.getDescription().setEnabled(false);
    chart.getXAxis().setTypeface(ActivityHelper.setTypeface(getBaseContext(), "Shabnam-FD.ttf"));
    chart.setScaleEnabled(true);
    chart.getXAxis().setTextColor(Color.WHITE);
    chart.getAxisLeft().setTextColor(Color.WHITE);
    chart.getAxisLeft().setEnabled(true);
    chart.getAxisRight().setEnabled(false);
    chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    chart.getXAxis().setDrawGridLines(false);
    chart.setExtraRightOffset(getResources().getDimension(R.dimen.table_margin));
    chart.getLegend().setEnabled(false);

    dateDaily = new ArrayList<>();
    pricesDaily = new ArrayList<>();

    dateMonthly = new ArrayList<>();
    pricesMonthly = new ArrayList<>();

    dateThreeMonths = new ArrayList<>();
    pricesThreeMonths = new ArrayList<>();

    dateSixMonths = new ArrayList<>();
    pricesSixMonths = new ArrayList<>();

    dateAllMonths = new ArrayList<>();
    pricesAllMonths = new ArrayList<>();

    getData();
  }

  @Override
  public boolean onPrepareOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.detail_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  protected boolean onTouchActionHandler(View v, MotionEvent event){
    int action = event.getAction();
    if (action == MotionEvent.ACTION_DOWN) {
      // Disable touch on transparent view
      scrollView.requestDisallowInterceptTouchEvent(true);
      return false;
    }
    return true;
  }

  void getData() {
    hideChart();
    Log.e("✅ RES", "GETTING DATA");
    AndroidNetworking
            .get("https://www.tgju.org/")
            .addQueryParameter("act","chart-api")
            .addQueryParameter("noview","null")
            .addQueryParameter("client","app")
            .addQueryParameter("appversion","3")
            .addQueryParameter("name",OBJECT)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                showChart();
                try {
                  // Get Today Chart
                  JSONArray today_table = response.getJSONArray("today_table");
                  for (int i = today_table.length() - 1; i >= 0; i--) {
                    try {
                      JSONObject jo = today_table.getJSONObject(i);
                      String time = jo.getString("time");
                      float price = Float.parseFloat(jo.getString("price").replace(",", ""));
                      dateDaily.add(time);
                      pricesDaily.add(price);
                    } catch (JSONException e) {
                      Log.e("🔴ERROR Parse Daily", String.valueOf(e));
                    }
                  }
                  // if today chart is empty, Hide the chip filter
                  if (dateDaily.isEmpty() && pricesDaily.isEmpty())
                    detail_daily_chip.setVisibility(View.GONE);

                  // Get Monthly Chart
                  String chart_1 = "[" + response.getString("chart_1").replaceFirst(".$","") + "]";
                  JSONArray chart_1_Array = new JSONArray(chart_1);

                  for (int i = 0; i < chart_1_Array.length(); i++) {
                    try {
                      JSONObject jo = chart_1_Array.getJSONObject(i);
                      String date_string = jo.getString("jdate");
                      date_string = date_string.substring(date_string.indexOf("/") + 1);
                      dateMonthly.add(date_string);
                      pricesMonthly.add((float) jo.getDouble("value"));
                    } catch (JSONException e) {
                      Log.e("🔴ERROR Parse Monthly", String.valueOf(e));
                    }
                  }
                  // if monthly chart is empty, Hide the chip filter
                  if (dateMonthly.isEmpty() && pricesMonthly.isEmpty()) {
                    detail_monthly_chip.setVisibility(View.GONE);
                    checkedFilter = "3MONTHS";
                  }

                  // Get 3 Months Chart
                  String chart_3 = "[" + response.getString("chart_3").replaceFirst(".$","") + "]";
                  JSONArray chart_3_Array = new JSONArray(chart_3);

                  for (int i = 0; i < chart_3_Array.length(); i++) {
                    try {
                      JSONObject jo = chart_3_Array.getJSONObject(i);
                      String date_string = jo.getString("jdate");
                      date_string = date_string.substring(date_string.indexOf("/") + 1);
                      dateThreeMonths.add(date_string);
                      pricesThreeMonths.add((float) jo.getDouble("value"));
                    } catch (JSONException e) {
                      Log.e("🔴ERROR Parse 3 Months", String.valueOf(e));
                    }
                  }

                  // Get 6 Months Chart
                  String chart_6 = "[" + response.getString("chart_6").replaceFirst(".$","") + "]";
                  JSONArray chart_6_Array = new JSONArray(chart_6);

                  for (int i = 0; i < chart_6_Array.length(); i++) {
                    try {
                      JSONObject jo = chart_6_Array.getJSONObject(i);
                      String date_string = jo.getString("jdate");
                      date_string = date_string.substring(date_string.indexOf("/") + 1);
                      dateSixMonths.add(date_string);
                      pricesSixMonths.add((float) jo.getDouble("value"));
                    } catch (JSONException e) {
                      Log.e("🔴ERROR Parse 6 Months", String.valueOf(e));
                    }
                  }

                  // Get Chart Summery
                  String chart_summary = "[" + response.getString("chart_summary") + "]";
                  JSONArray chart_summary_Array = new JSONArray(chart_summary);

                  for (int i = 0; i < chart_summary_Array.length(); i++) {
                    try {
                      JSONObject jo = chart_summary_Array.getJSONObject(i);
                      String date_string = jo.getString("jdate");
                      int index = date_string.lastIndexOf('/');
                      dateAllMonths.add(date_string.substring(0,index));
                      pricesAllMonths.add((float) jo.getDouble("value"));
                    } catch (JSONException e) {
                      Log.e("🔴ERROR Parse All", String.valueOf(e));
                    }
                  }

                  //Load filters
                  loadChips();

                } catch (JSONException e) {
                  Log.e("🔴 ERROR Parse", String.valueOf(e));
                }
              }

              @Override
              public void onError(ANError error) {
                Log.e("🔴 Network ERROR" , String.valueOf(error));
              }
            });
  }

  private void setDateChart(List<String> date) {
    XAxis xAxis = chart.getXAxis();
    xAxis.setAvoidFirstLastClipping(false);
    xAxis.setValueFormatter(new IndexAxisValueFormatter(date));
    IMarker marker = new TableMarker(getBaseContext(), R.layout.markerview_table, date, CURRENCY_TYPE);
    chart.setMarker(marker);
  }

  private void setDataChart(List<Float> price) {
    ArrayList<Entry> values1 = new ArrayList<>();

    for (int i = 0; i < price.size(); i++) {
      if (price.get(i) != 0)
        values1.add(new Entry(i, price.get(i)));
    }

    //Remove circles if data is more than 90
    boolean circleStatus = price.size() <= 90;

    LineDataSet lineDataSet;
    lineDataSet = new LineDataSet(values1, "");
    lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

    if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
      lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
      lineDataSet.setValues(values1);
      chart.getData().notifyDataChanged();
      chart.notifyDataSetChanged();
    } else {
      lineDataSet.setLineWidth(3f);
      lineDataSet.setDrawCircles(circleStatus);
      lineDataSet.setCircleColor(getResources().getColor(R.color.colorAccent));
      lineDataSet.setColor(getResources().getColor(R.color.colorAccent));
      lineDataSet.setCircleRadius(6f);
      lineDataSet.setCircleHoleColor(Color.WHITE);
      lineDataSet.setCircleHoleRadius(4f);
      lineDataSet.setValueTypeface(ActivityHelper.setTypeface(getBaseContext(), "Shabnam-FD.ttf"));

      lineDataSet.setDrawHorizontalHighlightIndicator(false);
      lineDataSet.setDrawVerticalHighlightIndicator(false);
      LineData data = new LineData(lineDataSet);
      data.setDrawValues(false);
      chart.setData(data);
      chart.invalidate();
    }
  }

  void loadChips() {

    switch (checkedFilter) {
      case "DAILY":
        tableChipGroup.check(R.id.detail_daily_chip);
        setDateChart(dateDaily);
        setDataChart(pricesDaily);
        break;
      case "MONTHLY":
        tableChipGroup.check(R.id.detail_monthly_chip);
        setDateChart(dateMonthly);
        setDataChart(pricesMonthly);
        break;
      case "3MONTHS":
        tableChipGroup.check(R.id.detail_three_months_chip);
        setDateChart(dateThreeMonths);
        setDataChart(pricesThreeMonths);
        break;
      case "6MONTHS":
        tableChipGroup.check(R.id.detail_six_months_chip);
        setDateChart(dateSixMonths);
        setDataChart(pricesSixMonths);
        break;
      case "ALL":
        tableChipGroup.check(R.id.detail_all_chip);
        setDateChart(dateAllMonths);
        setDataChart(pricesAllMonths);
        break;
    }

    tableChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
      switch (checkedId) {
        case R.id.detail_daily_chip:
          checkedFilter = "DAILY";
          chart.clearValues();
          setDateChart(dateDaily);
          setDataChart(pricesDaily);
          break;
        case R.id.detail_monthly_chip:
          chart.clearValues();
          checkedFilter = "MONTHLY";
          setDateChart(dateMonthly);
          setDataChart(pricesMonthly);
          break;
        case R.id.detail_three_months_chip:
          chart.clearValues();
          checkedFilter = "3MONTHS";
          setDateChart(dateThreeMonths);
          setDataChart(pricesThreeMonths);
          break;
        case R.id.detail_six_months_chip:
          chart.clearValues();
          checkedFilter = "6MONTHS";
          setDateChart(dateSixMonths);
          setDataChart(pricesSixMonths);
          break;
        case R.id.detail_all_chip:
          chart.clearValues();
          checkedFilter = "ALL";
          setDateChart(dateAllMonths);
          setDataChart(pricesAllMonths);
          break;
      }
    });
  }

  void hideChart() {
    chart.setVisibility(View.GONE);
    tableChipGroup.setVisibility(View.GONE);
    table_progressbar.setVisibility(View.VISIBLE);
  }

  void showChart() {
    chart.setVisibility(View.VISIBLE);
    tableChipGroup.setVisibility(View.VISIBLE);
    table_progressbar.setVisibility(View.GONE);
  }
}
