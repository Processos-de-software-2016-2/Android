package br.ufrn.imd.projeto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    String user;
    String password;
    boolean successfulLogin = false;

    Bundle bundle = new Bundle();
    ErrorDialog errorDialog = new ErrorDialog();
    LoadingDialog loadingDialog = new LoadingDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    public void signIn(View view) {
        user = ((EditText) findViewById(R.id.etUser)).getText().toString();
        password = ((EditText) findViewById(R.id.etPassword)).getText().toString();

        loadingDialog.show(getFragmentManager(), "loading");
        new ProcessLogin().execute(this);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);
    }

    public void loginFacebook(MenuItem item) {
        /*
        TODO login com facebook
         */
    }

    public void loginGoogle(MenuItem item) {
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

    private void setUserGlobalData(String userId) {
        /*
        TODO pegar os dados do usuário do BD
        remover os dados lacais abaixo e setar os vindos do BD
        use userId pare selecionar o usuário
         */
        Bitmap picture = BitmapFactory.decodeResource(this.getResources(), R.drawable.paul);
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
        ((BaseAppExtender) this.getApplication()).setInterest(interest);
    }

    // Thread que irá fazer o login
    private class ProcessLogin extends AsyncTask<Context, Void, Context> {

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
                Intent intent = new Intent(result, ProfileActivity.class);
                intent.putExtra("main", true);
                intent.putExtra("user", user);
                startActivity(intent);
            }
            else {
                bundle.putInt("code", 0);
                errorDialog.setArguments(bundle);
                errorDialog.show(getFragmentManager(), "error");
            }
        }

    }
}