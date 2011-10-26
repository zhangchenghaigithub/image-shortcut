package arnodenhond.imageshortcut;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public abstract class MediaShortcut extends Activity {

	abstract Uri getContentUri();

	abstract String getId();

	abstract String getName();

	abstract Bitmap getThumbnail(int id);

	// pick message
	// done message
	// cancel message

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent pickIntent = new Intent(Intent.ACTION_PICK, getContentUri());
		startActivityForResult(pickIntent, Activity.RESULT_FIRST_USER);
		Toast.makeText(this, R.string.selectimage, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent pickedMedia) {
		if (resultCode == Activity.RESULT_OK) {
			Uri data = pickedMedia.getData();
			String mediaId = data.getLastPathSegment();

			Cursor cursor = managedQuery(getContentUri(), new String[] { getId(), getName() }, getId() + "=?", new String[] { mediaId }, null);
			cursor.moveToFirst();
			String title = cursor.getString(1);
			cursor.close();

			Bitmap thumbnail = getThumbnail(Integer.parseInt(mediaId));

			Intent dataview = new Intent();
			dataview.setData(pickedMedia.getData());
			dataview.setAction(Intent.ACTION_VIEW);
			dataview.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

			Intent result = new Intent();
			result.putExtra(Intent.EXTRA_SHORTCUT_INTENT, dataview);
			result.putExtra(Intent.EXTRA_SHORTCUT_ICON, thumbnail);
			result.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
			setResult(RESULT_OK, result);
			Toast.makeText(this, R.string.shortcutadded, Toast.LENGTH_SHORT).show();
		} else {
			setResult(RESULT_CANCELED);
			Toast.makeText(this, R.string.shortcutcanceled, Toast.LENGTH_SHORT).show();
		}
		finish();
	}

}
