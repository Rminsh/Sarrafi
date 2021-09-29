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

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.model.FavoriteModel;
import com.shalchian.sarrafi.utils.ItemTouchHelperAdapter;
import com.shalchian.sarrafi.utils.ItemTouchHelperViewHolder;
import com.shalchian.sarrafi.utils.OnStartDragListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class EditListAdapter extends RecyclerView.Adapter<EditListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

  private ArrayList<FavoriteModel> data;

  private final OnStartDragListener mDragStartListener;

  public EditListAdapter(ArrayList<FavoriteModel> data, OnStartDragListener dragStartListener) {
    mDragStartListener = dragStartListener;
    this.data = data;
  }

  @NotNull
  @Override
  public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_list, parent, false);
    return new ItemViewHolder(view);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public void onBindViewHolder(final ItemViewHolder holder, int position) {
    holder.textView.setText(data.get(position).getName());

    // Start a drag whenever the handle view it touched
    holder.handleView.setOnTouchListener((v, event) -> {
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          mDragStartListener.onStartDrag(holder);
          break;
        case MotionEvent.ACTION_UP:
          v.performClick();
          break;
        default:
          break;
      }
      return false;
    });

    holder.removeView.setOnClickListener(view -> {
      data.remove(holder.getAdapterPosition());
      notifyItemRemoved(holder.getAdapterPosition());
      notifyItemRangeChanged(holder.getAdapterPosition(), data.size());
    });
  }

  @Override
  public void onItemDismiss(int position) {
    data.remove(position);
    notifyItemRemoved(position);
    notifyItemRangeChanged(position, data.size());
  }

  @Override
  public boolean onItemMove(int fromPosition, int toPosition) {
    Collections.swap(data, fromPosition, toPosition);
    notifyItemMoved(fromPosition, toPosition);
    return true;
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  /**
   * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
   * "handle" view that initiates a drag event when touched.
   */
  public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    public final TextView textView;
    public final ImageView handleView, removeView;

    public ItemViewHolder(View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.item_edit_text);
      handleView = itemView.findViewById(R.id.item_edit_handle);
      removeView = itemView.findViewById(R.id.item_edit_remove);
    }

    @Override
    public void onItemSelected() {
      itemView.setBackgroundColor(Color.parseColor("#3C446A"));
    }

    @Override
    public void onItemClear() {
      itemView.setBackgroundColor(0);
    }
  }

  public ArrayList<FavoriteModel> getList() {
    return data;
  }

}