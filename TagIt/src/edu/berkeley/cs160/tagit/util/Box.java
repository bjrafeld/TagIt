package edu.berkeley.cs160.tagit.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author bjrafeld
 * Date: 11/21/13
 */
public class Box {
	
	private String location;
	private ArrayList<String> contents;
	private String tagPicturePath;
	private String contentsPicturePath;
	private int boxID;
	
	private static int idCounter = 0;
	
	/**
	 * Creates a new Box object with a unique ID.
	 */
	protected Box() {
		this.boxID = idCounter;
		this.contents = new ArrayList<String>();
		idCounter++;
	}
	
	protected Box(String location, ArrayList<String> contents, String tagPicture, String contentsPicture) {
		this.location = location;
		this.contents = contents;
		this.tagPicturePath = tagPicture;
		this.contentsPicturePath = contentsPicture;
		this.boxID = idCounter;
		
		//Collections.sort(this.contents);
		
		idCounter++;
	}
	
	/**
	 * Set the location of the Box.
	 * 
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * Add an item to the contents of this box.
	 * 
	 * @param item
	 */
	public void addContents(String item) {
		this.contents.add(item);
		//Collections.sort(this.contents);
	}
	
	/**
	 * Remvoe an item from the contents of this Box.
	 * 
	 * @param item
	 */
	public void removeContents(String item) {
		this.contents.remove(item);
		//Collections.sort(this.contents);
	}
	
	/**
	 * Add a tagPicture to the Box.
	 * 
	 * @param f		File containing image bitmap
	 */
	public void addTagPicture(String path) {
		this.tagPicturePath = path;
	}
	
	/**
	 * Add a contentsPicture to the Box.
	 * 
	 * @param f		File containing image bitmap
	 */
	public void addContentsPicture(String path) {
		this.contentsPicturePath = path;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public ArrayList<String> getContents() {
		return this.contents;
	}
	
	public String getContentsString() {
		String result = "";
		for(int i = 0; i < this.contents.size(); i++) {
			if(i == (this.contents.size() - 1)) {
				result += this.contents.get(i);
			} else {
				result += this.contents.get(i) + ", ";
			}
		}
		return result;
	}
	
	public int getID() {
		return this.boxID;
	}
	
	public String getTagPicturePath() {
		return this.tagPicturePath;
	}
	
	public String getContentsPicturePath() {
		return this.contentsPicturePath;
	}
}
