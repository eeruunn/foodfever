package com.bigoval.foodfever;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AddressFragment extends Fragment implements AddressAdapter.OnItemClickListener {
    private Button addaddressbtn;
    private RecyclerView mRecyclerView;
    private AddressAdapter mAdapter;
    private ArrayList<AddressViewItem> mExampleList;
    private RequestQueue mRequestQueue;
    RequestQueue requestQueue;
    RequestQueue requestQueue2;
    RequestQueue rq;
    String regphone;
    String r;
    String deleteaddressurl = "http://dpend.pythonanywhere.com/accounts/deleteaddress/";
    String addressurl = "http://dpend.pythonanywhere.com/accounts/address/";
    LocationManager locationManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_addresses, container, false);
        Bundle lbundle = this.getArguments();
        String phone = lbundle.getString("customerphone");
        addaddressbtn = mview.findViewById(R.id.add_addressbtn);
        rq = Volley.newRequestQueue(getActivity());
        locationManager =(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE );
        if(isNetworkAvailable()){
                try {
                    regphone = phone;
                    String data = "{" +
                            "\"regphone\"" + ":" + "\"" + regphone + "\"" +
                            "}";
                    Submit(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        else {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ErrorFragment()).addToBackStack(null).commit();

        }

        addaddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Addaddresstypefragment()).addToBackStack(null).commit();
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                View view1 = getLayoutInflater().inflate(R.layout.addaddresstypedialog,null);
//                Button uclbtn = view1.findViewById(R.id.uclbtn);
//                Button malbtn =view1.findViewById(R.id.malbtn);
//                builder.setView(view1);
//                final AlertDialog dialog = builder.create();
//                dialog.show();
//                uclbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
//                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                                    ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                            ){//Can add more as per requirement
//
//                                ActivityCompat.requestPermissions(getActivity(),
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
//                                        123);
//                            }else {
//                                Intent intent = new Intent(getActivity(),Address_map_activity.class);
//                                startActivity(intent);
//                                dialog.dismiss();
//                            }
//
//
//                        }else {
//                            int a = askpermission();
//                            if (a==1){
//                                Intent intent = new Intent(getActivity(),Address_map_activity.class);
//                                startActivity(intent);
//                                dialog.dismiss();
//                            }
//                        }
//
//                    }
//                });
//                malbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddaddressFragment()).addToBackStack(null).commit();
//                        dialog.dismiss();
//                    }
//                });

            }
        });
        mRecyclerView = mview.findViewById(R.id.addressrecycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExampleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        return mview;
    }

    private void Submit(String data) {
        final String savedata = data;
        String URL = addressurl;

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final LoadingDialog ld = new LoadingDialog(getActivity());
        ld.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ld.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ld.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject data = jsonArray.getJSONObject(i);
                            System.out.println(data.toString());
                            AddressViewItem address = new AddressViewItem();
                            address.setAddress(data.getString("address1").toString());
                            address.setHousename(data.getString("housename").toString());
                            address.setLandmark(data.getString("landmark"));
                            address.setPhone(data.getString("mobilenumber"));
                            address.setname(data.getString("name"));
                            address.setPincode(data.getString("pincode"));
                            address.setTownname(data.getString("townname"));
                            address.setId(data.getString("id"));

                            mExampleList.add(address);


                        } catch (JSONException e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                        }
                    }
                    mAdapter = new AddressAdapter(getActivity().getApplicationContext(), mExampleList);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(AddressFragment.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ld.dismiss();
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

    public void removeItem(int position) {
        mExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    private void Submit2(String data) {
        final String savedata = data;
        String URL = deleteaddressurl;

        requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                    Toast.makeText(getActivity().getApplicationContext(), objres.toString(), Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    System.out.println(e);
//                    Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

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
        requestQueue2.add(stringRequest);
    }

    @Override
    public void onDeleteClick(int position) {
        AddressViewItem clickedItem = mExampleList.get(position);
        String data = "{" +
                "\"id\"" + ":" + "\"" + clickedItem.getId() + "\"" +
                "}";
        Submit2(data);
        removeItem(position);
        Toast.makeText(getActivity().getApplicationContext(), "address deleted", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onEditClick(int position) {
        Bundle bundle = new Bundle();
        AddressViewItem clickedItem = mExampleList.get(position);
        bundle.putString("name", clickedItem.getname());
        bundle.putString("address1", clickedItem.getAddress());
        bundle.putString("pincode", clickedItem.getPincode());
        bundle.putString("mobilenumber", clickedItem.getPhone());
        bundle.putString("landmark", clickedItem.getLandmark());
        bundle.putString("townname", clickedItem.getTownname());
        bundle.putString("housename", clickedItem.getHousename());
        bundle.putString("id", clickedItem.getId());
        EditaddressFragment fragment = new EditaddressFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){//Can add more as per requirement

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }
}
