package br.ufrn.imd.projeto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private boolean main;
    private String userId;
    private String[] suggestionTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = getIntent().getBooleanExtra("main", true);
        userId = getIntent().getStringExtra("user");

        suggestionTags = getResources().getStringArray(R.array.tags);

        getSuggestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(R.drawable.action_bar_logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        return true;
    }

    private void getSuggestion() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.llSuggestions);
        int size = ((BaseAppExtender) this.getApplication()).getMiniSize()/2;
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
        button.setText(this.getResources().getString(R.string.username) + "\n" + suggestionTags[0]);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                i.putExtra("ability", suggestionTags[0]);
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
        button.setText(this.getResources().getString(R.string.username) + "\n" + suggestionTags[1]);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                i.putExtra("ability", suggestionTags[1]);
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
        button.setText(this.getResources().getString(R.string.username) + "\n" + suggestionTags[2]);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                i.putExtra("ability", suggestionTags[2]);
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
        button.setText(this.getResources().getString(R.string.username) + "\n" + suggestionTags[3]);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                i.putExtra("ability", suggestionTags[3]);
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
        button.setText(this.getResources().getString(R.string.username) + "\n" + suggestionTags[4]);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                i.putExtra("ability", suggestionTags[4]);
                startActivity(i);
            }
        });
        layout.addView(button);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("main", true);
        intent.putExtra("user", userId);
        startActivity(intent);
    }

    public void goToSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
