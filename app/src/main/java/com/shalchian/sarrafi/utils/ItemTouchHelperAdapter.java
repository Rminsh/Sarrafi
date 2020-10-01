
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

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperAdapter {

  /**
   * Called when an item has been dragged far enough to trigger a move. This is called every time
   * an item is shifted, and <strong>not</strong> at the end of a "drop" event.<br/>
   * <br/>
   * Implementations should call {@link RecyclerView.Adapter#notifyItemMoved(int, int)} after
   * adjusting the underlying data to reflect this move.
   *
   * @param fromPosition The start position of the moved item.
   * @param toPosition   Then resolved position of the moved item.
   * @return True if the item was moved to the new adapter position.
   *
   * @see RecyclerView.ViewHolder#getAdapterPosition()
   */
  boolean onItemMove(int fromPosition, int toPosition);


  /**
   * Called when an item has been dismissed by a swipe.<br/>
   * <br/>
   * Implementations should call {@link RecyclerView.Adapter#notifyItemRemoved(int)} after
   * adjusting the underlying data to reflect this removal.
   *
   * @param position The position of the item dismissed.
   *
   * @see RecyclerView.ViewHolder#getAdapterPosition()
   */
  void onItemDismiss(int position);
}