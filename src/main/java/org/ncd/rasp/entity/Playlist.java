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
@Table(name = "playlist")
public class Playlist implements Cloneable{
	
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "ordernumber")
	private int orderNumber;
	
	@Column(name = "soundcount")
	private int soundCount;
	
	@Column(name = "createddate")
	@JsonFormat(pattern = Constants.OgnooniiFormat, timezone = Constants.TimeZone)
	private Date createdDate;
	
	@Column(name = "updateddate")
	@JsonFormat(pattern = Constants.OgnooniiFormat, timezone = Constants.TimeZone)
	private Date updatedDate;
	
	@Column(name = "createduser")
	private String createdUser;
	
	@Column(name = "updateduser")
	private String updatedUser;
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();  
	}

}
