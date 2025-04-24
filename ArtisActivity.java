package com.example.mylyrics;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class ArtisActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArtisAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artis);

        recyclerView = findViewById(R.id.recyclerViewArtis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            InputStream inputStream = getAssets().open("artis.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            List<ArtisModel> artisList = new Gson().fromJson(reader, new TypeToken<List<ArtisModel>>() {}.getType());

            adapter = new ArtisAdapter(this, artisList);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
