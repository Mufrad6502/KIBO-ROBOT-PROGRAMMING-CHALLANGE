package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.graphics.Bitmap;
import android.util.Log;


import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;


import java.util.ArrayList;
import java.util.List;





import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import static android.content.ContentValues.TAG;



public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        // astrobee is undocked and the mission starts
        api.startMission();

        int LOOP_COUNTER = 0;

        String pos_x = null;
        String pos_y = null;
        String pos_z = null;
        String qua_x = null;
        String qua_y = null;
        String qua_z = null;
        String qua_w = null;


        while (LOOP_COUNTER == 0) {
            moveToWrapper(11.21, -9.8, 4.79, 0, 0, -0.707, 0.707);
            moveToWrapper(11.21, -9.8, 4.79, 0, 0, -0.707, 0.707);
            Bitmap QR1 = api.getBitmapNavCam();

            try {
                pos_x = DECODE_QR(QR1);
                Log.i(TAG, "FINISH DECODE X'");
                pos_y = DECODE_QR(QR1);
                Log.i(TAG, "FINISH DECODE Y'");

                pos_z = DECODE_QR(QR1);
                Log.i(TAG, "FINISH DECODE Z'");

                qua_x = DECODE_QR(QR1);
                Log.i(TAG, "FINISH DECODE ox'");

                qua_y = DECODE_QR(QR1);
                Log.i(TAG, "FINISH DECODE oy'");

                qua_z = DECODE_QR(QR1);
                Log.i(TAG, "FINISH DECODE oz'");

                qua_w = DECODE_QR(QR1);
                Log.i(TAG, "FINISH DECODE ow'");


            } catch (Exception ignored) {

            }

            api.sendDiscoveredQR( pos_x);
            api.sendDiscoveredQR( pos_y);
            api.sendDiscoveredQR( pos_z);
            api.sendDiscoveredQR( qua_x);
            api.sendDiscoveredQR( qua_y);
            api.sendDiscoveredQR( qua_z);
            api.sendDiscoveredQR( qua_w);

            LOOP_COUNTER++;

        }

        api.laserControl(true);

        // take snapshots
        api.takeSnapshot();

        

        // Send mission completion
        api.reportMissionCompletion();

    }

    @Override
    protected void runPlan2(){
        // write here your plan 2
    }

    @Override
    protected void runPlan3(){
        // write here your plan 3
    }

    // You can add your method


    protected String DECODE_QR(Bitmap barcodeBmp) {


        int width = barcodeBmp.getWidth();
        int height = barcodeBmp.getHeight();
        int[] pixels = new int[width * height];
        barcodeBmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Image barcode = new Image(width, height, "RGB4");
        barcode.setData(pixels);
        ImageScanner reader = new ImageScanner();
        reader.setConfig(Symbol.NONE, Config.ENABLE, 0);
        reader.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
        int result = reader.scanImage(barcode.convert("Y800"));
        String value = null;


        if (result != 0) {

            SymbolSet syms = reader.getResults();
            for (Symbol sym : syms) {
                value = sym.getData();
            }
        } else {

            return null;

        }
        return value;

    }


    private void moveToWrapper(double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w){

        final int LOOP_MAX = 3;
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                                                     (float)qua_z, (float)qua_w);

        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        while(!result.hasSucceeded() || loopCounter < LOOP_MAX){
            result = api.moveTo(point, quaternion, true);
            ++loopCounter;
        }
    }

}

