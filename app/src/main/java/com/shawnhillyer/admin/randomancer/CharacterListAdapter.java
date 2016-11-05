package com.shawnhillyer.admin.randomancer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CharacterListAdapter extends ArrayAdapter<Character> {

    private ArrayList<Character> characters;

    public CharacterListAdapter(Context context, int resource, ArrayList<Character> objects) {
        super(context, resource, objects);
        characters = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }

        Character character = characters.get(position);

        TextView nameText = (TextView) convertView.findViewById(R.id.fullName);
        nameText.setText(character.getFullName());
        TextView genderText = (TextView) convertView.findViewById(R.id.gender);
        genderText.setText(character.getGender());
        TextView raceText = (TextView) convertView.findViewById(R.id.race);
        raceText.setText(character.getRace());
        TextView skillsText = (TextView) convertView.findViewById(R.id.skills);
        skillsText.setText(character.getSkills());

        return convertView;
    }
}
