package org.ncd.rasp.repository;

import java.util.List;

import org.ncd.rasp.entity.PlaylistSound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistSoundRepo extends JpaRepository<PlaylistSound, String> {
	
	List<PlaylistSound> findByPlaylistId(String playlistId);
	
	void deleteByPlaylistId(String playlistId);
	
	List<PlaylistSound> findBySoundName(String soundName);
}	
