package br.ufrn.imd.projeto.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
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

public class SearchActivity extends AppCompatActivity {
    private static final String TAG1 = "SearchTAG";
    private String[] items;
    private String searchTerm = "";
    private String userEmail;
    private BaseAppExtender app = (BaseAppExtender) this.getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userEmail = getIntent().getStringExtra("user");
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
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.llSearchResults);
        final int size = ((BaseAppExtender) this.getApplication()).getMiniSize();
        Button button;
        Bitmap bitmap;
        Drawable img;

        searchTerm = ((AutoCompleteTextView) findViewById(R.id.acSearch)).getText().toString();
        Log.i(TAG1,"firstp");
        if (Arrays.asList(items).contains(searchTerm)) {
            Log.i(TAG1,"firstp-entrou");
            layout.removeAllViews();

            request_users_by_skills(searchTerm, new Search_callback() {
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
                                    button.setText(users_search.get(i).name);


                                    final int finalI = i;
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.i(TAG1, "fourth-clicoup");
                                            request_user_information(new Email_Call() {
                                                @Override
                                                public void setUser_login(User user) {
                                                    Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                                                    i.putExtra("email",userEmail);
                                                    i.putExtra("ability", searchTerm);
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
        } else {
            String errorMessage = getResources().getString(R.string.error5);
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
            /*button = new Button(this);
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

            keyboard.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);*/
        //}
        /*else {
            String errorMessage = getResources().getString(R.string.error5);
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }*/
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

        Log.i(TAG1,"email passado + "+userEmail);
        Call<List<User>> callUser = UserAPI.getUserByEmail(userEmail);

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
