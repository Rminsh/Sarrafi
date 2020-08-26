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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.db.DatabaseManager;
import com.shalchian.sarrafi.model.PriceModel;

import java.util.List;

public class PriceListAdapterFactory implements RemoteViewsService.RemoteViewsFactory {
  private Context context;
  private List<PriceModel> priceModels;

  PriceListAdapterFactory(Context context) {
    this.context = context;
  }

  @Override
  public int getCount() {
    // Update beer list from database every time
    // when notify data changed was called
    priceModels = DatabaseManager.getInstance().getPriceList();
    return priceModels != null ? priceModels.size() : 0;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public RemoteViews getLoadingView() {
    return null;
  }

  @Override
  public RemoteViews getViewAt(int position) {
    final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.edge_plus_item_prices);
    try {
      PriceModel priceModel = priceModels.get(position);
      rv.setImageViewBitmap(R.id.edge_plus_prices_type, SetImageTypeface(priceModel.getType(), (int) context.getResources().getDimension(R.dimen.edge_item_medium), "Shabnam-FD"));
      rv.setImageViewBitmap(R.id.edge_plus_prices_price, SetImageTypeface(priceModel.getPrice() + " " + priceModel.getToCurrency(), (int) context.getResources().getDimension(R.dimen.edge_item_large), "Shabnam-Medium-FD"));
      rv.setImageViewBitmap(R.id.edge_plus_prices_percent, SetImageTypeface((priceModel.getPercent_change() + "٪"), (int) context.getResources().getDimension(R.dimen.edge_item_small), "Shabnam-FD"));
      rv.setImageViewBitmap(R.id.edge_plus_prices_date, SetImageTypeface(priceModel.getTime(), (int) context.getResources().getDimension(R.dimen.edge_item_small), "Shabnam-FD"));

      switch (priceModel.getStatus()) {
        case "low":
          rv.setInt(R.id.edge_plus_prices_layout, "setBackgroundResource",R.drawable.gradient_purple);
          //rv.setImageViewResource(R.id.widget_item_status, R.drawable.ic_price_down);
          //rv.setInt(R.id.widget_item_percent, "setTextColor",mContext.getResources().getColor(R.color.price_down));
          //rv.setInt(R.id.widget_item_price_change, "setTextColor",mContext.getResources().getColor(R.color.price_down));
          break;
        case "high":
          rv.setInt(R.id.edge_plus_prices_layout, "setBackgroundResource",R.drawable.gradient_new_leaf);
          //rv.setImageViewResource(R.id.widget_item_status, R.drawable.ic_price_up);
          //rv.setInt(R.id.widget_item_percent, "setTextColor",mContext.getResources().getColor(R.color.price_up));
          //rv.setInt(R.id.widget_item_price_change, "setTextColor",mContext.getResources().getColor(R.color.price_up));
          break;
        default:
          rv.setInt(R.id.edge_plus_prices_layout, "setBackgroundResource",R.drawable.gradient_ocean_blue);
          break;
      }
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    }
    return rv;
  }

  @Override
  public int getViewTypeCount() {
    return 1;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public void onCreate() {
  }

  @Override
  public void onDataSetChanged() {
  }

  @Override
  public void onDestroy() {
  }

  private Bitmap SetImageTypeface(String label, int size, String font) {
    Paint paint = new Paint();
    paint.setTextSize(size);
    Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + font + ".ttf");
    paint.setTypeface(typeface);
    paint.setColor(0xffffffff);
    paint.setTextAlign(Paint.Align.LEFT);
    paint.setSubpixelText(true);
    paint.setAntiAlias(true);
    float baseline = -paint.ascent();
    int width = (int) (paint.measureText(label) * 1f);
    int height = (int) (baseline + paint.descent() * 0.7);
    Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
    Canvas canvas = new Canvas(image);
    canvas.drawText(label, 0, baseline, paint);
    return image;
  }
}
