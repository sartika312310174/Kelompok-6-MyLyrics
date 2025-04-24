package com.example.mylyrics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class LyricsActivity extends AppCompatActivity {
    private TextView titleTextView, artistTextView, lyricsTextView;
    private String songTitle, songLyrics, artistName, chordLyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        titleTextView = findViewById(R.id.titleTextView);
        artistTextView = findViewById(R.id.artistTextView);
        lyricsTextView = findViewById(R.id.lyricsTextView);
        Button shareButton = findViewById(R.id.shareButton);
        Button favoriteButton = findViewById(R.id.favoriteButton);
        Button openSpotifyButton = findViewById(R.id.openSpotifyButton);
        Button openYoutubeButton = findViewById(R.id.openYoutubeButton);


        Intent intent = getIntent();
        songTitle = intent.getStringExtra("song_title");

        if (songTitle == null || songTitle.isEmpty()) {
            Toast.makeText(this, "Judul lagu tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadLyricsFromJSON(songTitle);

        shareButton.setOnClickListener(v -> {
            if (songLyrics != null && !songLyrics.isEmpty()) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Lirik Lagu: " + songTitle);
                shareIntent.putExtra(Intent.EXTRA_TEXT, songLyrics);
                startActivity(Intent.createChooser(shareIntent, "Bagikan Lirik via"));
            } else {
                Toast.makeText(this, "Lirik tidak tersedia untuk dibagikan.", Toast.LENGTH_SHORT).show();
            }
        });

        favoriteButton.setOnClickListener(this::addToFavorites);

        openSpotifyButton.setOnClickListener(v -> openSpotify(songTitle, artistName));

        openYoutubeButton.setOnClickListener(v -> openYoutube(songTitle, artistName));

    }

    private void loadLyricsFromJSON(String title) {
        String jsonString = loadJSONFromAsset("lyrics.json");
        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                boolean found = false;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject song = jsonArray.getJSONObject(i);
                    if (song.getString("title").equalsIgnoreCase(title)) {
                        songLyrics = song.getString("lyrics").replace("\\n", "\n");
                        artistName = song.getString("artist");
                        chordLyrics = song.optString("lyrics_chords", songLyrics).replace("\\n", "\n");

                        titleTextView.setText(song.getString("title"));
                        artistTextView.setText("Artis: " + artistName);
                        lyricsTextView.setText(songLyrics);

                        found = true;
                        break;
                    }
                }
                if (!found) {
                    lyricsTextView.setText("Lirik lagu tidak ditemukan.");
                }
            } catch (JSONException e) {
                lyricsTextView.setText("Gagal memuat lirik.");
                Log.e("LyricsActivity", "JSON Parsing Error: " + e.getMessage());
            }
        } else {
            lyricsTextView.setText("File JSON tidak ditemukan.");
        }
    }

    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public void addToFavorites(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyLyricsPrefs", Context.MODE_PRIVATE);
        Set<String> favoriteSet = new HashSet<>(sharedPreferences.getStringSet("favorites", new HashSet<>()));

        if (!favoriteSet.contains(songTitle)) {
            favoriteSet.add(songTitle);
            sharedPreferences.edit().putStringSet("favorites", favoriteSet).apply();
            Toast.makeText(this, "Lirik lagu '" + songTitle + "' ditambahkan ke favorit!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lirik lagu sudah ada di favorit.", Toast.LENGTH_SHORT).show();
        }

        Log.d("LyricsActivity", "Lirik lagu ditambahkan ke favorit: " + songTitle);
    }

    private void openSpotify(String songTitle, String artistName) {
        try {
            String searchQuery = Uri.encode(artistName + " " + songTitle);
            Uri spotifyWebUri = Uri.parse("https://open.spotify.com/search/" + searchQuery);
            Intent spotifyIntent = new Intent(Intent.ACTION_VIEW, spotifyWebUri);
            startActivity(spotifyIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Gagal membuka Spotify", Toast.LENGTH_SHORT).show();
        }
    }

    private void openYoutube(String songTitle, String artistName) {
        try {
            String searchQuery = Uri.encode(artistName + " " + songTitle);
            Uri youtubeUri = Uri.parse("https://www.youtube.com/results?search_query=" + searchQuery);
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, youtubeUri);
            startActivity(youtubeIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Gagal membuka YouTube", Toast.LENGTH_SHORT).show();
        }
    }
}
