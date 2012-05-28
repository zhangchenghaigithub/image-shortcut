
package arnodenhond.mediashortcut;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;
import arnodenhond.imageshortcut.R;

public abstract class MediaShortcut extends Activity {

    abstract String getId();

    abstract String getName();

    abstract Bitmap getThumbnail(Uri data);

    abstract String getType();

    @Override
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent pickedMedia) {
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
                Toast.makeText(MediaShortcut.this, R.string.shortcutadded, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MediaShortcut.this, R.string.shortcutcanceled, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        titleAlert.show();
    }

    private EditText getTitleEditText(String defaultTitle) {
        EditText title = new EditText(this);
        title.setText(defaultTitle);
        title.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        title.selectAll();
        title.setGravity(Gravity.CENTER);
        return title;
    }

    private Intent getViewIntent(Uri data) {
        Intent viewData = new Intent(Intent.ACTION_VIEW, data);
        viewData.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        viewData.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        viewData.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return viewData;
    }

    private String getTitleString(Uri data) {
        try {
            Uri contentUri = Uri.parse(data.toString().substring(0, data.toString().lastIndexOf('/')));
            Cursor cursor = managedQuery(contentUri, new String[] { getId(), getName() }, getId() + "=?", new String[] { data.getLastPathSegment() }, null);
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

    protected Bitmap iconify(Bitmap bitmap) {
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
