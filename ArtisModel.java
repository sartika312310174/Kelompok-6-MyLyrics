package com.example.mylyrics;

import java.util.ArrayList;

public class ArtisModel {
    private String nama;
    private String gambar;
    private ArrayList<String> laguList;

    public ArtisModel(String nama, String gambar, ArrayList<String> laguList) {
        this.nama = nama;
        this.gambar = gambar;
        this.laguList = laguList;
    }

    public String getNama() {
        return nama;
    }

    public String getGambar() {
        return gambar;
    }

    public ArrayList<String> getLaguList() {
        return laguList;
    }
}
