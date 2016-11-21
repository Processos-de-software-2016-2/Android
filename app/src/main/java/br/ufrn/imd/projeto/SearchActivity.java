package br.ufrn.imd.projeto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void search(View view) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.llSearchResults);
        int size = ((BaseAppExtender) this.getApplication()).getMiniSize();
        Button button;
        Bitmap bitmap;
        Drawable img;

        layout.removeAllViews();

        button = new Button(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar1);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, size, size);
        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username));
        layout.addView(button);

        button = new Button(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar2);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, size, size);
        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username));
        layout.addView(button);

        button = new Button(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar3);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, size, size);
        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username));
        layout.addView(button);

        button = new Button(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar4);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, size, size);
        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username));
        layout.addView(button);

        button = new Button(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar5);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, size, size);
        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username));
        layout.addView(button);
    }
}
