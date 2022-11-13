package com.example.flashlight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // Boolean, joka kertoo taskulampun tilan
    private boolean torchOn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void flashOnOff(View view) {
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.flashlightBackground);
        // Taskulamppu päälle/pois - yksinkertainen synkroninen kutsu
        // Background view id = 2131230820
        // Otetaan yhteys Camera Manageriin
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        // Käydään kaikki kamerat läpi ja luetaan niiden ominaisuudet
        try {
            for ( String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);
                // Etsitään kamera, jossa on flash
                if ( cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ) {
                    // Käynnistetään/pysäytetään taskulamppu
                    torchOn = !torchOn;
                    cameraManager.setTorchMode(id, torchOn);
                    if (!torchOn) {
                        System.out.println("Taskulamppu pois");
                        layout.setBackgroundColor(Color.BLACK);
                    } else {
                        System.out.println("Taskulamppu päällä");
                        layout.setBackgroundColor(Color.YELLOW);

                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void startSensor(View view) {
        // Käynnistetään kiihtyvyysanturin kuuntelu
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Listataan kaikki laitteella olevat sensorit
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for ( Sensor s : sensorList) {
            Toast.makeText(this, s.getName(), Toast.LENGTH_SHORT).show();
        }
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            // Löytyi kiihtyvyysanturi
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Accelerometer not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Sensorin muutokset tulevat tänne
        float xSensor = sensorEvent.values[0];
        float ySensor = sensorEvent.values[1];
        float zSensor = sensorEvent.values[2];

        // Kirjoitetaan sensorien arvot näytölle
        TextView sensorTextView = (TextView) findViewById(R.id.TVSensors);
        sensorTextView.setText("X: " + xSensor + " Y: " + ySensor + " Z: " + zSensor);
        if (xSensor > 0.05 && xSensor > -0.05) {
            // change background to red color
            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.flashlightBackground);
            layout.setBackgroundColor(Color.RED);


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Sensorin tarkkuusmuutokset tulevat tänne
    }
}

//View flash = findViewById(R.id.flash);
//        if (flash.getVisibility() == View.VISIBLE) {
//            flash.setVisibility(View.INVISIBLE);
//        } else {
//            flash.setVisibility(View.VISIBLE);
//        }