package br.ufrn.imd.projeto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    boolean register;
    String userId;
    Bitmap picture;
    String name;
    String email;
    String password;
    String passwordConfirm;
    String ability;
    String interest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        LoadingDialog loadingDialog = new LoadingDialog();

        loadingDialog.show(getFragmentManager(), "loading");

        initVariables();

        initFields();

        if (register) {
            findViewById(R.id.btUpdate).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.btRegister).setVisibility(View.GONE);
        }

        loadingDialog.dismiss();
    }

    private void initVariables() {
        register = getIntent().getBooleanExtra("register", true);
        userId = getIntent().getStringExtra("user");

        if (register) {
            picture = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
            name = "";
            email = "";
            ability = getResources().getString(R.string.selected_abilities);
            interest = getResources().getString(R.string.selected_interests);
        }
        else {
            getUserData();
        }
    }

    private void getUserData() {
        /*
        TODO carregar dados do usuário do BD
        use userId para selecionar o usuário no BD
        jogue a imagem em picture
        substitua "?" de acordo com as strings carregadas
         */

        picture = BitmapFactory.decodeResource(this.getResources(), android.R.drawable.ic_menu_help);
        name = "?";
        email = "?";
        ability = getResources().getString(R.string.selected_abilities) + "\n?";
        interest = getResources().getString(R.string.selected_interests) + "\n?";
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

        textView.setText(listAbility);
    }

    public void addInterest(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spInterestSelect);
        TextView textView = (TextView) findViewById(R.id.tvSelectedInterests);

        String newInterest = spinner.getSelectedItem().toString();
        String listInterest= textView.getText().toString();

        listInterest += "\n\t" + newInterest;

        textView.setText(listInterest);
    }

    public void confirmRegister(View view) {
        ErrorDialog errorDialog = new ErrorDialog();
        Bundle bundle = new Bundle();
        ImageButton imageButton = (ImageButton) findViewById(R.id.ibProfilePicture);

        picture = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
        name = ((EditText) findViewById(R.id.etName)).getText().toString();
        email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
        password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        passwordConfirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();
        ability = ((TextView) findViewById(R.id.tvSelectedAbilities)).getText().toString();
        interest = ((TextView) findViewById(R.id.tvSelectedInterests)).getText().toString();

        if (password.equals(passwordConfirm) && checkRegister(picture, name, email, password, ability, interest)) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        else {
            bundle.putInt("code", 1);
            errorDialog.setArguments(bundle);
            errorDialog.show(getFragmentManager(), "error");
        }
    }

    private boolean checkRegister(Bitmap picture, String name, String email, String password, String ability, String interest) {
        boolean success;
        LoadingDialog loadingDialog = new LoadingDialog();

        loadingDialog.show(getFragmentManager(), "loading");

        success = registerWithServer(picture, name, email, password, ability, interest);

        loadingDialog.dismiss();

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
        ErrorDialog errorDialog = new ErrorDialog();
        Bundle bundle = new Bundle();
        ImageButton imageButton = (ImageButton) findViewById(R.id.ibProfilePicture);

        picture = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
        name = ((EditText) findViewById(R.id.etName)).getText().toString();
        email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
        password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        passwordConfirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();
        ability = ((TextView) findViewById(R.id.tvSelectedAbilities)).getText().toString();
        interest = ((TextView) findViewById(R.id.tvSelectedInterests)).getText().toString();

        if (password.equals(passwordConfirm) && checkUpdate(picture, name, email, password, ability, interest)) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        else {
            bundle.putInt("code", 2);
            errorDialog.setArguments(bundle);
            errorDialog.show(getFragmentManager(), "error");
        }
    }

    private boolean checkUpdate(Bitmap picture, String name, String email, String password, String ability, String interest) {
        boolean success;
        LoadingDialog loadingDialog = new LoadingDialog();

        loadingDialog.show(getFragmentManager(), "loading");

        success = updateWithServer(picture, name, email, password, ability, interest);

        loadingDialog.dismiss();

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
}
