package com.shalchian.sarrafi.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.model.StaticPriceModel;
import com.shalchian.sarrafi.model.UnitItem;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class CurrenciesResourceParser {
  public static ArrayList<StaticPriceModel> processXMLData(Context context, XmlResourceParser parser)throws IOException, XmlPullParserException {
    int eventType = -1;

    ArrayList<StaticPriceModel> items = new ArrayList<>();
    while(eventType!=parser.END_DOCUMENT){
      if(eventType == XmlResourceParser.START_TAG){
        String currencyValue = parser.getName();
        if (currencyValue.equals("currency")){
          TypedArray ta = context.getResources().obtainAttributes(parser, R.styleable.currencyCategory);
          String object = ta.getString(R.styleable.currencyCategory_object);
          Log.e("OBJECT: ", object);
          String name = ta.getString(R.styleable.currencyCategory_name);
          Log.e("NAME: ", name);
          int drawableResId = ta.getResourceId(R.styleable.currencyCategory_drawable, -1);
          Drawable drawable = AppCompatResources.getDrawable(context, drawableResId);
          String exchange = ta.getString(R.styleable.currencyCategory_exchange);
          Log.e("EXCHANGE: ", exchange);
          items.add(new StaticPriceModel(object, name, drawable, exchange));
          ta.recycle();

        }
      }
      eventType = parser.next();
    }
    return items;
  }
}
