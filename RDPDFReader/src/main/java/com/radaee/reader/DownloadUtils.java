package com.radaee.reader;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadUtils {
    public static long downloadSamplePdf(DownloadManager downloadManager, File srcFile, String url) {
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationUri(Uri.fromFile(srcFile));
        request.setVisibleInDownloadsUi(true);
        request.setDescription("26 Nov 2018 edition");

        if (downloadManager != null) {
            return downloadManager.enqueue(request);
        }
        return -1;
    }

    public static boolean copyFile(File src, File dst) throws IOException {
        if(src.getAbsolutePath().equals(dst.getAbsolutePath())){

            return true;

        }else{
            InputStream is=new FileInputStream(src);
            OutputStream os=new FileOutputStream(dst);
            byte[] buff=new byte[1024];
            int len;
            while((len=is.read(buff))>0){
                os.write(buff,0,len);
            }
            is.close();
            os.close();
        }
        return true;
    }

    public static void deleteFile(Context context, File src) {
        if (src.exists()) {
            if (src.delete()) {
                Toast.makeText(context, "File deleted " + src.getPath(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "File not deleted " + src.getPath(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
