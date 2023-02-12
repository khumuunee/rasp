package org.ncd.rasp.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.ncd.rasp.entity.PlayerLog;
import org.ncd.rasp.entity.PlaylistSound;
import org.ncd.rasp.repository.PlayerLogRepo;
import org.ncd.rasp.tools.BaseTool;
import org.ncd.rasp.tools.Constants;
import org.ncd.rasp.tools.ServiceTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

@Component
public class SoundPlayer {

	@Autowired
	private PlayerLogRepo playerLogRepo;

	enum PlayerStatus {
		played, finished, stopped, paused, error
	}

//	String url = Constants.SOUND_FILE_PATH;
//	String url = "C:\\sounds\\";
	/***********************************************************
	 * Fields
	 ********************************************************/
	MediaPlayer mediaPlayer;
	double tempCurrentTime;
	Status tempCurrentStatus;
	int currentSoundIndex = -1;
	List<PlaylistSound> listSound;
	/**
	 * Scheduled sound status
	 */
	int scheduledSoundLoopCount = -1;

	/********************************************************
	 * Initialize Fields
	 *****************************************************/

	public void setListSound(List<PlaylistSound> listSound) {
		this.listSound = listSound;
	}

	public List<String> getListSoundName() {
		if (listSound == null)
			return new ArrayList<>();
		return listSound.stream().map(x -> x.getSoundName()).collect(Collectors.toList());
	}

	public int getCurrentSoundIndex() {
		return currentSoundIndex;
	}

	public void setCurrentSoundIndex(int index) {
		currentSoundIndex = index;
	}

	public void setScheduledSoundLoopCount(int loopCount) {
		scheduledSoundLoopCount = loopCount;
	}

	public boolean isPlayingScheduledSound() {
		return scheduledSoundLoopCount > -1;
	}

	public String getCurrentSoundName() {
		if (mediaPlayer != null) {
			String[] split = mediaPlayer.getMedia().getSource().split("/");
			return split[split.length - 1];
		}
		return null;
	}

	public void copyMediaPlayerToTemp() {
		if (currentSoundIndex == -1)
			return;
		tempCurrentTime = getCurrentTime();
		tempCurrentStatus = getPlayerStatus();
	}

	/***********************************************************
	 * Control from service
	 ********************************************************/

	public void startPlaylist() {
		if (BaseTool.khoosonJagsaaltEsekh(listSound)
				|| listSound.stream().filter(x -> !BaseTool.khoosonStringEsekh(x.getSoundName())).count() == 0)
			return;
		currentSoundIndex = 0;
		playSound();
	}

	public void playNextSound() {
		if (BaseTool.khoosonJagsaaltEsekh(listSound))
			return;
		currentSoundIndex++;
		playSound();
	}

	public void playPreviousSound() {
		if (BaseTool.khoosonJagsaaltEsekh(listSound))
			return;
		currentSoundIndex--;
		if (currentSoundIndex == -1)
			currentSoundIndex = listSound.size() - 1;
		playSound();
	}

	public void playNewWithSoundName(String soundName) {
		if (BaseTool.khoosonJagsaaltEsekh(listSound))
			return;
		PlaylistSound playlistSound = listSound.stream().filter(x -> soundName.equals(x.getSoundName())).findAny()
				.orElse(null);
		if (playlistSound == null)
			return;
		currentSoundIndex = listSound.indexOf(playlistSound);
		playNew(playlistSound);
	}

	public void setEndOfMediaAfterScheduledSound() {
		mediaPlayer.setOnEndOfMedia(() -> {
			System.out.println("Scheduled sound finished. Loop count: " + scheduledSoundLoopCount);
			scheduledSoundLoopCount--;
			if (scheduledSoundLoopCount == 0) {
				playerLogRepo.save(initPlayerLog(PlayerStatus.finished));
				scheduledSoundLoopCount = -1;
				System.out.println(
						"currentSoundIndex: " + currentSoundIndex + " " + tempCurrentTime + " " + tempCurrentStatus);
				if (!BaseTool.khoosonJagsaaltEsekh(listSound))
					playSound();
//				if(tempCurrentStatus != Status.PLAYING)
//					pause();
				// seek(tempCurrentTime); ene nuluuluhgui baina. setStartTime-g ashiglah
				// heregtei
			}
		});
	}
	
