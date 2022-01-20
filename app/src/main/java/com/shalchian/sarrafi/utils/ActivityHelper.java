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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;
import com.shalchian.sarrafi.R;
import com.shalchian.sarrafi.BuildConfig;

import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;

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
    String shareBody = subject + "\n" + body + "\n\n" + "اپ " + context.getResources().getString(R.string.app_name_fa);
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

    Intent startIntent = Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_using));
    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(startIntent);
  }

  private static void showUpdateDialog(Activity activity) {
    BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(activity)
            .setTitle(activity.getResources().getString(R.string.new_update_available))
            .setMessage(activity.getResources().getString(R.string.new_update_available_detail))
            .setAnimation("new_update.json")
            .setCancelable(true)
            .setPositiveButton(activity.getResources().getString(R.string.update_github), R.drawable.ic_github, (dialogInterface, which) -> {
              Intent intent = new Intent();
              intent.setAction(Intent.ACTION_VIEW);
              intent.setData(Uri.parse("https://github.com/Rminsh/Sarrafi/releases/latest"));
              activity.startActivity(intent);
            })
            .setNegativeButton(activity.getResources().getString(R.string.download_later), R.drawable.ic_close, (dialogInterface, which) -> dialogInterface.dismiss())
            .build();

    mBottomSheetDialog.show();
  }

  public static void checkUpdate(Activity activity,@NonNull Context context) {

    if (BuildConfig.showUpdater) {
      AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(context)
              .setGitHubUserAndRepo("Rminsh", "Sarrafi")
              .setUpdateFrom(UpdateFrom.GITHUB)
              .withListener(new AppUpdaterUtils.UpdateListener() {
                @Override
                public void onSuccess(Update update, Boolean isUpdateAvailable) {
                  if (isUpdateAvailable)
                    showUpdateDialog(activity);
                  Log.e("Latest Version", update.getLatestVersion());
                  Log.e("Latest Version Code", String.valueOf(update.getLatestVersionCode()));
                  Log.e("URL", String.valueOf(update.getUrlToDownload()));
                  Log.e("Is update available?", Boolean.toString(isUpdateAvailable));
                }

                @Override
                public void onFailed(AppUpdaterError error) {
                  Log.e("AppUpdater Error", "Something went wrong");
                }
              });
      appUpdaterUtils.start();
    }
  }

  public static void rateUS(Activity activity, Context baseContext) {
    ReviewManager reviewManager = ReviewManagerFactory.create(baseContext);
    Task<ReviewInfo> request = reviewManager.requestReviewFlow();
    request.addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        ReviewInfo reviewInfo = task.getResult();

        Task<Void> flow = reviewManager.launchReviewFlow(activity, reviewInfo);
        flow.addOnCompleteListener(task1 -> {
          Toast.makeText(baseContext, baseContext.getResources().getString(R.string.rate_thanks), Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
          Toast.makeText(baseContext, baseContext.getResources().getString(R.string.rate_error), Toast.LENGTH_SHORT).show();
        });
      } else {
        Log.e("Review error", "Review error");
        Toast.makeText(baseContext, baseContext.getResources().getString(R.string.rate_error), Toast.LENGTH_SHORT).show();
      }
    }).addOnFailureListener(e -> Log.e("Review error", "Review error"));
  }
}
