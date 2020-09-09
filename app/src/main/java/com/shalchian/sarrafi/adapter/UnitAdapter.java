
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

package com.shalchian.sarrafi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.model.UnitItem;

import java.util.ArrayList;

public class UnitAdapter extends ArrayAdapter<UnitItem> {
  public UnitAdapter(Context context, ArrayList<UnitItem> countryList) {
    super(context, 0, countryList);
  }
  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return initView(position, convertView, parent);
  }
  @Override
  public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return initView(position, convertView, parent);
  }
  private View initView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(
              R.layout.spinner_layout, parent, false
      );
    }
    ImageView imageViewFlag = convertView.findViewById(R.id.spinner_flag);
    TextView textViewName = convertView.findViewById(R.id.spinner_title);
    UnitItem currentItem = getItem(position);
    if (currentItem != null) {
      imageViewFlag.setImageResource(currentItem.getUnitFlagImage());
      textViewName.setText(currentItem.getUnitName());
    }
    return convertView;
  }
}