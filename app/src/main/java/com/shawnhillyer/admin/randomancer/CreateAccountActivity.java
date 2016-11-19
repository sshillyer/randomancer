package com.shawnhillyer.admin.randomancer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {

    static final int MIN_USERNAME_LENGTH = 5;
    static final int MIN_PASSWORD_LENGTH = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void createClickHandler(View view) {
        // Get the username and password text
        EditText usernameEt = (EditText) findViewById(R.id.userNameText);
        String userName = usernameEt.getText().toString();

        EditText passwordEt = (EditText) findViewById(R.id.passwordText);
        String password = passwordEt.getText().toString();

        // If Either username or password is not long enough, subroutine will toast and return false
        if (    !isValidLength(userName, "Username", MIN_USERNAME_LENGTH) ||
                !isValidLength(password, "Password", MIN_PASSWORD_LENGTH)) {
            return;
        }

        // Attempt to send the POST. If username already taken, status code will be 400; 201 if created
        sendUserPostRequest(userName, password);

    }

    private boolean isValidLength(String str, String label, int minLength) {
        boolean isValidLength = (str.length() >= minLength);
        if (!isValidLength) {
            Toast.makeText(this, label + " must be " + MIN_PASSWORD_LENGTH + " characters long.", Toast.LENGTH_LONG).show();
        }
        return isValidLength;
    }

    private boolean sendUserPostRequest(String username, String password) {

        // Trying pattern here this time: http://arnab.ch/blog/2013/08/asynchronous-http-requests-in-android-using-volley/
        String url = "http://52.26.146.27:8090/charmaker/users";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Toast.makeText(CreateAccountActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            Intent intentSelect = new Intent(CreateAccountActivity.this, MainActivity.class);
                            startActivity(intentSelect);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(CreateAccountActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(req);

        return true;
    }

}
