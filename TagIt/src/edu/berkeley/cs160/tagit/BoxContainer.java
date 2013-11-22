package edu.berkeley.cs160.tagit;

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
	public BoxContainer getBoxContainer() {
		if(singleton == null) {
			this.singleton = new BoxContainer();
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
	public void addBox(String location, ArrayList<String> contents, File tagPicture, File contentsPicture) {
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
	
	/*
	 * This class will hold implementation for all Box logic.
	 */
	
}
