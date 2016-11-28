package br.ufrn.imd.projeto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "insert_values_server";
    boolean register;
    String userId;
    boolean successfulOperation = false;
    static int age=45;
    static int id=31;

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
            Toast.makeText(this,"1",Toast.LENGTH_LONG).show();
            picture = BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);
            name = "";
            email = "";
            ability = getResources().getString(R.string.selected_abilities);
            interest = getResources().getString(R.string.selected_interests);
        }
        else {
            Toast.makeText(this,"2",Toast.LENGTH_LONG).show();
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

    /*here the first post is performed, a user is inserted into database*/

    public void request_server_email_insert(final Get_Email_Skill_Callback callback, final User us){
        Log.i(TAG,"three");

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
                    Toast.makeText(getApplicationContext(),"Usuário já existe!",Toast.LENGTH_LONG).show();
                } else { /*Successful post*/
                    Log.i(TAG,"four");
                    Toast.makeText(getApplicationContext(),"Post executado",Toast.LENGTH_LONG).show();

                    /*'return' the id of the user inserted */
                    Log.i(TAG,"onde "+us.email);
                    //callback.set_user_request(us.email);
                    callback.email_retrieved(us.email);
                }
            }
            /*Conexion problem*/
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Falha na conexão!",Toast.LENGTH_LONG).show();
                Log.i(TAG, "erroPost: " + t.getMessage());
            }
        });
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

    /*When the button of registration is clicked, this function get and send data towards the server*/

    public void confirmRegister(View view) {
        Log.i(TAG, "one");
        ImageButton imageButton = (ImageButton) findViewById(R.id.ibProfilePicture);

        picture = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();
        name = ((EditText) findViewById(R.id.etName)).getText().toString();
        email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
        password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
        passwordConfirm = ((EditText) findViewById(R.id.etConfirmPassword)).getText().toString();
        ability = ((TextView) findViewById(R.id.tvSelectedAbilities)).getText().toString();
        interest = ((TextView) findViewById(R.id.tvSelectedInterests)).getText().toString();

        Log.i(TAG, ability);
        Log.i(TAG, interest);

        /*Creates the vector of abilities and interests to send*/
        final String[] abilitiesToinsert = ability.split("\n");
        final String[] interestsToinsert = interest.split("\n");

        /*This user will be sent to the server*/
        User us = new User(email, age++, password, id++, name);
        Log.i(TAG, "two");

        /*send the user and wait for the response email using a callback*/
        request_server_email_insert(new Get_Email_Skill_Callback() {
            @Override
            public void email_retrieved(String email_send) {
                /*send the email and wait for information about the id of the user*/
                request_server_id_insert(new Email_callback() {
                    @Override
                    public void setEmailCallback(int id) {

                        for (int i = 1; i < abilitiesToinsert.length; i++) {

                            abilitiesToinsert[i] = abilitiesToinsert[i].replaceAll("\\s", "");

                            insert_ability_aux(abilitiesToinsert[i], id, new Insert_Skill_Call() {
                                @Override
                                public void Skill_Insert(int id_skill, int id_user) {
                                    Gson gson = new GsonBuilder()
                                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                            .create();

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(UserService.BASE_URL)
                                            .addConverterFactory(GsonConverterFactory.create(gson))
                                            .build();

                                    UserService UserAPI = retrofit.create(UserService.class);

                                    String idu = id_user+"";
                                    String idk = id_skill+"";

                                    Log.i(TAG, "fdjslbababa " + id_user + " " + id_skill);
                                    Call<Void> callPostSkill = UserAPI.insert_skill_user(new Skill_ID(idu,idk));

                                    callPostSkill.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            Log.i(TAG,"sucesso");
                                            Toast.makeText(getApplicationContext(),"Cadastro Efetuado",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {

                                        }
                                    });
                                }
                            });
                        }

                        //=============================================================================

                        for (int i = 1; i < interestsToinsert.length; i++) {

                            interestsToinsert[i] = interestsToinsert[i].replaceAll("\\s", "");

                            insert_ability_aux(interestsToinsert[i], id, new Insert_Skill_Call() {
                                @Override
                                public void Skill_Insert(int id_skill, int id_user) {
                                    Gson gson = new GsonBuilder()
                                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                            .create();

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(UserService.BASE_URL)
                                            .addConverterFactory(GsonConverterFactory.create(gson))
                                            .build();

                                    UserService UserAPI = retrofit.create(UserService.class);

                                    String idu = id_user+"";
                                    String idk = id_skill+"";

                                    Log.i(TAG, "fdjslbababa " + id_user + " " + id_skill);
                                    Call<Void> callPostSkill = UserAPI.insert_interest_user(new Skill_ID(idu,idk));

                                    callPostSkill.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            Log.i(TAG,"sucesso");
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {

                                        }
                                    });
                                }
                            });
                        }



                        //================================================================================
                    }
                }, email_send);
            }
        }, us);
    }



    public void insert_ability_aux(final String ability_name, final int id_user, final Insert_Skill_Call callback){
        Log.i(TAG,"six");
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

                    if(lst == null)
                        Log.i(TAG,"eight"+ lst.toString());
                    else{
                        Log.i(TAG,"eightdfslkl"+ lst.toString());
                    }

                    if(lst!=null && lst.size()!=0){
                        Log.i(TAG,"nine " + id_user);
                        callback.Skill_Insert(lst.get(0).id,id_user);
                    }
                    else{
                        Log.i(TAG,"nine_branch");
                        /*In the case where the skill is not in server a different id skill is passed*/
                        callback.Skill_Insert(-1,id_user);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {

            }
        });
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

        /*loadingDialog.show(getFragmentManager(), "loading");
        new ProcessUpdate().execute(this);*/
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
}

