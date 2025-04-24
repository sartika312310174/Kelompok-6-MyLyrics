package com.example.mylyrics;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FavoriteManager {
    private static final String PREFS_NAME = "favorites";
    private static final String FAVORITE_KEY = "favorite_songs";

    // Menambahkan lagu ke daftar favorit
    public static void addFavorite(Context context, String title, String artist, String lyrics) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String existingFavorites = prefs.getString(FAVORITE_KEY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(existingFavorites);
            JSONObject song = new JSONObject();
            song.put("title", title);
            song.put("artist", artist);
            song.put("lyrics", lyrics);
            jsonArray.put(song);
            editor.putString(FAVORITE_KEY, jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Mengambil daftar lagu favorit
    public static String getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(FAVORITE_KEY, "[]");
    }
}
