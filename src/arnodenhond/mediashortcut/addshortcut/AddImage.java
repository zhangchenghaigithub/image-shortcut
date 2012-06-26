
package arnodenhond.mediashortcut.addshortcut;

import android.graphics.Bitmap;
import android.net.Uri;

public class AddImage extends AddShortcut {

    @Override
    public Bitmap getThumbnail(Uri data) {
        try {
            return iconify(android.provider.MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), Integer.parseInt(data.getLastPathSegment()), android.provider.MediaStore.Images.Thumbnails.MICRO_KIND, null));
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public String getId() {
        return android.provider.MediaStore.Images.Media._ID;
    }

    @Override
    public String getName() {
        return android.provider.MediaStore.Images.Media.DISPLAY_NAME;
    }

    @Override
    String getType() {
        return "image/*";
    }

}