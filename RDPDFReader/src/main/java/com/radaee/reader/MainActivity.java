package com.radaee.reader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.radaee.util.RadaeePDFManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {

	private Button m_btn_file;
    private Button m_btn_pager;
	private Button m_btn_asset;
	private Button m_btn_sdcard;
	private Button m_btn_dl;
	private Button m_btn_http;
    private Button m_btn_gl;
    private Button m_btn_glfile;
    private Button m_btn_565;
    private Button m_btn_4444;
    private Button m_btn_curl;
    private Button m_btn_adv;
    private Button m_btn_create;
    private Button m_btn_js;
    private Button m_btn_about;

    private RadaeePDFManager mPDFManager;

    private long downloadID;

    File srcFile;
    File destFile;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                try {
                    boolean isSuccess = DownloadUtils.copyFile(srcFile, destFile);
                    if (isSuccess) {
                        DownloadUtils.deleteFile(MainActivity.this, srcFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mPDFManager = new RadaeePDFManager();
        //plz set this line to Activity in AndroidManifes.xml:
        //    android:configChanges="orientation|keyboardHidden|screenSize"
        //otherwise, APP shall destroy this Activity and re-create a new Activity when rotate. 
		RelativeLayout layout = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.activity_main, null);
		m_btn_file = (Button)layout.findViewById(R.id.btn_file);
        m_btn_glfile = (Button)layout.findViewById(R.id.btn_glfile);
        m_btn_pager = (Button)layout.findViewById(R.id.btn_pager);
		m_btn_asset = (Button)layout.findViewById(R.id.btn_asset);
		m_btn_sdcard = (Button)layout.findViewById(R.id.btn_sdcard);
		m_btn_dl = (Button)layout.findViewById(R.id.btn_dl);
		m_btn_http = (Button)layout.findViewById(R.id.btn_http);
        m_btn_gl = (Button)layout.findViewById(R.id.btn_gl);
        m_btn_565 = (Button)layout.findViewById(R.id.btn_565);
        m_btn_4444 = (Button)layout.findViewById(R.id.btn_4444);
        m_btn_curl = (Button)layout.findViewById(R.id.btn_curl);
        m_btn_adv = (Button)layout.findViewById(R.id.btn_advance);
        m_btn_create = (Button)layout.findViewById(R.id.btn_create);
        m_btn_js = (Button)layout.findViewById(R.id.btn_js);
        m_btn_about = (Button)layout.findViewById(R.id.btn_about);
		m_btn_file.setOnClickListener(this);
        m_btn_glfile.setOnClickListener(this);
        m_btn_pager.setOnClickListener(this);
		m_btn_asset.setOnClickListener(this);
		m_btn_sdcard.setOnClickListener(this);
		m_btn_dl.setOnClickListener(this);
		m_btn_http.setOnClickListener(this);
        m_btn_gl.setOnClickListener(this);
        m_btn_565.setOnClickListener(this);
        m_btn_4444.setOnClickListener(this);
        m_btn_curl.setOnClickListener(this);
        m_btn_adv.setOnClickListener(this);
        m_btn_create.setOnClickListener(this);
        m_btn_js.setOnClickListener(this);
        m_btn_about.setOnClickListener(this);
		setContentView(layout);
        verifyPermissions(this);
	}

    private void verifyPermissions(Activity act) {
        // Check if we have necessary permissions
        int storagePermissionWrite = ActivityCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storagePermissionRead = ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE);

        ArrayList<String> permissions = new ArrayList<>();

        if (storagePermissionWrite != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        if (storagePermissionRead != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permissions.size() > 0) {
            String[] permissionArray = new String[permissions.size()];
            permissions.toArray(permissionArray);

            ActivityCompat.requestPermissions(act, permissionArray, 1);
        }
    }

    @SuppressLint("InlinedApi")
	@Override
    protected void onDestroy() {
    	com.radaee.pdf.Global.RemoveTmp();
    	super.onDestroy();
    }

	@Override
	public void onClick(View arg0) {
		if( arg0 == m_btn_file ) {
			Intent intent = new Intent(this, com.radaee.reader.PDFNavAct.class);
			startActivity(intent);
		}
        else if( arg0 == m_btn_pager ) {
            Intent intent = new Intent(this, com.radaee.reader.PDFPagerAct.class);
            intent.putExtra( "PDFAsset", "test.PDF" );
            intent.putExtra( "PDFPswd", "" );//password
            startActivity(intent);
        }
		else if( arg0 == m_btn_asset ) {
            mPDFManager.openFromAssets(this, "news.pdf", "");
		}
		else if( arg0 == m_btn_sdcard ) {
			String pdf_path = "/sdcard/test.PDF";
			File file = new File(pdf_path);
			if( file.exists() ) {
                mPDFManager.show(this, pdf_path, "");
			}
			else {
                Toast.makeText(this, getString(R.string.file_not_exist) + pdf_path, Toast.LENGTH_SHORT).show();
			}
		}
        else if( arg0 == m_btn_dl ) {

            File srcFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "thestar.pdf");
            File destFile = new File(getFilesDir(), "thestar.pdf");

            //pre download
//            final DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//            final long downloadId = DownloadUtils.downloadSamplePdf(downloadManager, srcFile, "http://www.radaeepdf.com/documentation/MRBrochoure.pdf");
//            onRegisterDownload(downloadId, srcFile, destFile);
//            Toast.makeText(this, "download", Toast.LENGTH_SHORT).show();

            //post download
            if (destFile.exists())
                mPDFManager.show(this, destFile.getAbsolutePath(), "");
            else
                Toast.makeText(this, "File x wujud lagi. Use pre download.", Toast.LENGTH_SHORT).show();
        }
		else if( arg0 == m_btn_http ) {
			String http_link = "http://www.radaeepdf.com/documentation/MRBrochoure.pdf";
            //String http_link = "http://www.radaeepdf.com/documentation/oversize_pdf_test_0_opt.pdf";
            //String http_link = "http://www.anydomain.com/someWorkingPDFURL.pdf";
            mPDFManager.show(this, http_link, "");
		}
        else if( arg0 == m_btn_gl ) {
            Intent intent = new Intent(this, PDFGLSimpleAct.class);
            startActivity(intent);
        }
		else if( arg0 == m_btn_glfile ) {
            Intent intent = new Intent(this, com.radaee.reader.PDFNavAct.class);
            intent.putExtra("ENGINE", "OPENGL");
            startActivity(intent);
        }
        else if( arg0 == m_btn_565 ) {
            mPDFManager.openFromAssets(this, "test.PDF", "", "RGB_565");
        }
        else if( arg0 == m_btn_4444 ) {
            mPDFManager.openFromAssets(this, "test.PDF", "", "ARGB_4444");
        }
        else if( arg0 == m_btn_curl) {
            Intent intent = new Intent(this, com.radaee.reader.PDFGLSimpleAct.class);
            intent.putExtra("MODE", "CURL");
            startActivity(intent);
        }
        else if( arg0 == m_btn_adv) {
            Intent intent = new Intent(this, AdvanceAct.class);
            startActivity(intent);
        }
        else if( arg0 == m_btn_create ) {
            Intent intent = new Intent(this, PDFTestAct.class);
            startActivity(intent);
        }
        else if( arg0 == m_btn_js) {
            Intent intent = new Intent(this, PDFJSTestAct.class);
            startActivity(intent);
        }
        else if( arg0 == m_btn_about ) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
	}

    public void onRegisterDownload(final long downloadID, final File srcFile, final File destFile) {
        this.downloadID = downloadID;
        this.srcFile = srcFile;
        this.destFile = destFile;
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}