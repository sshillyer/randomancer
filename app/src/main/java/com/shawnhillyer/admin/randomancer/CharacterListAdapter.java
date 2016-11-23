package com.shawnhillyer.admin.randomancer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CharacterListAdapter extends ArrayAdapter<Character> {

    private ArrayList<Character> characters;

    public CharacterListAdapter(Context context, int resource, ArrayList<Character> objects) {
        super(context, resource, objects);
        // Set the private characters object to the list of characters passed in by constructor
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
                    editButton.setText((CharSequence) editButton.getTag());
                }
        });

        // Delete button
        final Button deleteButton = (Button) convertView.findViewById(R.id.deleteButton);
        deleteButton.setTag(character.getId());
        deleteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Make the edit button navigate to an Edit Character activity instead of... this :)
                        deleteButton.setText((CharSequence) deleteButton.getTag());
                        
                    }
                });

        return convertView;
    }
}
