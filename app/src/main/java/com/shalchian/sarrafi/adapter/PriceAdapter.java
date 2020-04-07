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

package com.shalchian.sarrafi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.activity.DetailActivity;
import com.shalchian.sarrafi.dialog.DetailsSheet;
import com.shalchian.sarrafi.model.PriceModel;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.viewHolder> {

  private ArrayList<PriceModel> data;
  private PriceModel model;
  private Context context;

  public PriceAdapter(ArrayList<PriceModel> data, Context context) {
    this.data = data;
    this.context = context;
  }

  static class viewHolder extends RecyclerView.ViewHolder {

    CardView item_prices_card;
    View     item_prices_linear;
    TextView item_prices_type;
    TextView item_prices_price;
    TextView item_prices_date;
    TextView item_prices_percent;


    viewHolder(View itemView) {
      super(itemView);
      item_prices_card = itemView.findViewById(R.id.item_prices_card);
      item_prices_linear = itemView.findViewById(R.id.item_prices_linear);
      item_prices_type = itemView.findViewById(R.id.item_prices_type);
      item_prices_price = itemView.findViewById(R.id.item_prices_price);
      item_prices_date = itemView.findViewById(R.id.item_prices_date);
      item_prices_percent = itemView.findViewById(R.id.item_prices_percent);
    }
  }

  @NotNull
  @Override
  public viewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_prices, parent, false);

    return new viewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NotNull viewHolder holder, int position) {

    model = data.get(position);

    String price = model.getPrice() + " " + model.getToCurrency();
    String percent_change = model.getPercent_change() + "٪";
    holder.item_prices_type.setText(model.getType());
    holder.item_prices_price.setText(price);
    holder.item_prices_date.setText(model.getTime());

    switch (model.getStatus()) {
      case "low":
        holder.item_prices_percent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0 );
        holder.item_prices_linear.setBackgroundResource(R.drawable.gradient_purple);
        break;
      case "high":
        holder.item_prices_percent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up, 0, 0, 0 );
        holder.item_prices_linear.setBackgroundResource(R.drawable.gradient_new_leaf);
        break;
      default:
        percent_change = "بدون تغییر";
        holder.item_prices_percent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0 );
        holder.item_prices_linear.setBackgroundResource(R.drawable.gradient_ocean_blue);
          break;
    }
    holder.item_prices_percent.setText(percent_change);

    holder.item_prices_card.setOnClickListener(view -> {
      Intent intent = new Intent(context, DetailActivity.class);
      intent.putExtra("TYPE", data.get(position).getType());
      intent.putExtra("OBJECT", data.get(position).getObjName());
      intent.putExtra("STATUS", data.get(position).getStatus());
      intent.putExtra("TOCURRENCY", data.get(position).getToCurrency());
      intent.putExtra("PRICE", data.get(position).getPrice());
      intent.putExtra("PRICE_UP", data.get(position).getPrice_high());
      intent.putExtra("PRICE_DOWN", data.get(position).getPrice_low());
      intent.putExtra("PRICE_CHANGE", data.get(position).getPrice_change());
      intent.putExtra("PERCENT_CHANGE", String.valueOf(data.get(position).getPercent_change()));
      intent.putExtra("UPDATE", data.get(position).getTime());
      context.startActivity(intent);
    });

    holder.item_prices_card.setOnLongClickListener(view -> {
      DetailsSheet.openDetailSheet(context, data.get(position));
      return true;
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

}
