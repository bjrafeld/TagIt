package edu.berkeley.cs160.tagit.util;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * @author bjrafeld
 * Date: 11/21/13
 */
public class BoxContainer {
	
	private ArrayList<Box> boxes;
	private static BoxContainer singleton = null;
	
	/**
	 * Private constructor for BoxConntainer. Only getBoxContainer should be used
	 * to access the BoxContainer
	 */
	private BoxContainer() {
		this.boxes = new ArrayList<Box>();
	}
	
	/**
	 * getBoxContainer() returns the the BoxContainer for the app/
	 */
	public static BoxContainer getInstance() {
		if(singleton == null) {
			singleton = new BoxContainer();
		}
		return singleton;
	}
	
	/**
	 * Add a new box to the container
	 * 
	 * @param location - Location of the box
	 * @param contents - An array of all the content Strings. If 
	 * 					no contents pass NULL in as argument.
	 * @param tagPicture - File for the picture of the tag
	 * @param contentsPicture - File for the picture of the contents
	 */
	public void addBox(String location, ArrayList<String> contents, String tagPicture, String contentsPicture) {
		if(contents == null) {
			contents = new ArrayList<String>();
		}
		Box b = new Box(location, contents, tagPicture, contentsPicture);
		boxes.add(b);
	}
	
	/**
	 * Removes a box from the container.
	 * 
	 * @param box - the box that you are deleting.
	 */
	public void removeBox(Box box) {
		boxes.remove(box);
	}
	
	public ArrayList<Box> getBoxes() {
		return this.boxes;
	}
	
	/**
	 * This method returns a list of all the boxes for a search
	 * 
	 * @param query		String to search on
	 * @return
	 */
	public ArrayList<Box> searchBoxes(String query) {
		String[] words = query.split("\\s+");
		ArrayList<Box> locationsList = new ArrayList<Box>();
		ArrayList<Box> contentsList = new ArrayList<Box>();
		boolean duplicateFlag = false;
		for (String w : words) {
			for(Box b : getBoxes()) {
				duplicateFlag = false;
				if(b.getLocation().equals(w)) {
					locationsList.add(b);
					duplicateFlag = true;
				}
				if(b.getContents().contains(w) && (!duplicateFlag)) {
					contentsList.add(b);
				}
			}
		}
		locationsList.addAll(contentsList);
		if(locationsList.isEmpty()) {
			locationsList = null;
		}
		return locationsList;
	}
	
	public ArrayList<Box> sortByColor() {
		//TODO
		return null;
	}
	
	public ArrayList<Box> sortByLocation() {
		//TODO
		return null;
	}
	
	/*
	 * This class will hold implementation for all Box logic.
	 */
	
}
