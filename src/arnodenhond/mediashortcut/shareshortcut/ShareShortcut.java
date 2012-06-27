package arnodenhond.mediashortcut.shareshortcut;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import arnodenhond.imageshortcut.R;
import arnodenhond.mediashortcut.MediaShortcut;

public abstract class ShareShortcut extends MediaShortcut {

	private static final String ACTION_CROP = "com.android.camera.action.CROP";
	private static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	private static final int REQUEST_CROP = 2;
	Uri data;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		data = (Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

		Intent cropIntent = new Intent(ACTION_CROP);
		cropIntent.setData(data);
		cropIntent.putExtra("crop", "true");
		cropIntent.putExtra("outputX", 100);
		cropIntent.putExtra("outputY", 100);
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		cropIntent.putExtra("scale", true);
		cropIntent.putExtra("return-data", true);

		if (!getPackageManager().queryIntentActivities(cropIntent, 0).isEmpty()) {
			startActivityForResult(cropIntent, REQUEST_CROP);
		} else {
			Toast.makeText(this, R.string.couldnotcrop, Toast.LENGTH_SHORT).show();
			onActivityResult(0, Activity.RESULT_OK, null);
			//continue as normal
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intentdata) {
		if (resultCode != Activity.RESULT_OK) {
			setResult(RESULT_CANCELED);
			Toast.makeText(this, R.string.shortcutcanceled, Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		final String defaultTitle = getTitleString(data);
		final Bitmap bitmap;
		if (requestCode == REQUEST_CROP) {
			bitmap = iconify((Bitmap) intentdata.getParcelableExtra("data"));
		} else {
			bitmap = getThumbnail(data);
		}
		if (bitmap == null) {
			setResult(RESULT_CANCELED);
			Toast.makeText(this, R.string.couldnotgetthumbnail, Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		final Intent dataview = getViewIntent(data);

		AlertDialog.Builder titleAlert = new AlertDialog.Builder(this);
		titleAlert.setTitle(R.string.settitle);
		final EditText title = getTitleEditText(defaultTitle);
		titleAlert.setView(title);
		titleAlert.setIcon(new BitmapDrawable(bitmap));
		titleAlert.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				sendBroadcast(getResultIntent(dataview, bitmap, title.getText().toString()));
				Toast.makeText(ShareShortcut.this, R.string.shortcutadded, Toast.LENGTH_SHORT).show();
				finish();
			}

			private Intent getResultIntent(Intent dataview, Bitmap bitmap, String title) {
				Intent result = new Intent();
				result.putExtra(Intent.EXTRA_SHORTCUT_INTENT, dataview);
				result.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
				result.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
				result.setAction(ACTION_INSTALL_SHORTCUT);
				return result;
			}
		});
		titleAlert.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				setResult(RESULT_CANCELED);
				Toast.makeText(ShareShortcut.this, R.string.shortcutcanceled, Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		titleAlert.show();
	}

}
