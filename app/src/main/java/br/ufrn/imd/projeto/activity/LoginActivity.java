package br.ufrn.imd.projeto.activity;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.projeto.R;
import br.ufrn.imd.projeto.apiClient.Email_Call;
import br.ufrn.imd.projeto.apiClient.Email_callback;
import br.ufrn.imd.projeto.apiClient.Interface_Call;
import br.ufrn.imd.projeto.apiClient.Skills_Callback;
import br.ufrn.imd.projeto.apiClient.service.UserService;
import br.ufrn.imd.projeto.dominio.BaseAppExtender;
import br.ufrn.imd.projeto.dominio.Skill;
import br.ufrn.imd.projeto.dominio.StateLogin;
import br.ufrn.imd.projeto.dominio.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG1 = "add abilities: ";
    private String user;
    private String password;
    private boolean successfulLogin = false;

    private final LoadingDialog loadingDialog = new LoadingDialog();
    private final LoginDialog loginDialog = new LoginDialog();

    static User global_user_login;
    static List<Skill> skills_user;
    static List<Skill> interests_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        int size = (width < height) ? width/2 : height/2;

        ((BaseAppExtender) this.getApplication()).setSize(size);
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

    public void enterSystem(View view) {
        Log.i(TAG1,"chama_fragmento_login");
        loginDialog.show(getFragmentManager(), "login");
    }

    public void signIn(View view) {
        Log.i(TAG1,"passa_user_password_chama_Main_activity");
        user = loginDialog.getUser();
        password = loginDialog.getPassword();

        //loginDialog.dismiss();


        request_server_login(new Interface_Call() {
            @Override
            public void login_success(boolean value, int type_error) {

                if (value) {

                    request_user_information(new Email_Call() {
                        @Override
                        public void setUser_login(User user) {

                            User user_login = new User(user.email, user.age, user.password, user.id, user.name);
                            global_user_login = user_login;

                            setUserGlobalDataLogin(user_login);
                        }
                    });
                } else {
                    String message_error = null;

                    if (type_error == 1) {
                        message_error = "Login Incorreto!";
                    } else if (type_error == 2) {
                        message_error = "Password Incorreto!";
                    } else {
                        message_error = "Sem acesso ao servidor";
                    }
                    Toast.makeText(getApplicationContext(), message_error, Toast.LENGTH_LONG).show();
                }
            }
        });
        /*loadingDialog.show(getFragmentManager(), "loading");
        new ProcessLogin().execute(this);*/
    }

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

        Call<List<User>> callUser = UserAPI.getUserByEmail(user);

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

    public void request_server_login(final Interface_Call callback) {
        Log.i(TAG1,"II");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);

        Call<StateLogin> login = UserAPI.getInfoLogin(user, password);

        login.enqueue(new Callback<StateLogin>() {
            @Override
            public void onResponse(Call<StateLogin> call, Response<StateLogin> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG1, "Resposta chegou mas algo deu errado");
                    callback.login_success(false, 1);

                } else {
                    //Log.i(TAG, "Resposta chegou com sucesso");
                    Log.i(TAG1,"V");
                    StateLogin aux = response.body();

                    if (aux.logged) {
                        callback.login_success(true, 0);
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Incorreto!", Toast.LENGTH_LONG).show();
                        callback.login_success(false, 2);
                    }
                }
            }

            @Override
            public void onFailure(Call<StateLogin> call, Throwable t) {
                Log.i(TAG1, "sem resposta");
                Log.i(TAG1, t.getMessage());
                callback.login_success(false, 3);
            }
        });
    }

    public void signUp(View view) {
        loginDialog.dismiss();

        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);
    }

    public void loginFacebook(View view) {
        /*
        TODO login com facebook
         */
    }

    public void loginGoogle(View view) {
        /*
        TODO login com google+
         */
    }

    private boolean checkLogin(String user, String password) {
        boolean success = loginWithServer(user, password);

        return success;
    }

    private boolean loginWithServer(String user, String password) {
        /*
        TODO login com o servidor
        retorne verdadeiro se login com sucesso
        retorn falso se login falhou
         */
        return true;
    }

   // private void setUserGlobalData(String userId) {
        /*
        TODO pegar os dados do usuário do BD
        remover os dados lacais abaixo e setar os vindos do BD
        use userId pare selecionar o usuário
         */
        /*Bitmap picture = BitmapFactory.decodeResource(this.getResources(), R.drawable.paul);
        String name = getResources().getString(R.string.username);
        String email = getResources().getString(R.string.email);

        List<String> ability = new ArrayList<>();
        ability.add("C++");
        ability.add("Java");
        ability.add("Android");
        ability.add("Tapioca Engineering");

        List<String> interest = new ArrayList<>();
        interest.add("Unity");
        interest.add("3D Modeling");
        interest.add("Game Engine Performance");
        interest.add("Cooking");

        // Seta os dados nas variáveis que serão utilisadas pelo app em qualquer tela
        ((BaseAppExtender) this.getApplication()).setPicture(picture);
        ((BaseAppExtender) this.getApplication()).setName(name);
        ((BaseAppExtender) this.getApplication()).setEmail(email);
        ((BaseAppExtender) this.getApplication()).setAbility(ability);
        ((BaseAppExtender) this.getApplication()).setInterest(interest);*/
    //}

    private void setUserGlobalDataLogin(final User uLogin) {
        final Bitmap picture = BitmapFactory.decodeResource(this.getResources(), R.drawable.paul);
        final String name = uLogin.name;


        retrieve_skills_signIn(new Skills_Callback() {

            @Override
            public void set_skills_callback(List<Skill> list, Application app) {

                retrieve_interests_signIn(new Skills_Callback() {
                    @Override
                    public void set_skills_callback(List<Skill> list, Application app) {

                        List<String> ability = new ArrayList<>();

                        if(skills_user != null) {
                            for (int i = 0; i < skills_user.size(); i++) {
                                ability.add(skills_user.get(i).name);
                            }
                        }
                        List<String> interest = new ArrayList<>();

                        if(interests_user != null){
                            for(int i=0; i < interests_user.size(); i++){
                                interest.add(interests_user.get(i).name);
                            }
                        }

                        ((BaseAppExtender) app).setPicture(picture);
                        ((BaseAppExtender) app).setName(uLogin.name);
                        ((BaseAppExtender) app).setEmail(uLogin.email);
                        ((BaseAppExtender) app).setAbility(ability);
                        ((BaseAppExtender) app).setInterest(interest);

                    }
                },uLogin.id, app);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.putExtra("main", true);
                intent.putExtra("user", user.toString());
                startActivity(intent);
            }
        }, uLogin.id, this.getApplication());

        Log.i(TAG1, "setou user habilidades");
        Log.i(TAG1, "setou user logado");
    }

    void retrieve_skills_signIn(final Skills_Callback callback, int id, final Application app) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        UserService UserAPI = retrofit.create(UserService.class);


        Call<List<Skill>> lst = UserAPI.getSkillsUser(id);

        lst.enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                if (!response.isSuccessful()) {
                    callback.set_skills_callback(null, app);
                    Toast.makeText(getApplicationContext(), "erro na resposta dos skills", Toast.LENGTH_LONG).show();
                } else {
                    skills_user = response.body();
                    if(skills_user!=null && skills_user.size() != 0) {
                        Log.i(TAG1, "sdsqf " + skills_user.get(0).name);
                        Toast.makeText(getApplicationContext(), "skills retornadas", Toast.LENGTH_LONG).show();
                        Log.i(TAG1, "XIX");
                    }
                    else{
                        Log.i(TAG1,"sem skills");
                        skills_user = null;
                    }
                    callback.set_skills_callback(skills_user,app);
                }
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falha no acesso ao servidor", Toast.LENGTH_LONG).show();
                callback.set_skills_callback(null,app);
            }
        });
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
        Log.i(TAG1, "fourth");
        lst.enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                if (!response.isSuccessful()) {
                    callback.set_skills_callback(null, app);
                    Toast.makeText(getApplicationContext(), "erro na resposta dos interests", Toast.LENGTH_LONG).show();
                } else {

                    interests_user = response.body();
                    if(interests_user!=null && interests_user.size() != 0) {
                        Log.i(TAG1, "sdsqf " + interests_user.get(0).name);
                        Toast.makeText(getApplicationContext(), "interests retornados", Toast.LENGTH_LONG).show();
                        Log.i(TAG1, "XIX");
                    }
                    else{
                        Log.i(TAG1,"sem interests");
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


    private void request_server_skills(final Email_callback email_callback) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);

        Call<List<User>> lst = UserAPI.getUserByEmail(user);

        lst.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Resposta dos Skills Corrompida", Toast.LENGTH_LONG).show();
                } else {
                    List<User> lst_u = response.body();

                    if (lst_u.size() == 0) {
                        Toast.makeText(getApplicationContext(), "Usuário sem skills", Toast.LENGTH_LONG).show();
                        email_callback.setEmailCallback(-1);
                        Log.i(TAG1,"15");
                    } else {
                        Log.i(TAG1,"XVIII");
                        email_callback.setEmailCallback(lst_u.get(0).id);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sem acesso ao servidor", Toast.LENGTH_LONG).show();
                email_callback.setEmailCallback(-1);
            }
        });


    }

    // Thread que irá fazer o login
    /*private class ProcessLogin extends AsyncTask<Context, Void, Context> {

        @Override
        protected Context doInBackground(Context... params) {
            if (checkLogin(user, password)) {
                setUserGlobalData(user);
                successfulLogin = true;
            }
            else {
                successfulLogin = false;
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(Context result) {
            loadingDialog.dismiss();

            if (successfulLogin) {
                Intent intent = new Intent(result, MainActivity.class);
                intent.putExtra("main", true);
                intent.putExtra("user", user);
                Log.i(TAG1,"passou_user_por_intent_para_main_activity");
                startActivity(intent);
            }
            else {
                String errorMessage = getResources().getString(R.string.error0);
                Toast toast = Toast.makeText(result, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }*/
}