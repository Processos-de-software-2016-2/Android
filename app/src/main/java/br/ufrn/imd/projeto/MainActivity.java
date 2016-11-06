package br.ufrn.imd.projeto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goLoginScreen(View view) {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }

    public void goProfileScreen(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);

        startActivity(intent);
    }

    public void goSearchScreen(View view) {
        Intent intent = new Intent(this, SearchActivity.class);

        startActivity(intent);
    }
}