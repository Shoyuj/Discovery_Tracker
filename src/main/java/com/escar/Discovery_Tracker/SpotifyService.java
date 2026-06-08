package com.escar.Discovery_Tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.Base64;
import java.util.List;

@Service
public class SpotifyService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() {
        String credentials = clientId + ":" + clientSecret;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encoded);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<java.util.Map> response = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token",
                request,
                java.util.Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    public Artist searchAndSaveArtist(String artistName, ArtistRepository artistRepository) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = "https://api.spotify.com/v1/search?q=" + artistName + "&type=artist&limit=1";

        ResponseEntity<com.fasterxml.jackson.databind.JsonNode> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                com.fasterxml.jackson.databind.JsonNode.class
        );

        com.fasterxml.jackson.databind.JsonNode item = response.getBody()
                .get("artists")
                .get("items")
                .get(0);

        Artist artist = new Artist();
        artist.setName(item.get("name").asText());
        artist.setSpotifyId(item.get("id").asText());
        artist.setImageUrl(item.get("images").get(0).get("url").asText());

        return artistRepository.save(artist);
    }

    public List<Song> fetchAndSaveSongs(String spotifyArtistId, Integer artistId, SongRepository songRepository) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = "https://api.spotify.com/v1/artists/" + spotifyArtistId + "/albums?include_groups=album,single";

        ResponseEntity<com.fasterxml.jackson.databind.JsonNode> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                com.fasterxml.jackson.databind.JsonNode.class
        );

        List<Song> savedSongs = new java.util.ArrayList<>();
        com.fasterxml.jackson.databind.JsonNode albums = response.getBody().get("items");

        for (com.fasterxml.jackson.databind.JsonNode album : albums) {
            String albumId = album.get("id").asText();
            String albumName = album.get("name").asText();
            savedSongs.addAll(fetchSongsFromAlbum(albumId, albumName, artistId, songRepository, token));
        }

        return savedSongs;
    }

    private List<Song> fetchSongsFromAlbum(String albumId, String albumName, Integer artistId, SongRepository songRepository, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = "https://api.spotify.com/v1/albums/" + albumId + "/tracks?limit=20";

        ResponseEntity<com.fasterxml.jackson.databind.JsonNode> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                com.fasterxml.jackson.databind.JsonNode.class
        );

        List<Song> songs = new java.util.ArrayList<>();
        com.fasterxml.jackson.databind.JsonNode tracks = response.getBody().get("items");

        for (com.fasterxml.jackson.databind.JsonNode track : tracks) {
            Song song = new Song();
            song.setTitle(track.get("name").asText());
            song.setAlbumName(albumName);
            song.setArtistId(String.valueOf(artistId));
            song.setStatus("UNHEARD");
            songs.add(songRepository.save(song));
        }

        return songs;
    }
}