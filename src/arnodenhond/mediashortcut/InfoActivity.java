package arnodenhond.mediashortcut;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;
import arnodenhond.imageshortcut.R;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		TextView tv = new TextView(this);
		int version = 0;
		try {
			version = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException nnfe) {
		}
		tv.setText(String.format(getString(R.string.info), version));
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(10, 10, 10, 10);
		Linkify.addLinks(tv, Linkify.WEB_URLS);
		setContentView(tv);
	}
}
