
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

package com.shalchian.sarrafi.activity;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.model.StaticPriceModel;
import com.shalchian.sarrafi.utils.CurrenciesResourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class SelectSymbolActivity extends AppCompatActivity {

  RecyclerView recycler_view;
  ArrayList<StaticPriceModel> list;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_symbol);
    recycler_view = findViewById(R.id.symbols_rcv);

    XmlResourceParser parser = getResources().getXml(R.xml.currencies);

    try{
      CurrenciesResourceParser.processXMLData(this, parser);
    }catch(IOException | XmlPullParserException e){
      e.printStackTrace();
    }

  }
}