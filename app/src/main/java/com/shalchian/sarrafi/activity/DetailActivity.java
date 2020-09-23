
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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.model.Progress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.db.DatabaseManager;
import com.shalchian.sarrafi.fragment.FavoriteListFragment;
import com.shalchian.sarrafi.model.PriceTableModel;
import com.shalchian.sarrafi.utils.ActivityHelper;
import com.shalchian.sarrafi.utils.JSONParser;
import com.shalchian.sarrafi.utils.TableMarker;

public class DetailActivity extends AppCompatActivity {

  NestedScrollView scrollView;
  View parent_chart_frame;
  LineChart chart;
  ProgressBar table_progressbar;
  Toolbar toolbar;
  MenuItem favoriteMenu;
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

  PriceTableModel priceTableModel;

  AtomicBoolean isFavorite;

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

    isFavorite = new AtomicBoolean(false);

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
        case R.id.menu_favorite:
          if (isFavorite.get()) {
            isFavorite.set(false);
            DatabaseManager.getInstance().deleteFromFavoriteList(OBJECT);
            favoriteMenu.setTitle(getResources().getString(R.string.add_to_favorite));
            favoriteMenu.setIcon(R.drawable.ic_star_line);
          } else {
            isFavorite.set(true);
            DatabaseManager.getInstance().addToFavoriteList(OBJECT);
            favoriteMenu.setTitle(getResources().getString(R.string.remove_from_favorites));
            favoriteMenu.setIcon(R.drawable.ic_star_fill);
          }
          FavoriteListFragment.getInstance().loadList();
          return true;

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

    getData();
  }

  @Override
  public boolean onPrepareOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.detail_menu, menu);
    favoriteMenu = menu.findItem(R.id.menu_favorite);
    ArrayList<String> list = DatabaseManager.getInstance().getFavoriteList();
    if (DatabaseManager.getInstance().isFavoriteListAvailable() && list.contains(OBJECT)) {
      favoriteMenu.setTitle(getResources().getString(R.string.remove_from_favorites));
      favoriteMenu.setIcon(R.drawable.ic_star_fill);
      isFavorite.set(true);
    }
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

                // Get Chart
                boolean isToman = CURRENCY_TYPE.equals("تومان");
                TableAsyncTask task = new TableAsyncTask();
                task.execute(new TableTaskParams(response, isToman));
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
        setDateChart(priceTableModel.getDateDaily());
        setDataChart(priceTableModel.getPricesDaily());
        break;
      case "MONTHLY":
        tableChipGroup.check(R.id.detail_monthly_chip);
        setDateChart(priceTableModel.getDateMonthly());
        setDataChart(priceTableModel.getPricesMonthly());
        break;
      case "3MONTHS":
        tableChipGroup.check(R.id.detail_three_months_chip);
        setDateChart(priceTableModel.getDateThreeMonths());
        setDataChart(priceTableModel.getPricesThreeMonths());
        break;
      case "6MONTHS":
        tableChipGroup.check(R.id.detail_six_months_chip);
        setDateChart(priceTableModel.getDateSixMonths());
        setDataChart(priceTableModel.getPricesSixMonths());
        break;
      case "ALL":
        tableChipGroup.check(R.id.detail_all_chip);
        setDateChart(priceTableModel.getDateAllMonths());
        setDataChart(priceTableModel.getPricesAllMonths());
        break;
    }

    tableChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
      switch (checkedId) {
        case R.id.detail_daily_chip:
          checkedFilter = "DAILY";
          chart.clearValues();
          setDateChart(priceTableModel.getDateDaily());
          setDataChart(priceTableModel.getPricesDaily());
          break;
        case R.id.detail_monthly_chip:
          chart.clearValues();
          checkedFilter = "MONTHLY";
          setDateChart(priceTableModel.getDateMonthly());
          setDataChart(priceTableModel.getPricesMonthly());
          break;
        case R.id.detail_three_months_chip:
          chart.clearValues();
          checkedFilter = "3MONTHS";
          setDateChart(priceTableModel.getDateThreeMonths());
          setDataChart(priceTableModel.getPricesThreeMonths());
          break;
        case R.id.detail_six_months_chip:
          chart.clearValues();
          checkedFilter = "6MONTHS";
          setDateChart(priceTableModel.getDateSixMonths());
          setDataChart(priceTableModel.getPricesSixMonths());
          break;
        case R.id.detail_all_chip:
          chart.clearValues();
          checkedFilter = "ALL";
          setDateChart(priceTableModel.getDateAllMonths());
          setDataChart(priceTableModel.getPricesAllMonths());
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

  private static class TableTaskParams {
    boolean isToman;
    JSONObject response;

    TableTaskParams(JSONObject response, boolean isToman) {
      this.response = response;
      this.isToman = isToman;
    }
  }

  private class TableAsyncTask extends AsyncTask<TableTaskParams, Progress, PriceTableModel> {

    @Override
    protected PriceTableModel doInBackground(TableTaskParams... params) {
      PriceTableModel model = new PriceTableModel();
      try {
        model = JSONParser.priceTableList(params[0].response, params[0].isToman);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return model;
    }

    @Override
    protected void onPostExecute(PriceTableModel result) {
      priceTableModel = result;

      // if today chart is empty, Hide the chip filter
      if (priceTableModel.getDateDaily().isEmpty() && priceTableModel.getPricesDaily().isEmpty())
        detail_daily_chip.setVisibility(View.GONE);

      // if monthly chart is empty, Hide the chip filter
      if (priceTableModel.getDateMonthly().isEmpty() && priceTableModel.getPricesMonthly().isEmpty()) {
        detail_monthly_chip.setVisibility(View.GONE);
        checkedFilter = "3MONTHS";
      }
      //Load filters and chart
      loadChips();
      showChart();
    }
  }
}
