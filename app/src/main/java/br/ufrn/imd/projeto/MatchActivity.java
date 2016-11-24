package br.ufrn.imd.projeto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MatchActivity extends AppCompatActivity {
    String targetSkill = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Button button = (Button) findViewById(R.id.btHeader);
        int size = ((BaseAppExtender) this.getApplication()).getMiniSize();
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar4);
        bitmap = new PictureCreator().getCroppedBitmap(bitmap);
        Drawable img = new BitmapDrawable(this.getResources(), bitmap);
        img.setBounds(0, 0, size, size);
        button.setCompoundDrawables(img, null, null, null);

        targetSkill = getIntent().getStringExtra("ability");

        ((TextView) findViewById(R.id.tvMatch)).setText(targetSkill);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendNotification(View view) {
        // TODO implementar envio de notificação
    }

    public void backToProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
