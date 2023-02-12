package org.ncd.rasp.controller;

import java.util.Map;

import org.ncd.rasp.service.SoundService;
import org.ncd.rasp.tools.ResponseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SoundPlayerController {
	
	@Autowired
	private SoundService soundService;
	
	@GetMapping("startPlaylist")
	public Map<String, Object> startPlaylist() {
		try {
			return soundService.startPlaylistWithCheckTime();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseTool.createResWithError(e);
		}
	}
	
	@GetMapping("getCurrentPlayerFromRaspberry")
	public Map<String, Object> getCurrentPlayerFromRaspberry() {
		try {
			return soundService.getCurrentPlayerFromRaspberry();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseTool.createResWithError(e);
		}
	}
	
	@GetMapping("getCurrentPlayerStatusFromRaspberry")
	public Map<String, Object> getCurrentPlayerStatusFromRaspberry() {
		try {
			return soundService.getCurrentPlayerStatusFromRaspberry();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseTool.createResWithError(e);
		}
	}
	
	@GetMapping("setCurrentPlayerProgressInRaspberry/{time}")
	public Map<String, Object> setCurrentPlayerProgressInRaspberry(@PathVariable double time) {
		try {
			return soundService.setCurrentPlayerProgressInRaspberry(time);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseTool.createResWithError(e);
		}
	}
	
	@GetMapping("pausePlayerInRaspberry")
	public Map<String, Object> pausePlayerInRaspberry() {
		try {
			return soundService.pausePlayerInRaspberry();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseTool.createResWithError(e);
		}
	}
	
	@GetMapping("playPlayerInRaspberry")
	public Map<String, Object> playPlayerInRaspberry() {
		try {
			return soundService.playPlayerInRaspberry();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseTool.createResWithError(e);
		}
	}
	
	@GetMapping("playNextSoundInRaspberry")
	public Map<String, Object> playNextSoundInRaspberry() {
		try {
			return soundService.playNextSoundInRaspberry();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseTool.createResWithError(e);
		}
	}
	
	@GetMapping("playPreviousSoundInRaspberry")
	public Map<String, Object> playPreviousSoundInRaspberry() {
		try {
			return soundService.playPreviousSoundInRaspberry();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseTool.createResWithError(e);
		}
	}
	
	@GetMapping("playSoundWithNameInRaspberry/{soundName}")
	public Map<String, Object> playSoundWithNameInRaspberry(@PathVariable String soundName) {
		try {
			return soundService.playSoundWithNameInRaspberry(soundName);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseTool.createResWithError(e);
		}
	}

}
