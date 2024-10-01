package com.bigoval.foodfever;

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

public class passwordresetpasswordfragment extends Fragment {
    Button confirm;
    EditText password1;
    EditText password2;
    RequestQueue requestQueue;
    String phonenumber;
    TextView passwordemptywarning;
    TextView passwordnotmatchingwarning;
    String key;
    String otp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_login, container, false);
        View mview = inflater.inflate(R.layout.passwordresetpasswordfragment, container, false);
        Bundle bundle = this.getArguments();
        phonenumber = bundle.getString("phone");
        key=bundle.getString("key");
        password1 = mview.findViewById(R.id.passwordresetpassword1);
        password2 = mview.findViewById(R.id.passwordresetpassword2);
        passwordemptywarning=mview.findViewById(R.id.passwordresetemptyphonewarning);
        passwordnotmatchingwarning=mview.findViewById(R.id.passwordresetpasswordmatchwarning);
        confirm = mview.findViewById(R.id.passwordresetpasswordconfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = password1.getText().toString();
                String pass2 = password2.getText().toString();
                if(!pass1.equals("") || !pass2.equals("")){
                    if (pass1.equals(pass2)){
                        String data = "{" +
                                "\"password\"" + ":" + "\"" + pass1 + "\"," +
                                "\"phone\"" + ":" + "\"" + phonenumber + "\"," +
                                "\"key\"" + ":" + "\"" + key + "\"" +
                                "}";
                        Submit(data);
                    }
                    else {
                        passwordnotmatchingwarning.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    passwordemptywarning.setVisibility(View.VISIBLE);
                }
            }
        });
        return mview;
    }

    private void Submit(String data) {
        final String savedata = data;
        String URL = "http://dpend.pythonanywhere.com/accounts/resetpasswordpasswordapi/";
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
                    String Responsecode = objres.getString("responsecode");
                    if(Responsecode.equals("1")){
                        Toast.makeText(getActivity().getApplicationContext(), "oops something went wrong", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "password reset successful", Toast.LENGTH_LONG).show();
                        getActivity().finish();

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
