package org.ncd.rasp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ncd.rasp.tools.Constants;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "playlistsound")
public class PlaylistSound implements Cloneable {
	
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "playlistid")
	private String playlistId;
	
	@Column(name = "soundname")
	private String soundName;
	
	@Column(name = "soundtype")
	private String soundType;

	@Column(name = "ordernumber")
	private int orderNumber;
	
	@Column(name = "createddate")
	@JsonFormat(pattern = Constants.OgnooniiFormat, timezone = Constants.TimeZone)
	private Date createdDate;
	
	@Column(name = "createduser")
	private String createdUser;
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();  
	}
	
}
