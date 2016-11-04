package com.shawnhillyer.admin.randomancer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MenuActivity extends AppCompatActivity {

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
                // Go to main menu activity
                // TODO: Update the last parameter to go to the CreateCharacterActivity after I create that activity
                Intent intentSelect = new Intent(MenuActivity.this, ViewCharactersActivity.class);
                startActivity(intentSelect);
            }
        });


    }
}
