package br.ufrn.imd.projeto.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import br.ufrn.imd.projeto.R;
import br.ufrn.imd.projeto.apiClient.Insert_Skill_Call;
import br.ufrn.imd.projeto.apiClient.Search_callback;
import br.ufrn.imd.projeto.apiClient.service.UserService;
import br.ufrn.imd.projeto.dominio.Match;
import br.ufrn.imd.projeto.dominio.Skill;
import br.ufrn.imd.projeto.dominio.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchUsers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void search(View view) {
        // Calculo do tamanho da foto
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final int height = displaymetrics.heightPixels - findViewById(R.id.rlSearchBar).getHeight();

        final LinearLayout layout = (LinearLayout) findViewById(R.id.llSearchResults);
        layout.removeAllViews();

        /*layout.removeAllViews();

        final Button button;
        final Bitmap bitmap;
        final Drawable img;

        button = new Button(this);*/

        /*button = new Button(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar1);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, height/4, height/4);
        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username) + "\n" + this.getResources().getString(R.string.starpoint));
        layout.addView(button);

        button = new Button(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar2);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, height/4, height/4);
        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username) + "\n" + this.getResources().getString(R.string.starpoint));
        layout.addView(button);

        button = new Button(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar3);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, height/4, height/4);
        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username) + "\n" + this.getResources().getString(R.string.starpoint));
        layout.addView(button);

        button = new Button(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar4);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, height/4, height/4);
        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username) + "\n" + this.getResources().getString(R.string.starpoint));
        layout.addView(button);

        button = new Button(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar5);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, height/4, height/4);
        button.setCompoundDrawables(img, null, null, null);
        button.setText(this.getResources().getString(R.string.username) + "\n" + this.getResources().getString(R.string.starpoint));
        layout.addView(button);*/

        //ability = ((TextView) findViewById(R.id.
        Spinner mySpinner = (Spinner) findViewById(R.id.spSearch);
        String text = mySpinner.getSelectedItem().toString();

        RadioButton myRadioSkills = (RadioButton) findViewById(R.id.radioButton2);
        //RadioButton myRadioInterests = (RadioButton) findViewById(R.id.radioButton);

        if (myRadioSkills.isChecked()) {
            request_users_by_skills(text, new Search_callback() {
                @Override
                public void users_search_callback(final List<User> users_search, final int id_skill) {

                    Button button;
                    Bitmap bitmap;
                    Drawable img;


                    if (users_search != null)
                        for (int i = 0; i < users_search.size(); i++) {
                            button = new Button(getApplicationContext());
                            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.avatar1);
                            bitmap = new PictureCreator().getCroppedBitmap(bitmap);
                            img = new BitmapDrawable(getApplicationContext().getResources(), bitmap);
                            img.setBounds(0, 0, height / 4, height / 4);
                            button.setCompoundDrawables(img, null, null, null);
                            button.setText(users_search.get(i).name + "\n" + users_search.get(i).email);
                            layout.addView(button);

                            final int finalI = i;
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    request_match(LoginActivity.global_user_login.id, users_search.get(finalI).id, id_skill, false);
                                }
                            });
                        }
                }
            });
        } else {
            request_users_by_interests(text, new Search_callback() {
                @Override
                public void users_search_callback(final List<User> users_search, final int id_interest) {
                    Button button;
                    Bitmap bitmap;
                    Drawable img;

                    if (users_search != null)
                        for (int i = 0; i < users_search.size(); i++) {
                            button = new Button(getApplicationContext());
                            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.avatar2);
                            bitmap = new PictureCreator().getCroppedBitmap(bitmap);
                            img = new BitmapDrawable(getApplicationContext().getResources(), bitmap);
                            img.setBounds(0, 0, height / 4, height / 4);
                            button.setCompoundDrawables(img, null, null, null);
                            button.setText(users_search.get(i).name + "\n" + users_search.get(i).email);
                            layout.addView(button);

                            final int finalI = i;
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    request_match(LoginActivity.global_user_login.id, users_search.get(finalI).id, id_interest, true);
                                }
                            });
                        }
                }
            });
        }
    }

    public void request_match(int id_user_logged, int id_user_searched, int id_skill_interest, boolean has_ability) {
        boolean control = false;

        if (id_user_logged != id_user_searched) {

            if (has_ability && LoginActivity.skills_user != null) {
                Log.i(TAG, "francaisdeux");
                for (int i = 0; i < LoginActivity.skills_user.size(); i++) {
                    if (LoginActivity.skills_user.get(i).id == id_skill_interest) {
                        i = LoginActivity.skills_user.size();
                        control = true;
                    }
                }
            } else {
                Log.i(TAG, "francaistrois");
                if (LoginActivity.interests_user != null) {
                    for (int i = 0; i < LoginActivity.interests_user.size(); i++) {
                        if (LoginActivity.interests_user.get(i).id == id_skill_interest) {
                            i = LoginActivity.interests_user.size();
                            control = true;
                        }
                    }
                }
            }

            if (control) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UserService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                UserService UserAPI = retrofit.create(UserService.class);

                Call<Void> postMatch;

                if (has_ability)
                    postMatch = UserAPI.perform_match(new Match(id_user_searched + "", id_user_logged + "", id_skill_interest + ""));
                else
                    postMatch = UserAPI.perform_match(new Match(id_user_logged + "", id_user_searched + "", id_skill_interest + ""));

                postMatch.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Match Realizado com Sucesso!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro no match!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Falha no acesso ao servidor", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                if (has_ability)
                    Toast.makeText(getApplicationContext(), "Não está registrado que você possui essa habilidade", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Não está registrado que você possui esse interesse", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Impossível fazer match consigo mesmo", Toast.LENGTH_LONG).show();
        }
    }


    public void request_users_by_skills(String name_skill, final Search_callback callback){

        request_id_by_name(name_skill, new Insert_Skill_Call.Skill_Interest_Id_Call() {
            @Override
            public void skill_interest_callback(final int id_skill_interest) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UserService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                UserService UserAPI = retrofit.create(UserService.class);

                Call<List<User>> callUsers = UserAPI.get_users_by_skill(id_skill_interest);

                callUsers.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        List<User> list_search = null;

                        if(response.isSuccessful()) {
                            list_search = response.body();
                        }

                        callback.users_search_callback(list_search,id_skill_interest);
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        callback.users_search_callback(null,id_skill_interest);
                    }
                });
            }
        });
    }

    public void request_id_by_name(final String name_skill, final Insert_Skill_Call.Skill_Interest_Id_Call callback){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);

        Call<List<Skill>> callSkillInterest = UserAPI.skill_by_name(name_skill);

        callSkillInterest.enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG,"Problema na resposta");
                }
                else{
                    List<Skill> lst = response.body();


                    if(lst!=null && lst.size()!=0){
                        callback.skill_interest_callback(lst.get(0).id);
                    }
                    else{
                        /*In the case where the skill is not in server, a different id skill is passed*/
                        callback.skill_interest_callback(-1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {

            }
        });
    }

    public void request_users_by_interests(String name_interest, final Search_callback callback){

        request_id_by_name(name_interest, new Insert_Skill_Call.Skill_Interest_Id_Call() {
            @Override
            public void skill_interest_callback(final int id_skill_interest) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UserService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                UserService UserAPI = retrofit.create(UserService.class);

                Call<List<User>> callUsers = UserAPI.get_users_by_interest(id_skill_interest);

                callUsers.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        List<User> list_search = null;

                        if(response.isSuccessful()) {
                            list_search = response.body();
                        }

                        callback.users_search_callback(list_search,id_skill_interest);
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        callback.users_search_callback(null,id_skill_interest);
                    }
                });
            }
        });
    }

}
