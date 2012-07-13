package arnodenhond.mediashortcut;

import java.net.URLDecoder;

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
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.EditText;
import arnodenhond.imageshortcut.R;

public abstract class MediaShortcut extends Activity {

	public abstract String getId();

	public abstract String getNameColumn();

	public abstract String getType();

	public abstract String getDataColumn();

	public abstract Bitmap getThumbnail(Uri data);

	public EditText getTitleEditText(String defaultTitle) {
		EditText title = new EditText(this);
		title.setText(defaultTitle);
		title.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		title.selectAll();
		title.setGravity(Gravity.CENTER);
		return title;
	}

	public Intent getViewIntent(Uri data) {
		Intent viewData = new Intent(Intent.ACTION_VIEW);
		viewData.setDataAndType(data, getType());
		viewData.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		viewData.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		viewData.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return viewData;
	}

	public String getTitleString(Uri data) {
		try {
			Uri contentUri = Uri.parse(data.toString().substring(0, data.toString().lastIndexOf('/')));
			Cursor cursor = managedQuery(contentUri, new String[] { getId(), getNameColumn() }, getId() + "=?", new String[] { data.getLastPathSegment() }, null);
			if (cursor.isAfterLast())
				return getString(R.string.settitle);
			cursor.moveToFirst();
			String defaultTitle = cursor.getString(1);
			cursor.close();
			return defaultTitle;
		} catch (Throwable t) {
			return data.toString();
		}
	}

	public Uri getDataString(Uri data) {
		try {
			Uri contentUri = Uri.parse(data.toString().substring(0, data.toString().lastIndexOf('/')));
			Cursor cursor = managedQuery(contentUri, new String[] { getId(), getDataColumn() }, getId() + "=?", new String[] { data.getLastPathSegment() }, null);
			if (cursor.isAfterLast())
				return data;
			cursor.moveToFirst();
			String defaultTitle = cursor.getString(1);
			defaultTitle = URLDecoder.decode(defaultTitle, "x-www-form-urlencoded");
			cursor.close();
			return Uri.parse("file://" + defaultTitle);
		} catch (Throwable t) {
			return data;
		}
	}

	public Bitmap iconify(Bitmap bitmap) {
		if (bitmap == null)
			return bitmap;
		bitmap = Bitmap.createScaledBitmap(bitmap, getIconSize(), getIconSize(), true);
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = getIconSize() / 5;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(0xff424242);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	private DisplayMetrics metrics;
	private static final int DENSITY_XHIGH = 320;

	private int getIconSize() {

		if (metrics == null) {
			metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
		}
		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			return 36;
		case DisplayMetrics.DENSITY_MEDIUM:
			return 48;
		case DisplayMetrics.DENSITY_HIGH:
			return 72;
		case DENSITY_XHIGH:
			return 96;
		}
		return 100;
	}

}
