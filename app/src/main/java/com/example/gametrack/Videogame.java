package com.example.gametrack;

public class Videogame {
    private long id;
    private String title;
    private String genre;
    private String platform;
    private String developer;
    private int releaseYear;
    private boolean owned;

    public Videogame(long id, String title, String genre, String platform, String developer, int releaseYear, boolean owned) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.developer = developer;
        this.releaseYear = releaseYear;
        this.owned = owned;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    @Override
    public String toString() {
        return "Videogame{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", platform='" + platform + '\'' +
                ", developer='" + developer + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }
}
