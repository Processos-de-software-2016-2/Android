package br.ufrn.imd.projeto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    boolean register;
    String userId;
    boolean successfulOperation = false;

    Bitmap picture;
    String name;
    String email;
    String password;
    String passwordConfirm;
    List<String> abilityList = new ArrayList<>();
    String ability;
    List<String> interestList = new ArrayList<>();
    String interest;

    Bundle bundle = new Bundle();
    ErrorDialog errorDialog = new ErrorDialog();
    LoadingDialog loadingDialog = new LoadingDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        register = getIntent().getBooleanExtra("register", true);
        userId = getIntent().getStringExtra("user");

        initVariables();

        initFields();

        if (register) {
            findViewById(R.id.btUpdate).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.btRegister).setVisibility(View.GONE);
        }
    }

    private void initVariables() {
        if (register) {
            picture = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
            name = "";
            email = "";
            ability = getResources().getString(R.string.selected_abilities);
            interest = getResources().getString(R.string.selected_interests);
        }
        else {
            picture = ((BaseAppExtender) this.getApplication()).getPicture();
            name = ((BaseAppExtender) this.getApplication()).getName();
            email = ((BaseAppExtender) this.getApplication()).getEmail();

            abilityList = ((BaseAppExtender) this.getApplication()).getAbility();
            ability = getResources().getString(R.string.selected_abilities);
            for (int i = 0; i < abilityList.size(); ++i) {
                ability += "\n\t" + abilityList.get(i);
            }

            interestList = ((BaseAppExtender) this.getApplication()).getInterest();
            interest = getResources().getString(R.string.selected_interests);
            for (int i = 0; i < interestList.size(); ++i) {
                interest += "\n\t" + interestList.get(i);
            }
        }
    }

    private void initFields() {
        ((ImageButton) findViewById(R.id.ibProfilePicture)).setImageBitmap(picture);
        ((EditText) findViewById(R.id.etName)).setText(name);
        ((EditText) findViewById(R.id.etEmail)).setText(email);
        ((TextView) findViewById(R.id.tvSelectedAbilities)).setText(ability);
        ((TextView) findViewById(R.id.tvSelectedInterests)).setText(interest);
    }

    public void loadPicture(View view) {
        /*
        TODO carregar imagem da galeria padrão do android
         */
    }

    public void addAbility(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spAbilitySelect);
        TextView textView = (TextView) findViewById(R.id.tvSelectedAbilities);

        String newAbility = spinner.getSelectedItem().toString();
        String listAbility = textView.getText().toString();

        listAbility += "\n\t" + newAbility;
        abilityList.add(newAbility);

        textView.setText(listAbility);
    }

    public void addInterest(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spInterestSelect);
        TextView textView = (TextView) findViewById(R.id.tvSelectedInterests);

        String newInterest = spinner.getSelectedItem().toString();
        String listInterest= textView.getText().toString();

        listInterest += "\n\t" + newInterest;
        interestList.add(newInterest);

        textView.setText(listInterest);
    }

    public void confirmRegister(View view) {
        ImageButton imageButton = (ImageButton) findViewById(R.id.ibProfilePicture);

        picture = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
        name = ((EditText) findViewById(R.id.etName)).getText().toString();
        email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
        password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        passwordConfirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();
        ability = ((TextView) findViewById(R.id.tvSelectedAbilities)).getText().toString();
        interest = ((TextView) findViewById(R.id.tvSelectedInterests)).getText().toString();

        loadingDialog.show(getFragmentManager(), "loading");
        new ProcessRegister().execute(this);
    }

    private boolean checkRegister(Bitmap picture, String name, String email, String password, String ability, String interest) {
        boolean success = registerWithServer(picture, name, email, password, ability, interest);

        return success;
    }

    private boolean registerWithServer(Bitmap picture, String name, String email, String password, String ability, String interest) {
        /*
        TODO cadastro com o servidor
        retorne verdadeiro se cadastro com sucesso
        retorne false se cadastro falhou
         */
        return true;
    }

    public void confirmUpdate(View view) {
        ImageButton imageButton = (ImageButton) findViewById(R.id.ibProfilePicture);

        picture = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
        name = ((EditText) findViewById(R.id.etName)).getText().toString();
        email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
        password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        passwordConfirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();
        ability = ((TextView) findViewById(R.id.tvSelectedAbilities)).getText().toString();
        interest = ((TextView) findViewById(R.id.tvSelectedInterests)).getText().toString();

        loadingDialog.show(getFragmentManager(), "loading");
        new ProcessUpdate().execute(this);
    }

    private boolean checkUpdate(Bitmap picture, String name, String email, String password, String ability, String interest) {
        boolean success = updateWithServer(picture, name, email, password, ability, interest);

        return success;
    }

    private boolean updateWithServer(Bitmap picture, String name, String email, String password, String ability, String interest) {
        /*
        TODO cadastro com o servidor
        retorne verdadeiro se cadastro com sucesso
        retorne false se cadastro falhou
         */
        return true;
    }

    private void setUserGlobalData() {
        // Seta os dados nas variáveis que serão utilisadas pelo app em qualquer tela
        ((BaseAppExtender) this.getApplication()).setPicture(picture);
        ((BaseAppExtender) this.getApplication()).setName(name);
        ((BaseAppExtender) this.getApplication()).setEmail(email);
        ((BaseAppExtender) this.getApplication()).setAbility(abilityList);
        ((BaseAppExtender) this.getApplication()).setInterest(interestList);
    }

    // Thread que irá fazer o registro
    private class ProcessRegister extends AsyncTask<Context, Void, Context> {

        @Override
        protected Context doInBackground(Context... params) {
            if (password.equals(passwordConfirm) && checkRegister(picture, name, email, password, ability, interest)) {
                setUserGlobalData();
                successfulOperation = true;
            }
            else {
                successfulOperation = false;
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(Context result) {
            loadingDialog.dismiss();

            if (successfulOperation) {
                Intent intent = new Intent(result, ProfileActivity.class);
                intent.putExtra("main", true);
                intent.putExtra("user", email);
                startActivity(intent);
            }
            else {
                bundle.putInt("code", 1);
                errorDialog.setArguments(bundle);
                errorDialog.show(getFragmentManager(), "error");
            }
        }

    }

    // Thread que irá fazer o update
    private class ProcessUpdate extends AsyncTask<Context, Void, Context> {

        @Override
        protected Context doInBackground(Context... params) {
            if (password.equals(passwordConfirm) && checkUpdate(picture, name, email, password, ability, interest)) {
                setUserGlobalData();
                successfulOperation = true;
            }
            else {
                successfulOperation = false;
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(Context result) {
            loadingDialog.dismiss();

            if (successfulOperation) {
                Intent intent = new Intent(result, ProfileActivity.class);
                intent.putExtra("main", true);
                intent.putExtra("user", email);
                startActivity(intent);
            }
            else {
                bundle.putInt("code", 2);
                errorDialog.setArguments(bundle);
                errorDialog.show(getFragmentManager(), "error");
            }
        }

    }
}
