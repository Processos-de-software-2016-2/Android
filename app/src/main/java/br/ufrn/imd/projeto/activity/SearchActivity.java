package br.ufrn.imd.projeto.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;

import br.ufrn.imd.projeto.R;
import br.ufrn.imd.projeto.dominio.BaseAppExtender;

public class SearchActivity extends AppCompatActivity {
    private String[] items;
    private String searchTerm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        items = getResources().getStringArray(R.array.tags);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.acSearch);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        autoCompleteTextView.setAdapter(adapter);
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
        InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        LinearLayout layout = (LinearLayout) findViewById(R.id.llSearchResults);
        int size = ((BaseAppExtender) this.getApplication()).getMiniSize();
        Button button;
        Bitmap bitmap;
        Drawable img;

        searchTerm = ((AutoCompleteTextView) findViewById(R.id.acSearch)).getText().toString();

        if (Arrays.asList(items).contains(searchTerm)) {
            layout.removeAllViews();

            button = new Button(this);
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar1);
            bitmap = new PictureCreator().getCroppedBitmap(bitmap);
            img = new BitmapDrawable(this.getResources(), bitmap);
            img.setBounds(0, 0, size, size);
            button.setCompoundDrawables(img, null, null, null);
            button.setText(this.getResources().getString(R.string.username));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                    i.putExtra("ability", searchTerm);
                    startActivity(i);
                }
            });
            layout.addView(button);

            button = new Button(this);
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar2);
            bitmap = new PictureCreator().getCroppedBitmap(bitmap);
            img = new BitmapDrawable(this.getResources(), bitmap);
            img.setBounds(0, 0, size, size);
            button.setCompoundDrawables(img, null, null, null);
            button.setText(this.getResources().getString(R.string.username));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                    i.putExtra("ability", searchTerm);
                    startActivity(i);
                }
            });
            layout.addView(button);

            button = new Button(this);
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar3);
            bitmap = new PictureCreator().getCroppedBitmap(bitmap);
            img = new BitmapDrawable(this.getResources(), bitmap);
            img.setBounds(0, 0, size, size);
            button.setCompoundDrawables(img, null, null, null);
            button.setText(this.getResources().getString(R.string.username));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                    i.putExtra("ability", searchTerm);
                    startActivity(i);
                }
            });
            layout.addView(button);

            button = new Button(this);
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar4);
            bitmap = new PictureCreator().getCroppedBitmap(bitmap);
            img = new BitmapDrawable(this.getResources(), bitmap);
            img.setBounds(0, 0, size, size);
            button.setCompoundDrawables(img, null, null, null);
            button.setText(this.getResources().getString(R.string.username));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                    i.putExtra("ability", searchTerm);
                    startActivity(i);
                }
            });
            layout.addView(button);

            button = new Button(this);
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar5);
            bitmap = new PictureCreator().getCroppedBitmap(bitmap);
            img = new BitmapDrawable(this.getResources(), bitmap);
            img.setBounds(0, 0, size, size);
            button.setCompoundDrawables(img, null, null, null);
            button.setText(this.getResources().getString(R.string.username));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                    i.putExtra("ability", searchTerm);
                    startActivity(i);
                }
            });
            layout.addView(button);

            keyboard.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        else {
            String errorMessage = getResources().getString(R.string.error5);
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
