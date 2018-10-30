package com.frankchang.orientationsensor_demo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView tvShow;
    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnetic;
    private SensorListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvShow = findViewById(R.id.tvShow);

        // 建立傳感器管理員
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 加速度傳感器
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 磁場傳感器
        magnetic = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 建立傳感器監聽器
        if (accelerometer != null && magnetic != null) {
            listener = new SensorListener();

            manager.registerListener(listener, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);

            manager.registerListener(listener, magnetic,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 關閉傳感器監聽器
        manager.unregisterListener(listener);
    }


    // 傳感器監聽器
    private class SensorListener implements SensorEventListener {

        private float[] value01;
        private float[] value02;


        @Override
        public void onSensorChanged(SensorEvent event) {
            // 取得傳感器數值變量
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                value01 = event.values.clone();
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                value02 = event.values.clone();
            }

            // 利用加速器和磁場感應傳感器，整合出目前方位角度
            float[] rotations = new float[9];
            float[] orientation = new float[3];

            manager.getRotationMatrix(rotations, null, value01, value02);
            manager.getOrientation(rotations, orientation);

            // 計算角度
            double degree = Math.toDegrees(orientation[0]);
            tvShow.setText("方位角度：" + degree);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // 未使用
        }
    }

}
