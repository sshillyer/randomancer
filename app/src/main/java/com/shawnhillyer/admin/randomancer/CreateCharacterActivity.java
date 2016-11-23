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
import com.github.tbouron.shakedetector.library.ShakeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

public class CreateCharacterActivity extends AppCompatActivity {

    private ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();

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
                // Validate input before sending the POST request
                if (inputIsValid()) {
                    sendCharacterPostRequest();
                    Toast successToast = Toast.makeText(getApplicationContext(), "Character Created", Toast.LENGTH_LONG);
                    successToast.show();
                    Intent intentSelect = new Intent(CreateCharacterActivity.this, ViewCharactersActivity.class);
                    startActivity(intentSelect);
                }
            }
        });

        // Setup event listener on the randomize button to randomize the fields selected
        Button randomButton = (Button) findViewById(R.id.randomButton);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomizeForm();
            }
        });

        // Event listener using ShakeDetector (https://github.com/tbouron/ShakeDetector)
        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake() {
                Toast.makeText(getApplicationContext(), "Device shaken!", Toast.LENGTH_SHORT).show();
                randomizeForm();
            }
        });

    }

    private boolean inputIsValid() {
        EditText firstNameEt = (EditText) findViewById(R.id.firstNameText);
        String firstName = firstNameEt.getText().toString();
        if (firstName.length() < 2) {
            Toast fNameShortToast = Toast.makeText(getApplicationContext(), R.string.first_name_min_length, Toast.LENGTH_LONG);
            fNameShortToast.show();
            return false;
        }
        return true;
    }

    private boolean randomizeForm() {
        Toast randomToast = Toast.makeText(getApplicationContext(), "Randomancer is Randomizing!", Toast.LENGTH_SHORT);
        randomToast.show();

        // randomize the names
        String randomFirstName = RandomAttributeGenerator.getRandomName(getApplicationContext());
        EditText firstNameEt = (EditText) findViewById(R.id.firstNameText);
        firstNameEt.setText(randomFirstName);

        String randomLastName = RandomAttributeGenerator.getRandomName(getApplicationContext());
        EditText lastNameEt = (EditText) findViewById(R.id.lastNameText);
        lastNameEt.setText(randomLastName);

        // Randomize the gender and race spinners
        String randomGender = RandomAttributeGenerator.getRandomGender(getApplicationContext());
        Spinner genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        genderSpinner.setSelection(((ArrayAdapter) genderSpinner.getAdapter()).getPosition(randomGender));

        String randomRace = RandomAttributeGenerator.getRandomRace(getApplicationContext());
        Spinner raceSpinner = (Spinner) findViewById(R.id.raceSpinner);
        raceSpinner.setSelection(((ArrayAdapter) raceSpinner.getAdapter()).getPosition(randomRace));

        // Randomly toggle the skills
        for (int i = 0; i < checkBoxes.size(); i++) {
            int denom = new Random().nextInt(4); // Should check about 1/4 of the values
            Boolean isChecked = false;
            if (denom == 1) {
                isChecked = true;
            }
            checkBoxes.get(i).setChecked(isChecked);

        }

        return true;
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
        // Read all the data from the "form"
        // Cite: http://stackoverflow.com/questions/33573803/how-to-send-a-post-request-using-volley-with-string-body
        try {
            RequestQueue requestQueue = MySingleton.getInstance(this.getApplicationContext()).
                    getRequestQueue();
            StringBuilder urlBuilder = new StringBuilder("http://52.26.146.27:8090/charmaker/users/");
            urlBuilder.append(User.getInstance().getUsername());
            urlBuilder.append("/characters");
            String url = urlBuilder.toString();

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

            // Build an ArrayList of the skill id's (hidden with setTag), put into jsonBody if checked
            ArrayList<String> skillIdList = new ArrayList<String>();
            for (int i = 0; i < checkBoxes.size(); i++) {
                CheckBox cb = checkBoxes.get(i);
                if (cb.isChecked()) {
                    skillIdList.add((String) cb.getTag());
                }
            }
            jsonBody.put("skills", new JSONArray(skillIdList));

            // Convert the JSONObject into a string
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                                checkBoxes.add(cb);
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


    @Override
    protected void onResume() {
        super.onResume();
        ShakeDetector.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShakeDetector.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShakeDetector.destroy();
    }
}
