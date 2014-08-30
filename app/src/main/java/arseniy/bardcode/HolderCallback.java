package arseniy.bardcode;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;

public class HolderCallback implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

    final int CAMERA_ID = 0;
    public FullscreenActivity activ;
    public Camera.PreviewCallback previewCallback;
    private byte[] cameraBuffer;

    public HolderCallback(FullscreenActivity activ, Camera.PreviewCallback previewCallback){
        super();
        this.activ = activ;
        this.previewCallback = previewCallback;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            activ.cam.setPreviewDisplay(holder);
            activ.cam.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        activ.cam.stopPreview();
        setCameraDisplayOrientation(CAMERA_ID);
        try {
            Camera.Parameters parameters = activ.cam.getParameters();
            Camera.Size previewSize = parameters.getPreviewSize();
            int imageFormat = parameters.getPreviewFormat();
            int bufferSize = previewSize.width * previewSize.height * ImageFormat.getBitsPerPixel(imageFormat) / 8;
            cameraBuffer = new byte[bufferSize];

            activ.cam.setPreviewDisplay(holder);
            activ.cam.setPreviewCallbackWithBuffer(null);
            activ.cam.setPreviewCallbackWithBuffer(previewCallback);
            activ.cam.addCallbackBuffer(cameraBuffer);
            activ.cam.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // holder.removeCallback(this);
        // activ.cam.stopPreview();
        // activ.cam.release();
    }

    /**
     * ротация камеры
     * @param cameraId
     */
    void setCameraDisplayOrientation(int cameraId) {
        // определяем насколько повернут экран от нормального положения

        int rotation = activ.getWindowManager().getDefaultDisplay().getRotation();
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
        Log.e("ORIENTATION", degrees+"");
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
        activ.cam.setDisplayOrientation(result);
    }


    @Override
    public void onAutoFocus(boolean b, Camera cam) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (activ.cam!=null && (Camera.Parameters.FOCUS_MODE_AUTO.equals(activ.cam.getParameters().getFocusMode()) ||
                        Camera.Parameters.FOCUS_MODE_MACRO.equals(activ.cam.getParameters().getFocusMode()))) {
                    activ.cam.autoFocus(HolderCallback.this);
                    Log.d("Avtofocus", "ok");
                }
            }
        }).start();
    }
}
