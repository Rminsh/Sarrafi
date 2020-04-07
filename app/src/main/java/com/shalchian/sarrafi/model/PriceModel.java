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

package com.shalchian.sarrafi.model;

public class PriceModel {

  private String objName, type, toCurrency, price, price_high, price_low, status, time, price_change;
  private Double percent_change;

  public String getObjName() {
    return objName;
  }

  public String getType() {
    return type;
  }

  public String getToCurrency() {
    return toCurrency;
  }

  public String getPrice() {
    return price;
  }

  public String getPrice_high() {
    return price_high;
  }

  public String getPrice_low() {
    return price_low;
  }

  public String getPrice_change() {
    return price_change;
  }

  public Double getPercent_change() {
    return percent_change;
  }

  public String getStatus() {
    return status;
  }

  public String getTime() {
    return time;
  }

  public PriceModel(String objName,String type, String toCurrency, String price, String price_high, String price_low, String price_change , Double percent_change, String status, String time) {
    this.objName = objName;
    this.type = type;
    this.toCurrency = toCurrency;
    this.price = price;
    this.price_high = price_high;
    this.price_low = price_low;
    this.price_change = price_change;
    this.percent_change = percent_change;
    this.status = status;
    this.time = time;
  }

}