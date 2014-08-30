package arseniy.bardcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import arseniy.bardcode.core.zbar.Result;

/**
 * Created by arseniy on 18/08/14.
 */
public class ZbarScan extends Activity implements ZbarCamView.ResultHandler {
    private ZbarCamView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mScannerView = new ZbarCamView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // код распознан
        //открываем следующий экран
        Intent intent = new Intent(this, ResultScan.class );
        intent.putExtra("cod", rawResult.getContents());
        intent.putExtra("format", rawResult.getBarcodeFormat().getName());
        startActivity(intent);
//        Toast.makeText(this, "Contents = " + rawResult.getContents() +
//                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
   //     mScannerView.startCamera();
    }
}
