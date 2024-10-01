package com.bigoval.foodfever;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

public class OTPFragment extends Fragment {
    Button confirm;
    EditText otptext;
    EditText inputcode1,inputcode2,inputcode3,inputcode4,inputcode5,inputcode6;
    TextView anotpwassent,resendotptxt;
    RequestQueue requestQueue;
    RequestQueue requestQueue2;
    DatabaseHelper dbhelper;
    DatabaseHelper mDatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_login, container, false);
        View mview = inflater.inflate(R.layout.fragment_otp, container, false);
        confirm = mview.findViewById(R.id.OTPconfirm);
        otptext = mview.findViewById(R.id.OTPtext);
        resendotptxt=mview.findViewById(R.id.resendotptxt);
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
        dbhelper = new DatabaseHelper(getActivity());
        mDatabaseHelper = new DatabaseHelper(getActivity());
        anotpwassent=mview.findViewById(R.id.otpwassenttxt);
        Bundle bundle = this.getArguments();
        final String phone = bundle.getString("phone");
        final String first_name = bundle.getString("first_name");
        final String last_name = bundle.getString("last_name");
        final String email = bundle.getString("email");
        final String password = bundle.getString("password");
        String txt ="An otp was sent to +91 "+phone;
        anotpwassent.setText(txt);
        resendotptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "{" +
                        "\"phone\"" + ":" + "\"" + phone + "\"" +
                        "}";
                Submit2(data);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = inputcode1.getText().toString()+
                        inputcode2.getText().toString()+
                        inputcode3.getText().toString()+
                        inputcode4.getText().toString()+
                        inputcode5.getText().toString()+
                        inputcode6.getText().toString();
                String data = "{" +
                        "\"first_name\"" + ":" + "\"" + first_name + "\"," +
                        "\"last_name\"" + ":" + "\"" + last_name + "\"," +
                        "\"email\"" + ":" + "\"" + email + "\"," +
                        "\"phone\"" + ":" + "\"" + phone + "\"," +
                        "\"password\"" + ":" + "\"" + password + "\"," +
                        "\"otp\"" + ":" + "\"" + otp + "\"" +
                        "}";
                Submit(data);
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
        String URL = "http://dpend.pythonanywhere.com/accounts/RegisterCustomer/";
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
                    Toast.makeText(getActivity().getApplicationContext(), objres.toString(), Toast.LENGTH_LONG).show();
                    String newEntry = objres.getString("token");
                    String id = objres.getString("id");
                    String name = objres.getString("first_name");
                    String phone = objres.getString("phone");
                    AddData(newEntry);
                    AddUserData(name, id, phone);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    SQLiteDatabase db = dbhelper.getReadableDatabase();
                    db.execSQL("UPDATE " + "login" + " SET loggedin = 1 WHERE loggedin = 0");
                    Toast.makeText(getActivity().getApplicationContext(), newEntry, Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    getActivity().finish();


                } catch (JSONException e) {
                    System.out.println(e);
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
        requestQueue.add(stringRequest);
    }

    public void AddData(String newEntry) {

        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    public void AddUserData(String name, String id, String phone) {
        boolean insertData = dbhelper.addUserData(name, id, phone);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    private void Submit2(String data) {
        final String savedata = data;
        String URL = "http://dpend.pythonanywhere.com/accounts/testapi/";
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
