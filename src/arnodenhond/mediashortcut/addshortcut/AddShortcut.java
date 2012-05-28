
package arnodenhond.mediashortcut.addshortcut;

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

public abstract class AddShortcut extends MediaShortcut {

    abstract String getType();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickIntent.setType(getType());

        if (!getPackageManager().queryIntentActivities(pickIntent, 0).isEmpty()) {
            startActivityForResult(pickIntent, Activity.RESULT_FIRST_USER);
            Toast.makeText(this, R.string.select, Toast.LENGTH_SHORT).show();
        } else {
            setResult(RESULT_CANCELED);
            Toast.makeText(this, R.string.cannotselect, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent pickedMedia) {
        if (resultCode != Activity.RESULT_OK) {
            setResult(RESULT_CANCELED);
            Toast.makeText(this, R.string.shortcutcanceled, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Uri data = pickedMedia.getData();

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
                setResult(RESULT_OK, getResultIntent(dataview, bitmap, title.getText().toString()));
                Toast.makeText(AddShortcut.this, R.string.shortcutadded, Toast.LENGTH_SHORT).show();
                finish();
            }

            private Intent getResultIntent(Intent dataview, Bitmap bitmap, String title) {
                Intent result = new Intent();
                result.putExtra(Intent.EXTRA_SHORTCUT_INTENT, dataview);
                result.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
                result.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
                return result;
            }
        });
        titleAlert.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                setResult(RESULT_CANCELED);
                Toast.makeText(AddShortcut.this, R.string.shortcutcanceled, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        titleAlert.show();
    }

}
