
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.activity.MainTabActivity;
import com.shalchian.sarrafi.adapter.PriceAdapter;
import com.shalchian.sarrafi.db.DatabaseManager;
import com.shalchian.sarrafi.model.PriceModel;
import com.shalchian.sarrafi.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class FavoriteListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  private static FavoriteListFragment instance = null;
  private static final String ARG_DATA_LIST = "data_key";

  ArrayList<PriceModel> list;
  PriceAdapter adapter;
  RecyclerView recycler_view;
  SwipeRefreshLayout swipeRefreshLayout;

  View status_layout;
  LottieAnimationView status_animation;
  TextView status_text;

  public static FavoriteListFragment newInstance(Bundle bundle) {
    FavoriteListFragment fragment = new FavoriteListFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  public static FavoriteListFragment getInstance() {
    return instance;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    instance = this;
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

    status_layout = root.findViewById(R.id.status_layout);
    status_animation = root.findViewById(R.id.status_animation);
    status_text = root.findViewById(R.id.status_text);

    adapter = new PriceAdapter(list, getContext());
    loadList();

    return root;
  }

  @Override
  public void onRefresh() {
    MainTabActivity activity = (MainTabActivity) getActivity();
    activity.checkConnection();
    swipeRefreshLayout.setRefreshing(false);
  }

  public void loadList() {
    try {
      list.clear();
      String data = getArguments().getString(ARG_DATA_LIST);
      JSONObject response = new JSONObject(Objects.requireNonNull(data));
      ArrayList<PriceModel> allItems = JSONParser.priceList(response, "", getContext());
      ArrayList<String> favoriteItems = DatabaseManager.getInstance().getFavoriteList();
      for (PriceModel item: allItems) {
        if(favoriteItems.contains(item.getObjName())) {
          list.add(item);
        }
      }

      recycler_view.setAdapter(adapter);

      if (list.isEmpty()) {
        recycler_view.setVisibility(View.GONE);
        status_layout.setVisibility(View.VISIBLE);
        status_animation.setAnimation("empty_box.json");
        status_animation.playAnimation();
        status_text.setText(getResources().getString(R.string.empty_list));
      } else {
        recycler_view.setVisibility(View.VISIBLE);
        status_layout.setVisibility(View.GONE);
        status_animation.pauseAnimation();
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}