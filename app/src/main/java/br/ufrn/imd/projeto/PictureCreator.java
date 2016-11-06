package br.ufrn.imd.projeto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 * Created by ledson on 11/6/16.
 */

public class PictureCreator {

    // Cria a foto circular
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        final int radius = (bitmap.getWidth() < bitmap.getHeight()) ? bitmap.getWidth() : bitmap.getHeight();
        final int widthFix = bitmap.getWidth() - radius;
        final int heightFix = bitmap.getHeight() - radius;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rectSrc = new Rect(widthFix / 2, heightFix / 2, widthFix / 2 + radius, heightFix / 2  + radius);
        final Rect rectDest = new Rect(0, 0, radius, radius);

        Bitmap output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(radius / 2, radius / 2, radius / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rectSrc, rectDest, paint);

        return output;
    }

}
