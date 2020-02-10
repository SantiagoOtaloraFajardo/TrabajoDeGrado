package com.example.androidopencv;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    Mat srcDetectarColor, dstDetectarColor;
    Scalar verdeOscuro, rojoOscuro, verdeClaro, rojoClaro;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
        //Oscuro = componente bajo y Claro = componente alto
        verdeOscuro= new Scalar(25,51,25);
        verdeClaro= new Scalar(229,255,229);
        rojoOscuro= new Scalar(51,25,25);
        rojoClaro= new Scalar(255,229,229);
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);

                switch (status) {

                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }


            }
        };
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frameDetectarColor = inputFrame.rgba();
        return frameDetectarColor;
    }
    private Mat detectarColor(CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {
        Imgproc.cvtColor(inputFrame.rgba(), srcDetectarColor,Imgproc.COLOR_BGR2HSV);
        Core.inRange(srcDetectarColor,verdeOscuro,verdeClaro,dstDetectarColor);
        return dstDetectarColor;
    }
    @Override
    public void onCameraViewStarted(int width, int height) {
        srcDetectarColor = new Mat(width,height, CvType.CV_16UC4);
        dstDetectarColor = new Mat(width,height, CvType.CV_16UC4);
    }

    @Override
    public void onCameraViewStopped() {

    }
    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"There's a problem, yo!", Toast.LENGTH_SHORT).show();
        }

        else
        {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null){

            cameraBridgeViewBase.disableView();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }
}
