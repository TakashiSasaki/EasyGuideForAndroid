package com.gmail.takashi316.easyguide.content;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.takashi316.easyguide.exception.InvalidDirectoryNameException;

/**
 * @author Takashi SASAKI {@link "http://twitter.com/TakashiSasaki"}
 * 
 */
public class DirectoryName {
	private String rawName = "unknown";
	public String name = "unknown";
	public int number = -1;
	public int x = -1, y = -1;
	private Double latitude;
	private Double longitude;

	/**
	 * a constructor takes directory name.
	 * 
	 * @throws Exception
	 */
	public DirectoryName(String directory_name) {
		MyLogger.info("parsing " + directory_name);
		this.rawName = directory_name;
		String[] parts = directory_name.split("[ ]+");
		
		try{
		switch(parts.length){
		case 1: parseOldStyleNaming1(directory_name); return;
		case 2: parseOldStyleNaming2(parts); return;
		case 4: parseOldStyleNaming4(parts); return;
		}//switch
		} catch (NumberFormatException e){
			// do nothing
		}//try
		parseNewStyleNaming(parts);
	}// a constructor
	
	private void parseNewStyleNaming(String[] parts){
		consumeXY(parts);
		consumeLatLng(parts);
		consumeNumber(parts);
		for(String part: parts){
			if (part !=null){
				this.name = part;
				return;
			}//if
		}//for
	}//parseNewStyleNaming
	
	private void consumeXY(String[] parts){
		Pattern p = Pattern.compile("^([0-9]+),([0-9]+)$");
		for(int i = 0; i<parts.length; ++i){
			Matcher m = p.matcher(parts[i]);
			if(m.groupCount()!=2) continue;
			this.x = Integer.parseInt(m.group(1));
			this.y = Integer.parseInt(m.group(2));
			parts[i] = null;
		}//for
	}// consumeXY
	
	@SuppressWarnings("boxing")
	private void consumeLatLng(String[] parts){
		// the order is latitude then longitude
		Pattern p = Pattern.compile("^([0-9]+.[0-9]+),([0-9]+.[0-9]+)$");
		for(int i=0; i<parts.length; ++i){
			Matcher m = p.matcher(parts[i]);
			if(m.groupCount()!=2) continue;
			this.latitude = Double.parseDouble(m.group(1));
			this.longitude = Double.parseDouble(m.group(2));
			parts[i] = null;
		}//for
	}// consumeLatLng

	private void consumeNumber(String[] parts){
		Pattern p = Pattern.compile("^[0-9]+$");
		for(int i=0; i<parts.length; ++i){
			Matcher m = p.matcher(parts[i]);
			if(m.groupCount()!=1) continue;
			this.number = Integer.parseInt(m.group(1));
			parts[i] = null;
		}//for
	}//consumeInteger
	
	private void parseOldStyleNaming1(String directory_name){
		this.name = directory_name;
		this.number = -1;
		this.x = -1;
		this.y = -1;
	}//parseOldStyleNaming1
	
	private void parseOldStyleNaming2(String[] parts){
		this.number = Integer.parseInt(parts[0]);
		if (this.number <= 0) {
			throw new InvalidDirectoryNameException("parsing" + parts[0]
					+ " and it should be positive integer");
		}//if
		this.name = parts[1];
	}//parseOldStyleNaming2
	
	private void parseOldStyleNaming4(String[] parts){
		parseOldStyleNaming2(parts);
		this.x = Integer.parseInt(parts[2]);
		this.y = Integer.parseInt(parts[3]);
	}//parseOldStyleNaming4

	public Double getLatitude(){
		return this.latitude;
	}// getLatitude
	
	public Double getLongitude(){
		return this.longitude;
	}// getLongitude

	static public void main(String[] args) {
		final String directory_path = "/C:/Users/sasaki/Google ドライブ/Billable/EasyGuide-contents/EASYGUIDE/www.yonden.co.jp/01 四国電力";
		final File directory = new File(directory_path);
		DirectoryName directory_name = new DirectoryName(directory.getName());
		MyLogger.info("name = " + directory_name.name);
		MyLogger.info("number = " + directory_name.number);
		MyLogger.info("x = " + directory_name.x + ", y = " + directory_name.y);
	}
}
