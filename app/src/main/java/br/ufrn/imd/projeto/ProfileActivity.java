package br.ufrn.imd.projeto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getIntent().getBooleanExtra("own", true)) {
            findViewById(R.id.ibContact).setVisibility(View.GONE);
            findViewById(R.id.ibBack).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.ibFind).setVisibility(View.GONE);
            findViewById(R.id.ibConfig).setVisibility(View.GONE);
        }

        findViewById(R.id.ivPicture).setVisibility(View.INVISIBLE);

        new LoadPhoto().execute(this);

        // Lista de habilidades e interesses
        // TODO Mudar para a lista vinda da internet
        List<String> abilities = new ArrayList<>();
        abilities.add("C++");
        abilities.add("Java");
        abilities.add("Android");
        abilities.add("Tapioca Engineering");

        List<String> interests = new ArrayList<>();
        interests.add("Unity");
        interests.add("3D Modeling");
        interests.add("Game Engine Performance");
        interests.add("Cooking");

        // Populando a lista com as habilidades e interesses
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.llAbilities);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.llInterests);

        TextView textAb = new TextView(this);
        String s1 = "<b>\u2022 Abilities</b>";

        for (int i = 0; i < abilities.size(); ++i) {
            s1 += "<br>\t" + abilities.get(i);
        }

        // A versão atual é API 24
        //noinspection deprecation
        textAb.setText(Html.fromHtml(s1));
        textAb.setTextColor(Color.BLACK);

        TextView textIn = new TextView(this);
        String s2 = "<b>\u2022 Interests</b>";

        for (int i = 0; i < interests.size(); ++i) {
            s2 += "<br>\t" + interests.get(i);
        }

        // A versão atual é API 24
        //noinspection deprecation
        textIn.setText(Html.fromHtml(s2));
        textIn.setTextColor(Color.BLACK);

        layout1.addView(textAb);
        layout2.addView(textIn);

        // Adicionar contatos
        LinearLayout layout3 = (LinearLayout) findViewById(R.id.llContacts);
        ImageButton imageButton;

        for (int i = 0; i < 2; ++i) {
            imageButton = new ImageButton(this);
            imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.monokuma));
            imageButton.setBackgroundResource(0);
            imageButton.setPadding(5, 0, 5, 0);
            layout3.addView(imageButton);
        }

        imageButton = new ImageButton(this);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), android.R.drawable.ic_menu_add));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        imageButton.setLayoutParams(layoutParams);
        imageButton.setBackgroundResource(0);
        imageButton.setPadding(5, 0, 5, 0);
        layout3.addView(imageButton);
    }

    // Função botão voltar
    public void backHistory(View view) {
        finish();
    }

    public void search(View view) {
        Intent intent = new Intent(this, SearchActivity.class);

        startActivity(intent);
    }

    private class LoadPhoto extends AsyncTask<Context, Void, Bitmap> {
        private int size;

        @Override
        protected Bitmap doInBackground(Context... params) {
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

            // Modificando e setando imagem do perfil
            // TODO Mudar para foto vindo da internet
            Bitmap bitmap = BitmapFactory.decodeResource(params[0].getResources(), R.drawable.paul);
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