package com.swindonviewpoint.svpcatalog;

import java.io.File;

public class Entry {

	int id;
	
	int nid;
	
	String duration;
	
	String title
	
	String description;
	
	String thumbnailUrl;
	
	String producedDate;
	
	String path;
	
	String baseDir;
	
	public Entry(int id, int nid, String duration, String title, 
				String description, String thumbnailUrl, String producedDate,
				String path, String baseDir){
		this.id = id;
		this.nid = nid;
		this.duration = duration;
		this.title = title;
		this.description = description;
		this.thumbnailUrl = thumbnailUrl;
		this.producedDate = producedDate;
		this.path = path;
		this.baseDir = baseDir;
	}

	public String getThumbnailPath() {
		return baseDir + File.separator + "temp" + File.separator + id + "-thumb.jpg";
	}
	
	public String getQRPath() {
		return baseDir + File.separator + "temp" + File.separator + id + "-qr.png";
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
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
