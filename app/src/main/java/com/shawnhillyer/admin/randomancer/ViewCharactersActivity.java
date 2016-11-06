package com.shawnhillyer.admin.randomancer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_characters);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Send an HTTP GET rerquest to url using Volley library
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        String url = "http://52.26.146.27:8090/charmaker/characters";

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            buildCharacterList(response);
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


    protected void buildCharacterList(JSONArray res) throws JSONException {
        // Cite: http://stackoverflow.com/questions/9440138/how-to-convert-jsonarray-to-listview
        // Empty arraylist of characters we can pass around later
        ArrayList<Character> characters = new ArrayList<Character>();

        // Iterate through the response, building Character objects from the JSON passed in
        for (int i = 0; i < res.length(); i++) {
            // Get each character object from the JSONArray
            JSONObject characterJson = res.getJSONObject(i);
            Character character = new Character(characterJson);
            characters.add(character);
        }

        // Pass in our customer layout to our custom list adapter class constructor
        CharacterListAdapter adapter = new CharacterListAdapter(
                this, R.layout.list_item, characters);
        // Adapter is initialized, now find the listView and tell it to use this adapter
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
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