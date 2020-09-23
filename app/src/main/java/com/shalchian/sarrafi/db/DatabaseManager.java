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

package com.shalchian.sarrafi.db;

import android.content.Context;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.shalchian.sarrafi.model.PriceModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
  private static final String KEY_EXCHANGE = "exchange_list";
  private static final String KEY_FAVORITE = "favorite_list";
  private static DatabaseManager databaseManager;

  public static DatabaseManager getInstance() {
    if (databaseManager == null) {
      databaseManager = new DatabaseManager();
    }
    return databaseManager;
  }

  public void init(Context context) {
    Hawk.init(context.getApplicationContext()).build();
  }

  public void deletePriceList() {
    Hawk.delete(KEY_EXCHANGE);
  }

  public void setPriceList(List<PriceModel> priceList) {
    boolean status = Hawk.put(KEY_EXCHANGE, priceList);
  }

  public List<PriceModel> getPriceList() {
    return Hawk.get(KEY_EXCHANGE);
  }

  public boolean isPriceListAvailable() {
    List<PriceModel> priceList = getPriceList();
    return priceList != null && !priceList.isEmpty();
  }

  public void deleteFavoriteList() {
    Hawk.delete(KEY_FAVORITE);
  }

  public void deleteFromFavoriteList(String item) {
    ArrayList<String> favoriteList = getFavoriteList();
    favoriteList.remove(item);
    setFavoriteList(favoriteList);
  }

  public void addToFavoriteList(String item) {
    ArrayList<String> favoriteList = getFavoriteList();
    if (favoriteList == null)
      favoriteList = new ArrayList<>();
    favoriteList.add(item);
    setFavoriteList(favoriteList);
    Log.e("⭐️ FAVORITE LIST:", String.valueOf(favoriteList));
  }

  public void setFavoriteList(ArrayList<String> favoriteList) {
    boolean status = Hawk.put(KEY_FAVORITE, favoriteList);
  }

  public ArrayList<String> getFavoriteList() {
    return Hawk.get(KEY_FAVORITE);
  }

  public boolean isFavoriteListAvailable() {
    ArrayList<String> favoriteList = getFavoriteList();
    return favoriteList != null && !favoriteList.isEmpty();
  }
}
