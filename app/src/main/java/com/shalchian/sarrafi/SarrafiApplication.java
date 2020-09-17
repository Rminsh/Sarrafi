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

package com.shalchian.sarrafi;

import androidx.multidex.MultiDexApplication;

import com.shalchian.sarrafi.db.DatabaseManager;
import com.androidnetworking.AndroidNetworking;

public class SarrafiApplication extends MultiDexApplication {

  @Override
  public void onCreate() {
    super.onCreate();
    DatabaseManager.getInstance().init(getApplicationContext());
    AndroidNetworking.initialize(getApplicationContext());
  }
}