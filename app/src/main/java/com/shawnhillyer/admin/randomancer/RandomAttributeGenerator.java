package com.shawnhillyer.admin.randomancer;

import android.content.Context;

import java.util.Random;

/**
 * Created by Admin on 11/6/2016.
 */

abstract public class RandomAttributeGenerator {

    // Reference: http://stackoverflow.com/questions/11600001/how-to-get-a-random-value-from-a-string-array-in-android
    public static String getRandomName(Context applicationContext) {
        // Read the string-array in the names.xml resource file
        String[] array = applicationContext.getResources().getStringArray(R.array.names);
        // generate a random index and assign string at that index to randomName
        String randomName = array[new Random().nextInt(array.length)];

        return randomName;
    }

    public static String getRandomGender(Context applicationContext) {
        String[] array = applicationContext.getResources().getStringArray(R.array.genders);
        String randomGender = array[new Random().nextInt(array.length)];

        return randomGender;
    }

    public static String getRandomRace(Context applicationContext) {
        String[] array = applicationContext.getResources().getStringArray(R.array.races);
        String randomRace = array[new Random().nextInt(array.length)];

        return randomRace;
    }
}
