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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.projeto.R;
import br.ufrn.imd.projeto.apiClient.Email_Call;
import br.ufrn.imd.projeto.apiClient.Insert_Skill_Call;
import br.ufrn.imd.projeto.apiClient.Search_callback;
import br.ufrn.imd.projeto.apiClient.service.UserService;
import br.ufrn.imd.projeto.dominio.BaseAppExtender;
import br.ufrn.imd.projeto.dominio.Skill;
import br.ufrn.imd.projeto.dominio.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        userId = ((BaseAppExtender)this.getApplication()).getEmail();
        //userId = getIntent().getStringExtra("user");
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
        List<User> suggestions = new ArrayList<User>();
        final List<String> interestsL = ((BaseAppExtender) this.getApplication()).getInterest();

        for(int j=0;j<interestsL.size();j++){
            Log.i(TAG1,"eita "+interestsL.get(j));
        }

        final LinearLayout layout = (LinearLayout) findViewById(R.id.llSuggestions);
        final int size = ((BaseAppExtender) this.getApplication()).getMiniSize() / 2;
        /*Button button;
        Bitmap bitmap;
        Drawable img;*/

        layout.removeAllViews();

        for (int k = 0; k < interestsL.size(); k++) {
            final int FinalK = k;
            request_users_by_skills(interestsL.get(k), new Search_callback() {
                        @Override
                        public void users_search_callback(final List<User> users_search, final int id_skill) {
                            Button button;
                            Bitmap bitmap;
                            Drawable img;

                            if (users_search != null) {
                                Log.i(TAG1, "thirdp-lista nao nula de users");
                                for (int i = 0; i < users_search.size(); i++) {
                                    button = new Button(getApplicationContext());
                                    bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.avatar1);
                                    bitmap = new PictureCreator().getCroppedBitmap(bitmap);
                                    img = new BitmapDrawable(getApplicationContext().getResources(), bitmap);
                                    img.setBounds(0, 0, size, size);
                                    button.setCompoundDrawables(img, null, null, null);
                                    //button.setText(users_search.get(i).name);
                                    button.setText(users_search.get(i).name + " --->  " + interestsL.get(FinalK));
                                    button.setGravity(Gravity.CENTER_VERTICAL);
                                    button.setCompoundDrawablePadding(10);

                                    final int finalI = i;
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.i(TAG1, "fourth-clicoup");
                                            request_user_information(new Email_Call() {
                                                @Override
                                                public void setUser_login(User user) {
                                                    Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                                                    i.putExtra("email", userId);
                                                    i.putExtra("ability", interestsL.get(FinalK));
                                                    i.putExtra("id_user_has", users_search.get(finalI).id + "");
                                                    i.putExtra("id_user_no", user.id + "");
                                                    i.putExtra("id_skill", id_skill + "");
                                                    startActivity(i);
                                                }
                                            });
                                            //request_match(LoginActivity.global_user_login.id, users_search.get(finalI).id, id_skill, false);
                                        }
                                    });
                                    layout.addView(button);
                                }
                            }
                        }
                    }
            );
        }
    }

        /*button = new Button(this);
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
        layout.addView(button);*/

        //1-primeiro eu devo pegar toda a lista de interesses do user
        //2-para cada interesse recuperar a lista de users e inflar cada um
    //}

    public void request_user_information(final Email_Call callback) {
        Log.i(TAG1,"VIII");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);

        Log.i(TAG1,"email passado + "+userId);
        Call<List<User>> callUser = UserAPI.getUserByEmail(userId);

        callUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG1, "erro: " + response.code());
                    Toast.makeText(getApplicationContext(), "Erro na resposta!", Toast.LENGTH_LONG).show();
                } else {
                    List<User> user_aux = response.body();
                    callback.setUser_login(user_aux.get(0));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sem Acesso ao Servidor", Toast.LENGTH_LONG).show();
                Log.i(TAG1, "serv " + t.getMessage());
            }
        });
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
                    Log.i(TAG1,"Problema na resposta");
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
        intent.putExtra("user",userId);
        startActivity(intent);
    }
}
