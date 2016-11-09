package br.ufrn.imd.projeto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void search(View view) {
        // Calculo do tamanho da foto
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels - findViewById(R.id.rlSearchBar).getHeight();

        LinearLayout layout = (LinearLayout) findViewById(R.id.llSearchResults);
        layout.removeAllViews();

        Button button = new Button(this);

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.paul);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        Drawable img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, height/4, height/4);

        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username) + "\n" + this.getResources().getString(R.string.starpoint));

        layout.addView(button);
    }
}
