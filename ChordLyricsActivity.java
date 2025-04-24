package com.example.mylyrics;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class ChordLyricsActivity extends AppCompatActivity {

    private TextView chordSongTitle, chordSongLyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_lyrics);

        chordSongTitle = findViewById(R.id.chordSongTitle);
        chordSongLyrics = findViewById(R.id.chordSongLyrics);

        String title = getIntent().getStringExtra("song_title");

        if (title != null && !title.isEmpty()) {
            loadChordFromJSON(title);
        } else {
            Toast.makeText(this, "Judul lagu tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadChordFromJSON(String title) {
        String jsonString = loadJSONFromAsset("chord.json");
        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                boolean found = false;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject song = jsonArray.getJSONObject(i);
                    if (song.getString("title").equalsIgnoreCase(title)) {
                        String tuning = song.optString("tuning", "");
                        String key = song.optString("key", "");
                        String capo = song.optString("capo", "");
                        String chords = song.optString("chords", "").replace("\\n", "\n");

                        StringBuilder chordText = new StringBuilder();
                        if (!tuning.isEmpty()) chordText.append("Tuning: ").append(tuning).append("\n");
                        if (!key.isEmpty()) chordText.append("Key: ").append(key).append("\n");
                        if (!capo.isEmpty()) chordText.append("Capo: ").append(capo).append("\n\n");
                        chordText.append(chords);

                        chordSongTitle.setText(title);
                        chordSongLyrics.setText(chordText.toString());

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    chordSongLyrics.setText("Chord tidak ditemukan.");
                }

            } catch (JSONException e) {
                chordSongLyrics.setText("Gagal memuat chord.");
            }
        } else {
            chordSongLyrics.setText("File chord.json tidak ditemukan.");
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
}
