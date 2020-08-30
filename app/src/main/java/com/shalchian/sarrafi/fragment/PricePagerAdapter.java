
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

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shalchian.sarrafi.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PricePagerAdapter extends FragmentPagerAdapter {

  private final ArrayList<Fragment> fragmentList = new ArrayList<>();

  @StringRes
  private static final int[] TAB_TITLES = new int[]{R.string.currency_items, R.string.gold_items, R.string.oil_items, R.string.digital_currency_items};
  private final Context mContext;

  public PricePagerAdapter(Context context, FragmentManager fm){
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    mContext = context;
  }

  @NotNull
  @Override
  public Fragment getItem(int position) {
    return fragmentList.get(position);
  }

  public void addFragment(Fragment fragment) {
    fragmentList.add(fragment);
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return mContext.getResources().getString(TAB_TITLES[position]);
  }

  @Override
  public int getCount() {
    return fragmentList.size();
  }
}