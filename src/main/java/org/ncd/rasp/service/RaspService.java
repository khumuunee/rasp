package org.ncd.rasp.service;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.ncd.rasp.entity.PlayerLog;
import org.ncd.rasp.entity.Playlist;
import org.ncd.rasp.entity.PlaylistSound;
import org.ncd.rasp.entity.ScheduledSound;
import org.ncd.rasp.repository.PlayerLogRepo;
import org.ncd.rasp.repository.PlaylistRepo;
import org.ncd.rasp.repository.PlaylistSoundRepo;
import org.ncd.rasp.repository.ScheduledSoundRepo;
import org.ncd.rasp.tools.BaseTool;
import org.ncd.rasp.tools.Constants;
import org.ncd.rasp.tools.ResponseTool;
import org.ncd.rasp.tools.ResponseTool.ResCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RaspService {
	
	@Autowired
	private PlaylistRepo playlistRepo;
	
	@Autowired
	private PlaylistSoundRepo playlistSoundRepo;
	
	@Autowired
	private ScheduledSoundRepo scheduledSoundRepo;
	
	@Autowired
	private SchedulerService schedulerService;
	
	@Autowired
	private PlayerLogRepo playerLogRepo;

	@Transactional
	public Map<String, Object> syncRaspberry(Map<String, Object> param) {
		String raspId = (String) param.get("raspId");
		Playlist playlist = BaseTool.convertMapToObject(Playlist.class, param.get("playlist"));
		List<PlaylistSound> listSound = BaseTool.convertLinkedToList(PlaylistSound.class, (List<?>)param.get("listSound"));
		List<ScheduledSound> listScheduledSound = BaseTool.convertLinkedToList(ScheduledSound.class, (List<?>)param.get("listScheduledSound"));		
		playlistRepo.deleteAll();
		playlistSoundRepo.deleteAll();
		scheduledSoundRepo.deleteAll();
		playlistRepo.save(playlist);
		List<String> listDownloadSoundName = new ArrayList<>();
		List<String> listDownloadAdSoundName = new ArrayList<>();
		if(!BaseTool.khoosonJagsaaltEsekh(listSound)) {
			for(PlaylistSound sound : listSound) {
				boolean isAd = "Ad".equals(sound.getSoundType());
				String filePath = isAd ? Constants.AD_SOUND_FILE_PATH : Constants.SOUND_FILE_PATH; 
				File f = new File(filePath + sound.getSoundName());
				if(!f.isFile()) {
					if(isAd)
						listDownloadAdSoundName.add(sound.getSoundName());
					else
						listDownloadSoundName.add(sound.getSoundName());
				}
				playlistSoundRepo.saveAll(listSound);
			}
		}
		List<String> listDownloadScheduledSoundName = new ArrayList<>();
		if(!BaseTool.khoosonJagsaaltEsekh(listScheduledSound)) {
			for(ScheduledSound sound : listScheduledSound) {
				File f = new File(Constants.SCHEDULED_SOUND_FILE_PATH + sound.getSoundName());
				if(!f.isFile())
					listDownloadScheduledSoundName.add(sound.getSoundName());
			}
			scheduledSoundRepo.saveAll(listScheduledSound);
		}
		schedulerService.removeAllTaskFromScheduler();
		boolean scheduledSoundExists = schedulerService.configureAllScheduledSound();
		if(!scheduledSoundExists)
			schedulerService.configureStartPlayer();
		schedulerService.configureStopPlayer();
		return ResponseTool.createRes(Map.of(ResponseTool.ResCodeStr, ResCode.Success, "raspId", raspId, "listDownloadSoundName", listDownloadSoundName, "listDownloadScheduledSoundName", listDownloadScheduledSoundName, "listDownloadAdSoundName", listDownloadAdSoundName));
	}

	public Map<String, Object> transferSound(List<MultipartFile> files, String raspId, List<String> seq) throws Exception {
		System.out.println("seq " + seq);
		int i = 0;
		for(MultipartFile multipartFile: files) {
			String type = seq.get(i).replace("[", "").replace("]", "");
			String path = "1".equals(type) ? Constants.SOUND_FILE_PATH : ("2".equals(type) ? Constants.AD_SOUND_FILE_PATH : Constants.SCHEDULED_SOUND_FILE_PATH);
			System.out.println(type + " " + path + " " + multipartFile.getOriginalFilename());
			File file = new File(path + multipartFile.getOriginalFilename());
			multipartFile.transferTo(file);
			i++;
		}
		return ResponseTool.createRes(Map.of(ResponseTool.ResCodeStr, ResCode.Success, "raspId", raspId));
	}
	
	public Map<String, Object> searchPlayerLogInRaspberry(Map<String, Object> param) throws ParseException {
		String searchDateStr = (String) param.get("searchDate");
		Date searchDate = BaseTool.convertStringToDate(searchDateStr, Constants.EngiinOgnooniiFormat);
		Date startDate = BaseTool.ognoondTsagOnooy(searchDate, 0, 0, 0);
		Date endDate = BaseTool.ognoondTsagOnooy(searchDate, 23, 59, 59);
		List<PlayerLog> listLog = playerLogRepo.findAllByTimeBetweenOrderByTime(startDate, endDate);
		return ResponseTool.createRes(Map.of("listLog", listLog));
	}

}
