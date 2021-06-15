package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.graphics.Bitmap;
import android.util.Log;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;



import org.json.JSONException;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;




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
        String pattern = null;

        while (LOOP_COUNTER == 0) {
            moveToWrapper(11.21, -9.8, 4.79, 0, 0, -0.707, 0.707);
            //moveToWrapper(11.30, -10.20, 4.95, 0, 0, -0.707, 0.707);
            Bitmap QR1 = api.getBitmapNavCam();

            try {
                pos_x = DECODE_QR(QR1);
                Log.i(TAG, "FINISH DECODE ");



            } catch (Exception ignored) {

            }

            while (pos_x == null) {

                moveToWrapper(11.21, -9.8, 4.79, 0, 0, -0.707, 0.707);
                try {
                    pos_x = DECODE_QR(QR1);
                    Log.i(TAG, "FINISH DECODE ");
                } catch (Exception ignored) {
                }

            }
            api.sendDiscoveredQR(pos_x);

            LOOP_COUNTER++;
        }

// --------------- Extracting Pattern Data from QR ---------------------------------
          String[] data = parseJSON(pos_x);

          String patt = data[0];



//       for pattern 3
         if (patt.equals("3")) {
           moveToWrapper(10.53,-9.90,5.49, 0, 0, -0.43, 0.90);
             api.laserControl(true);

             // take snapshots
             api.takeSnapshot();
             api.takeSnapshot();
        }
//        for pattern 4
           if (patt.equals("4")) {
               moveToWrapper(10.59,-9.90,5.49, 0, 0, -0.43, 0.90);
               api.laserControl(true);

               // take snapshots
               api.takeSnapshot();
               api.takeSnapshot();
            }
//        for pattern 2
      if (patt.equals("2")) {
           moveToWrapper(10.54, -9.90, 5.45, 0, 0, -0.43, 0.90);
          api.laserControl(true);

          // take snapshots
          api.takeSnapshot();
          api.takeSnapshot();
       }


//        for pattern 5
        if (patt.equals("5")) {
             moveToWrapper(10.5, -9.8, 4.79, 0, 0, -0.707, 0.707);
             moveToWrapper(10.54, -9.9, 5.43, 0, 0, -0.43, 0.90);
            api.laserControl(true);

            // take snapshots
            api.takeSnapshot();
            api.takeSnapshot();
        }

        // for pattern 6
        if (patt.equals("6")) {
             moveToWrapper(10.5, -9.8, 4.79, 0, 0, -0.707, 0.707);
             moveToWrapper(10.59, -9.9, 5.5, 0, 0, -0.43, 0.90);
            api.laserControl(true);

            // take snapshots
            api.takeSnapshot();
            api.takeSnapshot();
        }
        // for pattern 7
        if (patt.equals("7")) {
            moveToWrapper(11.5, -9.8, 5, 0, 0, -0.707, 0.707);
            moveToWrapper(11.44, -9.8, 5.47, 0, 0, -0.84, 0.54);
            api.laserControl(true);

            // take snapshots
            api.takeSnapshot();
            api.takeSnapshot();

            api.laserControl(false);
            moveToWrapper(11.5, -9.8, 4.80, 0, 0, -0.707, 0.707);
            moveToWrapper(10.5, -9.8, 4.80, 0, 0, -0.707, 0.707);

        }

//     for pattern 8
        if (patt.equals("8")) {
            moveToWrapper(11.5, -9.8, 5, 0, 0, -0.707, 0.707);
            moveToWrapper(11.46, -9.8, 5.44, 0, 0, -0.84, 0.54);
            api.laserControl(true);

            // take snapshots
            api.takeSnapshot();
            api.takeSnapshot();

            api.laserControl(false);
            moveToWrapper(11.5, -9.8, 4.80, 0, 0, -0.707, 0.707);
            moveToWrapper(10.5, -9.8, 4.80, 0, 0, -0.707, 0.707);
        }

        //     for pattern 1
        if (patt.equals("1")) {
            moveToWrapper(11.5, -9.8, 5, 0, 0, -0.707, 0.707);
            moveToWrapper(11.46, -9.8, 5.44, 0, 0, -0.84, 0.54);
            api.laserControl(true);

            // take snapshots
            api.takeSnapshot();
            api.takeSnapshot();

            api.laserControl(false);
            moveToWrapper(11.5, -9.8, 4.80, 0, 0, -0.707, 0.707);
            moveToWrapper(10.5, -9.8, 4.80, 0, 0, -0.707, 0.707);
        }



        // turn the laser off
           api.laserControl(false);

            // move to the rear of Bay7
            moveToWrapper(10.5, -9.8, 5.25, 0, 0, -0.707, 0.707);
            moveToWrapper(10.6, -8.0, 4.5, 0, 0, -0.707, 0.707);


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

    // -----------------------------------------You can add your method-------------------------------------------------------------------





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


    public String[] parseJSON(String qrData){
        String[] output = new String[qrData.length()];
        int i = 0;
        for (String keyValuePair:qrData.split(",")) {
            output[i++] = keyValuePair.split(":")[1];
        }
        return output;
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


