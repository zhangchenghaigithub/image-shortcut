
package arnodenhond.mediashortcut.shareshortcut;

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

    private static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = (Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

        final String defaultTitle = getTitleString(data);
        final Bitmap bitmap = getThumbnail(data);
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
