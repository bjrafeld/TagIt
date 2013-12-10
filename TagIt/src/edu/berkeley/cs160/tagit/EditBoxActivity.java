package edu.berkeley.cs160.tagit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.*;
import edu.berkeley.cs160.tagit.util.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by: Daniel
 * Date: 11/21/13
 * 
 * Last Edited by: Brendan Rafeld
 * Last Edited: 11/22/13
 */
public class EditBoxActivity extends Activity {

	private static final int CAPTURE_CONTENTS_PICTURE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_TAG_PICTURE_ACTIVITY_REQUEST_CODE = 200;
	
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private String title = "Add new box";
	
	/* Box related fields */
    private boolean isNew = true;

	private ArrayList<String> contents = null;
	private String tagPicturePath = null, contentsPicturePath = null;
	
	private BoxContainer boxContainer = BoxContainer.getInstance();
    private Box box = null;
	
	private ImageButton tagImageView;
	private ImageButton contentsImageView;
    private LinearLayout contentsList;
    private ImageButton back;
    private EditText addContentField;
    private ImageButton addContentButton;
    private EditText locationField;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_box);

        tagImageView = (ImageButton) findViewById(R.id.tagImageButton);
        contentsImageView = (ImageButton) findViewById(R.id.contentsImageButton);
        contentsList = (LinearLayout) findViewById(R.id.contents);
        addContentField = (EditText) findViewById(R.id.newContentText);
        locationField = (EditText) findViewById(R.id.location);
        addContentButton = (ImageButton) findViewById(R.id.addContentItem);

        contents = new ArrayList<String>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        if (!boxIsNew()) {
            int id = getIntent().getIntExtra("box_id", 0);
            box = boxContainer.getBox(id);

            isNew = false;
            populateFields();
            ((TextView)findViewById(R.id.tag_photo_label)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.contents_photo_label)).setVisibility(View.INVISIBLE);
        }
        setupActionBar();

        addContentField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    addContentButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupActionBar() {
        ActionBar bar = getActionBar();
        bar.setHomeButtonEnabled(false);
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setIcon(null);

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBar = inflator.inflate(R.layout.action_bar, null);

        ((TextView)actionBar.findViewById(R.id.title)).setText(title);

        back = (ImageButton)actionBar.findViewById(R.id.left_button);
        back.setImageResource(R.drawable.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EditBoxActivity.this.finish();
            }
        });

        bar.setCustomView(actionBar);
    }

    private void populateFields() {
        Intent intent = getIntent();
        box = BoxContainer.find(intent.getIntExtra("box_id", -1));
        if (box == null) return;

        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title");
        }
        findViewById(R.id.removeButton).setVisibility(View.VISIBLE);

        contentsPicturePath = box.getContentsPicturePath();
        tagPicturePath = box.getContentsPicturePath();

        setPicture(box.getContentsPicturePath(), contentsImageView);
        setPicture(box.getTagPicturePath(), tagImageView);
        locationField.setText(box.getLocation());
        for (String content : box.getContents()) {
            addContent(content);
        }
    }


    /**
     * On Click action for adding content
     * @param v
     */
    public void addContentFromView(View v) {
    	EditText newContentsItem = (EditText) findViewById(R.id.newContentText);
        String text = newContentsItem.getText().toString();
        if(text.matches("")) {
        	Toast toast = Toast.makeText(this, "You must enter an item before adding.", Toast.LENGTH_SHORT);
    		toast.show();
    		return;
        }
        newContentsItem.setText("");
        addContent(text);
    }

    /**
     * Adds a content item to the Box object, and updates the view
     * @param text  The name of the object being added
     */
    public void addContent(String text) {
        contents.add(text);

        View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.content_item, null);
        ((TextView)itemView.findViewById(R.id.text)).setText(text);
        (itemView.findViewById(R.id.removeContentItem)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeContent(view);
            }
        });

        contentsList.addView(itemView);

    }
    
    /**
     * On click action for removing content
     * @param v
     */
    public void removeContent(View v) {
        View itemView = (RelativeLayout)v.getParent();
        itemView.setVisibility(View.GONE);
    	contents.remove(itemView.findViewById(R.id.text));
    }
    /**
     * On CLick action for Tag Picture
     */
    public void takeTagPicture(View view) {
    	if(tagPicturePath != null) {
    		File deleted = new File(tagPicturePath);
    		deleted.delete();
    	}
    	File f = null;
    	try {
    		 f = setUpPhotoFile(CAPTURE_TAG_PICTURE_ACTIVITY_REQUEST_CODE);
    		 tagPicturePath = f.getAbsolutePath();
    		 takePicture(Uri.fromFile(f), CAPTURE_TAG_PICTURE_ACTIVITY_REQUEST_CODE);
            ((TextView)findViewById(R.id.tag_photo_label)).setVisibility(View.INVISIBLE);
    	} catch (IOException e) {
    		e.printStackTrace();
			f = null;
			tagPicturePath = null;
    	}
    }
    
    /**
     * On Click for Contents Picture
     */
    public void takeContentsPicture(View view) {
    	if(contentsPicturePath != null) {
    		File deleted = new File(contentsPicturePath);
    		deleted.delete();
    	}
    	File f = null;
    	try {
    		 f = setUpPhotoFile(CAPTURE_CONTENTS_PICTURE_ACTIVITY_REQUEST_CODE);
    		 contentsPicturePath = f.getAbsolutePath();
    		 takePicture(Uri.fromFile(f), CAPTURE_CONTENTS_PICTURE_ACTIVITY_REQUEST_CODE);
            ((TextView)findViewById(R.id.contents_photo_label)).setVisibility(View.INVISIBLE);
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
        String unAddedContent = addContentField.getText().toString().trim();
        String location = ((EditText)findViewById(R.id.location)).getText().toString().trim();

        if (!unAddedContent.equals("")) {
            contents.add(unAddedContent);
        }

    	if(boxIsValid()) {
            if (boxIsNew()) {
                boxContainer.addBox(location, contents, tagPicturePath, contentsPicturePath);
            } else {
                int id = box.getID();
                boxContainer.updateBox(id,location, contents, tagPicturePath, contentsPicturePath);
            }
            finish();
        }
    }

    public void removeBox(View v) {
        boxContainer.removeBox(box);
        finish();
    }

    private boolean boxIsValid() {
        boolean isValid = true;
        String location = ((EditText)findViewById(R.id.location)).getText().toString().trim();

        if(location.matches("")) {
            Toast toast = Toast.makeText(this, "You must enter a Location before saving.", Toast.LENGTH_SHORT);
            toast.show();
            isValid = false;
        }
        if(tagPicturePath == null) {
            Toast toast = Toast.makeText(this, "You must tag a picture of the Tag before saving.", Toast.LENGTH_SHORT);
            toast.show();
            isValid = false;
        } else if(contentsPicturePath == null) {
            Toast toast = Toast.makeText(this, "You must take a picture of the Contents before saving.", Toast.LENGTH_SHORT);
            toast.show();
            isValid = false;
        }

        return isValid;
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
        this.sendBroadcast(mediaScanIntent);
    }

    private boolean boxIsNew() {
        return !getIntent().hasExtra("box_id");
    }
    
    //Possible Bug: Orientation change may lost picture (Make sure to lock orientation)
    
    
}
