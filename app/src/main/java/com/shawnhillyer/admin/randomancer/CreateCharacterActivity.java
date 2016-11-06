package com.shawnhillyer.admin.randomancer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CreateCharacterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_character);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up gender spinner (CITE: https://developer.android.com/guide/topics/ui/controls/spinner.html
        Spinner genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.genders, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Set up race spinner
        Spinner raceSpinner = (Spinner) findViewById(R.id.raceSpinner);
        ArrayAdapter<CharSequence> raceAdapter = ArrayAdapter.createFromResource(this,
                R.array.races, android.R.layout.simple_spinner_item);
        raceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        raceSpinner.setAdapter(raceAdapter);

        // Call method that sends HTTP GET request to API /skills and builds checkboxes for skills
        buildSkillCheckboxes();

        // Setup event listener on the create character button to collect the data and send POST
        Button createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Validate input
                sendCharacterPostRequest();
                // TODO: Make a success toast and/or navigate to another page

            }
        });

        // Setup event listener on the randomize button to randomize the fields selected


    }

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

        switch (id) {
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


    private boolean sendCharacterPostRequest() {
        Toast clickCreateToast = Toast.makeText(getApplicationContext(), "You clicked create!", Toast.LENGTH_LONG);
        clickCreateToast.show();

        // Send an HTTP POST request to url using Volley library

        String url = "http://52.26.146.27:8090/charmaker/characters";

        // Read all the data from the "form"
        // Cite: http://stackoverflow.com/questions/33573803/how-to-send-a-post-request-using-volley-with-string-body
        try {
            RequestQueue requestQueue = MySingleton.getInstance(this.getApplicationContext()).
                    getRequestQueue();
            String URL = "http://52.26.146.27:8090/charmaker/characters";
            JSONObject jsonBody = new JSONObject();

            EditText firstNameEt = (EditText) findViewById(R.id.firstNameText);
            String firstName = firstNameEt.getText().toString();
            jsonBody.put("firstName", firstName);

            EditText lastNameEt = (EditText) findViewById(R.id.lastNameText);
            String lastName = lastNameEt.getText().toString();
            jsonBody.put("lastName", lastName);

            Spinner genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
            String gender = genderSpinner.getSelectedItem().toString();
            jsonBody.put("gender", gender);

            Spinner raceSpinner = (Spinner) findViewById(R.id.raceSpinner);
            String race = raceSpinner.getSelectedItem().toString();
            jsonBody.put("race", race);

            // TODO: Get the skills that are checked by calling getTag on each checkbox?


            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }


    private boolean buildSkillCheckboxes() {
        // Send an HTTP GET rerquest to url using Volley library
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        String url = "http://52.26.146.27:8090/charmaker/skills";

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Skill> skills = new ArrayList<Skill>();

                        // Iterate through the response, building Skill objects from the JSON received
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject skillJson;
                            try {
                                skillJson = response.getJSONObject(i);
                                Skill skill = new Skill(skillJson);
                                skills.add(skill);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        final LinearLayout ll = (LinearLayout) findViewById(R.id.skillLinearLayout);

                        // if we got some skills back, make some checkboxes
                        if (!skills.isEmpty()) {
                            // Debug Toast
                            Toast toast = Toast.makeText(getApplicationContext(), "We got some skills from server! Woohoo", Toast.LENGTH_SHORT);
                            toast.show();

                            // Go through the skills we created and make checkboxes
                            for (int i = 0; i < skills.size(); i++) {
                                CheckBox cb = new CheckBox(getApplicationContext());
                                Skill skill = skills.get(i);
                                cb.setText(skill.getSkill());
                                cb.setTextColor(getResources().getColor(R.color.colorTextSecondary));
                                cb.setTag(skill.getId());
                                ll.addView(cb);
                            } // End for loop that makes checkboxes
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Send the request
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

        return true;
    } // end buildSkillCheckboxes
}
