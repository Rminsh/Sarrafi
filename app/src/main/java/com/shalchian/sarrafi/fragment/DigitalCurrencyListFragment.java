
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

package com.shalchian.sarrafi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.activity.MainTabActivity;
import com.shalchian.sarrafi.adapter.PriceAdapter;
import com.shalchian.sarrafi.db.DatabaseManager;
import com.shalchian.sarrafi.model.PriceModel;
import com.shalchian.sarrafi.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DigitalCurrencyListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  ArrayList<PriceModel> list;
  PriceAdapter adapter;
  RecyclerView recycler_view;
  SwipeRefreshLayout swipeRefreshLayout;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    list = new ArrayList<>();
    list.clear();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_overview, container, false);
    recycler_view = root.findViewById(R.id.price_rcv);
    recycler_view.setHasFixedSize(true);

    swipeRefreshLayout = root.findViewById(R.id.main_page_refresh);
    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setColorSchemeResources(
            R.color.purple,
            R.color.blue,
            R.color.light_green);

    adapter = new PriceAdapter(list, getContext());
    list.clear();

    try {
      JSONObject response = new JSONObject(DatabaseManager.getInstance().getRawData());
      list.addAll(JSONParser.priceList(response, "digital_currency", getContext()));
      recycler_view.setAdapter(adapter);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return root;
  }

  @Override
  public void onRefresh() {
    MainTabActivity activity = (MainTabActivity) getActivity();
    activity.checkConnection();
    swipeRefreshLayout.setRefreshing(false);
  }
}