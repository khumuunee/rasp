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
@Table(name = "playerlog")
public class PlayerLog implements Cloneable{
	
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "soundname")
	private String soundName;
	
	@Column(name = "time")
	@JsonFormat(pattern = Constants.OgnooniiFormat, timezone = Constants.TimeZone)
	private Date time;
	
	@Column(name = "status")
	private String status;
	
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();  
	}
	
}
