package br.ufrn.imd.projeto.activity;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import br.ufrn.imd.projeto.R;
import br.ufrn.imd.projeto.apiClient.Skills_Callback;
import br.ufrn.imd.projeto.apiClient.service.UserService;
import br.ufrn.imd.projeto.dominio.BaseAppExtender;
import br.ufrn.imd.projeto.dominio.Match;
import br.ufrn.imd.projeto.dominio.Skill;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.ufrn.imd.projeto.activity.LoginActivity.interests_user;

public class MatchActivity extends AppCompatActivity {
    private String targetSkill = "";
    private String id_user_has = "";
    private String id_user_no = "";
    private String id_skill = "";
    private String email = "";
    private boolean has_interest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Button button = (Button) findViewById(R.id.btHeader);
        int size = ((BaseAppExtender) this.getApplication()).getMiniSize();
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar4);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        Drawable img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, size, size);
        button.setCompoundDrawables(img, null, null, null);

        targetSkill = getIntent().getStringExtra("ability");
        id_user_has = getIntent().getStringExtra("id_user_has");
        id_user_no = getIntent().getStringExtra("id_user_no");
        id_skill = getIntent().getStringExtra("id_skill");
        email = getIntent().getStringExtra("email");

        ((TextView) findViewById(R.id.tvMatch)).setText(targetSkill);
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

    public void sendNotification(View view) {
        // TODO implementar envio de notificação

        if(id_user_has.equals(id_user_no)){
            Toast.makeText(this,"Impossível fazer match consigo mesmo",Toast.LENGTH_LONG).show();
        }
        else{
            retrieve_interests_signIn(new Skills_Callback() {
                @Override
                public void set_skills_callback(List<Skill> list, Application app) {

                    if(list!=null){
                        int aux = Integer.parseInt(id_skill);

                        for(int i=0;i<list.size();i++){

                            if(list.get(i).id == aux){
                                i = list.size();
                                has_interest = true;
                            }
                        }
                    }

                    if(has_interest){
                        request_match();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Esse interesse não foi cadastrado",Toast.LENGTH_LONG).show();
                    }
                }
            },Integer.parseInt(id_user_no),this.getApplication());
        }
    }

    public void backToProfile(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("main",true);
        intent.putExtra("user",email);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void retrieve_interests_signIn(final Skills_Callback callback, int id, final Application app) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        UserService UserAPI = retrofit.create(UserService.class);


        Call<List<Skill>> lst = UserAPI.getInterestsUser(id);

        lst.enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                if (!response.isSuccessful()) {
                    callback.set_skills_callback(null, app);
                    Toast.makeText(getApplicationContext(), "erro na resposta dos interests", Toast.LENGTH_LONG).show();
                } else {

                    interests_user = response.body();
                    if(interests_user!=null && interests_user.size() != 0) {
                        //Toast.makeText(getApplicationContext(), "interests retornados", Toast.LENGTH_LONG).show();
                    }
                    else{
                        interests_user = null;
                    }
                    callback.set_skills_callback(interests_user,app);
                }
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Falha no acesso ao servidor", Toast.LENGTH_LONG).show();
                callback.set_skills_callback(null,app);
            }
        });
    }


    public void request_match() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);

        Call<Void> postMatch;


        postMatch = UserAPI.perform_match(new Match(id_user_no, id_user_has, id_skill));

        postMatch.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String message = getResources().getString(R.string.success_request);
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.show();
                    has_interest = false;

                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("main",true);
                intent.putExtra("user",email);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "cannot access server", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("main",true);
                intent.putExtra("user",email);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
