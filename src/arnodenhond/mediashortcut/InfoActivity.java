package arnodenhond.mediashortcut;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import arnodenhond.imageshortcut.R;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageshortcut);
		int version = 0;
		try {
			version = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException nnfe) {
		}
		setTitle(String.format(getString(R.string.header), getString(R.string.mediashortcut), version));
	}

	public void postcomment(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=arnodenhond.imageshortcut"));
		startActivity(intent);
	}

}
