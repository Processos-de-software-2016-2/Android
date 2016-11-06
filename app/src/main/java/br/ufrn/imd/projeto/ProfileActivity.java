package br.ufrn.imd.projeto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        int size = (width > height) ? height : width;

        // Modificando e setando imagem do perfil
        // TODO Mudar para foto vindo da internet
        ImageView imageView = (ImageView) findViewById(R.id.ivPicture);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.paul);
        bitmap = (new PictureCreator()).getCroppedBitmap(bitmap);
        imageView.setImageBitmap(bitmap);
        imageView.getLayoutParams().height = size;
        imageView.getLayoutParams().width = size;

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
    }

    // Função botão voltar
    public void backHistory(View view) {
        finish();
    }
}