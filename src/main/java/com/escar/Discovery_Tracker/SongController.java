package com.escar.Discovery_Tracker;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class SongController {

    public SongController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    record updateStatus(String status){};

    private SongRepository songRepository;


    @PutMapping("/api/v1/songs/{songId}/status")
    public void setStatus(@RequestBody updateStatus status, @PathVariable("songId") Integer id){

        Song song=songRepository.findById(id).orElseThrow();
        song.setStatus(status.status);
        songRepository.save(song);

    }

    @GetMapping("/api/v1/songs/{artistId}")
    public List<Song> getSongsByArtist(@PathVariable Integer artistId) {
        return songRepository.findByArtistId(String.valueOf(artistId));
    }
}
