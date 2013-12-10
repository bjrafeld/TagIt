package edu.berkeley.cs160.tagit.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import edu.berkeley.cs160.tagit.R;

import java.io.File;
import java.io.IOException;
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

    public void updateBox(int id, String location, ArrayList<String> contents, String tagPicture, String contentsPicture) {
        Box b = boxes.get(id);
        b.setLocation(location);
        b.setTagPicture(tagPicture);
        b.setContentsPicture(contentsPicture);
        b.setContents(contents);
    }
	
	/**
	 * Removes a box from the container.
	 * 
	 * @param box - the box that you are deleting.
	 */
	public void removeBox(Box box) {
		boxes.remove(box);
	}

    /**
     * Returns a box with the given id, or null if the box cannot be
     * found
     *
     * @param id
     */
    public static Box find(int id) {
        if (singleton != null) {
            for (int i = 0; i < singleton.boxes.size(); i++) {
                Box box = singleton.boxes.get(i);
                if (box.getID() == id) { return box; }
            }
        }
        return null;
    }
	
	public ArrayList<Box> getBoxes() {
		return this.boxes;
	}
	
	/**
	 * This method returns a list of all the boxes for a search.
	 * Returns NULL is nothing matches the search
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
	
	/**
	 * Returns a list of boxes grouped together by their color grouping
	 * 
	 * @return
	 */
	public ArrayList<Box> sortByColor() {
		//For each color create an array list
		//iterate through the list of boxes and add to appropriate ColorList
		//Concatenate all lists according to order we specify
		//return the output
		return null;
	}

    public Box getBox(int id) {
        for (Box b : boxes) {
            if (b.getID() == id) {
                return b;
            }
        }
        return null;
    }

    /**
     * Returns the box with the highest ID, or null if there are no boxes.
     * @return Box with highest ID
     */
    public Box lastBox() {
        if (boxes.isEmpty()) {
            return null;
        }

        Box last = boxes.get(0);
        for (Box box : boxes) {
           if (box.getID() > last.getID()) {
               last = box;
           }
        }

        return last;
    }
	
	/**
	 * Returns a list of the boxes sorted in alphabetical order by their location.
	 * @return
	 */
	public ArrayList<Box> sortByLocation() {
		//TODO
		return null;
	}
	
	/*
	 * This class will hold implementation for all Box logic.
	 */

    public static ArrayList<Box> prePopulatedBoxes(Context context) throws IOException {
        BoxContainer bc = getInstance();

        ArrayList contents;
        String contentsPath;
        String tagPath;

        // Electronics Box
        contents = new ArrayList();
        contents.add("Joystick");
        contents.add("Circuit board");
        contents.add("Bag of twist-ties");
        contents.add("DVI cable");
        contents.add("Wires");

        tagPath = Box.saveBitmapAt("robot_doodle.png", context);
        contentsPath = Box.saveBitmapAt("electronics.jpg", context);

        bc.addBox("Office", contents, tagPath, contentsPath);

        // Produce Box
        contents = new ArrayList();
        contents.add("Lettuce");
        contents.add("Chard");
        contents.add("Turnips");
        contents.add("Strawberries");
        contents.add("Onions");
        contents.add("Mango");

        tagPath = Box.saveBitmapAt("produce_label.jpg", context);
        contentsPath = Box.saveBitmapAt("produce.jpg", context);

        bc.addBox("Refrigerator", contents, tagPath, contentsPath);

        // Clothing Box
        contents = new ArrayList();
        contents.add("Jeans");
        contents.add("Belt");
        contents.add("Shoes");
        contents.add("Sweatshirt");

        tagPath = Box.saveBitmapAt("clothes_label.jpg", context);
        contentsPath = Box.saveBitmapAt("clothes.jpg", context);

        bc.addBox("Bedroom Closet", contents, tagPath, contentsPath);

        // Golf Balls Box
        contents = new ArrayList();
        contents.add("Golf balls");

        tagPath = Box.saveBitmapAt("balls_label.jpg", context);
        contentsPath = Box.saveBitmapAt("balls.jpg", context);

        bc.addBox("Garage", contents, tagPath, contentsPath);


        return bc.getBoxes();
    }
	
}
