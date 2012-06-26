
package arnodenhond.mediashortcut;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import arnodenhond.imageshortcut.R;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.imageshortcut);
		TextView tv = (TextView) findViewById(R.id.textview);
		int version = 0;
		try {
			version = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException nnfe) {
		}
		tv.setText(String.format(getString(R.string.header), version));
	}

	public void startmarket(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setData(Uri.parse("market://search?q=pub:Arno den Hond"));
		startActivity(intent);
	}
	
}
