package com.bigoval.foodfever;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ProfileFragment extends Fragment {
    TextView nametext;
    TextView phonetext;
    Button changepasswordbtn;
    Button addressbtn;
    Button ordersbtn;
    Button cartbtn;
    Button testbtn;
    String phone;
    ImageView editimg;
    RequestQueue requestQueue;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mview= inflater.inflate(R.layout.fragment_profile, container, false);
        if (!isNetworkAvailable()) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ErrorFragment()).addToBackStack(null).commit();
        }
        nametext = mview.findViewById(R.id.nametext);
        phonetext = mview.findViewById(R.id.phonetext);
        changepasswordbtn=mview.findViewById(R.id.profilechangepasswordbtn);
        addressbtn=mview.findViewById(R.id.profileyouraddressesbtn);
        ordersbtn=mview.findViewById(R.id.profileyourorderbtn);
        cartbtn=mview.findViewById(R.id.profileyourcartbtn);
        editimg=mview.findViewById(R.id.editimagebutton);
        editimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(),"feature currently unavailable",Toast.LENGTH_SHORT).show();
            }
        });
        Bundle bundle =this.getArguments();
        String name = bundle.getString("customername");
        phone = bundle.getString("customerphone");
        nametext.setText(name);
        phonetext.setText(phone);
        changepasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "{" +
                        "\"phone\"" + ":" + "\"" + phone + "\"" +
                        "}";
                Submit(data);
            }
        });
        addressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("customerphone",phone);
                Fragment fragment = new AddressFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        });
        ordersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdereditemsFragment()).addToBackStack(null).commit();

            }
        });
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).addToBackStack(null).commit();

            }
        });

        return mview;
    }
    private void Submit(String data) {
        final String savedata = data;
        String URL = "http://dpend.pythonanywhere.com/accounts/resetpasswordapi/";
        final LoadingDialog ld = new LoadingDialog(getActivity());
        ld.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        ld.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ld.dismiss();
                try {
                    JSONObject objres = new JSONObject(response);
                    String Response= objres.getString("response");
                    String responsecode=objres.getString("responsecode");
                    System.out.println(Response);
                    if(responsecode.equals("0")){
                        Intent intent = new Intent(getActivity(),PasswordResetActivity.class);
                        intent.putExtra("phonenumber",phone);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "oops something went wrong", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

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


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
