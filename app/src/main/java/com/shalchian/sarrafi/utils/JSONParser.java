package com.shalchian.sarrafi.utils;

import android.util.Log;

import com.shalchian.sarrafi.model.PriceModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {
  public static ArrayList<PriceModel> priceList(JSONObject response, String checkedFilter) throws JSONException {
    ArrayList<PriceModel> list = new ArrayList<>();

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

      JSONObject crypto_bitcoin = jsonData.getJSONObject("crypto-bitcoin");
      JSONObject crypto_ethereum = jsonData.getJSONObject("crypto-ethereum");
      JSONObject crypto_ripple = jsonData.getJSONObject("crypto-ripple");
      JSONObject crypto_dash = jsonData.getJSONObject("crypto-dash");
      JSONObject crypto_litecoin = jsonData.getJSONObject("crypto-litecoin");
      JSONObject crypto_stellar = jsonData.getJSONObject("crypto-stellar");

      if (checkedFilter.equals("currency") || checkedFilter.equals("")) {
        list.add(addObject("price_dollar_rl","دلار", price_dollar_rl, "ریال" ));
        list.add(addObject("price_dollar_soleymani","دلار سلیمانیه", price_dollar_soleymani, "ریال" ));
        list.add(addObject("price_eur","یورو", price_eur, "ریال" ));
        list.add(addObject("price_cad","دلار کانادا", price_cad, "ریال" ));
        list.add(addObject("price_gbp","پوند انگلیس", price_gbp, "ریال" ));
        list.add(addObject("price_aed","درهم امارات", price_aed, "ریال" ));
        list.add(addObject("price_try","لیر ترکیه", price_try, "ریال" ));
        list.add(addObject("price_cny","یوان چین", price_cny, "ریال" ));
        list.add(addObject("price_jpy","ین ژاپن", price_jpy, "ریال" ));
        list.add(addObject("price_afn","افغانی", price_afn, "ریال" ));
        list.add(addObject("price_iqd","دینار عراق", price_iqd, "ریال" ));
        list.add(addObject("price_myr","رینگت مالزی", price_myr, "ریال" ));
        list.add(addObject("price_rub","روبل روسیه", price_rub, "ریال" ));
      }

      if (checkedFilter.equals("gold") || checkedFilter.equals("")) {
        list.add(addObject("sekee","سکه امامی", sekee, "ریال" ));
        list.add(addObject("sekeb","سکه بهار آزادی", sekeb, "ریال" ));
        list.add(addObject("nim","نیم سکه", nim, "ریال" ));
        list.add(addObject("rob","ربع سکه", rob, "ریال" ));
        list.add(addObject("geram24","طلای ۲۴ عیار", geram24, "ریال" ));
        list.add(addObject("geram18","طلای ۱۸ عیار", geram18, "ریال" ));
        list.add(addObject("mesghal","مثقال طلا", mesghal, "ریال" ));
        list.add(addObject("gerami","سکه گرمی", gerami, "ریال" ));
        list.add(addObject("ons","انس طلا", ons, "ریال" ));
        list.add(addObject("silver","انس نقره", silver, "ریال" ));
        list.add(addObject("gold_mini_size","طلای دست دوم", gold_mini_size, "ریال" ));
      }

      if (checkedFilter.equals("oil") || checkedFilter.equals("")) {
        list.add(addObject("oil","نفت سبک", oil, "دلار" ));
        list.add(addObject("oil_brent","نفت برنت", oil_brent, "دلار" ));
        list.add(addObject("oil_opec","نفت اوپک", oil_opec, "دلار" ));
      }

      if (checkedFilter.equals("digital_currency") || checkedFilter.equals("")) {
        list.add(addObject("crypto-bitcoin","بیت کوین / Bitcoin", crypto_bitcoin, "دلار" ));
        list.add(addObject("crypto-ethereum","اتریوم / Ethereum", crypto_ethereum, "دلار" ));
        list.add(addObject("crypto-ripple","ریپل / Ripple", crypto_ripple,  "دلار" ));
        list.add(addObject("crypto-dash","دش / Dash", crypto_dash,  "دلار" ));
        list.add(addObject("crypto-litecoin","لایت کوین / Litecoin", crypto_litecoin,  "دلار" ));
        list.add(addObject("crypto-stellar","استلار / Stellar", crypto_stellar,  "دلار" ));
      }
    }

    return list;
  }

  private static PriceModel addObject(String objectName, String name, JSONObject object, String toCurrency) {
    PriceModel priceModel = null;
    Log.e("NAME PRICE",name);
    try {
      priceModel = new PriceModel(
              objectName,
              name,
              toCurrency,
              object.getString("p"),
              object.getString("h"),
              object.getString("l"),
              object.getString("d"),
              object.getDouble("dp"),
              object.getString("dt"),
              object.getString("t"));
    } catch (JSONException e) {
      Log.e("ERROR EXCEPTION", String.valueOf(e));
    }
    return priceModel;
  }
}
