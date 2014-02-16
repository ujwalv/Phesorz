package com.example.choppingimage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class InternalStorageContentProvider extends ContentProvider {
	
	public static final Uri CONTENT_URI = Uri.parse("content://eu.janmuller.android.simplecropimage.example/");
	private static final HashMap<String, String> MIME_TYPES = new HashMap<String, String>();
	
	static {
		MIME_TYPES.put(".jpg", "image/jpeg");
		MIME_TYPES.put(".jpeg", "image/jpeg");
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		String path = uri.toString();
		for (String extension : MIME_TYPES.keySet()) {
			if (path.endsWith(extension)) {
				return (MIME_TYPES.get(extension));
			}
		}
		return (null);
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		File f = new File(getContext().getFilesDir(), CameraActivity.TEMP_PHOTO_FILE_NAME);
		if (f.exists()) {
			return (ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_WRITE));
		}
		throw new FileNotFoundException(uri.getPath());
	}
	
	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		try {
			File mFile = new File(getContext().getFilesDir(), CameraActivity.TEMP_PHOTO_FILE_NAME);
			if(!mFile.exists()) {
				mFile.createNewFile();
				getContext().getContentResolver().notifyChange(CONTENT_URI, null);
			}
			return (true);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
