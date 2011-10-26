package arnodenhond.imageshortcut;

import android.graphics.Bitmap;
import android.net.Uri;

public class VideoShortcut extends MediaShortcut {

	@Override
	Bitmap getThumbnail(int id) {
		Bitmap thumbnail = android.provider.MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), id, android.provider.MediaStore.Video.Thumbnails.MICRO_KIND, null);
		// TODO rounded corners
		return thumbnail;
	}

	@Override
	Uri getContentUri() {
		return android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	}

	@Override
	String getId() {
		return android.provider.MediaStore.Video.Media._ID;
	}

	@Override
	String getName() {
		return android.provider.MediaStore.Video.Media.DISPLAY_NAME;
	}

}
