package com.shawnhillyer.admin.randomancer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCharactersActivity extends AppCompatActivity {
    protected void buildScrollList(JSONArray res) throws JSONException {
//        final TextView text = (TextView) findViewById(R.id.textView);
//        text.setText("Response is: " + res.toString());

        // Cite: http://stackoverflow.com/questions/9440138/how-to-convert-jsonarray-to-listview
        ArrayList<String> items = new ArrayList<String>();

        for (int i = 0; i < res.length(); i++) {

            // Get each character object from the JSONArray
            JSONObject character = res.getJSONObject(i);

            // Get the name
            String firstName = character.getString("firstName");
            String lastName = character.getString("lastName");
            String gender = character.getString("gender");
            String race = character.getString("race");

            // Build strings into an array
            StringBuilder skillList = new StringBuilder(); // Empty array of strings
            JSONArray skills = character.getJSONArray("skills");
            int numSkills = skills.length();
            if (numSkills > 0) {
                for (int j = 0; j < numSkills; j++) {
                    JSONObject skill = skills.getJSONObject(j);
                    String skillName = skill.getString("skill");
                    skillList.append(skillName);
                    if (j != numSkills -1)
                        skillList.append(", ");
                }
            } else {
                skillList.append("None");
            }


            items.add(firstName + " " + lastName + "\n"
                    + "Gender: " + gender + "\n"
                    + "Race: " + race + "\n"
                    + "Skills: " + skillList);
        }

        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(mArrayAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_characters);

        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        String url = "http://52.26.146.27:8090/charmaker/characters";

//        final TextView text = (TextView) findViewById(R.id.textView);

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
//                        text.setText("Response is: " + response.toString());
                        try {
                            buildScrollList(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("That didn't work!");
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }
}


// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        text.setText("Response is: " + response.substring(0, 500));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                text.setText("That didn't work!");
//            }
//        });
//        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


//        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // Display the first 500 characters of the response string.
//                        text.setText("Response is: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        text.setText("That didn't work!");
//                    }
//        });
//        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);