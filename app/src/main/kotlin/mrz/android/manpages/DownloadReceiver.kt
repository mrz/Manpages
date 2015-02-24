package mrz.android.manpages

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.os.ParcelFileDescriptor
import java.io.FileInputStream

public class DownloadReceiver(val context: Context, val downloadId: Long) : BroadcastReceiver() {
    val mDownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    override fun onReceive(context: Context, intent: Intent) {
        val action: String = intent.getAction();

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            val query = Query();

            query.setFilterById(downloadId);

            val c = mDownloadManager.query(query);
            if (c.moveToFirst()) {
                val columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                    // val uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    val file: ParcelFileDescriptor = mDownloadManager.openDownloadedFile(downloadId)
                    val fileInputStream: FileInputStream = ParcelFileDescriptor.AutoCloseInputStream(file)
                }
            }
        }
        throw UnsupportedOperationException()
    }
}

