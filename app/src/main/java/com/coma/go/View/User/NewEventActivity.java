package com.coma.go.View.User;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coma.go.Entity.Event;
import com.coma.go.Misc.App;
import com.coma.go.R;
import com.coma.go.Service.FBIO;
import com.coma.go.Utils.ViewUtils;
import com.coma.go.View.LoadImageActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class NewEventActivity extends LoadImageActivity implements OnMapReadyCallback {

    @BindView(R.id.editText_name)
    EditText editTextName;
    @BindView(R.id.editText_description)
    EditText editTextDescription;
    @BindView(R.id.button_add_event)
    Button buttonAdd;
    @BindView(R.id.imageView_image)
    ImageView imageView_image;
    @BindView(R.id.spinner)
    Spinner spinner;

   // SupportMapFragment mapFragment;
    @BindView(R.id.map)
    MapView mMapView;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);
       // mapFragment = (SupportMapFragment) getSupportFragmentManager()
       //         .findFragmentById(R.id.map);

        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }



        buttonAdd.setOnClickListener(v -> {
            add();
        });
        imageView_image.setOnClickListener(view -> {
            chooseImage();
        });
    }

    @SuppressLint("CheckResult")
    private void add() {
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String category = spinner.getSelectedItem().toString();

        Double lat = null;
        Double lon = null;

        if(marker != null) {
            lat = marker.getPosition().latitude;
            lon = marker.getPosition().longitude;
        }
        App.getApp().getComponent().userApi()
                .createNote(name, description, category, image,
                        lat, lon)
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribe,
                        this::error);
    }

    private void subscribe(Response<Void> voidResponse) {
        if(voidResponse.isSuccessful()){
            ViewUtils.showToast(this, "Event was created!");
            editTextDescription.setText("");
            editTextName.setText("");
            this.finish();
        }else{
            ViewUtils.showToast(this, "Was not created.");
        }
    }
    private void error(Throwable throwable) {
        Log.d("sign", throwable.getMessage());
        ViewUtils.showToast(this, "Error!");
    }



    @Override
    protected void loadImage(Uri path){
        Glide.with(this)
                .asBitmap()
                .load(path)
                .apply(ViewUtils.getImageOptions())
                .transition(withCrossFade())
                .into(imageView_image);
    }

    @Override
    protected void loadImage(String path) {
        Glide.with(this)
                .asBitmap()
                .load(path)
                .apply(ViewUtils.getImageOptions())
                .transition(withCrossFade())
                .into(imageView_image);
    }

    Marker marker;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(point -> {
           if(marker != null)
               mMap.clear();
            marker = mMap.addMarker(new MarkerOptions().position(point).title("Your place"));
        });
        try {

            if(checkPermission())
                mMap.setMyLocationEnabled(true);
            else
                askPermission();

        }catch (SecurityException se){
            Log.e("onMapReady", se.toString());
        }
        try {
            LatLng sydney = null;
            LatLng sydney2 = null;

            try {
               // latLng = list.get(0);
               // sydney2 = list.get(1);
            }catch (IndexOutOfBoundsException iobe){
                iobe.printStackTrace();

            }

            if(sydney != null && sydney2 != null){
             //   mMap.addMarker(new MarkerOptions().position(latLng).title( listNames.get(0) ));
             //   mMap.addMarker(new MarkerOptions().position(sydney2).title( listNames.get(1) ));


                CameraPosition cameraPosition;
                try {
                    cameraPosition = new CameraPosition.Builder().target(sydney).zoom(18).bearing(0).tilt(0).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }catch(NullPointerException e){
                    Log.e("Error", e.toString());
                }


            }

        }catch (ArrayIndexOutOfBoundsException abe){
            Log.e("onMapReadyListLoad", abe.toString());
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
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ;
    }

    private void askPermission() {
        Log.d("askPermission", "askPermission()");
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
    }
}
