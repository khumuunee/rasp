package org.ncd.rasp.service;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ncd.rasp.config.SoundPlayer;
import org.ncd.rasp.entity.Playlist;
import org.ncd.rasp.entity.PlaylistSound;
import org.ncd.rasp.repository.PlaylistRepo;
import org.ncd.rasp.repository.PlaylistSoundRepo;
import org.ncd.rasp.tools.BaseTool;
import org.ncd.rasp.tools.Constants;
import org.ncd.rasp.tools.ResponseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoundService {
	
	@Autowired
	private PlaylistRepo playlistRepo;
	
	@Autowired
	private SoundPlayer soundPlayer;
	
	@Autowired
	private PlaylistSoundRepo playlistSoundRepo;
	
	public Map<String, Object> startPlaylistWithCheckTime() throws Exception {
		String startHour = (String) Constants.mainStore.get("startHour");
		String startMinute = (String) Constants.mainStore.get("startMinute");
		String stopHour = (String) Constants.mainStore.get("stopHour");
		String stopMinute = (String) Constants.mainStore.get("stopMinute");
		String hour = "" + LocalTime.now().getHour();
		String minute = "" + LocalTime.now().getMinute();
		String second = "" + LocalTime.now().getSecond();
		startHour = (startHour.length() == 1 ? "0" : "") + startHour;
		startMinute = (startMinute.length() == 1 ? "0" : "") + startMinute;
		stopHour = (stopHour.length() == 1 ? "0" : "") + stopHour;
		stopMinute = (stopMinute.length() == 1 ? "0" : "") + stopMinute;
		hour = (hour.length() == 1 ? "0" : "") + hour;
		minute = (minute.length() == 1 ? "0" : "") + minute;
		second = (second.length() == 1 ? "0" : "") + second;
		LocalTime startTime = LocalTime.parse(startHour + ":" + startMinute + ":00");
		LocalTime stopTime = LocalTime.parse(stopHour + ":" + stopMinute + ":00");
		LocalTime time = LocalTime.parse(hour + ":" + minute + ":" + second);
		if(time.isAfter(startTime) && time.isBefore(stopTime)) {
			System.out.println("Start play...");
			startPlaylistOnPlayer();
		}
		else {
			System.out.println("Wait until start time...");
			soundPlayer.clearMediaPlayer();
		}
		return ResponseTool.createRes();
	}
	
	public Map<String, Object> startPlaylistOnPlayer() throws Exception {
		List<PlaylistSound> listSound = playlistSoundRepo.findAll();
		if(BaseTool.khoosonJagsaaltEsekh(listSound))
			throw new Exception("PlaylistSound is empty");
		listSound.sort(Comparator.comparing(PlaylistSound::getOrderNumber));
		soundPlayer.setListSound(listSound);
		soundPlayer.startPlaylist();
		return ResponseTool.createRes();
	}

	public Map<String, Object> getCurrentPlayerFromRaspberry() throws Exception {
		List<Playlist> listPlaylist = playlistRepo.findAll();
		if(BaseTool.khoosonJagsaaltEsekh(listPlaylist))
			throw new Exception("No playlist found");
		List<PlaylistSound> listSound = playlistSoundRepo.findAll();
		if(BaseTool.khoosonJagsaaltEsekh(listSound))
			throw new Exception("PlaylistSound is empty");
		listSound.sort(Comparator.comparing(PlaylistSound::getOrderNumber));
		Map<String, Object> res = new HashMap<>();
		res.put("playlistName", listPlaylist.get(0).getName());
		res.put("listSound", listSound.stream().map(x -> x.getSoundName()).collect(Collectors.toList()));
		res.put("currentSoundIndex", soundPlayer.getCurrentSoundIndex());
		res.put("listSoundName", soundPlayer.getListSoundName());
		res.put("totalDuration", soundPlayer.getTotalDuration());
		res.put("currentTime", soundPlayer.getCurrentTime());
		res.put("isPlaying", soundPlayer.isPlaying());
		return ResponseTool.createRes(res);
	}

	public Map<String, Object> getCurrentPlayerStatusFromRaspberry() {
		Map<String, Object> res = new HashMap<>();
		res.put("currentSoundIndex", soundPlayer.getCurrentSoundIndex());
		res.put("totalDuration", soundPlayer.getTotalDuration());
		res.put("currentTime", soundPlayer.getCurrentTime());
		res.put("isPlaying", soundPlayer.isPlaying());
		res.put("isPlayingScheduledSound", soundPlayer.isPlayingScheduledSound());
		if(soundPlayer.isPlayingScheduledSound()) {
			res.put("currentSoundName", soundPlayer.getCurrentSoundName());
			res.put("currentSoundIndex", -2);
		}
		return ResponseTool.createRes(res);
	}

	public Map<String, Object> setCurrentPlayerProgressInRaspberry(double time) {
		soundPlayer.seek(time);
		return ResponseTool.createRes();
	}

	public Map<String, Object> pausePlayerInRaspberry() {
		soundPlayer.pause();
		return ResponseTool.createRes();
	}

	public Map<String, Object> playPlayerInRaspberry() {
		soundPlayer.play();
		return ResponseTool.createRes();
	}

	public Map<String, Object> playNextSoundInRaspberry() {
		soundPlayer.playNextSound();
		return ResponseTool.createRes();
	}

	public Map<String, Object> playPreviousSoundInRaspberry() {
		soundPlayer.playPreviousSound();
		return ResponseTool.createRes();
	}
	
	public Map<String, Object> playSoundWithNameInRaspberry(String soundName) throws Exception {
		if(BaseTool.khoosonJagsaaltEsekh(soundPlayer.getListSoundName())) {
			List<PlaylistSound> listSound = playlistSoundRepo.findAll();
			if(BaseTool.khoosonJagsaaltEsekh(listSound))
				throw new Exception("PlaylistSound is empty");
			listSound.sort(Comparator.comparing(PlaylistSound::getOrderNumber));
			soundPlayer.setListSound(listSound);
		}
		String[] split = soundName.split("ENUUGEERTASLAARAI");
		soundPlayer.playNewWithIndex(Integer.parseInt(split[1]));
		return ResponseTool.createRes();
	}

}
