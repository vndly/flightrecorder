package com.mauriciotogneri.fcs.ground;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.adapters.ViewPagerAdapter;
import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.model.BarometerData;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.network.FirebaseNetworkGround;
import com.mauriciotogneri.fcs.network.FirebaseNetworkGround.SessionListener;
import com.mauriciotogneri.fcs.satellite.fragments.AccelerometerFragment;
import com.mauriciotogneri.fcs.satellite.fragments.BarometerFragment;
import com.mauriciotogneri.fcs.satellite.fragments.LocationFragment;
import com.mauriciotogneri.fcs.satellite.fragments.RotationFragment;
import com.mauriciotogneri.fcs.satellite.fragments.SessionFragment;
import com.mauriciotogneri.fcs.satellite.sensors.SensorListener;

public class GroundActivity extends AppCompatActivity implements SensorListener, SessionListener
{
    private SessionFragment sessionFragment;
    private AccelerometerFragment accelerometerFragment;
    private RotationFragment rotationFragment;
    private BarometerFragment barometerFragment;
    private LocationFragment locationFragment;
    private FirebaseNetworkGround firebaseNetwork;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ground_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        progressDialog = progressDialog();

        setupNetwork();
        setupFragments();
    }

    private ProgressDialog progressDialog()
    {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.screen_main_session_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        return progressDialog;
    }

    private void setupNetwork()
    {
        this.firebaseNetwork = new FirebaseNetworkGround(this, this);
    }

    private void setupFragments()
    {
        this.sessionFragment = SessionFragment.newInstance("");
        this.accelerometerFragment = new AccelerometerFragment();
        this.rotationFragment = new RotationFragment();
        this.barometerFragment = new BarometerFragment();
        this.locationFragment = new LocationFragment();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(sessionFragment, getString(R.string.screen_satellite_session));
        adapter.addFragment(accelerometerFragment, getString(R.string.screen_satellite_accelerometer));
        adapter.addFragment(rotationFragment, getString(R.string.screen_satellite_rotation));
        adapter.addFragment(barometerFragment, getString(R.string.screen_satellite_barometer));
        adapter.addFragment(locationFragment, getString(R.string.screen_satellite_location));

        viewPager.setOffscreenPageLimit(5);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        accelerometerFragment.onAccelerometerData(data);
    }

    @Override
    public void onRotationData(RotationData data)
    {
        rotationFragment.onRotationData(data);
        locationFragment.onRotationData(data);
    }

    @Override
    public void onBarometerData(BarometerData data)
    {
        barometerFragment.onBarometerData(data);
    }

    @Override
    public void onLocationData(LocationData data)
    {
        locationFragment.onLocationData(data);
    }

    @Override
    public void onSessionStarted(String id)
    {
        progressDialog.dismiss();
        sessionFragment.start(id);
    }

    @Override
    protected void onDestroy()
    {
        firebaseNetwork.stop();

        super.onDestroy();
    }
}