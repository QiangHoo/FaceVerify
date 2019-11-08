package com.arcsoft.arcfacedemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.loadtoast.LoadToastView;


public class VechileActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vechle);

        final TextView titleTv = findViewById(R.id.tv_safe);
        final LoadToastView tireIv = findViewById(R.id.tire_iv);
        final LoadToastView lightIv = findViewById(R.id.light_iv);
        final LoadToastView oilIv = findViewById(R.id.oir_iv);
        final LoadToastView seatIv = findViewById(R.id.seat_iv);
        final LoadToastView doorIv = findViewById(R.id.door_iv);

        tireIv.setText(getResources().getString(R.string.tire_text));
        tireIv.setTextColor(getResources().getColor(R.color.color_white));
        tireIv.setBackgroundColor(getResources().getColor(R.color.color_accent));
        lightIv.setText(getResources().getString(R.string.light_text));
        lightIv.setTextColor(getResources().getColor(R.color.color_white));
        lightIv.setBackgroundColor(getResources().getColor(R.color.color_accent));
        oilIv.setText(getResources().getString(R.string.oil_text));
        oilIv.setTextColor(getResources().getColor(R.color.color_white));
        oilIv.setBackgroundColor(getResources().getColor(R.color.color_accent));
        seatIv.setText(getResources().getString(R.string.seat_text));
        seatIv.setTextColor(getResources().getColor(R.color.color_white));
        seatIv.setBackgroundColor(getResources().getColor(R.color.color_accent));
        doorIv.setText(getResources().getString(R.string.door_text));
        doorIv.setTextColor(getResources().getColor(R.color.color_white));
        doorIv.setBackgroundColor(getResources().getColor(R.color.color_accent));

        final TextView tireTv = findViewById(R.id.tire_tv);
        final TextView lightTv = findViewById(R.id.light_tv);
        final TextView oilTv = findViewById(R.id.oir_tv);
        final TextView seatTv = findViewById(R.id.seat_tv);
        final TextView doorTv = findViewById(R.id.door_tv);

        final LinearLayout lightLayout = findViewById(R.id.light_ll);
        final LinearLayout oirLayout = findViewById(R.id.oir_ll);
        final LinearLayout seatLayout = findViewById(R.id.seat_ll);
        final LinearLayout doorLayout = findViewById(R.id.door_ll);

        tireIv.initSpinner(2);
        tireIv.setOnToastAnimationFinish(new LoadToastView.ToastAnimationFinish() {
            @Override
            public void onFinished() {
                tireTv.setVisibility(View.VISIBLE);
                lightLayout.setVisibility(View.VISIBLE);
                lightIv.initSpinner(1);
            }
        });

        lightIv.setOnToastAnimationFinish(new LoadToastView.ToastAnimationFinish() {
            @Override
            public void onFinished() {
                lightTv.setVisibility(View.VISIBLE);
                oirLayout.setVisibility(View.VISIBLE);
                oilIv.initSpinner(1);
            }
        });

        oilIv.setOnToastAnimationFinish(new LoadToastView.ToastAnimationFinish() {
            @Override
            public void onFinished() {
                oilTv.setVisibility(View.VISIBLE);
                seatLayout.setVisibility(View.VISIBLE);
                seatIv.initSpinner(1);
            }
        });

        seatIv.setOnToastAnimationFinish(new LoadToastView.ToastAnimationFinish() {
            @Override
            public void onFinished() {
                seatTv.setVisibility(View.VISIBLE);
                doorLayout.setVisibility(View.VISIBLE);
                doorIv.initSpinner(1);
            }
        });

        doorIv.setOnToastAnimationFinish(new LoadToastView.ToastAnimationFinish() {
            @Override
            public void onFinished() {
                doorTv.setVisibility(View.VISIBLE);
                titleTv.setText(getResources().getString(R.string.detection_pass));
                Toast.makeText(VechileActivity.this, getResources().getString(R.string.detection_pass),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private boolean checkLightIsOk() {
        //TODO 检测灯光
        return false;
    }

    private boolean checkOilIsOk() {
        //TODO Oil检测
        return false;
    }

    private boolean checkSeatIsOk() {
        //TODO Seat检测
        return false;
    }

    private boolean checkDoorIsClose() {
        //TODO Door检测
        return false;
    }

    private boolean checkTireIsOk() {
        return checkTirePressIsOk() && checkTireTempIsOk();
    }

    private boolean checkTirePressIsOk() {
        //TODO Tire 压力 检测
        return false;
    }

    private boolean checkTireTempIsOk() {
        //TODO Tire 温度 检测
        return false;
    }
}