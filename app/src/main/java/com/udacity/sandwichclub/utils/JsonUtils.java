package com.udacity.sandwichclub.utils;


import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private static String retrieveRequiredString(JSONObject jsonObject, String name) {
        String value;
        try {
            value = jsonObject.getString(name);
        }
        catch (JSONException e)
        {
            Log.e(TAG, String.format("No %s specified in: '%s'", name, jsonObject));
            return null;
        }
        return value;
    }

    private static ArrayList<String> retrieveOptionalList(JSONObject jsonObject, String name) {
        ArrayList<String> values = new ArrayList<String>();
        JSONArray jsonValues;
        jsonValues = jsonObject.optJSONArray(name);
        try {
            for(int i=0;i< jsonValues.length();i++ ) {
                values.add(jsonValues.getString(i));
            }
        }
        catch (JSONException e)
        {
            return null;
        }
        return values;
    }

    public static Sandwich parseSandwichJson(String json) {

        JSONObject JSONSandwich;
        Sandwich sandwich = new Sandwich();

        // Attempt to parse a Sandwich out of JSON
        try {
             JSONSandwich = new JSONObject(json);
        }
        catch (JSONException e)
        {
            Log.e(TAG, String.format("Cannot parse JSON from %s", json));
            return null;
        }
        // Attempt to retrieve the name
        JSONObject name;
        try {
            name = JSONSandwich.getJSONObject("name");
        }
        catch (JSONException e)
        {
            Log.e(TAG, String.format("No name specified in: '%s'", JSONSandwich));
            return null;
        }
        return new Sandwich(
                retrieveRequiredString(name, "mainName"),
                retrieveOptionalList(name, "alsoKnownAs"),
                retrieveRequiredString(JSONSandwich, "placeOfOrigin"),
                retrieveRequiredString(JSONSandwich, "description"),
                retrieveRequiredString(JSONSandwich, "image"),
                retrieveOptionalList(JSONSandwich, "ingredients"));


    }
}
