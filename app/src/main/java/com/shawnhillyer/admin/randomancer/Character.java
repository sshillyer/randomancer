package com.shawnhillyer.admin.randomancer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Character {
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String race;
    private String skills;
    private ArrayList<Skill> skillObjs;

    public Character(String id, String firstName, String lastName, String gender, String race, String skills) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.race = race;
        this.skills = skills;
    }

    public Character(JSONObject character) throws JSONException {
        this.id = character.getString("_id");
        this.firstName = character.getString("firstName");
        this.lastName = character.getString("lastName");
        this.gender = character.getString("gender");
        this.race = character.getString("race");


        // Build skills into an array
        JSONArray skillArray = new JSONArray();
        try {
            skillArray = character.getJSONArray("skills");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setSkills(skillArray);
    }

    public void setSkills(JSONArray skills) throws JSONException {
        // Build skills into an array
        StringBuilder skillList = new StringBuilder(); // Empty array of strings

        int numSkills = skills.length();
        if (numSkills > 0) {
            for (int j = 0; j < numSkills; j++) {
                JSONObject skill = skills.getJSONObject(j);
                String skillName = skill.getString("skill");
                skillList.append(skillName);
                if (j != numSkills - 1)
                    skillList.append(", ");
            }
        } else {
            skillList.append("None");
        }

        this.skills = skillList.toString();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        StringBuilder fullname = new StringBuilder();
        fullname.append(getFirstName());
        if (lastName.length() > 0) {
            fullname.append(" ");
            fullname.append(getLastName());
        }

        return fullname.toString();
    }

    public String getGender() {
        return gender;
    }

    public String getRace() {
        return race;
    }

    public String getSkills() {
        return skills;
    }

    public ArrayList<Skill> getSkillObjs() {
        return this.skillObjs;
    }

}
