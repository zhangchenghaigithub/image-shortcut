package arnodenhond.mediashortcut;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageShortcut extends MediaShortcut {

	@Override
	Bitmap getThumbnail(Uri data) {
		return iconify(android.provider.MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), Integer.parseInt(data.getLastPathSegment()), android.provider.MediaStore.Images.Thumbnails.MICRO_KIND, null));
	}

	@Override
	String getId() {
		return android.provider.MediaStore.Images.Media._ID;
	}

	@Override
	String getName() {
		return android.provider.MediaStore.Images.Media.DISPLAY_NAME;
	}

	@Override
	String getType() {
		return "image/*";
	}

}