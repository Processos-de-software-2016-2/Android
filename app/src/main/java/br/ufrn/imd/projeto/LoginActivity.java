package br.ufrn.imd.projeto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

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
        String user = ((EditText) findViewById(R.id.etUser)).getText().toString();
        String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        ErrorDialog errorDialog = new ErrorDialog();
        Bundle bundle = new Bundle();

        if (checkLogin(user, password)) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        else {
            bundle.putInt("code", 0);
            errorDialog.setArguments(bundle);
            errorDialog.show(getFragmentManager(), "error");
        }
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
        boolean success;
        LoadingDialog loadingDialog = new LoadingDialog();

        loadingDialog.show(getFragmentManager(), "loading");

        success = loginWithServer(user, password);

        loadingDialog.dismiss();

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
}