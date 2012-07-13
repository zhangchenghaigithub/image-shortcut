package arnodenhond.mediashortcut.shareshortcut;

import android.graphics.Bitmap;
import android.net.Uri;

public class ShareVideo extends ShareShortcut {
	@Override
	public Bitmap getThumbnail(Uri data) {
		try {
			return iconify(android.provider.MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), Integer.parseInt(data.getLastPathSegment()), android.provider.MediaStore.Video.Thumbnails.MICRO_KIND, null));
		} catch (Throwable t) {
			return null;
		}
	}

	@Override
	public String getId() {
		return android.provider.MediaStore.Video.Media._ID;
	}

	@Override
	public String getNameColumn() {
		return android.provider.MediaStore.Video.Media.DISPLAY_NAME;
	}

	@Override
	public String getDataColumn() {
		return android.provider.MediaStore.Video.Media.DATA;
	}

	@Override
	public String getType() {
		return "video/*";
	}
}
