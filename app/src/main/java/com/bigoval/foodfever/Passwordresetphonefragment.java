package com.bigoval.foodfever;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class Passwordresetphonefragment extends Fragment {
    private Button resetphoneconfirm;
    private EditText passwordresetphone;
    private TextView emptyphonewarning;
    private TextView invalidphonewarning;
    private RequestQueue requestQueue;
    private DatabaseHelper mDatabaseHelper;
    private DatabaseHelper dbhelper;
    RequestQueue rq;
    String r;
    String phone;
    String loginurl = "http://dpend.pythonanywhere.com/accounts/Login/";
    String loginapiurl = "http://dpend.pythonanywhere.com/accounts/Loginapi/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_login, container, false);
        View mview = inflater.inflate(R.layout.fragment_resetpasswordphone, container, false);
        passwordresetphone = mview.findViewById(R.id.resetphonetext);
        resetphoneconfirm=mview.findViewById(R.id.resetphoneconfirm);
        emptyphonewarning=mview.findViewById(R.id.passwordresetemptyphonewarning);
        invalidphonewarning=mview.findViewById(R.id.passwordresetinvalidphonewarning);
        resetphoneconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    phone = passwordresetphone.getText().toString();
                    if (!phone.equals("")){
                        String data = "{" +
                                "\"phone\"" + ":" + "\"" + phone + "\"" +
                                "}";
                        Submit(data);

                    }
                    else {
                        emptyphonewarning.setVisibility(View.VISIBLE);

                    }
                } catch (Exception e) {
                    emptyphonewarning.setVisibility(View.VISIBLE);
                }

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
                        getActivity().getSupportFragmentManager().popBackStack();
                        startActivity(intent);
                    }
                    else {
                        invalidphonewarning.setVisibility(View.VISIBLE);
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
}
