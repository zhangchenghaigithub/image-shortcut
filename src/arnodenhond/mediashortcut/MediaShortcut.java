package arnodenhond.mediashortcut;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import arnodenhond.imageshortcut.R;

public abstract class MediaShortcut extends Activity {

	abstract String getId();

	abstract String getName();

	abstract Bitmap getThumbnail(String id);
	
	abstract String getType();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
		pickIntent.setType(getType());

		if (getPackageManager().queryIntentActivities(pickIntent, 0).size() > 0) {
			startActivityForResult(pickIntent, Activity.RESULT_FIRST_USER);
			Toast.makeText(this, R.string.select, Toast.LENGTH_SHORT).show();
		} else {
			setResult(RESULT_CANCELED);
			Toast.makeText(this, R.string.cannotselect, Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent pickedMedia) {
		if (resultCode == Activity.RESULT_OK) {
			Uri data = pickedMedia.getData();
			String mediaId = data.getLastPathSegment();
			Uri contentUri = Uri.parse(data.toString().substring(0, data.toString().lastIndexOf('/')));
			
			Cursor cursor = managedQuery(contentUri, new String[] { getId(), getName() }, getId() + "=?", new String[] { mediaId }, null);
			cursor.moveToFirst();
			String title = cursor.getString(1);
			cursor.close();

			Intent dataview = new Intent();
			dataview.setData(data);
			dataview.setAction(Intent.ACTION_VIEW);
			dataview.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

			Intent result = new Intent();
			result.putExtra(Intent.EXTRA_SHORTCUT_INTENT, dataview);
			result.putExtra(Intent.EXTRA_SHORTCUT_ICON, getThumbnail(mediaId));
			result.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
			setResult(RESULT_OK, result);
			Toast.makeText(this, R.string.shortcutadded, Toast.LENGTH_SHORT).show();
		} else {
			setResult(RESULT_CANCELED);
			Toast.makeText(this, R.string.shortcutcanceled, Toast.LENGTH_SHORT).show();
		}
		finish();
	}

	protected Bitmap roundify(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

}