	public void clearMediaPlayer() {
		stop();
		setListSound(null);
		setCurrentSoundIndex(-1);
	}

	/***********************************************************
	 * Control from inside
	 ********************************************************/

	private void setEndOfMedia() {
		mediaPlayer.setOnEndOfMedia(() -> {
			System.out.println("Sound finished");
			PlayerLog playerLog = initPlayerLog(PlayerStatus.finished, ServiceTool.generateIdForFinishPlayer(), getCurrentSoundName());
			ServiceTool.runTaskWithDelay(() -> {
				playerLogRepo.save(playerLog);
			}, 2000);
			playNextSound();
		});

	}

	private void setOnError() {
		mediaPlayer.setOnError(() -> {
			System.out.println("Aldaa garlaa: " + mediaPlayer.getError());
			mediaPlayer.getError().printStackTrace();
		});
	}

	private void playSound() {
		if (currentSoundIndex == -1 || listSound.size() == currentSoundIndex)
			currentSoundIndex = 0;
		PlaylistSound sound = listSound.get(currentSoundIndex);
		playNew(sound);
	}

	private void playNew(PlaylistSound playlistSound) {
		playNew(playlistSound.getSoundType(), playlistSound.getSoundName(), true);
	}

	public void playNew(String soundType, String soundName, boolean shouldInit) {
		try {
			stop();
			String string = new File(getUrl(soundType) + soundName).toURI().toString();
			Media sound = new Media(string);
			mediaPlayer = new MediaPlayer(sound);
			if (shouldInit)
				setEndOfMedia();
			setOnError();
			play();
		} catch (Exception ex) {
			ex.printStackTrace();
			playerLogRepo.save(initPlayerLog(PlayerStatus.error, ServiceTool.generateId(), BaseTool.textiigKhuvaaya(ex.getMessage(), 240)));
			throw ex;
		}
	}

	private String getUrl(String soundType) {
		return "Song".equals(soundType) ? Constants.SOUND_FILE_PATH
				: ("Ad".equals(soundType) ? Constants.AD_SOUND_FILE_PATH : Constants.SCHEDULED_SOUND_FILE_PATH);
	}

	private PlayerLog initPlayerLog(PlayerStatus status) {
		return initPlayerLog(status, ServiceTool.generateId(), getCurrentSoundName());
	}

	private PlayerLog initPlayerLog(PlayerStatus status, String id, String name) {
		PlayerLog playerLog = new PlayerLog();
		playerLog.setId(id);
		playerLog.setSoundName(name);
		playerLog.setTime(new Date());
		playerLog.setStatus(status.toString());
		return playerLog;
	}

	/**************************************
	 * Media player deerh uildluud
	 ********************************************/

	public void play() {
		if (mediaPlayer != null) {
			mediaPlayer.play();
			playerLogRepo.save(initPlayerLog(PlayerStatus.played));
		}
	}

	public void pause() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
			playerLogRepo.save(initPlayerLog(PlayerStatus.paused));
		}
	}

	public void stop() {
		if (mediaPlayer != null) {
			playerLogRepo.save(initPlayerLog(PlayerStatus.stopped));
			try {
				mediaPlayer.stop();
				mediaPlayer.dispose();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			mediaPlayer = null;
		}
	}

	public void seek(double seconds) {
		if (mediaPlayer != null) {
			mediaPlayer.seek(Duration.seconds(seconds));
		}
	}

	public void setStartTime(int seconds) {
		if (mediaPlayer != null)
			mediaPlayer.setStartTime(Duration.seconds(seconds));
	}

	/**************************************
	 * Media player-iin info
	 ********************************************/

	public double getTotalDuration() {
		if (mediaPlayer != null)
			return mediaPlayer.getMedia().getDuration().toSeconds();
		return 0;
	}

	public double getCurrentTime() {
		if (mediaPlayer != null)
			return mediaPlayer.getCurrentTime().toSeconds();
		return 0;
	}

	public void setLoopCount(int count) {
		if (mediaPlayer != null)
			mediaPlayer.setCycleCount(count);
	}

	public boolean isPlaying() {
		if (mediaPlayer != null)
			return mediaPlayer.getStatus().equals(Status.PLAYING);
		return false;
	}

	public Status getPlayerStatus() {
		if (mediaPlayer != null)
			return mediaPlayer.getStatus();
		return null;
	}

}
