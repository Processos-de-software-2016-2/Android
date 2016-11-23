package br.ufrn.imd.projeto;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "android_application";
    String user;
    String password;
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

    public void request_server_login(final Interface_Call callback){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);
        Call<List<User>> callUser = UserAPI.getUserByEmail(user);


        //asynchronous call
        callUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "erro: " + response.code());

                } else {
                    List<User> user_aux = response.body();

                    if (user_aux.size()!=0 && user_aux.get(0).password.equals(password)) {
                        Log.i(TAG, "passou1");
                        callback.setUser_login(user_aux.get(0));
                        callback.login_success(true);
                    } else if(user_aux.size()==0){
                        Log.i(TAG, "passou2");
                        Toast.makeText(getApplicationContext(), "Email incorreto!",Toast.LENGTH_LONG).show();
                        callback.login_success(false);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Senha incorreta",Toast.LENGTH_LONG).show();
                        Log.i(TAG, "nao passou mas email existe");
                        callback.login_success(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sem Acesso ao Servidor",Toast.LENGTH_LONG).show();
                Log.i(TAG,"serv " + t.getMessage());
                callback.login_success(false);
            }
        });
    }

    public void signIn(View view)  {
        user = loginDialog.getUser();
        password = loginDialog.getPassword();

        Log.i(TAG, "passou aqui");

        request_server_login(new Interface_Call() {
            @Override
            public void login_success(boolean value) {
                Log.i(TAG, "entrou login success");
                if (value == true) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("main", true);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    bundle.putInt("code", 0);
                    errorDialog.setArguments(bundle);
                    errorDialog.show(getFragmentManager(), "error");
                }
            }

            @Override
            public void setUser_login(User user) {
                Log.i(TAG, "entrou no set user login");

                User user_login = new User(user.email,user.age,user.password,user.id,user.name);

                Log.i(TAG, "vai chamar setUserGlobal e setou user");
                setUserGlobalData(user_login);
                Log.i(TAG, "saiu do set user login");
            }
        });

        Log.i(TAG, "passou100");
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

        if(user.equals(this.password) && password.equals(this.password)){
            return true;
        }
        return false;
    }

    private void setUserGlobalData(User uLogin) {
        Log.i(TAG, "entrou para setar usuario");

        Bitmap picture = BitmapFactory.decodeResource(this.getResources(), R.drawable.paul);
        //String name = getResources().getString(R.string.username);
        String name = uLogin.name;
        //String email = getResources().getString(email);

        List<String> ability = new ArrayList<>();
        ability.add("C++");
        ability.add("Java");
        ability.add("Android");
        ability.add("Tapioca Engineering");
        Log.i(TAG, "setou user habilidades");

        List<String> interest = new ArrayList<>();
        interest.add("Unity");
        interest.add("3D Modeling");
        interest.add("Game Engine Performance");
        interest.add("Cooking");
        Log.i(TAG, "setou user interesses");
        // Seta os dados nas variáveis que serão utilisadas pelo app em qualquer tela
        ((BaseAppExtender) this.getApplication()).setPicture(picture);
        ((BaseAppExtender) this.getApplication()).setName(name);
        ((BaseAppExtender) this.getApplication()).setEmail(uLogin.email);
        ((BaseAppExtender) this.getApplication()).setAbility(ability);
        ((BaseAppExtender) this.getApplication()).setInterest(interest);

        Log.i(TAG, "setou user logado");
    }

}

