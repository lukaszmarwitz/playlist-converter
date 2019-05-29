package com.wooky.core;

public class Video {

    private int id;
    private String artist;
    private String title;
    private String token;

    Video(int id, String artist, String title, String token) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getToken() {
        return token;
    }
}
