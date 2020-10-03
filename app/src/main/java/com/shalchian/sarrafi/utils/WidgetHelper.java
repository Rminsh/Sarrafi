
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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

public class WidgetHelper {
  public static Bitmap BuildUpdate(String text, String font, int size , Context context) {
    Paint paint = new Paint();
    paint.setTextSize(size);
    Typeface typeface = Typeface.createFromAsset(context.getAssets() , "fonts/" + font + ".ttf");
    paint.setTypeface(typeface);
    paint.setColor(0xffffffff);
    paint.setTextAlign(Paint.Align.LEFT);
    paint.setSubpixelText(true);
    paint.setAntiAlias(true);
    float baseline = -paint.ascent();
    int width = (int) (paint.measureText(text));
    int height = (int) (baseline+paint.descent());
    Bitmap image = Bitmap.createBitmap(width , height , Bitmap.Config.ARGB_4444);
    Canvas canvas = new Canvas(image);
    canvas.drawText(text , 0 , baseline , paint);
    return image;
  }
}
