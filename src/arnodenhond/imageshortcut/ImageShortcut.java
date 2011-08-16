package arnodenhond.imageshortcut;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class ImageShortcut extends Activity {

	// created when user chooses to add one of our shortcuts. starts the
	// imagepicker intent.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Toast.makeText(this, R.string.selectimage, Toast.LENGTH_SHORT).show();
		Intent pickimage = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(pickimage, Activity.RESULT_FIRST_USER);
	}

	// receives the selected image. creates shortcut and returns it.
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent pickedimage) {
		if (resultCode == Activity.RESULT_OK) {
			Uri data = pickedimage.getData();
			// even though the action_pick was started on the Images content uri,
			// the returned data could point to video in stead of an image!
			boolean isVideo = hasVideoSegment(data);
			String imageid = data.getLastPathSegment();

			// get title
			Cursor cursor = null;

			if (isVideo)
				cursor = managedQuery(
						android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
						new String[] {
								android.provider.MediaStore.Video.Media._ID,
								android.provider.MediaStore.Video.Media.DISPLAY_NAME },
						android.provider.MediaStore.Video.Media._ID + "=?",
						new String[] { imageid }, null);
			else
				cursor = managedQuery(
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new String[] {
								android.provider.MediaStore.Images.Media._ID,
								android.provider.MediaStore.Images.Media.DISPLAY_NAME },
						android.provider.MediaStore.Images.Media._ID + "=?",
						new String[] { imageid }, null);
			cursor.moveToFirst();
			String title = cursor.getString(1);
			cursor.close();

			Bitmap thumbnail = null;
			// get thumbnail
			if (isVideo)
				thumbnail = android.provider.MediaStore.Video.Thumbnails
						.getThumbnail(
								getContentResolver(),
								Integer.parseInt(imageid),
								android.provider.MediaStore.Video.Thumbnails.MICRO_KIND,
								null);
			else
				thumbnail = android.provider.MediaStore.Images.Thumbnails
						.getThumbnail(
								getContentResolver(),
								Integer.parseInt(imageid),
								android.provider.MediaStore.Images.Thumbnails.MICRO_KIND,
								null);

			// when the shortcut is clicked, view the selected image
			Intent dataview = new Intent();
			dataview.setData(pickedimage.getData());
			dataview.setAction(Intent.ACTION_VIEW);
			dataview.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

			// create shortcut and return it
			Intent result = new Intent();
			result.putExtra(Intent.EXTRA_SHORTCUT_INTENT, dataview);
			result.putExtra(Intent.EXTRA_SHORTCUT_ICON, thumbnail);
			result.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
			setResult(RESULT_OK, result);
			Toast.makeText(this, R.string.shortcutadded, Toast.LENGTH_SHORT)
					.show();
		} else {
			setResult(RESULT_CANCELED);
			Toast.makeText(this, R.string.shortcutcanceled, Toast.LENGTH_SHORT)
					.show();
		}
		finish();
	}

	// content://media/external/video/media/3
	// content://media/external/images/media/318
	//just to make it a bit more flexible but still safe, i hope..
	private boolean hasVideoSegment(Uri data) {
		for (String segment : data.getPathSegments()) {
			if (segment.equalsIgnoreCase("video")) {
				return true;
			}
		}
		return false;
		//TODO handle audio
	}

}