package mrz.android.manpages

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.os.ParcelFileDescriptor
import java.io.FileInputStream
import timber.log.Timber
import de.greenrobot.event.EventBus
import mrz.android.manpages.events.FileDownloadedEvent

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
                val columnStatus = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                val columnReason = c.getColumnIndex(DownloadManager.COLUMN_REASON)

                when (c.getInt(columnStatus)) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        val file: ParcelFileDescriptor = mDownloadManager.openDownloadedFile(
                                downloadId)
                        Timber.i("Downloaded ${file}")
                        EventBus.getDefault().post(FileDownloadedEvent(file))
                    }
                    DownloadManager.STATUS_FAILED -> Timber.e("Download ${downloadId} failed: ${c.getString(columnReason)}")

                    DownloadManager.STATUS_RUNNING -> Timber.i("Download ${downloadId} started")
                }
            }
        }
    }
}

