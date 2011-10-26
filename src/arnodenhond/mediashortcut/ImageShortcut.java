package arnodenhond.mediashortcut;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageShortcut extends MediaShortcut {
	
	@Override
	Bitmap getThumbnail(String id) {
		return roundify(android.provider.MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), Integer.parseInt(id), android.provider.MediaStore.Images.Thumbnails.MICRO_KIND, null));
	}

	@Override
	Uri getContentUri() {
		return android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	}

	@Override
	String getId() {
		return android.provider.MediaStore.Images.Media._ID;
	}

	@Override
	String getName() {
		return android.provider.MediaStore.Images.Media.DISPLAY_NAME;
	}

}