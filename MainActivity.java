package com.example.mylyrics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Buka halaman pencarian lirik
    public void openSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    // Buka halaman lagu favorit
    public void openFavorites(View view) {
        startActivity(new Intent(this, FavoriteActivity.class));
    }

    // Buka halaman daftar artis
    public void openArtis(View view) {
        startActivity(new Intent(this, ArtisActivity.class));
    }

    // Buka halaman chord
    public void openChord(View view) {
        startActivity(new Intent(this, ChordItem.class));
    }

    public void openSummary(View view) {
        startActivity(new Intent(this, SummaryActivity.class));
    }

}
