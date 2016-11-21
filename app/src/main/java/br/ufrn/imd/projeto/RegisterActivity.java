package br.ufrn.imd.projeto;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final int RESULT_SELECT_IMAGE = 100;
    private final int RESULT_CROP = 400;

    private boolean register;
    private String userId;
    private boolean successfulOperation = false;

    private Bitmap picture;
    private String name;
    private String email;
    private String password;
    private String passwordConfirm;
    private List<String> abilityList = new ArrayList<>();
    private String ability;
    private List<String> interestList = new ArrayList<>();
    private String interest;

    private final LoadingDialog loadingDialog = new LoadingDialog();

    private ImageButton imageButton;

    private int registerPart = 0;

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

        imageButton = (ImageButton) findViewById(R.id.ibProfilePicture);
        imageButton.getLayoutParams().width = ((BaseAppExtender) this.getApplication()).getSize();
        imageButton.getLayoutParams().height = ((BaseAppExtender) this.getApplication()).getSize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case RESULT_SELECT_IMAGE:

                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                        if (cursor != null) {
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            cursor.close();

                            performCrop(picturePath);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    String errorMessage = getResources().getString(R.string.error4);
                    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;

            case RESULT_CROP:

                if(resultCode == Activity.RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");

                    imageButton.setImageBitmap(selectedBitmap);
                }

                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    }

    public void loadPicture(View view) {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_SELECT_IMAGE);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void goToAbilityRegister(View view) {
        picture = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
        name = ((EditText) findViewById(R.id.etName)).getText().toString();
        email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
        password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        passwordConfirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llRegister);
        View v = getLayoutInflater().inflate(R.layout.activity_register_ability, null);

        linearLayout.removeAllViews();
        linearLayout.addView(v);

        ((TextView) findViewById(R.id.tvSelectedAbilities)).setText(ability);

        ++registerPart;
    }

    public void addAbility(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spAbilitySelect);
        TextView textView = (TextView) findViewById(R.id.tvSelectedAbilities);

        if (spinner.getSelectedItemPosition() > 0) {
            String newAbility = spinner.getSelectedItem().toString();
            String listAbility;

            if (abilityList.contains(newAbility)) {
                abilityList.remove(abilityList.indexOf(newAbility));
            }
            else {
                abilityList.add(newAbility);
            }

            listAbility = getResources().getString(R.string.selected_abilities);

            for (int i = 0; i < abilityList.size(); ++i) {
                listAbility += "\n\t" + abilityList.get(i);
            }

            textView.setText(listAbility);
        }
    }

    public void goToInterestRegister(View view) {
        ability = ((TextView) findViewById(R.id.tvSelectedAbilities)).getText().toString();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llRegister);
        View v = getLayoutInflater().inflate(R.layout.activity_register_interest, null);

        linearLayout.removeAllViews();
        linearLayout.addView(v);

        ((TextView) findViewById(R.id.tvSelectedInterests)).setText(interest);

        if (register) {
            findViewById(R.id.btUpdate).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.btRegister).setVisibility(View.GONE);
        }

        ++registerPart;
    }

    public void addInterest(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spInterestSelect);
        TextView textView = (TextView) findViewById(R.id.tvSelectedInterests);

        if (spinner.getSelectedItemPosition() > 0) {
            String newInterest = spinner.getSelectedItem().toString();
            String listInterest;

            if (interestList.contains(newInterest)) {
                interestList.remove(interestList.indexOf(newInterest));
            }
            else {
                interestList.add(newInterest);
            }

            listInterest = getResources().getString(R.string.selected_interests);

            for (int i = 0; i < interestList.size(); ++i) {
                listInterest += "\n\t" + interestList.get(i);
            }

            textView.setText(listInterest);
        }
    }

    public void confirmRegister(View view) {
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

    private void performCrop(String picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 360);
            cropIntent.putExtra("outputY", 360);

            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        catch (ActivityNotFoundException e) {
            String errorMessage = getResources().getString(R.string.error3);
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
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
                String errorMessage = getResources().getString(R.string.error1);
                Toast toast = Toast.makeText(result, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
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
                String errorMessage = getResources().getString(R.string.error2);
                Toast toast = Toast.makeText(result, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }
}
