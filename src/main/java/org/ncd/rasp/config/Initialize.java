package org.ncd.rasp.config;

import org.ncd.rasp.service.SchedulerService;
import org.ncd.rasp.service.SoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class Initialize implements ApplicationListener<ApplicationStartedEvent> {

	@Autowired
	private SchedulerService schedulerService;

	@Autowired
	SoundService soundService;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		boolean scheduledSoundExists = schedulerService.configureAllScheduledSound();
		if (!scheduledSoundExists)
			schedulerService.configureStartPlayer();
		schedulerService.configureStopPlayer();
		try {
			soundService.startPlaylistWithCheckTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
