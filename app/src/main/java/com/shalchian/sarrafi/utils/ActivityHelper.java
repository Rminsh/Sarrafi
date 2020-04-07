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
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shalchian.sarrafi.R;

public class ActivityHelper {

  public static boolean checkConnection(Context context) {
    final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetworkInfo = null;
    if (connMgr != null) {
      activeNetworkInfo = connMgr.getActiveNetworkInfo();
    }

    if (activeNetworkInfo != null) { // connected to the internet

      // connected to the mobile provider's data plan
      if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
        // connected to wifi
        return true;
      } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }
    return false;
  }

  public static Typeface setTypeface(Context context, String fontPath) {
    return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontPath);
  }

  public static void shareText(Context context , String subject , String body) {
    String shareBody = subject + "\n" + body + "\n\n" + "اپ " + context.getResources().getString(R.string.app_name);
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

    Intent startIntent = Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_using));
    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(startIntent);
  }
}
