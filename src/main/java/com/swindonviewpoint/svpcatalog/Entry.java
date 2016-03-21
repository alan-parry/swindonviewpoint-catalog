package com.swindonviewpoint.svpcatalog;

import java.io.File;

public class Entry {

	private int id;
	
	private int nid;
	
	private String duration;
	
	private String title;
	
	private String description;
	
	private String thumbnailUrl;
	
	private String producedDate;
	
	private String path;
	
	public static final String thumbnailFilename = "-thumb.jpg";
	public static final String qrFilename = "-qr.jpg";
	
	public Entry(int id, int nid, String duration, String title, String description,
				 String thumbnailUrl, String producedDate, String path){
		this.id = id;
		this.nid = nid;
		this.duration = duration;
		this.title = title;
		this.description = description;
		this.thumbnailUrl = thumbnailUrl;
		this.producedDate = producedDate;
		this.path = path;
	}

	public String getQRFilename() {
		 return  id + qrFilename;
	}

	public String getThumbnailFilename() {
		 return  id + thumbnailFilename;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getProducedDate() {
		return producedDate;
	}

	public void setProducedDate(String producedDate) {
		this.producedDate = producedDate;
	}
	
	
}
