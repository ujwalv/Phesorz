package com.example.choppingimage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import eu.janmuller.android.simplecropimage.CropImage;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends Activity {

	public static final String TAG = "MainActivity";

	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

	public static final int REQUEST_CODE_GALLERY = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	public static final int REQUEST_CODE_CROP_IMAGE = 0x3;

	private ImageView mImageView;
	private File mFileTemp;
	Button bCamera,bGalerry,bSave,bUndo;
	Bitmap mutableBitmap,bitmap,croppedBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bGalerry = (Button) findViewById(R.id.bgallery);
		bCamera = (Button) findViewById(R.id.bCamera);
		bSave = (Button) findViewById(R.id.bSave);
		bUndo = (Button) findViewById(R.id.bUndo);
		
		mImageView = (ImageView) findViewById(R.id.imageView1);
        
    	String state = Environment.getExternalStorageState();
		
		bGalerry.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openGallery();
			}
		});
		
		bCamera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				takePicture();
			}
		});
		
		bSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String root = Environment.getExternalStorageDirectory().toString();
				File myDir = new File(root + "/DEV");    
				myDir.mkdirs();
				Random generator = new Random();
				int n = 10000;
				n = generator.nextInt(n);
				String fname = "Image-"+ n +".jpg";
				File file = new File (myDir, fname);
				if (file.exists ()) file.delete (); 
				try {
				       FileOutputStream out = new FileOutputStream(file);
				       mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				       out.flush();
				       out.close();
				       Toast.makeText(getApplicationContext(), "Image saved in 'root/DEV/' folder", Toast.LENGTH_LONG).show();

				} catch (Exception e) {
				       e.printStackTrace();
				       Toast.makeText(getApplicationContext(), "Failed to save image", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		bUndo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mImageView.setImageBitmap(getCroppedBitmap());
			}
		});
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {
    		mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
    	}
    	else {
    		mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
    	}
		
		mImageView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent arg1) {

				/*
				 * int[] viewCoords = new int[2];
				 * image.getLocationOnScreen(viewCoords); float touchXI =
				 * arg1.getRawX(); float touchYI = arg1.getRawY(); float imageX
				 * = touchXI - viewCoords[0]; // viewCoords[0] is the X
				 * coordinate float imageY = touchYI - viewCoords[1]; //
				 * viewCoords[1] is the y coordinate
				 * 
				 * touchX=touchX- image.getWidth() / 2; touchY=touchY-
				 * image.getHeight() / 2;
				 */
				bSave.setVisibility(View.VISIBLE);
				bUndo.setVisibility(View.VISIBLE);
				int toucha = (int) arg1.getRawX();
				int touchb = (int) arg1.getRawY();

				int x = (int) arg1.getX() - (50 / 2); 
				int y = (int) arg1.getY() - (50 / 2);

				// Toast.makeText(getApplicationContext(),toucha+" "+touchb,
				// Toast.LENGTH_LONG).show();
				//tvCoor.setText(toucha + " " + touchb);

				BitmapFactory.Options myOptions = new BitmapFactory.Options();
				myOptions.inDither = true;
				myOptions.inScaled = false;
				myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
				myOptions.inPurgeable = true;

				//Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher, myOptions);
				Bitmap bitmap = BitmapFactory.decodeFile(mFileTemp.getPath(), myOptions);
				//Bitmap circleBm =  BitmapFactory.decodeResource(getResources(),R.drawable.circleoutline);
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setColor(Color.RED);

				Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
				mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

				
				Canvas canvas = new Canvas(mutableBitmap);
				canvas.drawCircle(x, y, 50, paint);
				

				mImageView.setAdjustViewBounds(false);
				mImageView.setImageBitmap(mutableBitmap);

				/*
				 * Bitmap bitmap =
				 * BitmapFactory.decodeResource(getApplicationContext
				 * ().getResources(), R.drawable.ic_launcher); set other image
				 * top of the first icon Bitmap bitmapStar =
				 * BitmapFactory.decodeResource
				 * (getApplicationContext().getResources(),
				 * android.R.drawable.ic_menu_recent_history);
				 * 
				 * Bitmap bmOverlay =
				 * Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),
				 * Config.ARGB_8888);
				 * 
				 * Canvas canvas = new Canvas(bmOverlay); canvas.drawARGB(0x00,
				 * 0, 0, 0); canvas.drawBitmap(bitmap, 0, 0, null);
				 * canvas.drawBitmap(bitmapStar, 0, 0, null);
				 * 
				 * BitmapDrawable dr = new BitmapDrawable(bmOverlay);
				 * dr.setBounds(0, 0, dr.getIntrinsicWidth(),
				 * dr.getIntrinsicHeight());
				 * 
				 * image.setImageDrawable(dr);
				 */

				// //////////////////////////

				/*
				 * System.out.println("Touch recieved at "+arg1.getX() + " " +
				 * arg1.getY()); touchX = (arg1.getX()); touchY = (arg1.getY());
				 * 
				 * System.out.println("Touch recieved at "+touchX + " " +
				 * touchY);
				 */
				// image.setImageBitmap(createImage());

				return true;
			}
		});
	}
	
	private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
        	Uri mImageCaptureUri = null;
        	String state = Environment.getExternalStorageState();
        	if (Environment.MEDIA_MOUNTED.equals(state)) {
        		mImageCaptureUri = Uri.fromFile(mFileTemp);
        	}
        	else {
	        	/*
	        	 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
	        	 */
	        	mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
        	}	
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d(TAG, "cannot take picture", e);
        }
    }
	
	
	 private void openGallery() {

	        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
	        photoPickerIntent.setType("image/*");
	        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	    }
	 
	 private void startCropImage() {

	        Intent intent = new Intent(this, CropImage.class);
	        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
	        intent.putExtra(CropImage.SCALE, true);

	        intent.putExtra(CropImage.ASPECT_X, 3);
	        intent.putExtra(CropImage.ASPECT_Y, 2);

	        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	    }
	 
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	        if (resultCode != RESULT_OK) {

	            return;
	        }

	       

	        switch (requestCode) {

	            case REQUEST_CODE_GALLERY:

	                try {

	                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
	                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
	                    copyStream(inputStream, fileOutputStream);
	                    fileOutputStream.close();
	                    inputStream.close();

	                    startCropImage();

	                } catch (Exception e) {

	                    Log.e(TAG, "Error while creating temp file", e);
	                }

	                break;
	            case REQUEST_CODE_TAKE_PICTURE:

	                startCropImage();
	                break;
	            case REQUEST_CODE_CROP_IMAGE:

	                String path = data.getStringExtra(CropImage.IMAGE_PATH);
	                if (path == null) {

	                    return;
	                }

	                bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
	                setBitmap(bitmap);
	                mImageView.setImageBitmap(bitmap);
	                break;
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	 
	 public static void copyStream(InputStream input, OutputStream output)
	            throws IOException {

	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = input.read(buffer)) != -1) {
	            output.write(buffer, 0, bytesRead);
	        }
	    }
	
	 public void setBitmap(Bitmap bm){
		 croppedBitmap = bm;
	 }
	 public Bitmap getCroppedBitmap(){
		 return croppedBitmap;
	 }
}
