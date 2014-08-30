package arseniy.bardcode;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.ArrayList;
import java.util.List;

import arseniy.bardcode.util.SystemUiHider;

/**
* An example full-screen activity that shows and hides the system UI (i.e.
* status bar and navigation/system bar) with user interaction.
*
* @see SystemUiHider
*/
public class FullscreenActivity extends Activity {

    public SurfaceView surf;
    public SurfaceHolder surfHolder;
    public Camera cam;
    public HolderCallback holderCallback;

    final int CAMERA_ID = 0;
    final boolean FULL_SCREEN = true;

    TextView tvName;

    static {
        System.loadLibrary("iconv");
    }

    private ImageScanner scanner;
    private net.sourceforge.zbar.Image codeImage;
    private String lastScannedCode;


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });


        surf = (SurfaceView) findViewById(R.id.SurfaceViewPrev);
        surfHolder = surf.getHolder();
        surfHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
              //  Log.d("CameraTestActivity", "onPreviewFrame data length = " + (data != null ? data.length : 0));
                codeImage.setData(data);
                //Log.e("DATA RESOLUTION", "H: "+codeImage.getHeight());

                int result = scanner.scanImage(codeImage);
                if (result != 0) {
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        lastScannedCode = sym.getData();
                        if (lastScannedCode != null) {
                            tvName.setText(lastScannedCode);
                            // barcodeScanned = true;
                        }
                    }
                }
                cam.addCallbackBuffer(data);
            }
        };

        holderCallback = new HolderCallback(this, previewCb);
        surfHolder.addCallback(holderCallback);

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        tvName = (TextView) findViewById(R.id.tvName);
    }

    Spinner initSpinner(int spinnerId, List<String> data, String currentValue) {
        // настройка спиннера и адаптера для него
        Spinner spinner = (Spinner) findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // определеяем какое значение в списке является текущей настройкой
        for (int i = 0; i < data.size(); i++) {
            String item = data.get(i);
            if (item.equals(currentValue)) {
                spinner.setSelection(i);
            }
        }

        return spinner;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.restartCamera();
    }

    @Override
    protected void onStop(){
        super.onStop();
        this.stopCamera();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        this.restartCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.stopCamera();
    }

    //Освобождение камеры
    private void stopCamera(){
        if (cam != null)
        {
            cam.setPreviewCallback(null);
            cam.stopPreview();
            cam.release();
            cam = null;
        }
    }

    //Перезапуск камеры
    private void restartCamera(){
        int w=0,h=0;
        if(cam == null) {
            cam = Camera.open(CAMERA_ID);
            final Camera.Parameters params = cam.getParameters();
            final List<String> colorEffects = cam.getParameters()
                    .getSupportedColorEffects();
            Spinner spEffect = initSpinner(R.id.spEffect, colorEffects, cam
                    .getParameters().getColorEffect());
            // обработчик выбора
            spEffect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                    params.setColorEffect(colorEffects.get(arg2));
                    cam.setParameters(params);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            //область фокусировки
            if (params.getMaxNumMeteringAreas() > 0){ // check that metering areas are supported
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();

                Rect areaRect1 = new Rect(-100, -100, 100, 100);    // specify an area in center of image
                meteringAreas.add(new Camera.Area(areaRect1, 600)); // set weight to 60%
                Rect areaRect2 = new Rect(800, -1000, 1000, -800);  // specify an area in upper right of image
                meteringAreas.add(new Camera.Area(areaRect2, 400)); // set weight to 40%
                params.setMeteringAreas(meteringAreas);
            }


        }
        Camera.Parameters parameters = cam.getParameters();
            //максимальное разрешение
            List<Camera.Size> sl = parameters.getSupportedPictureSizes();
            //now that you have the list of supported sizes, pick one and set it back to the parameters...

            for(Camera.Size s : sl){

                if(h < s.height) {
                    w = s.width;
                    h = s.height;
                }
            }
            Log.e("RESOLUTION", "W: "+w+" H: "+h);
            parameters.setPictureSize(w, h);
            //parameters.setPreviewSize(w,h);
            //parameters.setPreviewFrameRate(5);

            int maxZoom = parameters.getMaxZoom();
            if (parameters.isZoomSupported()) {
                Log.e("ZOOM", "Z: "+maxZoom);
                parameters.setZoom(maxZoom);
            }

            cam.setParameters(parameters);

            cam.startPreview();
            setCameraDisplayOrientation(CAMERA_ID);
            setPreviewSize(FULL_SCREEN);

            Camera.Size size = parameters.getPreviewSize();
            w = size.width;
            h = size.height;
            Log.e("RESOLUTION PREVIEW", "W: "+size.width+" H: "+size.height);
            codeImage = new net.sourceforge.zbar.Image(w, h, "Y800");

    }

    void setPreviewSize(boolean fullScreen) {

        // получаем размеры экрана
        Display display = getWindowManager().getDefaultDisplay();
        boolean widthIsMax = display.getWidth() > display.getHeight();

        // определяем размеры превью камеры
        Camera.Size size = cam.getParameters().getPreviewSize();

        RectF rectDisplay = new RectF();
        RectF rectPreview = new RectF();

        // RectF экрана, соотвествует размерам экрана
        rectDisplay.set(0, 0, display.getWidth(), display.getHeight());

        // RectF первью
        if (widthIsMax) {
            // превью в горизонтальной ориентации
            rectPreview.set(0, 0, size.width, size.height);
        } else {
            // превью в вертикальной ориентации
            rectPreview.set(0, 0, size.height, size.width);
        }

        //rectPreview.set(0, 0, 250, 250);

        Matrix matrix = new Matrix();
        // подготовка матрицы преобразования
        if (!fullScreen) {
            // если превью будет "втиснут" в экран (второй вариант из урока)
            matrix.setRectToRect(rectPreview, rectDisplay,
                    Matrix.ScaleToFit.START);
        } else {
            // если экран будет "втиснут" в превью (третий вариант из урока)
            matrix.setRectToRect(rectDisplay, rectPreview,
                    Matrix.ScaleToFit.START);
            matrix.invert(matrix);
        }
        // преобразование
        matrix.mapRect(rectPreview);

        // установка размеров surface из получившегося преобразования
        surf.getLayoutParams().height = (int) (rectPreview.bottom);
        surf.getLayoutParams().width = (int) (rectPreview.right);
    }

    /**
     * ротация камеры
     * @param cameraId
     */
    void setCameraDisplayOrientation(int cameraId) {
        // определяем насколько повернут экран от нормального положения

        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Log.e("ORIENTATION", degrees + "");
        int result = 0;

        // получаем инфо по камере cameraId
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        // задняя камера
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            result = ((360 - degrees) + info.orientation);
        } else
            // передняя камера
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = ((360 - degrees) - info.orientation);
                result += 360;
            }
        result = result % 360;
        cam.setDisplayOrientation(result);
    }


}
