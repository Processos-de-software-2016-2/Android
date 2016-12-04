package br.ufrn.imd.projeto.activity;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import br.ufrn.imd.projeto.R;
import br.ufrn.imd.projeto.apiClient.Add_Current_Interests_Callback;
import br.ufrn.imd.projeto.apiClient.Add_Current_Skills_Callback;
import br.ufrn.imd.projeto.apiClient.Email_callback;
import br.ufrn.imd.projeto.apiClient.Get_Email_Skill_Callback;
import br.ufrn.imd.projeto.apiClient.Insert_Skill_Call;
import br.ufrn.imd.projeto.apiClient.Remove_Current_Interests_Call;
import br.ufrn.imd.projeto.apiClient.Remove_Current_Skills_Call;
import br.ufrn.imd.projeto.apiClient.service.UserService;
import br.ufrn.imd.projeto.dominio.BaseAppExtender;
import br.ufrn.imd.projeto.dominio.Skill;
import br.ufrn.imd.projeto.dominio.Skill_ID;
import br.ufrn.imd.projeto.dominio.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private static final int RESULT_SELECT_IMAGE = 100;
    private static final String TAG = "insercao_usuario";
    private static final String TAG1 = "add abilities:";
    private final int RESULT_CROP = 400;

    private boolean register;
    private String userId;
    private boolean successfulOperation = false;

    private BaseAppExtender app = ((BaseAppExtender) this.getApplication());
    private Bitmap picture;
    private String name;
    private String email;
    private String password = "";
    private String passwordConfirm = "";
    private List<String> abilityList = new ArrayList<>();
    /**/
    private List<String> abilityListBefore;
    private List<String> interestListBefore;
    private String ability;
    private List<String> interestList = new ArrayList<>();
    private String interest;
    private String ability_to_insert;
    private String ability_to_delete;
    private String interest_to_insert;
    private String interest_to_delete;

    private int iteracao;

    static int age=45;
    static int id=31;

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

        iteracao = 0;

        register = getIntent().getBooleanExtra("register", true);
        registerPart = getIntent().getIntExtra("part", 0);
        userId = getIntent().getStringExtra("user");

        /*here, all possible abilities are called from my resources*/
        items = getResources().getStringArray(R.array.tags);

        /*Guarda os valores que estavam antes globalmente*/
        abilityListBefore = new ArrayList<String>(((BaseAppExtender) this.getApplication()).getAbility());
        interestListBefore = new ArrayList<String>(((BaseAppExtender) this.getApplication()).getInterest());

        for(int i=0;i<abilityListBefore.size();i++){
            Log.i(TAG1,"1aux "+abilityListBefore.get(i));
        }

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
                Log.i(TAG1,"vai inflar as habilidades");
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

    /*pega a habilidade do campo autocompletavel e dar refresh na tela e na lista de habilidades*/
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

    /*Transforma a lista de habilidades em uma lista de botoes dinamicamente configurados e atualizando a lista de hablidades*/
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
                    Log.i(TAG1,"removeu uma habilidade");
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

    /*here the first post is performed, a user is inserted into database*/

    public void request_server_email_insert(final Get_Email_Skill_Callback callback, final User us){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);

        /*finally send the user of the sign up activity*/

        Call<Void> callPostUser = UserAPI.register_user(new User(us.email,us.age,us.password,us.id,us.name));

        /*Assyncronous task in background*/
        callPostUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "erroPost: " + response.code());
                    successfulOperation = false;
                    callback.email_retrieved("");
                    Toast.makeText(getApplicationContext(),"Usuário já existe!",Toast.LENGTH_LONG).show();
                } else { /*Successful post*/
                    Toast.makeText(getApplicationContext(),"Post executado",Toast.LENGTH_LONG).show();

                    successfulOperation = true;

                    /*'return' the id of the user inserted */
                    callback.email_retrieved(us.email);
                }
            }
            /*Conexion problem*/
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),"Falha na conexão!",Toast.LENGTH_LONG).show();
                Log.i(TAG, "erroPost: " + t.getMessage());
                successfulOperation = false;
                callback.email_retrieved("");

            }
        });
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

        User us = new User(email, age++, password, id++, name);

        request_server_email_insert(new Get_Email_Skill_Callback() {
            @Override
            public void email_retrieved(String email) {
                if (successfulOperation) {
                    setUserGlobalData();
                    Log.i(TAG,"un");
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("main", true);
                    intent.putExtra("user", email);
                    startActivity(intent);
                }
                else {
                    Log.i(TAG,"deux");
                    String errorMessage = getResources().getString(R.string.error2);
                    Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        },us);

        /*loadingDialog.show(getFragmentManager(), "loading");
        new ProcessRegister().execute(this);*/
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
                Log.i(TAG1,"entrouafldjslk");
                picture = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();
                name = ((EditText) findViewById(R.id.etName)).getText().toString();
                email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
                password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                passwordConfirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();
                break;

            case 1:
                request_server_id_insert(new Email_callback() {
                    @Override
                    public void setEmailCallback(final int id) {

                        adiciona_current_skills(id,new Add_Current_Skills_Callback() {
                            @Override
                            public void wait_insert_current_skills(boolean status) {
                                if(status){
                                    remove_current_skills(id, new Remove_Current_Skills_Call() {
                                        @Override
                                        public void wait_delete_current_skills(boolean status) {
                                            if(status){
                                                Toast.makeText(getApplicationContext(),"Server Atualizado",Toast.LENGTH_LONG).show();
                                                ability = ((TextView) findViewById(R.id.tvSelectedAbilities)).getText().toString();
                                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("main", true);
                                                intent.putExtra("user", userId);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                                else{
                                    Log.i(TAG1,"ainda nao, iteracao = "+iteracao);
                                }
                            }
                        });
                }},userId);
                break;
            case 2:
                request_server_id_insert(new Email_callback() {
                    @Override
                    public void setEmailCallback(final int id) {

                        adiciona_current_interests(id,new Add_Current_Interests_Callback() {
                            @Override
                            public void wait_insert_current_interests(boolean status) {
                                if(status){
                                    remove_current_interests(id, new Remove_Current_Interests_Call() {
                                        @Override
                                        public void wait_delete_current_interests(boolean status) {
                                            if(status){
                                                Toast.makeText(getApplicationContext(),"Servidor Atualizado",Toast.LENGTH_LONG).show();
                                                interest = ((TextView) findViewById(R.id.tvSelectedInterests)).getText().toString();
                                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("main", true);
                                                intent.putExtra("user", userId);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                                else{
                                    Log.i(TAG1,"ainda nao, iteracao = "+iteracao);
                                }
                            }
                        });
                    }},userId);
                break;
        }
    }


    /*Funcao que efetua a insercao dos skills que acabaram de ser adicionados*/

    void adiciona_current_skills(int id_u, final Add_Current_Skills_Callback callback){

        final Collection <String> abilityCol = new ArrayList<>(abilityList);
        Collection <String> abilityBeforeCol = new ArrayList<>(abilityListBefore);

        abilityCol.removeAll(abilityBeforeCol);

        if(abilityCol.size() != 0) {

            for (int i = 0; i < abilityList.size(); i++) {
                if (!abilityListBefore.contains(abilityList.get(i))) {
                    ability_to_insert = abilityList.get(i);

                    insert_delete_ability_aux(abilityList.get(i), id_u, new Insert_Skill_Call() {
                        @Override
                        public void Skill_Insert_Delete(int id_skill, int id_user) {
                            Gson gson = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                    .create();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(UserService.BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .build();

                            UserService UserAPI = retrofit.create(UserService.class);

                            String idu = id_user + "";
                            String idk = id_skill + "";

                            Call<Void> callPostSkill = UserAPI.insert_skill_user(new Skill_ID(idu, idk));

                            callPostSkill.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Log.i(TAG, "sucesso, acaba de inserir " + ability_to_insert + " ao global");
                                        iteracao++;

                                        if(iteracao  == abilityCol.size()) {
                                            iteracao = 0;
                                            callback.wait_insert_current_skills(true);
                                        }
                                        else{
                                            callback.wait_insert_current_skills(false);
                                        }
                                    } else {
                                        iteracao = 0;
                                        Toast.makeText(getApplicationContext(), "Algum erro na inserção", Toast.LENGTH_LONG).show();
                                        callback.wait_insert_current_skills(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    iteracao=0;
                                    Toast.makeText(getApplicationContext(), "Sem acesso ao Servidor", Toast.LENGTH_LONG).show();
                                    callback.wait_insert_current_skills(true);
                                }
                            });
                        }
                    });
                }
            }
        }
        else{
            callback.wait_insert_current_skills(true);
        }
    }

    void remove_current_skills(int id_u,final Remove_Current_Skills_Call callback) {

        final Collection<String> abilityBeforeCol = new ArrayList<>(abilityListBefore);
        Collection<String> abilityCol = new ArrayList<>(abilityList);

        abilityBeforeCol.removeAll(abilityCol);

        if (abilityBeforeCol.size() != 0) {

            for (int i = 0; i < abilityListBefore.size(); i++) {
                if (!abilityList.contains(abilityListBefore.get(i))) {

                    ability_to_delete = abilityListBefore.get(i);

                    insert_delete_ability_aux(abilityListBefore.get(i), id_u, new Insert_Skill_Call() {
                        @Override
                        public void Skill_Insert_Delete(int id_skill, int id_user) {

                            Gson gson = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                    .create();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(UserService.BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .build();

                            UserService UserAPI = retrofit.create(UserService.class);

                            Call<Void> callPostSkill = UserAPI.delete_skill_user(id_user, id_skill);

                            callPostSkill.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Delete Efetuado", Toast.LENGTH_LONG).show();
                                        iteracao++;

                                        if(iteracao == abilityBeforeCol.size()){
                                            iteracao = 0;
                                            callback.wait_delete_current_skills(true);
                                        }
                                        else{
                                            callback.wait_delete_current_skills(false);
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Erro ao deletar", Toast.LENGTH_LONG).show();
                                        iteracao = 0;
                                        callback.wait_delete_current_skills(true);

                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Servidor não alcançado", Toast.LENGTH_LONG).show();
                                    iteracao = 0;
                                    callback.wait_delete_current_skills(true);
                                }
                            });
                        }
                    });}
                }
            }
            else{
                iteracao = 0;
                callback.wait_delete_current_skills(true);
            }
        }

    void adiciona_current_interests(int id_u, final Add_Current_Interests_Callback callback){

        final Collection <String> interestCol = new ArrayList<>(interestList);
        Collection <String> interestBeforeCol = new ArrayList<>(interestListBefore);

        interestCol.removeAll(interestBeforeCol);

        if(interestCol.size() != 0) {

            for (int i = 0; i < interestList.size(); i++) {
                if (!interestListBefore.contains(interestList.get(i))) {
                    interest_to_insert = interestList.get(i);

                    insert_delete_ability_aux(interestList.get(i), id_u, new Insert_Skill_Call() {
                        @Override
                        public void Skill_Insert_Delete(int id_skill, int id_user) {
                            Gson gson = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                    .create();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(UserService.BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .build();

                            UserService UserAPI = retrofit.create(UserService.class);

                            String idu = id_user + "";
                            String idk = id_skill + "";

                            Call<Void> callPostSkill = UserAPI.insert_interest_user(new Skill_ID(idu, idk));

                            callPostSkill.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        iteracao++;

                                        if(iteracao  == interestCol.size()) {
                                            iteracao = 0;
                                            callback.wait_insert_current_interests(true);
                                        }
                                        else{
                                            callback.wait_insert_current_interests(false);
                                        }
                                        //Toast.makeText(getApplicationContext(), "Cadastro Efetuado", Toast.LENGTH_LONG).show();
                                    } else {
                                        iteracao = 0;
                                        Toast.makeText(getApplicationContext(), "Algum erro na inserção", Toast.LENGTH_LONG).show();
                                        callback.wait_insert_current_interests(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    iteracao=0;
                                    Toast.makeText(getApplicationContext(), "Sem acesso ao Servidor", Toast.LENGTH_LONG).show();
                                    callback.wait_insert_current_interests(true);
                                }
                            });
                        }
                    });
                }
            }
        }
        else{
            callback.wait_insert_current_interests(true);
        }
    }

    void remove_current_interests(int id_u,final Remove_Current_Interests_Call callback) {

        final Collection<String> interestBeforeCol = new ArrayList<>(interestListBefore);
        Collection<String> abilityCol = new ArrayList<>(abilityList);

        interestBeforeCol.removeAll(abilityCol);

        if (interestBeforeCol.size() != 0) {

            for (int i = 0; i < interestListBefore.size(); i++) {
                if (!interestList.contains(interestListBefore.get(i))) {

                    interest_to_delete = interestListBefore.get(i);

                    insert_delete_ability_aux(interestListBefore.get(i), id_u, new Insert_Skill_Call() {
                        @Override
                        public void Skill_Insert_Delete(int id_skill, int id_user) {

                            Gson gson = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                    .create();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(UserService.BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .build();

                            UserService UserAPI = retrofit.create(UserService.class);

                            Call<Void> callPostSkill = UserAPI.delete_interest_user(id_user, id_skill);

                            callPostSkill.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Delete Efetuado", Toast.LENGTH_LONG).show();
                                        iteracao++;

                                        if(iteracao == interestBeforeCol.size()){
                                            iteracao = 0;
                                            callback.wait_delete_current_interests(true);
                                        }
                                        else{
                                            callback.wait_delete_current_interests(false);
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Erro ao deletar", Toast.LENGTH_LONG).show();
                                        iteracao = 0;
                                        callback.wait_delete_current_interests(true);

                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Servidor não alcançado", Toast.LENGTH_LONG).show();
                                    iteracao = 0;
                                    callback.wait_delete_current_interests(true);
                                }
                            });
                        }
                    });}
            }
        }
        else{
            iteracao = 0;
            callback.wait_delete_current_interests(true);
        }
    }


    public void request_server_id_insert(final Email_callback callback, String email){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);

        Call<List<User>> userA = UserAPI.getUserByEmail(email);

        userA.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    List<User> user_added = response.body();

                    callback.setEmailCallback(user_added.get(0).id);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    public void insert_delete_ability_aux(final String ability_name, final int id_user, final Insert_Skill_Call callback){
        /*Gets the id of an specific skill using her name and pass this information to a callback method*/

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserService UserAPI = retrofit.create(UserService.class);

        Log.i(TAG,"seven " + ability_name);
        /*Gets ability information of the server using her name*/
        Call<List<Skill>> callUser = UserAPI.skill_by_name(ability_name);

        callUser.enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG,"Problema na resposta");
                }
                else{
                    List<Skill> lst = response.body();

                    if(lst!=null && lst.size()!=0){
                        callback.Skill_Insert_Delete(lst.get(0).id,id_user);
                    }
                    else{
                        /*In the case where the skill is not in server a different id skill is passed*/
                        callback.Skill_Insert_Delete(-1,id_user);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {

            }
        });
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
