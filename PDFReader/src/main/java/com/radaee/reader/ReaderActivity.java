package com.radaee.reader;


import android.app.Activity;
import android.os.Bundle;

import com.radaee.pdf.Document;
import com.radaee.pdf.Global;
import com.radaee.util.PDFAssetStream;

/**
 * Created with IntelliJ IDEA.
 * User: Erick
 * Date: 1/21/13
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReaderActivity extends Activity {

    private ReaderController m_vPDF = null;
    private Document doc = new Document();
    //stream from file
    //private PDFFileStream stream = new PDFFileStream();
    
    //stream from assets
    private PDFAssetStream stream = new PDFAssetStream();

    //stream from AES
    //private PDFAESStream stream = new PDFAESStream();
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //plz set this line to Activity in AndroidManifes.xml:
        //    android:configChanges="orientation|keyboardHidden|screenSize"
        //otherwise, APP shall destroy this Activity and re-create a new Activity when rotate. 
        Global.Init( this );
        
        
        m_vPDF = new ReaderController(this);
        doc.Close();

        //stream from file
        //String pathToPDF = "/sdcard/ebook/pdf.pdf";
        //if( pathToPDF != null )
        //{
        //	to open encrypted document, modify codes below
        //	stream.open(pathToPDF);

        //stream from Assets
    	stream.open(getAssets(), "test.PDF");

    	//stream from AES
        //byte[] key = "MySecretKeyABCDE".getBytes();
        //stream.open("/sdcard/ttt.pdf", key);
        //stream.ImportFromFile("/sdcard/test.pdf");
        //stream.ExportToFile("/sdcard/test1.pdf");
        //stream.close();
        //stream.open("/sdcard/EncryptedCatalog.pdf", key);
        //stream.ExportToFile("/sdcard/decrypt.pdf");
    	{
            int ret = doc.OpenStream(stream, null);
            //int ret = doc.Open("/sdcard/test1.pdf", null);
            switch( ret )
            {
                case -1://need input password
                    finish();
                    break;
                case -2://unknown encryption
                    finish();
                    break;
                case -3://damaged or invalid format
                    finish();
                    break;
                case -10://access denied or invalid file path
                    finish();
                    break;
                case 0://succeeded, and continue
                    break;
                default://unknown error
                    finish();
                    break;
            }

            m_vPDF.open(doc);
        }
        setContentView( m_vPDF );
        //m_vPDF = new PDFSimple(ReaderActivity.this);
    }


    public void onDestroy()
    {
        if( m_vPDF != null )
        {
            m_vPDF.close();
            m_vPDF = null;
        }
        if( doc != null )
        {
        	doc.Close();
        	doc = null;
        }
        if( stream != null )
        {
        	stream.close();
        	stream = null;
        }
        Global.RemoveTmp();
        super.onDestroy();
    }

}
