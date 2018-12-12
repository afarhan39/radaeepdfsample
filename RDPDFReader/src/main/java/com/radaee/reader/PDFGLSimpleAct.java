package com.radaee.reader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.radaee.pdf.Document;
import com.radaee.pdf.Global;
import com.radaee.util.PDFAssetStream;

public class PDFGLSimpleAct extends Activity
{
    private Document m_doc = new Document();
    private PDFGLLayoutView m_view;
    private PDFAssetStream m_asset_stream = null;
    private String m_mode;
    private void onFail(String msg)//treat open failed.
    {
        m_doc.Close();
        m_doc = null;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }
    private final void ProcessOpenResult(int ret)
    {
        switch( ret )
        {
            case -1://need input password
                onFail(getString(R.string.failed_invalid_password));
                break;
            case -2://unknown encryption
                onFail(getString(R.string.failed_encryption));
                break;
            case -3://damaged or invalid format
                onFail(getString(R.string.failed_invalid_format));
                break;
            case -10://access denied or invalid file path
                onFail(getString(R.string.failed_invalid_path));
                break;
            case 0://succeeded, and continue
                m_view = new PDFGLLayoutView(this);
                m_view.PDFOpen(m_doc, null);
                if(m_mode != null && m_mode.equals("CURL"))
                    m_view.PDFSetView(2);
                setContentView(m_view);
                break;
            default://unknown error
                onFail(getString(R.string.failed_unknown));
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Global.Init( this );
        m_asset_stream = new PDFAssetStream();
        m_asset_stream.open(getAssets(), "test.PDF");
        m_doc = new Document();
        int ret = m_doc.OpenStream(m_asset_stream, null);
        m_mode = getIntent().getStringExtra("MODE");
        ProcessOpenResult(ret);
    }
    @Override
    protected void onDestroy()
    {
        m_view.PDFClose();
        super.onDestroy();
        if(m_doc != null) m_doc.Close();
        if(m_asset_stream != null) m_asset_stream.close();
        Global.RemoveTmp();
    }
}
