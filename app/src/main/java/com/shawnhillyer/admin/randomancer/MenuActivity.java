package com.shawnhillyer.admin.randomancer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MenuActivity extends AppCompatActivity {

    // Create the drop down menu on top right (... button)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch(id) {
            case R.id.action_logout:
                // TODO: Add prompt "are you sure want to log out"
                // TODO: Actually log user out
                // TODO: Note the above doesn't apply for assignment 4
                Intent intentLogout = new Intent(this, MainActivity.class);
                startActivity(intentLogout);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // View Characters button
        Button viewCharactersButton = (Button) findViewById(R.id.viewCharactersButton);
        viewCharactersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to main menu activity
                Intent intentSelect = new Intent(MenuActivity.this, ViewCharactersActivity.class);
                startActivity(intentSelect);
            }
        });

        // Create Character button
        Button createCharacterButton = (Button) findViewById(R.id.createCharacterButton);
        createCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to Create Character activity
                Intent intentSelect = new Intent(MenuActivity.this, CreateCharacterActivity.class);
                startActivity(intentSelect);
            }
        });

    }


}
