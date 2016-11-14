package br.ufrn.imd.projeto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    boolean main;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        main = getIntent().getBooleanExtra("main", true);
        userId = getIntent().getStringExtra("user");

        initFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile_own, menu);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    // Função botão procurar
    public void search(MenuItem item) {
        Intent intent = new Intent(this, SearchActivity.class);

        startActivity(intent);
    }

    public void editProfile(MenuItem item) {
        Intent intent = new Intent(this, RegisterActivity.class);

        intent.putExtra("register", false);
        intent.putExtra("user", ((TextView) findViewById(R.id.tvComplement)).getText().toString());

        startActivity(intent);
    }

    public void contactUser(MenuItem item) {
        /*
        TODO contact user
         */
    }

    public void backToMain(MenuItem item) {
        /*
        TODO back to user
         */
    }

    public void initFields() {
        new LoadPhoto().execute(((BaseAppExtender) this.getApplication()).getPicture());
        ((TextView) findViewById(R.id.tvName)).setText(((BaseAppExtender) this.getApplication()).getName());
        ((TextView) findViewById(R.id.tvComplement)).setText(((BaseAppExtender) this.getApplication()).getEmail());
        initAbilitiesInterests();
        initContacts();
    }

    // Inicia lista de habilidades e interesses
    private void initAbilitiesInterests() {
        List<String> abilities = ((BaseAppExtender) this.getApplication()).getAbility();
        List<String> interests = ((BaseAppExtender) this.getApplication()).getInterest();

        // Populando a lista com as habilidades e interesses
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.llAbilities);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.llInterests);

        TextView textAb = new TextView(this);
        String s1 = "<b>\u2022 " + getResources().getString(R.string.abilities) + "</b>";

        for (int i = 0; i < abilities.size(); ++i) {
            s1 += "<br>\t" + abilities.get(i);
        }

        // A versão atual é API 24
        //noinspection deprecation
        textAb.setText(Html.fromHtml(s1));
        textAb.setTextColor(Color.BLACK);

        TextView textIn = new TextView(this);
        String s2 = "<b>\u2022 " + getResources().getString(R.string.interests) + "</b>";

        for (int i = 0; i < interests.size(); ++i) {
            s2 += "<br>\t" + interests.get(i);
        }

        // A versão atual é API 24
        //noinspection deprecation
        textIn.setText(Html.fromHtml(s2));
        textIn.setTextColor(Color.BLACK);

        layout1.addView(textAb);
        layout2.addView(textIn);
    }

    private void initContacts() {
        // Adicionar contatos TODO mudar para contatos vindos da internet
        LinearLayout layout3 = (LinearLayout) findViewById(R.id.llContacts);
        ImageButton imageButton;

        imageButton = new ImageButton(this);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar1));
        imageButton.setBackgroundResource(0);
        imageButton.setPadding(0, 0, 0, 0);
        layout3.addView(imageButton);

        imageButton = new ImageButton(this);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar2));
        imageButton.setBackgroundResource(0);
        imageButton.setPadding(0, 0, 0, 0);
        layout3.addView(imageButton);

        imageButton = new ImageButton(this);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar3));
        imageButton.setBackgroundResource(0);
        imageButton.setPadding(0, 0, 0, 0);
        layout3.addView(imageButton);

        imageButton = new ImageButton(this);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar4));
        imageButton.setBackgroundResource(0);
        imageButton.setPadding(0, 0, 0, 0);
        layout3.addView(imageButton);

        imageButton = new ImageButton(this);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar5));
        imageButton.setBackgroundResource(0);
        imageButton.setPadding(0, 0, 0, 0);
        layout3.addView(imageButton);
    }

    // Thread que irá fazer o serviço de cortar a imagem do perfil
    private class LoadPhoto extends AsyncTask<Bitmap, Void, Bitmap> {
        private int size;

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            // Calculo do tamanho da foto do perfil
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            if  (width > height) {
                width = width / 2;
            }
            else {
                height = height / 2;
            }

            size = (width > height) ? height : width;

            Bitmap bitmap = params[0];
            bitmap = new PictureCreator().getCroppedBitmap(bitmap);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = (ImageView) findViewById(R.id.ivPicture);
            imageView.setImageBitmap(result);
            imageView.getLayoutParams().height = size;
            imageView.getLayoutParams().width = size;
            imageView.setVisibility(View.VISIBLE);

            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }

    }
}