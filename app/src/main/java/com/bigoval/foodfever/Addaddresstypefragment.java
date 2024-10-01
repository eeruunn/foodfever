package com.bigoval.foodfever;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Addaddresstypefragment extends Fragment {
    Button uclbtn;
    Button mclbtn;
    LocationManager locationManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.addaddresstypefragment,null);
        uclbtn=mview.findViewById(R.id.uclbtn);
        locationManager =(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE );
        uclbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ){
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                                123);
                    }else {
                        Intent intent = new Intent(getActivity(),Address_map_activity.class);
                        startActivity(intent);
                    }
                }else {
                    int a = askpermission();
                    if (a==1){
                        Intent intent = new Intent(getActivity(),Address_map_activity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        return mview;
    }
    private int askpermission(){
        final int[] set = new int[1];
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                set[0] = 1;
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                set[0] = 0;
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(getActivity(), 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        return set[0];
    }
}
