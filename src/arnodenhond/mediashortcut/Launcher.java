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
		startActivity(new Intent(this, InfoActivity.class));
		finish();
	}

}
