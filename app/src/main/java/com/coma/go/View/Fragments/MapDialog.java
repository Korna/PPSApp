package com.coma.go.View.Fragments;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coma.go.R;
import com.coma.go.Utils.ViewUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapDialog extends DialogFragment implements OnMapReadyCallback {
    public MapDialog() {}

    @BindView(R.id.map)
    MapView mMapView;

    private GoogleMap mMap;
    LatLng latLng = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            Double lat = this.getArguments().getDouble("lat");
            Double lon = this.getArguments().getDouble("lon");
            latLng = new LatLng(lat, lon);
        }catch (NullPointerException npe){
            ViewUtils.showToast(getActivity(), "No point");
        }

        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {

            if(checkPermission())
                mMap.setMyLocationEnabled(true);
            else
                askPermission();

        }catch (SecurityException se){
            Log.e("onMapReady", se.toString());
        }

            if(latLng != null){
                   mMap.addMarker(new MarkerOptions().position(latLng).title("The place"));
                CameraPosition cameraPosition;
                try {
                    cameraPosition = new CameraPosition.Builder().target(latLng).zoom(18).bearing(0).tilt(0).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }catch(NullPointerException e){
                    Log.e("Error", e.toString());
                }


            }

    }

    public static final int REQ_PERMISSION = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("onRequestPermissions", "onRequestPermissionsResult()");
        switch (requestCode) {
            case REQ_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (checkPermission())
                        mMap.setMyLocationEnabled(true);
                break;
            default:
                break;
        }
    }


    public boolean checkPermission() {
        Log.d("checkPermission", "checkPermission()");
        return ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ;
    }

    private void askPermission() {
        Log.d("askPermission", "askPermission()");
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
    }
}
