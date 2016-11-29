package br.ufrn.imd.projeto;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    private String password = "";
    private String passwordConfirm = "";
    private List<String> abilityList = new ArrayList<>();
    private String ability;
    private List<String> interestList = new ArrayList<>();
    private String interest;

    private final LoadingDialog loadingDialog = new LoadingDialog();

    private ImageButton imageButton;

    private int registerPart = 0;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        register = getIntent().getBooleanExtra("register", true);
        registerPart = getIntent().getIntExtra("part", 0);
        userId = getIntent().getStringExtra("user");

        items = getResources().getStringArray(R.array.tags);

        initVariables();

        inflateView();
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

        if (register) {
            switch (registerPart) {
                case 0:
                    if (item.getItemId() == android.R.id.home) {
                        finish();
                        return true;
                    }
                    break;
                case 1:
                    inflateUser();
                    break;
                case 2:
                    interest = ((TextView) findViewById(R.id.tvSelectedInterests)).getText().toString();
                    inflateAbility();
                    break;
            }

            if (register) {
                findViewById(R.id.btUpdate).setVisibility(View.GONE);
            } else {
                findViewById(R.id.btGoNextStep).setVisibility(View.GONE);
            }

            --registerPart;
        }
        else {
            if (item.getItemId() == android.R.id.home) {
                finish();
                return true;
            }
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

        if (abilityList.isEmpty()) ability += "\n\t" + getResources().getString(R.string.emptyAbility);
        if (interestList.isEmpty()) interest += "\n\t" + getResources().getString(R.string.emptyInterest);
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

    private void inflateView() {
        switch (registerPart) {
            case 0:
                inflateUser();
                break;
            case 1:
                inflateAbility();
                break;
            case 2:
                inflateInterest();
                break;
        }

        if (register) {
            findViewById(R.id.btUpdate).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.btGoNextStep).setVisibility(View.GONE);
        }
    }

    private void inflateUser() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llRegister);
        View view;

        view = getLayoutInflater().inflate(R.layout.activity_register_user, null);
        linearLayout.removeAllViews();
        linearLayout.addView(view);

        ((ImageButton) findViewById(R.id.ibProfilePicture)).setImageBitmap(picture);
        ((EditText) findViewById(R.id.etName)).setText(name);
        ((EditText) findViewById(R.id.etEmail)).setText(email);

        imageButton = (ImageButton) findViewById(R.id.ibProfilePicture);
        imageButton.getLayoutParams().width = ((BaseAppExtender) this.getApplication()).getSize();
        imageButton.getLayoutParams().height = ((BaseAppExtender) this.getApplication()).getSize();
    }

    private void inflateAbility() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llRegister);
        View view;

        view = getLayoutInflater().inflate(R.layout.activity_register_ability, null);
        linearLayout.removeAllViews();
        linearLayout.addView(view);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.acAbilitySelect);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        autoCompleteTextView.setAdapter(adapter);

        ((TextView) findViewById(R.id.tvSelectedAbilities)).setText(ability);

        refreshAbilityList();
    }

    private void inflateInterest() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llRegister);
        View view;

        view = getLayoutInflater().inflate(R.layout.activity_register_interest, null);
        linearLayout.removeAllViews();
        linearLayout.addView(view);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.acInterestSelect);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        autoCompleteTextView.setAdapter(adapter);

        ((TextView) findViewById(R.id.tvSelectedInterests)).setText(interest);

        refreshInterestList();
    }

    public void goToAbilityRegister(View view) {
        picture = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
        name = ((EditText) findViewById(R.id.etName)).getText().toString();
        email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
        password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        passwordConfirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();

        ++registerPart;

        inflateView();
    }

    public void addAbility(final View view) {
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.acAbilitySelect);
        String newAbility = autoCompleteTextView.getText().toString();
        InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if (Arrays.asList(items).contains(newAbility)) {
            if (abilityList.contains(newAbility)) {
                String errorMessage = newAbility + getResources().getString(R.string.error7);
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                abilityList.add(newAbility);
            }

            refreshAbilityList();

            autoCompleteTextView.setText("");

            keyboard.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        else {
            String errorMessage = getResources().getString(R.string.error5);
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void refreshAbilityList() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.llSelectedAbilities);
        TextView textView = (TextView) findViewById(R.id.tvSelectedAbilities);
        String listAbility = getResources().getString(R.string.selected_abilities);
        Button button;
        Bitmap bitmap;
        Drawable img;

        layout.removeAllViews();

        if (abilityList.isEmpty()) listAbility += "\n\t" + getResources().getString(R.string.emptyAbility);

        textView.setText(listAbility);

        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_clear_black_18dp);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < abilityList.size(); ++i) {
            final int j = i;
            button = new Button(this);
            button.setCompoundDrawables(null, null, img, null);
            button.setText(abilityList.get(i));
            button.setBackgroundResource(0);
            button.setPadding(5, 3, 5, 3);
            button.setGravity(Gravity.START);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abilityList.remove(j);
                    refreshAbilityList();
                }
            });
            layout.addView(button);
        }
    }

    public void goToInterestRegister(View view) {
        ability = ((TextView) findViewById(R.id.tvSelectedAbilities)).getText().toString();

        ++registerPart;

        inflateView();
    }

    public void addInterest(View view) {
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.acInterestSelect);
        String newInterest = autoCompleteTextView.getText().toString();
        InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if (Arrays.asList(items).contains(newInterest)) {
            if (interestList.contains(newInterest)) {
                String errorMessage = newInterest + getResources().getString(R.string.error7);
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                interestList.add(newInterest);
            }

            refreshInterestList();

            autoCompleteTextView.setText("");

            keyboard.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        else {
            String errorMessage = getResources().getString(R.string.error6);
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void refreshInterestList() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.llSelectedInterests);
        TextView textView = (TextView) findViewById(R.id.tvSelectedInterests);
        String listInterest = getResources().getString(R.string.selected_interests);
        Button button;
        Bitmap bitmap;
        Drawable img;

        layout.removeAllViews();

        if (interestList.isEmpty()) listInterest += "\n\t" + getResources().getString(R.string.emptyInterest);

        textView.setText(listInterest);

        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_clear_black_18dp);
        img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < interestList.size(); ++i) {
            final int j = i;
            button = new Button(this);
            button.setCompoundDrawables(null, null, img, null);
            button.setText(interestList.get(i));
            button.setBackgroundResource(0);
            button.setPadding(5, 3, 5, 3);
            button.setGravity(Gravity.START);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interestList.remove(j);
                    refreshInterestList();
                }
            });
            layout.addView(button);
        }
    }

    public void confirmRegister(View view) {
        switch(registerPart) {
            case 0:
                picture = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
                name = ((EditText) findViewById(R.id.etName)).getText().toString();
                email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
                password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                passwordConfirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();
                break;
            case 1:
                ability = ((TextView) findViewById(R.id.tvSelectedAbilities)).getText().toString();
                break;
            case 2:
                interest = ((TextView) findViewById(R.id.tvSelectedInterests)).getText().toString();
                break;
        }

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
        switch(registerPart) {
            case 0:
                picture = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
                name = ((EditText) findViewById(R.id.etName)).getText().toString();
                email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
                password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                passwordConfirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();
                break;
            case 1:
                ability = ((TextView) findViewById(R.id.tvSelectedAbilities)).getText().toString();
                break;
            case 2:
                interest = ((TextView) findViewById(R.id.tvSelectedInterests)).getText().toString();
                break;
        }

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
                String message = getResources().getString(R.string.register) + " " + getResources().getString(R.string.ok);
                Toast toast = Toast.makeText(result, message, Toast.LENGTH_SHORT);
                toast.show();

                Intent intent = new Intent(result, MainActivity.class);
                intent.putExtra("main", true);
                intent.putExtra("user", email);
                startActivity(intent);

                finish();
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
