package br.ufrn.imd.projeto.activity;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private static final String TAG = "android_application";
    String user;
    String password;
    List<Skill> skills_user;
    List<Skill> interests_user;

    BaseAppExtender my_app = ((BaseAppExtender) this.getApplication());

    boolean successfulLogin = false;

    Bundle bundle = new Bundle();
    ErrorDialog errorDialog = new ErrorDialog();
    LoadingDialog loadingDialog = new LoadingDialog();
    LoginDialog loginDialog = new LoginDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    /*entra no sistema apos clique do botao inicial*/
    public void enterSystem(View view) {
        loginDialog.show(getFragmentManager(), "login");
    }


    public void request_user_information(final Email_Call callback) {
        Log.i(TAG,"VIII");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);

        Call<List<User>> callUser = UserAPI.getUserByEmail(user);
        Log.i(TAG,"IX");

        callUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "erro: " + response.code());
                    Toast.makeText(getApplicationContext(), "Erro na resposta!", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG,"XI");
                    List<User> user_aux = response.body();
                    Log.i(TAG,"XII");
                    callback.setUser_login(user_aux.get(0));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sem Acesso ao Servidor", Toast.LENGTH_LONG).show();
                Log.i(TAG, "serv " + t.getMessage());
            }
        });
    }

    public void request_server_login(final Interface_Call callback) {
        Log.i(TAG,"II");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);
        Log.i(TAG,"III");

        Call<StateLogin> login = UserAPI.getInfoLogin(user, password);
        Log.i(TAG,"IV");

        login.enqueue(new Callback<StateLogin>() {
            @Override
            public void onResponse(Call<StateLogin> call, Response<StateLogin> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "Resposta chegou mas algo deu errado");
                    callback.login_success(false, 1);

                } else {
                    //Log.i(TAG, "Resposta chegou com sucesso");
                    Log.i(TAG,"V");
                    StateLogin aux = response.body();

                    if (aux.logged) {
                        //Log.i(TAG, "logou");
                        Log.i(TAG,"VI");
                        callback.login_success(true, 0);
                    } else {
                        Log.i(TAG, "nao logou");
                        Toast.makeText(getApplicationContext(), "Login Incorreto!", Toast.LENGTH_LONG).show();
                        callback.login_success(false, 2);
                    }
                }
            }

            @Override
            public void onFailure(Call<StateLogin> call, Throwable t) {
                Log.i(TAG, "sem resposta");
                Log.i(TAG, t.getMessage());
                callback.login_success(false, 3);
            }
        });
    }

    public void signIn(View view) {

        user = loginDialog.getUser();
        password = loginDialog.getPassword();

        Log.i(TAG,"I");

        request_server_login(new Interface_Call() {
            @Override
            public void login_success(boolean value, int type_error) {

                if (value) {
                    Log.i(TAG,"VII");
                    request_user_information(new Email_Call() {
                        @Override
                        public void setUser_login(User user) {
                            Log.i(TAG,"XIII");
                            User user_login = new User(user.email, user.age, user.password, user.id, user.name);

                            //setUserGlobalData(user_login);

                            setUserGlobalDataLogin(user_login);
                            /*Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.putExtra("main", true);
                            intent.putExtra("user", user.toString());
                            startActivity(intent);*/
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
                    /*bundle.putInt("code", 0);
                    errorDialog.setArguments(bundle);
                    errorDialog.show(getFragmentManager(), "error");*/
                }
            }
        });
        //loadingDialog.show(getFragmentManager(), "loading");
        //new ProcessLogin().execute(this);
    }

    public void signUp(View view) {
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

        if (user.equals(this.password) && password.equals(this.password)) {
            return true;
        }
        return false;
    }

    /*private void setUserGlobalData(final User uLogin) {

        Log.i(TAG,"XIV");
        final Bitmap picture = BitmapFactory.decodeResource(this.getResources(), R.drawable.paul);
        //String name = getResources().getString(R.string.username);
        final String name = uLogin.name;
        //String email = getResources().getString(email);

        //Log.i(TAG, "fsdjkllfjks");

        retrieve_skills(new Skills_Callback() {

            @Override
            public void set_skills_callback(List<Skill> list) {
                if(skills_user == null){
                    Log.i(TAG, "auau");
                }
                else{
                    Log.i(TAG,"XX");
                    List<String> ability = new ArrayList<>();

                    for(int i=0; i < skills_user.size(); i++){
                        ability.add(skills_user.get(i).name);
                    }
                    Log.i(TAG,"XXI");
                    Log.i(TAG, "eueu");

                    List<String> interest = new ArrayList<>();
                    interest.add("Unity");
                    interest.add("3D Modeling");
                    interest.add("Game Engine Performance");
                    interest.add("Cooking");
                    Log.i(TAG, "setou user interesses");
                    // Seta os dados nas variáveis que serão utilisadas pelo app em qualquer tela

                    ((BaseAppExtender) my_app).setPicture(picture);
                    ((BaseAppExtender) my_app).setName(name);
                    ((BaseAppExtender) my_app).setEmail(uLogin.email);
                    ((BaseAppExtender) my_app).setAbility(ability);
                    ((BaseAppExtender) my_app).setInterest(interest);

                }

            }
        });
        Log.i(TAG, "aaafdklsjlflkdsj");
        //request_abilities()
        //.getApplication


        //ability.add("C++");
        //ability.add("Java");
        //ability.add("Android");
        //ability.add("Tapioca Engineering");
        Log.i(TAG, "setou user habilidades");//*/




       // Log.i(TAG, "setou user logado");
    //}

    private void setUserGlobalDataLogin(final User uLogin) {
        Log.i(TAG,"XIV");
        final Bitmap picture = BitmapFactory.decodeResource(this.getResources(), R.drawable.paul);
        //String name = getResources().getString(R.string.username);
        final String name = uLogin.name;
        //String email = getResources().getString(email);

        //Log.i(TAG, "fsdjkllfjks");

        retrieve_skills_signIn(new Skills_Callback() {

            @Override
            public void set_skills_callback(List<Skill> list,Application app) {

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
                Log.i(TAG,"XX");

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

                intent.putExtra("main", true);
                intent.putExtra("user", user.toString());
                startActivity(intent);
                Log.i(TAG,"passou");
            }
        }, uLogin.id, this.getApplication());


        //request_abilities()
        //.getApplication


        //ability.add("C++");
        //ability.add("Java");
        //ability.add("Android");
        //ability.add("Tapioca Engineering");
        Log.i(TAG, "setou user habilidades");//*/




        Log.i(TAG, "setou user logado");
    }


    void retrieve_skills_signIn(final Skills_Callback callback, int id, final Application app) {
        Log.i(TAG, "XV");

        //Log.i(TAG, "third");
        Log.i(TAG, "XIX");

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        UserService UserAPI = retrofit.create(UserService.class);


        Call<List<Skill>> lst = UserAPI.getSkillsUser(id);
        Log.i(TAG, "fourth");
        lst.enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "fifth");
                    callback.set_skills_callback(null, app);
                    Toast.makeText(getApplicationContext(), "erro na resposta dos skills", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "sixth");

                    skills_user = response.body();
                    if(skills_user!=null && skills_user.size() != 0) {
                        Log.i(TAG, "sdsqf " + skills_user.get(0).name);
                        Toast.makeText(getApplicationContext(), "skills retornadas", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "XIX");
                    }
                    else{
                        Log.i(TAG,"sem skills");
                        skills_user = null;
                    }
                    callback.set_skills_callback(skills_user,app);
                }
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {
                Log.i(TAG, "seventh");
                Toast.makeText(getApplicationContext(), "Falha no acesso ao servidor", Toast.LENGTH_LONG).show();
                callback.set_skills_callback(null,app);
            }
        });
    }


    void retrieve_interests_signIn(final Skills_Callback callback, int id, final Application app) {
        Log.i(TAG, "XV");

        //Log.i(TAG, "third");
        Log.i(TAG, "XIX");

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        UserService UserAPI = retrofit.create(UserService.class);


        Call<List<Skill>> lst = UserAPI.getInterestsUser(id);
        Log.i(TAG, "fourth");
        lst.enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "fifth");
                    callback.set_skills_callback(null, app);
                    Toast.makeText(getApplicationContext(), "erro na resposta dos interests", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "sixth");

                    interests_user = response.body();
                    if(interests_user!=null && interests_user.size() != 0) {
                        Log.i(TAG, "sdsqf " + interests_user.get(0).name);
                        Toast.makeText(getApplicationContext(), "interests retornados", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "XIX");
                    }
                    else{
                        Log.i(TAG,"sem interests");
                        interests_user = null;
                    }
                    callback.set_skills_callback(interests_user,app);
                }
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {
                Log.i(TAG, "seventh");
                Toast.makeText(getApplicationContext(), "Falha no acesso ao servidor", Toast.LENGTH_LONG).show();
                callback.set_skills_callback(null,app);
            }
        });
    }


    private void request_server_skills(final Email_callback email_callback) {
        Log.i(TAG,"XVI");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Log.i(TAG,"11");
        UserService UserAPI = retrofit.create(UserService.class);

        //Log.i(TAG,"12");
        Call<List<User>> lst = UserAPI.getUserByEmail(user);

        //Log.i(TAG,"13");
        lst.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG,"14");
                    Toast.makeText(getApplicationContext(), "Resposta dos Skills Corrompida", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG,"XVII");
                    List<User> lst_u = response.body();
                    //List<Skill> lst_s = null;

                    if (lst_u.size() == 0) {
                        Toast.makeText(getApplicationContext(), "Usuário sem skills", Toast.LENGTH_LONG).show();
                        email_callback.setEmailCallback(-1);
                        Log.i(TAG,"15");
                    } else {
                        Log.i(TAG,"XVIII");
                        email_callback.setEmailCallback(lst_u.get(0).id);
                        //Log.i(TAG,"16 " + lst_u.get(0).id);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i(TAG,"17");
                Toast.makeText(getApplicationContext(), "Sem acesso ao servidor", Toast.LENGTH_LONG).show();
                email_callback.setEmailCallback(-1);
            }
        });


    }
}


