package org.ncd.rasp.repository;

import java.util.List;

import org.ncd.rasp.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepo extends JpaRepository<Playlist, String> {
	
	void deleteByName(String name);
	
	List<Playlist> findByIdIn(List<String> ids);
	

}	
