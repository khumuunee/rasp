package org.ncd.rasp.service;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import org.ncd.rasp.config.SoundPlayer;
import org.ncd.rasp.entity.ScheduledSound;
import org.ncd.rasp.repository.ScheduledSoundRepo;
import org.ncd.rasp.tools.BaseTool;
import org.ncd.rasp.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javafx.scene.media.MediaPlayer.Status;

@Service
public class SchedulerService {

	@Autowired
	private TaskScheduler scheduler;

	@Autowired
	private ScheduledSoundRepo scheduledSoundRepo;

	@Autowired
	private SoundPlayer soundPlayer;

	@Autowired
	private SoundService soundService;

	private void addTaskToScheduler(String id, Runnable task, CronTrigger cronTrigger) {
		ScheduledFuture<?> schedule = scheduler.schedule(task, cronTrigger);
		Constants.jobsMap.put(id, schedule);
	}

	private void removeTaskFromScheduler(String id) {
		ScheduledFuture<?> scheduledTask = Constants.jobsMap.get(id);
		if (scheduledTask != null) {
			scheduledTask.cancel(true);
			Constants.jobsMap.put(id, null);
		}
	}

	public void removeAllTaskFromScheduler() {
		if (BaseTool.khoosonMapEsekh(Constants.jobsMap))
			return;
		for (String id : Constants.jobsMap.keySet()) {
			removeTaskFromScheduler(id);
		}
	}

	public boolean configureAllScheduledSound() {		
		List<ScheduledSound> listSound = scheduledSoundRepo.findAll();
		if (BaseTool.khoosonJagsaaltEsekh(listSound))
			return false;
		System.out.println("configureAllScheduledSound");
		List<LocalTime> listStartTime = listSound.stream().collect(Collectors.groupingBy(ScheduledSound::getStartTime))
				.keySet().stream().collect(Collectors.toList());
		System.out.println(listStartTime);
		Collections.sort(listStartTime);
		System.out.println(listStartTime);
		int hour = listStartTime.get(0).getHour();
		int minute = listStartTime.get(0).getMinute();
		Constants.mainStore.put("startHour", "" + hour);
		Constants.mainStore.put("startMinute", "" + minute);
		for (LocalTime startTime : listStartTime) {
			ScheduledSound sound = listSound.stream().filter(x -> x.getStartTime().equals(startTime)).findFirst()
					.orElse(null);
			String cronTime = String.format("0 %s %s * * *", startTime.getMinute(), startTime.getHour());
			CronTrigger cronTrigger = new CronTrigger(cronTime);
			addTaskToScheduler(sound.getId(), () -> runScheduledSound(sound.getSoundName(), sound.getLoopCount()),
					cronTrigger);
		}
		return true;
	}

	private void runScheduledSound(String soundName, int loopCount) {
		System.out.println("runScheduledSound: " + soundName + " " + loopCount);
		if (BaseTool.khoosonJagsaaltEsekh(soundPlayer.getListSoundName()))
			// Player init hiigdeegui uyed orj baina
			try {
				System.out.println("Player initialized...");
				soundService.startPlaylistOnPlayer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		Status playerStatus = soundPlayer.getPlayerStatus();
		if (playerStatus != null && !soundPlayer.isPlayingScheduledSound())
			soundPlayer.copyMediaPlayerToTemp();
		soundPlayer.setScheduledSoundLoopCount(loopCount);
		soundPlayer.playNew("ScheduledSound", soundName, false);
		soundPlayer.setLoopCount(loopCount);
		soundPlayer.setEndOfMediaAfterScheduledSound();
	}

	public void configureStartPlayer() {
		System.out.println("configureStartPlayer");
		int hour = 10;
		int minute = 0;
		Constants.mainStore.put("startHour", "" + hour);
		Constants.mainStore.put("startMinute", "" + minute);
		String cronTime = String.format("0 %s %s * * *", minute, hour);
		CronTrigger cronTrigger = new CronTrigger(cronTime);
		addTaskToScheduler("1", () -> {
			try {
				soundService.startPlaylistOnPlayer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, cronTrigger);
	}

	public void configureStopPlayer() {
		System.out.println("configureStopPlayer");
		int hour = 22;
		int minute = 0;
		List<ScheduledSound> listSound = scheduledSoundRepo.findAll();
		if (!BaseTool.khoosonJagsaaltEsekh(listSound)) {
			List<LocalTime> listStartTime = listSound.stream()
					.collect(Collectors.groupingBy(ScheduledSound::getStartTime)).keySet().stream()
					.collect(Collectors.toList());
			System.out.println(listStartTime);
			Collections.sort(listStartTime);
			System.out.println(listStartTime);
			hour = listStartTime.get(listStartTime.size() - 1).getHour();
			minute = listStartTime.get(listStartTime.size() - 1).getMinute();
		}
		Constants.mainStore.put("stopHour", "" + hour);
		Constants.mainStore.put("stopMinute", "" + minute);
		String cronTime = String.format("0 %s %s * * *", minute, hour);
		CronTrigger cronTrigger = new CronTrigger(cronTime);
		addTaskToScheduler("2", () -> {
			soundPlayer.clearMediaPlayer();
		}, cronTrigger);
	}

}
