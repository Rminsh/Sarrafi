package com.shalchian.sarrafi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.shalchian.sarrafi.model.StaticPriceModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import com.shalchian.sarrafi.R;

public class SymbolsAdapter extends RecyclerView.Adapter<SymbolsAdapter.viewHolder> {

  private ArrayList<StaticPriceModel> data;
  private StaticPriceModel model;
  private Context context;

  public SymbolsAdapter(ArrayList<StaticPriceModel> data, Context context) {
    this.data = data;
    this.context = context;
  }

  static class viewHolder extends RecyclerView.ViewHolder {

    View item_symbol_layout;
    ImageView item_symbol_flag;
    TextView item_symbol_text;


    viewHolder(View itemView) {
      super(itemView);
      item_symbol_layout = itemView.findViewById(R.id.item_symbol_layout);
      item_symbol_flag = itemView.findViewById(R.id.item_symbol_flag);
      item_symbol_text = itemView.findViewById(R.id.item_symbol_text);
    }
  }

  @NotNull
  @Override
  public viewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_select_symbol, parent, false);

    return new viewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NotNull viewHolder holder, int position) {

    model = data.get(position);
    holder.item_symbol_text.setText(model.getName());
    holder.item_symbol_flag.setImageDrawable(model.getDrawable());

    holder.item_symbol_layout.setOnClickListener(view -> {
      model = data.get(position);
      Toast.makeText(context, model.getExchange(), Toast.LENGTH_LONG).show();
    });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

}

