package com.shawnhillyer.admin.randomancer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Handle clicking the "log in" button
        Button button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract username
                EditText usernameEt = (EditText) findViewById(R.id.usernameText);
                String username = usernameEt.getText().toString();

                // Extract password
                EditText passwordEt = (EditText) findViewById(R.id.passwordText);
                String password = passwordEt.getText().toString();

                // TODO: try and log in to something!

                // Go to main menu activity
                // TODO: Make a new activity: Select menu.
                Intent intentSelect = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentSelect);

            }
        });

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(MainActivity.this, "Your orientation is portrait", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Your orientation is landscape", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
            case R.id.action_about:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                return true;
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

}
