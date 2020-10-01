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

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.adapter.EditListAdapter;
import com.shalchian.sarrafi.db.DatabaseManager;
import com.shalchian.sarrafi.fragment.FavoriteListFragment;
import com.shalchian.sarrafi.model.FavoriteModel;
import com.shalchian.sarrafi.utils.OnStartDragListener;
import com.shalchian.sarrafi.utils.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class EditFavoriteListActivity extends AppCompatActivity implements OnStartDragListener {

  Toolbar toolbar;
  private ItemTouchHelper mItemTouchHelper;
  EditListAdapter adapter;

  View status_layout;
  LottieAnimationView status_animation;
  TextView status_text;

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    ViewPump.init(ViewPump.builder().addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Shabnam-FD.ttf").setFontAttrId(R.attr.fontPath).build())).build());
    super.onCreate(savedInstanceState);
    getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    setContentView(R.layout.activity_edit_favorite_list);

    toolbar = findViewById(R.id.toolbar);
    toolbar.setTitle(getResources().getString(R.string.edit_list));
    toolbar.setBackground(null);
    setSupportActionBar(toolbar);
    toolbar.inflateMenu(R.menu.detail_menu);
    toolbar.setNavigationIcon(R.drawable.ic_back);
    toolbar.setNavigationOnClickListener(view -> this.finish());

    status_layout = findViewById(R.id.status_layout);
    status_animation = findViewById(R.id.status_animation);
    status_text = findViewById(R.id.status_text);

    ArrayList<FavoriteModel> list = DatabaseManager.getInstance().getFavoriteList();
    adapter = new EditListAdapter(list, this);

    RecyclerView recyclerView = findViewById(R.id.fav_list_rcv);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

    if (list.isEmpty()) {
      recyclerView.setVisibility(View.GONE);
      status_layout.setVisibility(View.VISIBLE);
      status_animation.setAnimation("empty_box.json");
      status_animation.playAnimation();
      status_text.setText(getResources().getString(R.string.empty_list));
    } else {
      recyclerView.setVisibility(View.VISIBLE);
      status_layout.setVisibility(View.GONE);
      status_animation.pauseAnimation();
    }

    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
    mItemTouchHelper = new ItemTouchHelper(callback);
    mItemTouchHelper.attachToRecyclerView(recyclerView);
  }

  @Override
  public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
    mItemTouchHelper.startDrag(viewHolder);
  }

  @Override
  public void finish() {
    DatabaseManager.getInstance().setFavoriteList(adapter.getList());
    FavoriteListFragment.getInstance().loadList();
    super.finish();
  }
}
