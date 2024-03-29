package arnodenhond.mediashortcut;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import arnodenhond.imageshortcut.R;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.imageshortcut);
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.mediashortcut);
		int version = 0;
		try {
			version = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException nnfe) {
		}
		setTitle(String.format(getString(R.string.header), getString(R.string.mediashortcut), version));
		hideDisableLauncher();
	}

	private void hideDisableLauncher() {
		if (getPackageManager().getComponentEnabledSetting(new ComponentName(this, Launcher.class))==PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
			findViewById(R.id.infoheader).setVisibility(View.GONE);
			findViewById(R.id.disablelauncher).setVisibility(View.GONE);
		}
	}
	
	public void sendfeedback(View v) {
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "arnodenhond+imageshortcut@gmail.com" });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.mediashortcut)+" feedback");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Enter your feedback here..");
		startActivity(emailIntent);
	}

	public void postcomment(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setData(Uri.parse("market://details?id=arnodenhond.imageshortcut"));
		startActivity(intent);
	}

	public void shareurl(View v) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mediashortcut) + " http://play.google.com/store/apps/details?id=arnodenhond.imageshortcut");
		startActivity(intent);
	}

	public void disablelauncher(View v) {
		Intent result = new Intent();
		result.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this, InfoActivity.class));
		result.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.mediashortcut));
		result.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.mediashortcut));
		result.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		sendBroadcast(result);
		getPackageManager().setComponentEnabledSetting(new ComponentName(this, Launcher.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		Toast.makeText(this, R.string.launcherdisabled, Toast.LENGTH_SHORT).show();
		hideDisableLauncher();
	}

}
