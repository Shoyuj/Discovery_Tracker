package com.escar.Discovery_Tracker;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Song {

    @Id
    @SequenceGenerator(
            sequenceName = "song_id",
            name = "song_id"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "song_id"
    )

    private Integer id;
    private String title;
    private String albumName;
    private String artistId;
    private String status;

    Song(){}

    public Song(Integer id, String title, String albumName, String artistId, String status) {
        this.id = id;
        this.title = title;
        this.albumName = albumName;
        this.artistId = artistId;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", albumName='" + albumName + '\'' +
                ", artistId='" + artistId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id) && Objects.equals(title, song.title) && Objects.equals(albumName, song.albumName) && Objects.equals(artistId, song.artistId) && Objects.equals(status, song.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, albumName, artistId, status);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
