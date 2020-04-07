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

import com.orhanobut.hawk.Hawk;
import com.shalchian.sarrafi.model.PriceModelLegacy;

import java.util.List;

public class DatabaseManager {
  private static final String KEY_EXCHANGE = "exchange";
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

  public void setPriceList(List<PriceModelLegacy> priceList) {
    boolean status = Hawk.put(KEY_EXCHANGE, priceList);
  }

  public List<PriceModelLegacy> getPriceList() {
    return Hawk.get(KEY_EXCHANGE);
  }

  public boolean isPriceListAvailable() {
    List<PriceModelLegacy> priceList = getPriceList();
    return priceList != null && !priceList.isEmpty();
  }
}
