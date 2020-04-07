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

public class PriceModelLegacy {
  private String objectName, name, toCurrency, price, date, status;
  private Double percent;

  public String getObjectName() {
    return objectName;
  }

  public String getName() {
    return name;
  }

  public String getToCurrency() {
    return toCurrency;
  }

  public String getPrice() {
    return price;
  }

  public Double getPercent() {
    return percent;
  }

  public String getDate() {
    return date;
  }

  public String getStatus() {
    return status;
  }

  public PriceModelLegacy(String objectName, String name, String toCurrency, String price, Double percent, String status, String date) {
    this.objectName = objectName;
    this.name = name;
    this.toCurrency = toCurrency;
    this.price = price;
    this.percent = percent;
    this.date = date;
    this.status = status;
  }
}
