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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract username
                EditText usernameEt = (EditText) findViewById(R.id.usernameText);
                String username = usernameEt.getText().toString();

                // Extract password
                EditText passwordEt = (EditText) findViewById(R.id.passwordText);
                String password = passwordEt.getText().toString();

                sendUserLoginPostRequest(username, password);
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

        // Create Account button
        Button createAccountButton = (Button) findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to create user page
                Intent intentSelect = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intentSelect);
            }
        });

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

    // Send the username and password to server and attempt to log in
    private boolean sendUserLoginPostRequest(final String username, String password) {

        // Trying pattern here this time: http://arnab.ch/blog/2013/08/asynchronous-http-requests-in-android-using-volley/
        String url = "http://52.26.146.27:8090/charmaker/users/login";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Toast.makeText(MainActivity.this, "Logging in to " + User.getInstance().getUsername(), Toast.LENGTH_SHORT).show();

                            // Set the username
                            User.getInstance().setUsername(username);

                            // Go to the menu activity
                            Intent intentSelect = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intentSelect);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(MainActivity.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(req);

        return true;
    }
}
