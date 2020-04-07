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

package com.shalchian.sarrafi.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.model.PriceModel;
import com.shalchian.sarrafi.utils.ActivityHelper;

public class DetailsSheet {

  public static void openDetailSheet(Context context , PriceModel priceModel) {
    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View bottomSheetView = Objects.requireNonNull(layoutInflater).inflate(R.layout.bottom_sheet_details, null);
    BottomSheetDialog dialog = new BottomSheetDialog(context,R.style.AppBottomSheetDialogTheme);
    TextView popup_header = bottomSheetView.findViewById(R.id.popup_header);
    TextView popup_header_price = bottomSheetView.findViewById(R.id.popup_header_price);
    TextView popup_price_high = bottomSheetView.findViewById(R.id.popup_price_high);
    TextView popup_price_low = bottomSheetView.findViewById(R.id.popup_price_low);
    TextView popup_price_change = bottomSheetView.findViewById(R.id.popup_price_change);
    TextView popup_price_percent_change = bottomSheetView.findViewById(R.id.popup_price_percent_change);
    TextView popup_date = bottomSheetView.findViewById(R.id.popup_date);

    Button popup_share = bottomSheetView.findViewById(R.id.popup_share_button);

    String price = priceModel.getPrice() +  " " + priceModel.getToCurrency();
    String priceHigh = priceModel.getPrice_high() +  " " + priceModel.getToCurrency();
    String priceLow = priceModel.getPrice_low() +  " " + priceModel.getToCurrency();
    String price_change = priceModel.getPrice_change() +  " " + priceModel.getToCurrency();
    String percent_change = priceModel.getPercent_change() + "٪";
    String price_date = context.getResources().getString(R.string.last_update) + priceModel.getTime();

    popup_header.setText(priceModel.getType());
    popup_header_price.setText(price);
    popup_price_high.setText(priceHigh);
    popup_price_low.setText(priceLow);
    popup_price_change.setText(price_change);
    popup_price_percent_change.setText(percent_change);
    popup_date.setText(price_date);

    switch (priceModel.getStatus()) {
      case "low":
        popup_price_change.setTextColor(context.getResources().getColor(R.color.price_down));
        popup_price_percent_change.setTextColor(context.getResources().getColor(R.color.price_down));
        break;

      case "high":
        popup_price_change.setTextColor(context.getResources().getColor(R.color.price_up));
        popup_price_percent_change.setTextColor(context.getResources().getColor(R.color.price_up));
        break;

      default:
        break;
    }

    popup_share.setOnClickListener(view -> {
      String header = "🏷 " + priceModel.getType() + priceModel.getPrice() +  " " + priceModel.getToCurrency();
      String priceUp = "📈 " + context.getResources().getString(R.string.price_high) + " " + priceModel.getPrice_high() +  " " + priceModel.getToCurrency();
      String priceDown = "📉 " + context.getResources().getString(R.string.price_low) + " " + priceModel.getPrice_low() +  " " + priceModel.getToCurrency();
      String priceChange = "🧮 " + context.getResources().getString(R.string.price_change) + " " + priceModel.getPrice_change() +  " " + priceModel.getToCurrency();
      String pricePercentChange = "📊 " + context.getResources().getString(R.string.price_percent_change) + " " + priceModel.getPercent_change() + "٪";
      String time = "🕰 " + context.getResources().getString(R.string.last_update) + " " + priceModel.getTime();
      ActivityHelper.shareText(context, header, priceUp + "\n" + priceDown + "\n" + priceChange + "\n" + pricePercentChange + "\n" + time);
    });

    dialog.setContentView(bottomSheetView);
    dialog.show();
  }
}
