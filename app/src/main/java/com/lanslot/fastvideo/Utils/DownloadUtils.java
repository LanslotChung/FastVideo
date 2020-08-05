package com.lanslot.fastvideo.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.lanslot.fastvideo.MyApplication;

import java.io.File;
import java.util.Date;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadUtils {
    public static void downloadApk(Context context,String apkUrl) {
        Uri uri = Uri.parse(apkUrl);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(request.NETWORK_MOBILE | request.NETWORK_WIFI);
        //设置是否允许漫游
        request.setAllowedOverRoaming(true);
        //设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(apkUrl));
        request.setMimeType(mimeString);
        //在通知栏中显示
        request.setNotificationVisibility(request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("正在下载");
        request.setVisibleInDownloadsUi(true);
        //sdcard目录下的download文件夹
        String apkName = "fastvideo-"+new Date().getTime() + ".apk";
        MyApplication.setApkName(apkName);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, apkName);
        // 将下载请求放入队列
        downloadManager.enqueue(request);

        Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
    }
}
