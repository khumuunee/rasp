package org.ncd.rasp.repository;

import java.util.List;

import org.ncd.rasp.entity.ScheduledSound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledSoundRepo extends JpaRepository<ScheduledSound, String> {
	
	List<ScheduledSound> findByGroupId(String groupId);
	
	void deleteByGroupId(String groupId);
	
	List<ScheduledSound> findBySoundName(String soundName);	
}	
