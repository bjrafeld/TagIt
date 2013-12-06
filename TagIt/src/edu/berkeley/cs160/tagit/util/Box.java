package edu.berkeley.cs160.tagit.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

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

    public Box(Parcel parcel) {
        location            = parcel.readString();
        parcel.readStringList(contents);
        tagPicturePath      = parcel.readString();
        contentsPicturePath = parcel.readString();
        boxID               = parcel.readInt();
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

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }
	
	/**
	 * Add a tagPicture to the Box.
	 * 
	 * @param path		Path to file containing image bitmap
	 */
	public void setTagPicture(String path) {
		this.tagPicturePath = path;
	}

	/**
	 * Add a contentsPicture to the Box.
	 * 
	 * @param path		Path to file containing image bitmap
	 */
	public void setContentsPicture(String path) {
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
	
	public Bitmap getContentsBitmap(int h, int w) {
		return getBitmap(contentsPicturePath, h, w);
	}
	
	public Bitmap getTagBitmap(int h, int w) {
		return getBitmap(tagPicturePath, h, w);
	}
	
	private Bitmap getBitmap(String path, int targetH, int targetW) {
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
		return bitmap;
	}
}
