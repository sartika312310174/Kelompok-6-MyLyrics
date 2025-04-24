package com.example.mylyrics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoriteActivity extends AppCompatActivity {
    private ListView ListView;
    private ArrayList<String> favoriteSongs;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ListView = findViewById(R.id.ListView);
        sharedPreferences = getSharedPreferences("MyLyricsPrefs", Context.MODE_PRIVATE);

        // Load data favorit
        loadFavorites();

        // Klik untuk melihat lirik
        ListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(FavoriteActivity.this, LyricsActivity.class);
            intent.putExtra("song_title", favoriteSongs.get(position));
            startActivity(intent);
        });

        // Klik lama untuk menghapus dari favorit
        ListView.setOnItemLongClickListener((parent, view, position, id) -> {
            removeFavorite(favoriteSongs.get(position));
            return true;
        });
    }

    private void loadFavorites() {
        Set<String> favoriteSet = sharedPreferences.getStringSet("favorites", new HashSet<>());
        favoriteSongs = new ArrayList<>(favoriteSet);

        // 🔹 Log daftar lagu favorit
        Log.d("FavoriteActivity", "Daftar favorit: " + favoriteSongs);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoriteSongs);
        ListView.setAdapter(adapter);
    }

    private void removeFavorite(String songTitle) {
        Set<String> favoriteSet = sharedPreferences.getStringSet("favorites", new HashSet<>());
        if (favoriteSet.contains(songTitle)) {
            favoriteSet.remove(songTitle);
            sharedPreferences.edit().putStringSet("favorites", favoriteSet).apply();
            Log.d("FavoriteActivity", "Lagu dihapus: " + songTitle); // 🔹 Log saat lagu dihapus
            loadFavorites();
        }
    }
}
