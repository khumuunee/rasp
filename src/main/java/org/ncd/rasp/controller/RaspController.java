package org.ncd.rasp.controller;

import java.util.List;
import java.util.Map;

import org.ncd.rasp.service.RaspService;
import org.ncd.rasp.tools.ResponseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class RaspController {
	
	@Autowired
	private RaspService raspService;
	
	@GetMapping("turshilt")
	public String turshilt() {
		return "Turshilt amjilttai bolloo hu";
	}
	
	@GetMapping("checkConnection/{raspId}")
	public String checkConnection(@PathVariable String raspId) {
		System.out.println("checkConnection: " + raspId);
		return raspId + "_Success";
	}
	
	@PostMapping("syncRaspberry")
	public Map<String, Object> syncRaspberry(@RequestBody Map<String, Object> param) {
		String raspId = (String) param.get("raspId");
		try {
			return raspService.syncRaspberry(param);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String,Object> resWithError = ResponseTool.createResWithError(e);
			resWithError.put("raspId", raspId);
			return resWithError;
		}
	}
	
	@PostMapping("transferSound")
	public Map<String, Object> transferSound(@RequestParam("files") List<MultipartFile> files,					
			@RequestParam("raspId") String raspId,
			@RequestParam("sequence") List<String> seq) {
		System.out.println("raspId ================================> " + raspId);
		try {
			return raspService.transferSound(files, raspId, seq);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String,Object> resWithError = ResponseTool.createResWithError(e);
			resWithError.put("raspId", raspId);
			return resWithError;
		}
	}
	
	@PostMapping("searchPlayerLogInRaspberry")
	public Map<String, Object> searchPlayerLogInRaspberry(@RequestBody Map<String, Object> param) {
		try {
			return raspService.searchPlayerLogInRaspberry(param);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseTool.createResWithError(e);
		}
	}
	
}
