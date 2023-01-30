package com.mine.common.tools

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mine.common.BuildConfig
import java.io.File
import java.lang.ref.WeakReference
import java.net.URI
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

/**
 * DownLoadManager
 */
object LocalDownloadManager {

    private const val HTTP = "http"
    private const val HTTPS = "https"
    private const val LOADING_TEXT = "正在下载"
    private const val TEMP_TEXT = "temp"
    private const val NET_SEPARATOR = "/"
    private const val FILE_PROVIDER_AUTHORITIES = "com.min.frame.fileProvider"
    private const val APK_MIME_TYPE = "application/vnd.android.package-archive"
    private val runningTask: MutableMap<String, Long> = mutableMapOf()

    private val bindReceivers: ConcurrentHashMap<WeakReference<AppCompatActivity>, DownLoadReceiver> =
        ConcurrentHashMap()

    private var downloadId = 0L

    private var downloadManager: DownloadManager? = null

    private var installPermissionLauncher: ActivityResultLauncher<String>? = null

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    private var currentUri: Uri? = null

    /**
     * 初始化（需要在onResume之前调用）
     */
    fun init(activity: AppCompatActivity, autoBindReceiver: Boolean = true) {
        activity.lifecycleScope.launchWhenCreated {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                installPermissionLauncher = activity.registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) {
                    if (it) {
                        goInstallAct(activity, currentUri)
                    } else {
                        with(Intent()) {
                            this.data =
                                Uri.parse("package:${activity.packageName}")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                this.action =
                                    Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
                            } else {
                                this.action =
                                    Settings.ACTION_SECURITY_SETTINGS
                            }
                            activityResultLauncher?.launch(this)
                        }
                    }
                }
                activityResultLauncher =
                    activity.registerForActivityResult(
                        ActivityResultContracts.StartActivityForResult()
                    ) {
                        if (activity.packageManager.canRequestPackageInstalls()) {
                            goInstallAct(activity, currentUri)
                        } else {
                            //文件已下载 未授予未知来源应用安装权限
                            printLog("文件已下载,未授予未知来源应用安装权限")
                        }
                    }
            }
        }
        autoBindReceiver.takeIf { it }?.also {
            bindDownloadReceiver(activity)
            activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    unbindDownloadReceiver(activity)
                    printLog("onDestroy - current state = ${activity.lifecycle.currentState.name}")
                }
            })
        }
    }

    fun downLoad(context: Context, url: String, mimeType: String = APK_MIME_TYPE) {
        currentUri = null
        downloadManager
            ?: (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).also {
                downloadManager = it
            }
        val netUrl = Uri.parse(url)
        if (!netUrl.scheme.equals(HTTP, true) && !netUrl.scheme.equals(HTTPS, true)) {
            return
        }
        val request = DownloadManager.Request(Uri.parse(url))
            //设置允许使用的网络类型
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            //设置允许漫游
            .setAllowedOverRoaming(true)
            //设置文件格式
            .setMimeType(mimeType)
            //设置通知显示的时机
            // -下载中展示 VISIBILITY_VISIBLE(默认)
            // -下载中和完成时展示 VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            // -下载完成时展示 VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION
            // -不展示 VISIBILITY_HIDDEN
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            //设置通知的标题
            .setTitle(getFileName(url))
            //设置通知的描述信息
            .setDescription(LOADING_TEXT).also {
                //设置下载地址
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.mkdirs()
                it.setDestinationInExternalFilesDir(
                    context,
                    Environment.DIRECTORY_DOWNLOADS,
                    getFileName(url)
                )
            }
        downloadId = downloadManager?.enqueue(request)!!
        runningTask[url] = downloadId
    }

    fun bindDownloadReceiver(context: AppCompatActivity) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        intentFilter.addAction(DownloadManager.ACTION_VIEW_DOWNLOADS)
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        val downLoadReceiver = DownLoadReceiver()
        context.registerReceiver(downLoadReceiver, intentFilter)
        bindReceivers[WeakReference(context)] = downLoadReceiver
    }

    fun unbindDownloadReceiver(context: Context) {
        for (entry in bindReceivers.entries) {
            entry.key.also {
                if (it.get() == context) {
                    context.unregisterReceiver(entry.value)
                    bindReceivers.remove(entry.key)
                    return
                }
            }
        }
    }

    private fun getFileName(url: String): String {
        val start = url.lastIndexOf(NET_SEPARATOR)
        val end = url.length
        return if (start != -1 && start != end) {
            url.substring(start + 1, url.length)
        } else {
            TEMP_TEXT
        }
    }

    /**
     * 下载状态广播
     */
    class DownLoadReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkDownloadStatus(context, intent)
        }

    }

    /**
     * 安装页面
     */
    fun goInstallAct(context: Context, uri: Uri?) {
        if (uri == null) return
        with(Intent()) {
            this.action = Intent.ACTION_VIEW
            this.setDataAndType(uri, APK_MIME_TYPE)
            this.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
    }

    private fun checkDownloadStatus(context: Context?, intent: Intent?) {
        when (intent?.action) {
            DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                printLog("ACTION_DOWNLOAD_COMPLETE")
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
                val query = DownloadManager.Query()
                query.setFilterById(downloadId) //筛选下载任务，传入任务ID，可变参数
                downloadManager?.query(query).also { cursor ->
                    if (cursor?.moveToFirst() == true) {
                        val index = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        if (index < 0 || context == null)
                            return
                        when (cursor.getInt(index)) {
                            DownloadManager.STATUS_PAUSED -> {
                                printLog("下载暂停")
                            }
                            DownloadManager.STATUS_PENDING -> {
                                printLog("下载延迟")
                            }
                            DownloadManager.STATUS_RUNNING -> printLog("正在下载")
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                val mimeType =
                                    downloadManager?.getMimeTypeForDownloadedFile(downloadId)
                                for (entry in runningTask) {
                                    if (downloadId == entry.value) {
                                        runningTask.remove(entry.key)
                                    }
                                    break
                                }
                                val localUrlIndex =
                                    cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                                val localUrl =
                                    if (localUrlIndex >= 0) cursor.getString(localUrlIndex) else null
                                if (mimeType == APK_MIME_TYPE) {
                                    localUrl?.also {
                                        val file = File(URI.create(localUrl))
                                        if (!file.exists()) return
                                        val url = FileProvider.getUriForFile(
                                            context,
                                            FILE_PROVIDER_AUTHORITIES,
                                            file
                                        ).also {
                                            currentUri = it
                                        }
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            if (context.packageManager.canRequestPackageInstalls()) {
                                                goInstallAct(context, url)
                                            } else {
                                                installPermissionLauncher?.launch(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                                            }
                                        } else {
                                            goInstallAct(context, url)
                                        }

                                    }
                                }
                                printLog("下载完成,文件 uri = $localUrl 文件类型 = $mimeType")
                            }
                            DownloadManager.STATUS_FAILED -> {
                                for (entry in runningTask) {
                                    if (downloadId == entry.value) {
                                        runningTask.remove(entry.key)
                                        downloadManager?.remove(downloadId)
                                    }
                                    break
                                }
                                printLog("下载失败")
                            }
                        }
                    }
                }
            }
            DownloadManager.ACTION_VIEW_DOWNLOADS -> {
                printLog("ACTION_VIEW_DOWNLOADS")

            }
            DownloadManager.ACTION_NOTIFICATION_CLICKED -> {
                printLog("ACTION_NOTIFICATION_CLICKED")
            }
        }
    }

    private fun printLog(msg: String) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(this@LocalDownloadManager::class.java.name, msg)
        }
    }
}