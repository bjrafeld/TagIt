package edu.berkeley.cs160.tagit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.berkeley.cs160.adapter.TabAdapter.TabAdapter;
import edu.berkeley.cs160.tagit.util.AlbumStorageDirFactory;
import edu.berkeley.cs160.tagit.util.BaseAlbumDirFactory;
import edu.berkeley.cs160.tagit.util.BoxContainer;
import edu.berkeley.cs160.tagit.util.FroyoAlbumDirFactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by: Daniel
 * Date: 11/21/13
 * 
 * Last Edited by: Brendan Rafeld
 * Last Edited: 11/22/13
 */
public class AddBoxFragment extends Fragment {
	
	private static final int CAPTURE_CONTENTS_PICTURE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_TAG_PICTURE_ACTIVITY_REQUEST_CODE = 200;
	
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	
	/* Box related fields */
	private String location;
	private ArrayList<String> contents = null;
	private String tagPicturePath = null, contentsPicturePath = null;
	
	private BoxContainer boxContainer = BoxContainer.getInstance();
	
	private ImageButton tagImageView;
	private ImageButton contentsImageView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_add_box, container, false);
    	
    	tagImageView = (ImageButton) v.findViewById(R.id.tagImageButton);
    	contentsImageView = (ImageButton) v.findViewById(R.id.contentsImageButton);
    	
    	//Can't add Fragment methods to view in XML
    	tagImageView.setOnClickListener(
    		new OnClickListener() {
    			public void onClick(View v) {
    				takeTagPicture();
    			}
    		}
    	);
    	
    	contentsImageView.setOnClickListener(
        		new OnClickListener() {
        			public void onClick(View v) {
        				takeContentsPicture();
        			}
        		}
        	);
    	
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
    	
        return v;
    }
    
    /**
     * On Click action for adding content
     * @param v
     */
    public void addContent(View v) {
    	if(contents == null) {
    		contents = new ArrayList<String>();
    	}
    	
    	//Should be changed if not using EditText for Contents
    	EditText contentView = (EditText) v.findViewById(R.id.locationText);
    	contents.add(contentView.getText().toString());
    }
    
    /**
     * On click action for removing content
     * @param v
     */
    public void removeContent(View v) {
    	EditText contentView = (EditText) v;
    	contents.remove(contentView.getText().toString());
    }
    
    /**
     * On CLick action for Tag Picture
     * @param v
     */
    public void takeTagPicture() {
    	if(tagPicturePath != null) {
    		File deleted = new File(tagPicturePath);
    		deleted.delete();
    	}
    	File f = null;
    	try {
    		 f = setUpPhotoFile(CAPTURE_TAG_PICTURE_ACTIVITY_REQUEST_CODE);
    		 tagPicturePath = f.getAbsolutePath();
    		 takePicture(Uri.fromFile(f), CAPTURE_TAG_PICTURE_ACTIVITY_REQUEST_CODE);
    	} catch (IOException e) {
    		e.printStackTrace();
			f = null;
			tagPicturePath = null;
    	}
    }
    
    /**
     * On Click for Contents Picture
     * @param v
     */
    public void takeContentsPicture() {
    	if(contentsPicturePath != null) {
    		File deleted = new File(contentsPicturePath);
    		deleted.delete();
    	}
    	File f = null;
    	try {
    		 f = setUpPhotoFile(CAPTURE_CONTENTS_PICTURE_ACTIVITY_REQUEST_CODE);
    		 contentsPicturePath = f.getAbsolutePath();
    		 takePicture(Uri.fromFile(f), CAPTURE_CONTENTS_PICTURE_ACTIVITY_REQUEST_CODE);
    	} catch (IOException e) {
    		e.printStackTrace();
			f = null;
			tagPicturePath = null;
    	}
    }
    
    /**
     * On click for the Save Box Button
     * @param v
     */
    public void saveBox(View v) {
    	if(location == null) {
    		Toast toast = Toast.makeText(getView().getContext(), "You must enter a Location before saving.", Toast.LENGTH_SHORT);
    		toast.show();
    	} else if(tagPicturePath == null) {
    		Toast toast = Toast.makeText(getView().getContext(), "You must tag a picture of the Tag before saving.", Toast.LENGTH_SHORT);
    		toast.show();
    	} else if(contentsPicturePath == null) {
    		Toast toast = Toast.makeText(getView().getContext(), "You must take a picture of the Contents before saving.", Toast.LENGTH_SHORT);
    		toast.show();
    	}
    	boxContainer.addBox(location, contents, tagPicturePath, contentsPicturePath);
    	MainActivity a = (MainActivity) getActivity();
    	a.selectPage(TabAdapter.BOX_FRAGMENT);
    }
    
    private void takePicture(Uri fileURI, int resultCode) {
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	intent.putExtra(MediaStore.EXTRA_OUTPUT, fileURI);
    	
    	startActivityForResult(intent, resultCode);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode) {
    	case CAPTURE_TAG_PICTURE_ACTIVITY_REQUEST_CODE: {
    		if(resultCode == Activity.RESULT_OK) {
    			setTagPicture();
    		}
    		break;
    	}
    		
    	case CAPTURE_CONTENTS_PICTURE_ACTIVITY_REQUEST_CODE: {
    		if(resultCode == Activity.RESULT_OK) {
    			setContentsPicture();
    		}
    		break;
    	}
    	}
    }
    
    public File setUpPhotoFile(int resultCode) throws IOException {
    	File f = createImageFile();
    	if(resultCode == CAPTURE_CONTENTS_PICTURE_ACTIVITY_REQUEST_CODE) {
    		contentsPicturePath = f.getAbsolutePath();
    	} else if (resultCode == CAPTURE_TAG_PICTURE_ACTIVITY_REQUEST_CODE) {
    		tagPicturePath = f.getAbsolutePath();
    	}
		
		return f;
    }
    
    private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}
    
    private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
			
		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}
		
		return storageDir;
	}
    
    private String getAlbumName() {
		return getString(R.string.app_name);
	}
    
    private void setTagPicture() {
    	setPicture(tagPicturePath, tagImageView);
		savePicture(tagPicturePath);
    }
    
    private void setContentsPicture() {
    	setPicture(contentsPicturePath, contentsImageView);
		savePicture(contentsPicturePath);
    }
    
    private void setPicture(String path, ImageView imageView) {
    	/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = imageView.getWidth();
		int targetH = imageView.getHeight();

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
		
		/* Associate the Bitmap to the ImageView */
		imageView.setImageBitmap(bitmap);
    }
    
    private void savePicture(String path) {
    	Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }
    
    //Possible Bug: Orientation change may lost picture (Make sure to lock orientation)
    
    
}
