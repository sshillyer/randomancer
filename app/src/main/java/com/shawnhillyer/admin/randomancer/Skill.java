package com.shawnhillyer.admin.randomancer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 11/4/2016.
 */
public class Skill {
    private String id;
    private String skill;

    public Skill(String id, String skill) {
        this.id = id;
        this.skill = skill;
    }

    public Skill(JSONObject skillJson) {
        try {
            this.id = skillJson.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            this.skill = skillJson.getString("skill");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getSkill() {
        return skill;
    }
}
