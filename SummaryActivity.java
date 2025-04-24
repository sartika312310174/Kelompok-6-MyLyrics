package com.example.mylyrics;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mylyrics.HuggingFaceAPI;
import com.example.mylyrics.R;
import com.example.mylyrics.SummaryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SummaryActivity extends AppCompatActivity {

    EditText etLyrics;
    Button btnSummarize;
    TextView tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        etLyrics = findViewById(R.id.etLyrics);
        btnSummarize = findViewById(R.id.btnSummarize);
        tvSummary = findViewById(R.id.tvSummary);

        btnSummarize.setOnClickListener(v -> summarizeLyrics());
    }

    private void summarizeLyrics() {
        String text = etLyrics.getText().toString().trim();
        if (text.isEmpty()) {
            tvSummary.setText("Silakan masukkan lirik lagu terlebih dahulu.");
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-inference.huggingface.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HuggingFaceAPI api = retrofit.create(HuggingFaceAPI.class);
        HuggingFaceAPI.TextInput input = new HuggingFaceAPI.TextInput(text);

        Call<List<SummaryResponse.Summary>> call = api.getSummary(input);
        tvSummary.setText("Memproses ringkasan...");

        call.enqueue(new Callback<List<SummaryResponse.Summary>>() {
            @Override
            public void onResponse(Call<List<SummaryResponse.Summary>> call, Response<List<SummaryResponse.Summary>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    String summary = response.body().get(0).getSummary_text();
                    tvSummary.setText(summary);
                } else {
                    tvSummary.setText("Gagal mengambil ringkasan. Coba lagi nanti.");
                }
            }

            @Override
            public void onFailure(Call<List<SummaryResponse.Summary>> call, Throwable t) {
                tvSummary.setText("Terjadi kesalahan: " + t.getMessage());
            }
        });
    }
}
