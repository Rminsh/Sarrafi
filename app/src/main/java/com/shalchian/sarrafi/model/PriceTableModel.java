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

import java.util.List;

public class PriceTableModel {

  private List<String> dateDaily, dateMonthly, dateThreeMonths, dateSixMonths, dateAllMonths;
  private List<Float> pricesDaily, pricesMonthly, pricesThreeMonths, pricesSixMonths, pricesAllMonths;

  public List<String> getDateDaily() {
    return dateDaily;
  }

  public void setDateDaily(List<String> dateDaily) {
    this.dateDaily = dateDaily;
  }

  public List<Float> getPricesDaily() {
    return pricesDaily;
  }

  public void setPricesDaily(List<Float> pricesDaily) {
    this.pricesDaily = pricesDaily;
  }

  public List<String> getDateMonthly() {
    return dateMonthly;
  }

  public void setDateMonthly(List<String> dateMonthly) {
    this.dateMonthly = dateMonthly;
  }

  public List<Float> getPricesMonthly() {
    return pricesMonthly;
  }

  public void setPricesMonthly(List<Float> pricesMonthly) {
    this.pricesMonthly = pricesMonthly;
  }

  public List<String> getDateThreeMonths() {
    return dateThreeMonths;
  }

  public void setDateThreeMonths(List<String> dateThreeMonths) {
    this.dateThreeMonths = dateThreeMonths;
  }

  public List<Float> getPricesThreeMonths() {
    return pricesThreeMonths;
  }

  public void setPricesThreeMonths(List<Float> pricesThreeMonths) {
    this.pricesThreeMonths = pricesThreeMonths;
  }

  public List<String> getDateSixMonths() {
    return dateSixMonths;
  }

  public void setDateSixMonths(List<String> dateSixMonths) {
    this.dateSixMonths = dateSixMonths;
  }

  public List<Float> getPricesSixMonths() {
    return pricesSixMonths;
  }

  public void setPricesSixMonths(List<Float> pricesSixMonths) {
    this.pricesSixMonths = pricesSixMonths;
  }

  public List<String> getDateAllMonths() {
    return dateAllMonths;
  }

  public void setDateAllMonths(List<String> dateAllMonths) {
    this.dateAllMonths = dateAllMonths;
  }

  public List<Float> getPricesAllMonths() {
    return pricesAllMonths;
  }

  public void setPricesAllMonths(List<Float> pricesAllMonths) {
    this.pricesAllMonths = pricesAllMonths;
  }

}
