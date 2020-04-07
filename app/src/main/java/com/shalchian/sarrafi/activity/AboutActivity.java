
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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

import java.util.Objects;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.shalchian.sarrafi.BuildConfig;
import com.shalchian.sarrafi.R;

public class AboutActivity extends MaterialAboutActivity {

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    ViewPump.init(ViewPump.builder().addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Shabnam-FD.ttf").setFontAttrId(R.attr.fontPath).build())).build());
    super.onCreate(savedInstanceState);
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.about);
  }

  @NonNull
  @Override
  protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {
    MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
    buildApp(context, appCardBuilder);
    MaterialAboutCard.Builder miscCardBuilder = new MaterialAboutCard.Builder();
    buildMisc(context, miscCardBuilder);
    return new MaterialAboutList(appCardBuilder.build(), miscCardBuilder.build());
  }

  @Override
  protected CharSequence getActivityTitle() {
    return getString(R.string.app_name);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void buildMisc(Context context, MaterialAboutCard.Builder miscCardBuilder) {
    miscCardBuilder.title(R.string.about)
            .addItem(new MaterialAboutActionItem.Builder()
                    .text(R.string.github)
                    .icon(ContextCompat.getDrawable(context, R.drawable.ic_github))
                    .setOnClickAction(() -> {
                      Intent intent = new Intent();
                      intent.setAction(Intent.ACTION_VIEW);
                      intent.setData(Uri.parse("https://github.com/Rminsh/Sarrafi"));
                      startActivity(intent);
                    })
                    .build())
            .addItem(new MaterialAboutActionItem.Builder()
                    .text(R.string.email)
                    .icon(ContextCompat.getDrawable(context, R.drawable.ic_email))
                    .setOnClickAction(() -> {
                      Intent intent = new Intent(Intent.ACTION_SEND);
                      intent.setType("plain/text");
                      intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"armin536@gmail.com"});
                      intent.putExtra(Intent.EXTRA_SUBJECT, "نظراات و پیشنهادات");
                      intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                      startActivity(Intent.createChooser(intent, ""));
                    })
                    .build());
  }

  private void buildApp(Context context, MaterialAboutCard.Builder appCardBuilder) {
    appCardBuilder
            .addItem(new MaterialAboutTitleItem.Builder()
                    .text(R.string.app_name)
                    .icon(R.mipmap.ic_launcher_round)
                    .build())
            .addItem(new MaterialAboutActionItem.Builder()
                    .text(getResources().getString(R.string.version))
                    .icon(ContextCompat.getDrawable(context, R.drawable.ic_information))
                    .subText(BuildConfig.VERSION_NAME)
                    .build())
            .addItem(new MaterialAboutActionItem.Builder()
                    .text(getResources().getString(R.string.open_source_libraries))
                    .icon(ContextCompat.getDrawable(context, R.drawable.ic_library))
                    .setOnClickAction(() ->{
                      LibsBuilder libsBuilder = new LibsBuilder();
                      libsBuilder.start(this);
                    })
                    .build());
  }
}