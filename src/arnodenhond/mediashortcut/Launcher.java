package arnodenhond.mediashortcut;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;
import arnodenhond.imageshortcut.R;
import arnodenhond.mediashortcut.shareshortcut.ShareShortcut;

public class Launcher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent result = new Intent();
		result.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this, InfoActivity.class));
		result.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.mediashortcut));
		result.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.mediashortcut));
		result.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		sendBroadcast(result);

		startActivity(new Intent(this, InfoActivity.class));
		finish();
	}

}
