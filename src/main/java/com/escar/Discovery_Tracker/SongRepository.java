package com.escar.Discovery_Tracker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song,Integer> {

    List<Song> findByArtistId(String artistId);
}
