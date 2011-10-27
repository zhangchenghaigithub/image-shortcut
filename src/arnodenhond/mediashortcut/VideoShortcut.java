package arnodenhond.mediashortcut;

import android.graphics.Bitmap;
import android.net.Uri;

public class VideoShortcut extends MediaShortcut {

	@Override
	Bitmap getThumbnail(String id) {
		return roundify(android.provider.MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), Integer.parseInt(id), android.provider.MediaStore.Video.Thumbnails.MICRO_KIND, null));
	}

	@Override
	String getId() {
		return android.provider.MediaStore.Video.Media._ID;
	}

	@Override
	String getName() {
		return android.provider.MediaStore.Video.Media.DISPLAY_NAME;
	}

	@Override
	String getType() {
		return "video/*";
	}

}
