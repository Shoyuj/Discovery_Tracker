package com.escar.Discovery_Tracker;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ArtistController {

    private final SpotifyService spotifyService;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;

    public ArtistController(SpotifyService spotifyService, ArtistRepository artistRepository, SongRepository songRepository) {
        this.spotifyService = spotifyService;
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
    }

    @PostMapping("/artists/{artistName}")
    public Artist addArtist(@PathVariable String artistName) {
        return spotifyService.searchAndSaveArtist(artistName, artistRepository);
    }

    @PostMapping("/artists/{artistId}/songs")
    public List<Song> fetchSongs(@PathVariable Integer artistId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow();
        return spotifyService.fetchAndSaveSongs(artist.getSpotifyId(), artistId, songRepository);
    }

    @GetMapping("/artists/{artistId}/stats")
    public java.util.Map<String, Object> getArtistStats(@PathVariable Integer artistId) {
        List<Song> songs = songRepository.findByArtistId(String.valueOf(artistId));
        int total = songs.size();
        int listened = (int) songs.stream().filter(s -> s.getStatus().equals("LISTENED")).count();
        double percentage = total > 0 ? Math.round((listened * 100.0) / total * 100.0) / 100.0 : 0;

        return java.util.Map.of(
                "total", total,
                "listened", listened,
                "percentage", percentage
        );
    }
}