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

package com.shalchian.sarrafi.utils;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

import com.shalchian.sarrafi.R;

public class TableMarker extends MarkerView {

  private TextView marker_price;
  private TextView marker_date;
  private MPPointF mOffset;
  private List<String> date;
  private String priceType;

  public TableMarker(Context context, int layoutResource, List<String> date, String priceType) {
    super(context, layoutResource);
    marker_price = findViewById(R.id.marker_price);
    marker_date = findViewById(R.id.marker_date);
    this.date = date;
    this.priceType = priceType;
  }

  @Override
  public void refreshContent(Entry e, Highlight highlight) {
    String yValue = e.getY() + " " + priceType;
    String xValue = "تاریخ " + date.get((int) e.getX());
    marker_price.setText(yValue);
    marker_date.setText(xValue);

    super.refreshContent(e, highlight);
  }

  @Override
  public MPPointF getOffset() {

    if(mOffset == null) {
      // center the marker horizontally and vertically
      mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
    }

    return mOffset;
  }
}