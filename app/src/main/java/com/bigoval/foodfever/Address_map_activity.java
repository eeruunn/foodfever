package com.bigoval.foodfever;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Address_map_activity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener,GoogleMap.OnMyLocationClickListener {
    TextView add;
    TextView addfromloc;
    LatLng latLng;
    View mapView;
    GoogleMap mMap;
    Location mLastKnownLocation;
    private final float DEFAULT_ZOOM = 15;
    Boolean buttonclicked = false;
    SupportMapFragment smf;
    LocationCallback locationCallback;
    LocationManager manager;
    FusedLocationProviderClient fusedLocationProviderClient;
    String r;
    String addaddressurl="http://dpend.pythonanywhere.com/accounts/addaddress/";
    String loginapiurl="http://dpend.pythonanywhere.com/accounts/Loginapi/";
    DatabaseHelper dbhelper;
    RequestQueue requestQueue;
    RequestQueue rq;
    Button saveaddress;
    EditText additionaladdressbox;
    String regphone;
    String customer;
    String latitude;
    String longitude;
    String addressfromlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_map_activity);
        smf=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);
        smf.getMapAsync(this);
        mapView=smf.getView();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Address_map_activity.this);
        addfromloc = findViewById(R.id.addressfromlocationtxt);
        additionaladdressbox = findViewById(R.id.addiaddressedittext);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        saveaddress=findViewById(R.id.saveaddress);
        rq= Volley.newRequestQueue(Address_map_activity.this);
        checkPermission();
        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            final LoadingDialog dialog = new LoadingDialog(Address_map_activity.this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    getDeviceLocation();

                }
            }, 10000);


        }else {
            askpermission();
        }
        dbhelper=new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String token[] = {"token"};
        Cursor c2 = db.query("tokens",token,null,null,null,null,null);
        c2.moveToFirst();
        try {
            r=c2.getString(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        parsejsonstring();

        saveaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address;
                try {
                    address = addressfromlocation +"  -  "+additionaladdressbox.getText().toString();
                } catch (Exception e) {
                    address = addressfromlocation;
                }

                String data = "{"+
                        "\"pincode\"" + ":" + "\"" + "" + "\","+
                        "\"mobilenumber\"" + ":" + "\"" + "" + "\","+
                        "\"address1\"" + ":" + "\"" + address  + "\","+
                        "\"landmark\"" + ":" + "\"" + "" + "\","+
                        "\"housename\"" + ":" + "\"" + ""+ "\","+
                        "\"townname\"" + ":" + "\"" + "" + "\","+
                        "\"customer\"" + ":" + "\"" + customer + "\","+
                        "\"regphone\"" + ":" + "\"" + regphone + "\","+
                        "\"longitude\"" + ":" + "\"" + longitude + "\","+
                        "\"latitude\"" + ":" + "\"" + latitude + "\","+
                        "\"name\"" + ":" + "\"" + "" + "\""+
                        "}";
                Submit(data);
            }
        });

    }


    private void askpermission(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        getDeviceLocation();
        SettingsClient settingsClient = LocationServices.getSettingsClient(Address_map_activity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(Address_map_activity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(Address_map_activity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(Address_map_activity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 480);
        }
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.8505, 76.2711), 5));


        //check if gps is enabled or not and then request user to enable it


    }
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                latLng = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("you are here");
                                mMap.clear();
//                                Toast.makeText(MainActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                                mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                Geocoder geocoder = new Geocoder(Address_map_activity.this,Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude(),1);
                                    latitude=""+mLastKnownLocation.getLatitude();
                                    longitude=""+mLastKnownLocation.getLongitude();
                                    String address = addresses.get(0).getAddressLine(0);
                                    addressfromlocation=address;
                                    addfromloc.setText(address);
                                } catch (IOException e) {
                                    addfromloc.setText("error");
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(Address_map_activity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(Address_map_activity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            System.out.println(task.getException().toString());

                        }
                    }
                });
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getDeviceLocation();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    private void parsejsonstring(){
        final String ACCESS_TOKEN="Token "+r;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginapiurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name=response.getString("name");
                    customer = response.getString("id");
                    regphone = response.getString("phone");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Address_map_activity.this,"error",Toast.LENGTH_LONG).show();

            }

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", ACCESS_TOKEN);
                return params;
            }
        };

        rq.add(jsonObjectRequest);
    }
    private void Submit(String data)
    {
        final String savedata= data;
        String URL=addaddressurl;

        requestQueue = Volley.newRequestQueue(Address_map_activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres=new JSONObject(response);
                    String responsecode = objres.getString("responsecode");
                    if(responsecode.equals("0")) {
                        Toast.makeText(Address_map_activity.this.getApplicationContext(), "Address added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(Address_map_activity.this.getApplicationContext(),"oops something went wrong",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(Address_map_activity.this,"Server Error",Toast.LENGTH_LONG).show();
                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //Log.v("Unsupported Encoding while trying to get the bytes", data);
                    return null;
                }
            }

        };
        requestQueue.add(stringRequest);
    }

}
