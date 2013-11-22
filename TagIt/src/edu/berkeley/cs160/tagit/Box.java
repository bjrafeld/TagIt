package edu.berkeley.cs160.tagit;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * @author bjrafeld
 * Date: 11/21/13
 */
public class Box {
	
	private String location;
	private ArrayList<String> contents;
	private File tagPicture;
	private File contentsPicture;
	private int boxID;
	
	private static int idCounter = 0;
	
	/**
	 * Creates a new Box object with a unique ID.
	 */
	public Box() {
		this.boxID = idCounter;
		this.contents = new ArrayList<String>();
		idCounter++;
	}
	
	public Box(String location, ArrayList<String> contents, File tagPicture, File contentsPicture) {
		this.location = location;
		this.contents = contents;
		this.tagPicture = tagPicture;
		this.contentsPicture = contentsPicture;
		this.boxID = idCounter;
		
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
	}
	
	/**
	 * Remvoe an item from the contents of this Box.
	 * 
	 * @param item
	 */
	public void removeContents(String item) {
		this.contents.remove(item);
	}
	
	/**
	 * Add a tagPicture to the Box.
	 * 
	 * @param f		File containing image bitmap
	 */
	public void addTagPicture(File f) {
		this.tagPicture = f;
	}
	
	/**
	 * Add a contentsPicture to the Box.
	 * 
	 * @param f		File containing image bitmap
	 */
	public void addContentsPicture(File f) {
		this.contentsPicture = f;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public ArrayList<String> getContents() {
		return this.contents;
	}
	
	public int getID() {
		return this.boxID;
	}
	
	public File getTagPicture() {
		return this.tagPicture;
	}
	
	public File getContentsPicture() {
		return this.contentsPicture;
	}
}
