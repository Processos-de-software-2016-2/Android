package br.ufrn.imd.projeto.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.ufrn.imd.projeto.R;
import br.ufrn.imd.projeto.dominio.BaseAppExtender;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "Profile Erro";
    private static final String TAG1 = "add abilities: " ;
    private boolean main;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        main = getIntent().getBooleanExtra("main", true);
        userId = getIntent().getStringExtra("user");
        Log.i(TAG1,"Captura na tela de perfil intent do email: "+ userId);

        initFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile_own, menu);

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

    // Função botão procurar
    public void search(MenuItem item) {

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("user",userId);
        startActivity(intent);
    }

    // Função botão editar perfil
    public void editProfile(MenuItem item) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("register", false);
        intent.putExtra("user", ((TextView) findViewById(R.id.tvComplement)).getText().toString());

        startActivity(intent);
    }

    public void editAbility(View view) {
        Log.i(TAG1,"acaba de clicar no botao para edicao de habilidades: "+ ((TextView) findViewById(R.id.tvComplement)).getText().toString());
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("register", false);
        intent.putExtra("part", 1);
        intent.putExtra("user", ((TextView) findViewById(R.id.tvComplement)).getText().toString());
        //Log.i(TAG,"o complemento é: "+((TextView) findViewById(R.id.tvComplement)).getText().toString());
        Log.i(TAG1,"eleventh ");
        startActivity(intent);
    }

    public void editInterest(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("register", false);
        intent.putExtra("part", 2);
        intent.putExtra("user", ((TextView) findViewById(R.id.tvComplement)).getText().toString());

        startActivity(intent);
    }

    private void initFields() {
        Log.i(TAG1,"carrega tudo do profile activity ");
        new LoadPhoto().execute(this);
        ((TextView) findViewById(R.id.tvName)).setText(((BaseAppExtender) this.getApplication()).getName());
        ((TextView) findViewById(R.id.tvComplement)).setText(((BaseAppExtender) this.getApplication()).getEmail());

        initAbilitiesInterests();
        initContacts();
    }

    // Inicia lista de habilidades e interesses
    private void initAbilitiesInterests() {
        Log.i(TAG1,"iniciando lista de habilidades/interesses");
        List<String> abilities = ((BaseAppExtender) this.getApplication()).getAbility();

        for(int i=0;i<abilities.size();i++){
            Log.i(TAG1,"alfa "+abilities.get(i));
        }
        List<String> interests = ((BaseAppExtender) this.getApplication()).getInterest();

        // Populando a lista com as habilidades e interesses
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.llAbilities);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.llInterests);

        TextView textAb = new TextView(this);
        String s1 = "<b>\u2022 " + getResources().getString(R.string.abilities) + "</b>";

        for (int i = 0; i < abilities.size(); ++i) {
            s1 += "<br>\t" + abilities.get(i);
        }

        if (abilities.isEmpty()) s1 += "<br>\t" + getResources().getString(R.string.emptyAbility);

        // A versão atual é API 24
        //noinspection deprecation
        textAb.setText(Html.fromHtml(s1));
        textAb.setTextColor(Color.BLACK);

        TextView textIn = new TextView(this);
        String s2 = "<b>\u2022 " + getResources().getString(R.string.interests) + "</b>";

        for (int i = 0; i < interests.size(); ++i) {
            s2 += "<br>\t" + interests.get(i);
        }

        if (interests.isEmpty()) s2 += "<br>\t" + getResources().getString(R.string.emptyInterest);

        // A versão atual é API 24
        //noinspection deprecation
        textIn.setText(Html.fromHtml(s2));
        textIn.setTextColor(Color.BLACK);

        layout1.addView(textAb);
        layout2.addView(textIn);
        Log.i(TAG1,"nineth");
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
        imageButton.getLayoutParams().height = ((BaseAppExtender) this.getApplication()).getMiniSize();
        imageButton.getLayoutParams().width = ((BaseAppExtender) this.getApplication()).getMiniSize();
        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageButton.setAdjustViewBounds(true);

        imageButton = new ImageButton(this);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar2));
        imageButton.setBackgroundResource(0);
        imageButton.setPadding(0, 0, 0, 0);
        layout3.addView(imageButton);
        imageButton.getLayoutParams().height = ((BaseAppExtender) this.getApplication()).getMiniSize();
        imageButton.getLayoutParams().width = ((BaseAppExtender) this.getApplication()).getMiniSize();
        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageButton.setAdjustViewBounds(true);

        imageButton = new ImageButton(this);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar3));
        imageButton.setBackgroundResource(0);
        imageButton.setPadding(0, 0, 0, 0);
        layout3.addView(imageButton);
        imageButton.getLayoutParams().height = ((BaseAppExtender) this.getApplication()).getMiniSize();
        imageButton.getLayoutParams().width = ((BaseAppExtender) this.getApplication()).getMiniSize();
        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageButton.setAdjustViewBounds(true);

        imageButton = new ImageButton(this);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar4));
        imageButton.setBackgroundResource(0);
        imageButton.setPadding(0, 0, 0, 0);
        layout3.addView(imageButton);
        imageButton.getLayoutParams().height = ((BaseAppExtender) this.getApplication()).getMiniSize();
        imageButton.getLayoutParams().width = ((BaseAppExtender) this.getApplication()).getMiniSize();
        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageButton.setAdjustViewBounds(true);

        imageButton = new ImageButton(this);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar5));
        imageButton.setBackgroundResource(0);
        imageButton.setPadding(0, 0, 0, 0);
        layout3.addView(imageButton);
        imageButton.getLayoutParams().height = ((BaseAppExtender) this.getApplication()).getMiniSize();
        imageButton.getLayoutParams().width = ((BaseAppExtender) this.getApplication()).getMiniSize();
        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageButton.setAdjustViewBounds(true);

        Log.i(TAG1,"tenth");
    }

    // Thread que irá fazer o serviço de cortar a imagem do perfil
    private class LoadPhoto extends AsyncTask<Activity, Void, Bitmap> {
        int size;

        @Override
        protected Bitmap doInBackground(Activity... params) {
            size = ((BaseAppExtender) params[0].getApplication()).getSize();

            Bitmap bitmap = ((BaseAppExtender) params[0].getApplication()).getPicture();
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