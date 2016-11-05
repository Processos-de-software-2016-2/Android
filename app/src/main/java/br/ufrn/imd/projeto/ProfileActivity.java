package br.ufrn.imd.projeto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView imageView = (ImageView) findViewById(R.id.ivPicture);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.monokuma);
        bitmap = getCroppedBitmap(bitmap);
        imageView.setImageBitmap(bitmap);

        ListView listView = (ListView) findViewById(R.id.lvAbilities);

        //TODO criar sub listas
        List<String> abilities = new ArrayList<String>();
        abilities.add("Habilidade0");
        abilities.add("Habilidade1");
        abilities.add("Habilidade2");
        abilities.add("Habilidade3");
        abilities.add("Habilidade4");
        abilities.add("Habilidade5");
        abilities.add("Habilidade6");
        abilities.add("Habilidade7");
        abilities.add("Habilidade8");
        abilities.add("Habilidade9");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, abilities);

        listView.setAdapter(arrayAdapter);

    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        final int radius = (bitmap.getWidth() < bitmap.getHeight()) ? bitmap.getWidth() : bitmap.getHeight();
        final int widthFix = bitmap.getWidth() - radius;
        final int heightFix = bitmap.getHeight() - radius;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rectSrc = new Rect(0 + widthFix / 2, 0 + heightFix / 2, widthFix / 2 + radius, heightFix / 2  + radius);
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