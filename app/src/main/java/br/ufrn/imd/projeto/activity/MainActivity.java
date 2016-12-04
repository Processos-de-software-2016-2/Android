package br.ufrn.imd.projeto.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import br.ufrn.imd.projeto.R;
import br.ufrn.imd.projeto.dominio.BaseAppExtender;

public class MainActivity extends AppCompatActivity {
    private static final String TAG1 = "add abilities: ";
    private boolean main;
    private String userId;
    private String[] suggestionTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = getIntent().getBooleanExtra("main", true);

        userId = getIntent().getStringExtra("user");
        Log.i(TAG1,"pegou user e jogou em userId : "+userId);

        suggestionTags = getResources().getStringArray(R.array.tags);
        Log.i(TAG1,"pegou o array de abilities e jogou em suggestionTags");

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

    /*Metodo que preenche todas as sugestoes na tela principal*/
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
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setCompoundDrawablePadding(10);
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
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setCompoundDrawablePadding(10);
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
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setCompoundDrawablePadding(10);
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
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setCompoundDrawablePadding(10);
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
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setCompoundDrawablePadding(10);
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

    /*No clique do botao profile chama o perfil usando userId*/
    public void goToProfile(View view) {
        Log.i(TAG1,"acaba de clicar/chamar activity profile passando user-> email");
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("main", true);
        intent.putExtra("user", userId);
        startActivity(intent);
    }

    /*No clique do botao Search chama a activity de pesquisa*/
    public void goToSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
