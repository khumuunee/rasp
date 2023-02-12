package org.ncd.rasp.entity;

import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.ncd.rasp.tools.Constants;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scheduledsound")
public class ScheduledSound implements Cloneable{
	
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "groupid")
	private String groupId;
	
	@Column(name = "soundname")
	private String soundName;
	
	@Column(name = "starttime")
	@JsonFormat(pattern = Constants.TsagiinFormat, timezone = Constants.TimeZone)
	private LocalTime startTime;
	
	@Column(name = "loopcount")
	private int loopCount;
	
	@Column(name = "createddate")
	@JsonFormat(pattern = Constants.OgnooniiFormat, timezone = Constants.TimeZone)
	private Date createdDate;
	
	@Column(name = "createduser")
	private String createdUser;
	
	@Transient
	private int status;
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();  
	}
	
}
