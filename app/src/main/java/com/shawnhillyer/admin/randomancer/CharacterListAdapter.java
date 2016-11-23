package com.shawnhillyer.admin.randomancer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CharacterListAdapter extends ArrayAdapter<Character> {

    private ArrayList<Character> characters;
    private Context mContext;

    public CharacterListAdapter(Context context, int resource, ArrayList<Character> objects) {
        super(context, resource, objects);
        // Set the private characters object to the list of characters passed in by constructor
        this.mContext = context;
        characters = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }

        // This grabs the character in the position required as we move through private list of chars
        Character character = characters.get(position);

        // Each pair grabs the id in the custom view passed in (convertView), then sets text content
        TextView nameText = (TextView) convertView.findViewById(R.id.fullName);
        nameText.setText(character.getFullName());

        TextView genderText = (TextView) convertView.findViewById(R.id.gender);
        genderText.setText(character.getGender());

        TextView raceText = (TextView) convertView.findViewById(R.id.race);
        raceText.setText(character.getRace());

        TextView skillsText = (TextView) convertView.findViewById(R.id.skills);
        skillsText.setText(character.getSkills());

        // Edit button
        final Button editButton = (Button) convertView.findViewById(R.id.editButton);
        editButton.setTag(character.getId());
        editButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Make the edit button navigate to an Edit Character activity instead of... this :)
                    String charId = (String) editButton.getTag();

                    Intent intent = new Intent(v.getContext(), EditCharacterActivity.class);
                    intent.putExtra("charId", charId);
                    v.getContext().startActivity(intent);
                }
        });

        // Delete button
        final Button deleteButton = (Button) convertView.findViewById(R.id.deleteButton);
        deleteButton.setTag(character.getId());
        deleteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // Grab the id from the tag on the delete button
                        String charId = (String) deleteButton.getTag();

                        // Send an HTTP DELETE request to url using Volley library
                        RequestQueue queue = Volley.newRequestQueue(getContext());

                        // Target string is: <baseurl>/charmaker/users/{username}/characters/{charId}
                        StringBuilder urlBuilder = new StringBuilder("http://52.26.146.27:8090/charmaker/users/");
                        urlBuilder.append(User.getInstance().getUsername());
                        urlBuilder.append("/characters/");
                        urlBuilder.append(charId);
                        String url = urlBuilder.toString();

                        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.DELETE, url, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        // Do something based on response
                                        Toast.makeText(v.getContext(), "Character Deleted", Toast.LENGTH_SHORT).show();

                                        // Not sure why but this never gets called; server response code is "200 OK" but goes to error response
                                        Intent intent = new Intent(v.getContext(), ViewCharactersActivity.class);
                                        v.getContext().startActivity(intent);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(v.getContext(), "Character Deleted", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(v.getContext(), ViewCharactersActivity.class);
                                        v.getContext().startActivity(intent);
                            }
                        });
                        queue.add(jsObjRequest);
                    }
                });

        return convertView;
    }
}
