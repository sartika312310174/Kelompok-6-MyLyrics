package com.example.mylyrics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ArtisAdapter extends RecyclerView.Adapter<ArtisAdapter.ViewHolder> {

    private Context context;
    private List<ArtisModel> artisList;
    private List<ArtisModel> artisListFull;

    public ArtisAdapter(Context context, List<ArtisModel> artisList) {
        this.context = context;
        this.artisList = artisList;
        this.artisListFull = new ArrayList<>(artisList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageArtis;
        TextView namaArtis;

        public ViewHolder(View view) {
            super(view);
            imageArtis = view.findViewById(R.id.imageArtis);
            namaArtis = view.findViewById(R.id.namaArtis);
        }
    }

    @Override
    public ArtisAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artis, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArtisAdapter.ViewHolder holder, int position) {
        ArtisModel artis = artisList.get(position);
        holder.namaArtis.setText(artis.getNama());

        int imageRes = context.getResources().getIdentifier(artis.getGambar(), "drawable", context.getPackageName());
        holder.imageArtis.setImageResource(imageRes);

        // Set onClickListener untuk item artis
        holder.itemView.setOnClickListener(v -> {
            // Mengambil data dari artis yang diklik
            String namaArtis = artis.getNama();
            ArrayList<String> laguList = artis.getLaguList(); // Pastikan ArtisModel memiliki daftar lagu

            // Mengirim data ke LaguActivity
            Intent intent = new Intent(context, LaguActivity.class);
            intent.putExtra("nama", namaArtis);  // Kirim nama artis
            intent.putStringArrayListExtra("laguList", laguList); // Kirim daftar lagu
            context.startActivity(intent); // Mulai LaguActivity
        });
    }

    @Override
    public int getItemCount() {
        return artisList.size();
    }

    public void filter(String keyword) {
        artisList.clear();
        if (keyword.isEmpty()) {
            artisList.addAll(artisListFull);
        } else {
            for (ArtisModel artis : artisListFull) {
                if (artis.getNama().toLowerCase().contains(keyword.toLowerCase())) {
                    artisList.add(artis);
                }
            }
        }
        notifyDataSetChanged();
    }
}
