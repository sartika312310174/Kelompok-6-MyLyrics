package com.example.mylyrics;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LaguActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lagu);

        // Ambil data yang dikirim dari ArtisAdapter
        String namaArtis = getIntent().getStringExtra("nama");
        ArrayList<String> laguList = getIntent().getStringArrayListExtra("laguList");

        // Inisialisasi tampilan
        TextView tvNamaArtis = findViewById(R.id.tvNamaArtis);
        LinearLayout layoutLagu = findViewById(R.id.layoutLagu);

        // Set nama artis
        tvNamaArtis.setText("Lagu oleh " + namaArtis);

        // Tampilkan daftar lagu
        if (laguList != null) {
            for (String lagu : laguList) {
                TextView tv = new TextView(this);
                tv.setText("• " + lagu);
                tv.setTextSize(18);
                tv.setPadding(8, 12, 8, 12);
                layoutLagu.addView(tv);
            }
        }
    }
}
