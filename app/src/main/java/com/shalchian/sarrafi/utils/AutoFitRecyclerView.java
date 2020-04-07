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
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AutoFitRecyclerView extends RecyclerView {
  private GridLayoutManager manager;
  private int columnWidth = -1; //default value

  public AutoFitRecyclerView(Context context) {
    super(context);
    init(context, null);
  }

  public AutoFitRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public AutoFitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    if (attrs != null) {
      int[] attrsArray = {android.R.attr.columnWidth};
      TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
      columnWidth = array.getDimensionPixelSize(0, -1);
      array.recycle();
    }

    manager = new GridLayoutManager(getContext(), 1);
    setLayoutManager(manager);
  }

  @Override
  protected void onMeasure(int widthSpec, int heightSpec) {
    super.onMeasure(widthSpec, heightSpec);
    if (columnWidth > 0) {
      int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
      manager.setSpanCount(spanCount);
    }
  }
}
