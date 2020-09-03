
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

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.shalchian.sarrafi.model.PriceModel;
import com.shalchian.sarrafi.model.PriceTableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {
  public static ArrayList<PriceModel> priceList(JSONObject response, String checkedFilter, Context context) throws JSONException {
    ArrayList<PriceModel> list = new ArrayList<>();

    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    boolean tomanConvert = preferences.getBoolean("toman", false);
    String iran_currency = tomanConvert ? "تومان" : "ریال";
    
    JSONObject jsonData = response.optJSONObject("current");
    if (jsonData != null) {
      JSONObject price_dollar_rl = jsonData.getJSONObject("price_dollar_rl");
      JSONObject price_eur              = jsonData.getJSONObject("price_eur");
      JSONObject price_dollar_soleymani = jsonData.getJSONObject("price_dollar_soleymani");
      JSONObject price_cad              = jsonData.getJSONObject("price_cad");
      JSONObject price_gbp              = jsonData.getJSONObject("price_gbp");
      JSONObject price_aed              = jsonData.getJSONObject("price_aed");
      JSONObject price_try              = jsonData.getJSONObject("price_try");
      JSONObject price_cny              = jsonData.getJSONObject("price_cny");
      JSONObject price_jpy              = jsonData.getJSONObject("price_jpy");
      JSONObject price_afn              = jsonData.getJSONObject("price_afn");
      JSONObject price_iqd              = jsonData.getJSONObject("price_iqd");
      JSONObject price_myr              = jsonData.getJSONObject("price_myr");
      JSONObject price_rub              = jsonData.getJSONObject("price_rub");

      JSONObject sekee            = jsonData.getJSONObject("sekee");
      JSONObject sekeb            = jsonData.getJSONObject("sekeb");
      JSONObject nim              = jsonData.getJSONObject("nim");
      JSONObject rob              = jsonData.getJSONObject("rob");
      JSONObject geram24          = jsonData.getJSONObject("geram24");
      JSONObject geram18          = jsonData.getJSONObject("geram18");
      JSONObject mesghal          = jsonData.getJSONObject("mesghal");
      JSONObject gerami           = jsonData.getJSONObject("gerami");
      JSONObject ons              = jsonData.getJSONObject("ons");
      JSONObject silver           = jsonData.getJSONObject("silver");
      JSONObject gold_mini_size   = jsonData.getJSONObject("gold_mini_size");

      JSONObject oil        = jsonData.getJSONObject("oil");
      JSONObject oil_brent  = jsonData.getJSONObject("oil_brent");
      JSONObject oil_opec   = jsonData.getJSONObject("oil_opec");
      JSONObject general_9  = jsonData.getJSONObject("general_9");
      JSONObject general_10  = jsonData.getJSONObject("general_10");
      JSONObject general_11  = jsonData.getJSONObject("general_11");

      JSONObject crypto_bitcoin = jsonData.getJSONObject("crypto-bitcoin");
      JSONObject crypto_ethereum = jsonData.getJSONObject("crypto-ethereum");
      JSONObject crypto_ripple = jsonData.getJSONObject("crypto-ripple");
      JSONObject crypto_dash = jsonData.getJSONObject("crypto-dash");
      JSONObject crypto_litecoin = jsonData.getJSONObject("crypto-litecoin");
      JSONObject crypto_stellar = jsonData.getJSONObject("crypto-stellar");

      if (checkedFilter.equals("currency") || checkedFilter.equals("")) {
        list.add(addObject("price_dollar_rl","دلار", price_dollar_rl, iran_currency, tomanConvert ));
        list.add(addObject("price_dollar_soleymani","دلار سلیمانیه", price_dollar_soleymani, iran_currency, tomanConvert ));
        list.add(addObject("price_eur","یورو", price_eur, iran_currency, tomanConvert ));
        list.add(addObject("price_cad","دلار کانادا", price_cad, iran_currency, tomanConvert ));
        list.add(addObject("price_gbp","پوند انگلیس", price_gbp, iran_currency, tomanConvert ));
        list.add(addObject("price_aed","درهم امارات", price_aed, iran_currency, tomanConvert ));
        list.add(addObject("price_try","لیر ترکیه", price_try, iran_currency, tomanConvert ));
        list.add(addObject("price_cny","یوان چین", price_cny, iran_currency, tomanConvert ));
        list.add(addObject("price_jpy","ین ژاپن", price_jpy, iran_currency, tomanConvert ));
        list.add(addObject("price_afn","افغانی", price_afn, iran_currency, tomanConvert ));
        list.add(addObject("price_iqd","دینار عراق", price_iqd, iran_currency, tomanConvert ));
        list.add(addObject("price_myr","رینگت مالزی", price_myr, iran_currency, tomanConvert ));
        list.add(addObject("price_rub","روبل روسیه", price_rub, iran_currency, tomanConvert ));
      }

      if (checkedFilter.equals("gold") || checkedFilter.equals("")) {
        list.add(addObject("sekee","سکه امامی", sekee, iran_currency, tomanConvert ));
        list.add(addObject("sekeb","سکه بهار آزادی", sekeb, iran_currency, tomanConvert ));
        list.add(addObject("nim","نیم سکه", nim, iran_currency, tomanConvert ));
        list.add(addObject("rob","ربع سکه", rob, iran_currency, tomanConvert ));
        list.add(addObject("geram24","طلای ۲۴ عیار", geram24, iran_currency, tomanConvert ));
        list.add(addObject("geram18","طلای ۱۸ عیار", geram18, iran_currency, tomanConvert ));
        list.add(addObject("mesghal","مثقال طلا", mesghal, iran_currency, tomanConvert ));
        list.add(addObject("gerami","سکه گرمی", gerami, iran_currency, tomanConvert ));
        list.add(addObject("ons","انس طلا", ons, "دلار", false ));
        list.add(addObject("silver","انس نقره", silver, "دلار", false ));
        list.add(addObject("gold_mini_size","طلای دست دوم", gold_mini_size, iran_currency, tomanConvert ));
      }

      if (checkedFilter.equals("oil") || checkedFilter.equals("")) {
        list.add(addObject("oil","نفت سبک (امریکا)", oil, "دلار", false ));
        list.add(addObject("oil_brent","نفت برنت", oil_brent, "دلار", false ));
        list.add(addObject("oil_opec","نفت اوپک", oil_opec, "دلار", false ));
        list.add(addObject("general_9","بنزین (گالن) (امریکا)", general_9, "دلار", false ));
        list.add(addObject("general_10","گاز طبیعی (امریکا)", general_10, "دلار", false ));
        list.add(addObject("general_11","گازوئیل (انگلیس)", general_11, "دلار", false ));
      }

      if (checkedFilter.equals("digital_currency") || checkedFilter.equals("")) {
        list.add(addObject("crypto-bitcoin","بیت کوین / Bitcoin", crypto_bitcoin, "دلار", false ));
        list.add(addObject("crypto-ethereum","اتریوم / Ethereum", crypto_ethereum, "دلار", false ));
        list.add(addObject("crypto-ripple","ریپل / Ripple", crypto_ripple,  "دلار", false ));
        list.add(addObject("crypto-dash","دش / Dash", crypto_dash,  "دلار", false ));
        list.add(addObject("crypto-litecoin","لایت کوین / Litecoin", crypto_litecoin,  "دلار", false ));
        list.add(addObject("crypto-stellar","استلار / Stellar", crypto_stellar,  "دلار", false ));
      }
    }

    return list;
  }

  private static PriceModel addObject(String objectName, String name, JSONObject object, String toCurrency, boolean tomanConvert) {
    PriceModel priceModel = null;
    try {
      String price = object.getString("p");
      String priceHigh = object.getString("h");
      String priceLow = object.getString("h");
      String priceChange = object.getString("d");

      // Change price if toman is selected in settings
      if (tomanConvert) {
        price = changeToToman(price, true);
        priceHigh = changeToToman(priceHigh, true);
        priceLow = changeToToman(priceLow, true);
        priceChange = changeToToman(priceChange, true);
      }

      priceModel = new PriceModel(
              objectName,
              name,
              toCurrency,
              price,
              priceHigh,
              priceLow,
              priceChange,
              object.getDouble("dp"),
              object.getString("dt"),
              object.getString("t"));
    } catch (JSONException e) {
      Log.e("ERROR EXCEPTION", String.valueOf(e));
    }
    return priceModel;
  }

  public static PriceTableModel priceTableList(JSONObject response, boolean isToman) throws JSONException {
    PriceTableModel model = new PriceTableModel();

    List<String> dateDaily = new ArrayList<>();
    List<Float> pricesDaily = new ArrayList<>();

    List<String> dateMonthly = new ArrayList<>();
    List<Float> pricesMonthly = new ArrayList<>();

    List<String> dateThreeMonths = new ArrayList<>();
    List<Float> pricesThreeMonths = new ArrayList<>();

    List<String> dateSixMonths = new ArrayList<>();
    List<Float> pricesSixMonths = new ArrayList<>();

    List<String> dateAllMonths = new ArrayList<>();
    List<Float> pricesAllMonths = new ArrayList<>();

    // Get Today Chart
    JSONArray today_table = response.getJSONArray("today_table");
    for (int i = today_table.length() - 1; i >= 0; i--) {
      try {
        JSONObject jo = today_table.getJSONObject(i);
        String time = jo.getString("time");
        String priceValue = jo.getString("price").replace(",", "");
        if (isToman)
          priceValue = changeToToman(priceValue, false);
        float price = Float.parseFloat(priceValue);
        dateDaily.add(time);
        pricesDaily.add(price);
      } catch (JSONException e) {
        Log.e("🔴ERROR Parse Daily", String.valueOf(e));
      }
    }

    // Get Monthly Chart
    String chart_1 = "[" + response.getString("chart_1").replaceFirst(".$","") + "]";
    JSONArray chart_1_Array = new JSONArray(chart_1);

    for (int i = 0; i < chart_1_Array.length(); i++) {
      try {
        JSONObject jo = chart_1_Array.getJSONObject(i);
        String date_string = jo.getString("jdate");
        date_string = date_string.substring(date_string.indexOf("/") + 1);
        String priceValue = String.valueOf(jo.getDouble("value"));
        if (isToman)
          priceValue = changeToToman(priceValue, false);
        float price = Float.parseFloat(priceValue);
        dateMonthly.add(date_string);
        pricesMonthly.add(price);
      } catch (JSONException e) {
        Log.e("🔴ERROR Parse Monthly", String.valueOf(e));
      }
    }

    // Get 3 Months Chart
    String chart_3 = "[" + response.getString("chart_3").replaceFirst(".$","") + "]";
    JSONArray chart_3_Array = new JSONArray(chart_3);

    for (int i = 0; i < chart_3_Array.length(); i++) {
      try {
        JSONObject jo = chart_3_Array.getJSONObject(i);
        String date_string = jo.getString("jdate");
        date_string = date_string.substring(date_string.indexOf("/") + 1);
        String priceValue = String.valueOf(jo.getDouble("value"));
        if (isToman)
          priceValue = changeToToman(priceValue, false);
        float price = Float.parseFloat(priceValue);
        dateThreeMonths.add(date_string);
        pricesThreeMonths.add(price);
      } catch (JSONException e) {
        Log.e("🔴ERROR Parse 3 Months", String.valueOf(e));
      }
    }

    // Get 6 Months Chart
    String chart_6 = "[" + response.getString("chart_6").replaceFirst(".$","") + "]";
    JSONArray chart_6_Array = new JSONArray(chart_6);

    for (int i = 0; i < chart_6_Array.length(); i++) {
      try {
        JSONObject jo = chart_6_Array.getJSONObject(i);
        String date_string = jo.getString("jdate");
        date_string = date_string.substring(date_string.indexOf("/") + 1);
        String priceValue = String.valueOf(jo.getDouble("value"));
        if (isToman)
          priceValue = changeToToman(priceValue, false);
        float price = Float.parseFloat(priceValue);
        dateSixMonths.add(date_string);
        pricesSixMonths.add(price);
      } catch (JSONException e) {
        Log.e("🔴ERROR Parse 6 Months", String.valueOf(e));
      }
    }

    // Get Chart Summery
    String chart_summary = "[" + response.getString("chart_summary") + "]";
    JSONArray chart_summary_Array = new JSONArray(chart_summary);

    for (int i = 0; i < chart_summary_Array.length(); i++) {
      try {
        JSONObject jo = chart_summary_Array.getJSONObject(i);
        String date_string = jo.getString("jdate");
        int index = date_string.lastIndexOf('/');
        String priceValue = String.valueOf(jo.getDouble("value"));
        if (isToman)
          priceValue = changeToToman(priceValue, false);
        float price = Float.parseFloat(priceValue);
        dateAllMonths.add(date_string.substring(0,index));
        pricesAllMonths.add(price);
      } catch (JSONException e) {
        Log.e("🔴ERROR Parse All", String.valueOf(e));
      }
    }

    model.setDateDaily(dateDaily);
    model.setPricesDaily(pricesDaily);
    model.setDateMonthly(dateMonthly);
    model.setPricesMonthly(pricesMonthly);
    model.setDateThreeMonths(dateThreeMonths);
    model.setPricesThreeMonths(pricesThreeMonths);
    model.setDateSixMonths(dateSixMonths);
    model.setPricesSixMonths(pricesSixMonths);
    model.setDateAllMonths(dateAllMonths);
    model.setPricesAllMonths(pricesAllMonths);

    return model;
  }

  public static String changeToToman(String price, boolean commaNeeded) {
    price = price.replace(",", "");
    double priceValue = Double.parseDouble(price) / 10;

    NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setGroupingUsed(false);
    price = numberFormat.format(priceValue);

    if (commaNeeded)
      price = addComma(price);

    return price;
  }

  public static String addComma(String str) {
    int floatPos = str.contains(".") ? str.length() - str.indexOf(".") : 0;
    int nGroups= (str.length()-floatPos-1-(str.contains("-") ?1:0))/3;
    for(int i=0; i<nGroups; i++){
      int commaPos = str.length() - i * 4 - 3 - floatPos;
      str = str.substring(0,commaPos) + "," + str.substring(commaPos);
    }
    return str;
  }
}
