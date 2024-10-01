package com.bigoval.foodfever;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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

public class passwordresetotpfragment extends Fragment {
    Button confirm;
    EditText otptext;
    EditText inputcode1,inputcode2,inputcode3,inputcode4,inputcode5,inputcode6;
    TextView wrongotp;
    TextView anotpwassent,resendotptxt;
    RequestQueue requestQueue;
    RequestQueue requestQueue2;
    String phonenumber;
    String key;
    DatabaseHelper dbhelper;
    DatabaseHelper mDatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_login, container, false);
        View mview = inflater.inflate(R.layout.fragment_otp, container, false);
        Bundle bundle = this.getArguments();
        phonenumber = bundle.getString("phonenumber");
        otptext = mview.findViewById(R.id.OTPtext);
        confirm = mview.findViewById(R.id.OTPconfirm);
        wrongotp=mview.findViewById(R.id.wrongotpwarning);
        resendotptxt=mview.findViewById(R.id.resendotptxt);
        anotpwassent=mview.findViewById(R.id.otpwassenttxt);
        String txt ="An otp was sent to +91 "+phonenumber;
        anotpwassent.setText(txt);
        String rtext = "Didn't recieve otp? ";
        String next = "<font color='#0593ff'>RESEND OTP</font>";
        resendotptxt.setText(Html.fromHtml(rtext + next));
        inputcode1=mview.findViewById(R.id.inputcode1);
        inputcode2=mview.findViewById(R.id.inputcode2);
        inputcode3=mview.findViewById(R.id.inputcode3);
        inputcode4=mview.findViewById(R.id.inputcode4);
        inputcode5=mview.findViewById(R.id.inputcode5);
        inputcode6=mview.findViewById(R.id.inputcode6);
        SetOtp();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String otp =otptext.getText().toString();
                String otp = inputcode1.getText().toString()+
                             inputcode2.getText().toString()+
                             inputcode3.getText().toString()+
                             inputcode4.getText().toString()+
                             inputcode5.getText().toString()+
                             inputcode6.getText().toString();
                String data = "{" +
                        "\"phone\"" + ":" + "\"" + phonenumber + "\"," +
                        "\"otp\"" + ":" + "\"" + otp + "\"" +
                        "}";
                Submit(data);
            }
        });
        resendotptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "{" +
                        "\"phone\"" + ":" + "\"" + phonenumber + "\"" +
                        "}";
                Submit2(data);
            }
        });
        return mview;
    }
    private void SetOtp(){
        inputcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputcode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputcode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputcode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void Submit(String data) {
        final String savedata = data;
        String URL = "http://dpend.pythonanywhere.com/accounts/resetpasswordotpconfirmationapi/";
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
                    String responsecode=objres.getString("responsecode");
                    if(!responsecode.equals("1")){
                        key = objres.getString("key");
                        Bundle bundle = new Bundle();
                        bundle.putString("phone",phonenumber);
                        bundle.putString("key",key);
                        Fragment fragment = new passwordresetpasswordfragment();
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container3, fragment).addToBackStack(null).commit();

                    }
                    else{
                        wrongotp.setVisibility(View.VISIBLE);
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
    private void Submit2(String data) {
        final String savedata = data;
        String URL = "http://dpend.pythonanywhere.com/accounts/resetpasswordapi/";
        final LoadingDialog ld = new LoadingDialog(getActivity());
        ld.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
        ld.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ld.dismiss();
                try {
                    JSONObject objres = new JSONObject(response);
                    String Response = objres.getString("response");
                    System.out.println(Response);

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
        requestQueue2.add(stringRequest);
    }
}
