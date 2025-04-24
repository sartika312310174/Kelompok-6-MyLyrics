package com.example.mylyrics;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class ChordItem extends AppCompatActivity {
    private ListView ListView;
    private EditText SearchEditText;
    private ImageButton VoiceSearchButton;
    private ArrayList<String> songTitles;
    private ArrayAdapter<String> adapter;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private static final int REQUEST_PERMISSION_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Inisialisasi UI
        ListView = findViewById(R.id.ListView);
        SearchEditText = findViewById(R.id.SearchEditText);
        VoiceSearchButton = findViewById(R.id.btnVoiceSearch);

        songTitles = new ArrayList<>();

        // Load JSON
        String jsonString = loadJSONFromAsset();
        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject song = jsonArray.getJSONObject(i);
                    songTitles.add(song.getString("title"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Set ke ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songTitles);
        ListView.setAdapter(adapter);

        // Klik lagu untuk melihat lirik
        ListView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedSong = adapter.getItem(position);
            openLyricsActivity(selectedSong);
        });

        // Cek izin mikrofon
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_RECORD_AUDIO);
        }

        // Tombol pencarian suara
        VoiceSearchButton.setOnClickListener(v -> startVoiceRecognition());

        // Tambahkan pencarian teks
        SearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ucapkan nama lagu...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Perangkat tidak mendukung pencarian suara", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty()) {
                String recognizedText = matches.get(0);
                SearchEditText.setText(recognizedText);
            }
        }
    }

    private void openLyricsActivity(String songTitle) {
        Intent intent = new Intent(ChordItem.this, ChordLyricsActivity.class);
        intent.putExtra("song_title", songTitle);
        startActivity(intent);
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("chord.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin mikrofon diberikan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Izin mikrofon diperlukan untuk pencarian suara", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
