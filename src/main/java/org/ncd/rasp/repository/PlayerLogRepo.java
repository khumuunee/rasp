package org.ncd.rasp.repository;

import java.util.Date;
import java.util.List;

import org.ncd.rasp.entity.PlayerLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerLogRepo extends JpaRepository<PlayerLog, String> {
	
	List<PlayerLog> findAllByTimeBetweenOrderByTime(Date timeStart, Date timeEnd);
		
}	
