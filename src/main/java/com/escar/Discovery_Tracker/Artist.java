package com.escar.Discovery_Tracker;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Artist {
    @Id

    @SequenceGenerator(
            sequenceName = "Artist_id",
            name = "Artist_id"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Artist_id"
    )

    private Integer id;
    private String name;
    private String spotifyId;;
    private String imageUrl;

    Artist(){}

    public Artist(Integer id, String name, String spotifyId, String imageUrl) {
        this.id = id;
        this.name = name;
        this.spotifyId = spotifyId;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id) && Objects.equals(name, artist.name) && Objects.equals(spotifyId, artist.spotifyId) && Objects.equals(imageUrl, artist.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, spotifyId, imageUrl);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", spotifyId=" + spotifyId +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
