package com.bigoval.foodfever;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
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

public class RegisterFragment extends Fragment {
    EditText phone;
    EditText first_name;
    EditText last_name;
    EditText email;
    EditText password;
    EditText password2;
    TextView logintext;
    Button registerbtn;
    TextView skip;
    Button login;
    RequestQueue requestQueue;
    DatabaseHelper mDatabaseHelper;
    DatabaseHelper dbhelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_register, container, false);
        dbhelper = new DatabaseHelper(getActivity());
        mDatabaseHelper = new DatabaseHelper(getActivity());
//        SQLiteDatabase db = dbhelper.getWritableDatabase();
//        ContentValues content =new ContentValues();
//        content.put("loggedin",0);
//        db.insert("login",null,content);

        phone = mview.findViewById(R.id.regphone);
        first_name = mview.findViewById(R.id.regfirstname);
        last_name = mview.findViewById(R.id.reglastname);
        email = mview.findViewById(R.id.regemail);
        password = mview.findViewById(R.id.regpass);
        password2 = mview.findViewById(R.id.regpass2);
        registerbtn = mview.findViewById(R.id.registerbtn2);
        logintext=mview.findViewById(R.id.logintext);
        String rtext = "Already have an account? ";
        String next = "<font color='#0593ff'>Login</font>";
        logintext.setText(Html.fromHtml(rtext + next));
        skip = mview.findViewById(R.id.skipregtxt);
        login=mview.findViewById(R.id.alreadyhaveanaccount);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });
//        registerbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new OTPFragment()).addToBackStack(null).commit();
//            }
//        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorStateList colorStateList = ColorStateList.valueOf(Color.RED);
                if(phone.getText().toString().equals("")){
                    phone.setBackgroundTintList(colorStateList);
                }
                else if(first_name.getText().toString().equals("")){
                    first_name.setBackgroundTintList(colorStateList);
                }
                else if(last_name.getText().toString().equals("")){
                    last_name.setBackgroundTintList(colorStateList);
                }
                else if(phone.getText().toString().length() != 10){
                    phone.setBackgroundTintList(colorStateList);
                    Toast.makeText(getActivity().getApplicationContext(), "Enter a valid mobile number", Toast.LENGTH_LONG).show();
                }
                else if(password.getText().toString().length() < 8){
                    password.setBackgroundTintList(colorStateList);
                    Toast.makeText(getActivity().getApplicationContext(), "password must have 8 characters", Toast.LENGTH_LONG).show();
                }
                else {
                    String data = "{" +
                            "\"phone\"" + ":" + "\"" + phone.getText().toString() + "\"" +
                            "}";
                    Submit(data);
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return mview;
    }


    private void Submit(String data) {
        final String savedata = data;
        String URL = "http://dpend.pythonanywhere.com/accounts/testapi/";
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
                    String responsecode = objres.getString("responsecode");
                    if (responsecode.equals("1")){
                        ColorStateList colorStateList = ColorStateList.valueOf(Color.RED);
                        phone.setBackgroundTintList(colorStateList);
                        Toast.makeText(getActivity().getApplicationContext(), objres.getString("message").toString(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putString("phone", phone.getText().toString());
                        bundle.putString("first_name", first_name.getText().toString());
                        bundle.putString("last_name", last_name.getText().toString());
                        bundle.putString("password", password.getText().toString());
                        bundle.putString("email", email.getText().toString());
                        OTPFragment Ofrag = new OTPFragment();
                        Ofrag.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, Ofrag).addToBackStack(null).commit();

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

    public void AddData(String newEntry) {

        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
//    public void AddloginData(Integer newEntry) {
//        boolean insertData = dbhelper.addlogginData(newEntry);
//
//        if (insertData) {
//            toastMessage("Data Successfully Inserted!");
//        } else {
//            toastMessage("Something went wrong");
//        }
//    }
}
