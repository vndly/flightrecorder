package com.mauriciotogneri.fcs.fragments;

import android.widget.TextView;

import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.database.RotationData;
import com.mauriciotogneri.fcs.sensors.RotationSensor.RotationListener;

public class RotationFragment extends BaseFragment implements RotationListener
{
    private TextView lastValueX;
    private TextView lastValueY;
    private TextView lastValueZ;

    @Override
    public void initialize()
    {
        lastValueX = (TextView) findViewById(R.id.last_rotation_x);
        lastValueY = (TextView) findViewById(R.id.last_rotation_y);
        lastValueZ = (TextView) findViewById(R.id.last_rotation_z);
    }

    @Override
    public int layout()
    {
        return R.layout.screen_rotation;
    }

    @Override
    public void onRotationData(RotationData data)
    {
        lastValueX.setText(String.valueOf(data.x()));
        lastValueY.setText(String.valueOf(data.y()));
        lastValueZ.setText(String.valueOf(data.z()));
    }
}