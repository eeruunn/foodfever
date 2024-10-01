package com.bigoval.foodfever;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    private Button login;
    private EditText loginphone;
    private EditText loginpassword;
    private TextView credwarning;
    TextView registertext;
    TextView forgotpass;
    TextView skiplogintxt;
    private RequestQueue requestQueue;
    private DatabaseHelper mDatabaseHelper;
    private DatabaseHelper dbhelper;
    RequestQueue rq;
    String r;
    String loginurl = "http://dpend.pythonanywhere.com/accounts/Login/";
    String loginapiurl = "http://dpend.pythonanywhere.com/accounts/Loginapi/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_login, container, false);
        View mview = inflater.inflate(R.layout.fragment_login, container, false);
        rq = Volley.newRequestQueue(getActivity());
        login = mview.findViewById(R.id.login);
        forgotpass=mview.findViewById(R.id.forgotpass);
        loginphone = mview.findViewById(R.id.loginphone1);
        loginpassword = mview.findViewById(R.id.loginpass1);
        skiplogintxt=mview.findViewById(R.id.skiplogintxt);
        credwarning = mview.findViewById(R.id.credwarning);
        registertext=mview.findViewById(R.id.registertext);
        String rtext = "Dont have an account? ";
        String next = "<font color='#0593ff'>Register</font>";
        registertext.setText(Html.fromHtml(rtext + next));
        dbhelper = new DatabaseHelper(getActivity());
        mDatabaseHelper = new DatabaseHelper(getActivity());
        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new RegisterFragment()).addToBackStack(null).commit();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "{" +
                        "\"username\"" + ":" + "\"" + loginphone.getText().toString() + "\"," +
                        "\"password\"" + ":" + "\"" + loginpassword.getText().toString() + "\"" +
                        "}";
                Submit(data);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new Passwordresetphonefragment()).addToBackStack(null).commit();
            }
        });
        skiplogintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return mview;
    }

    private void Submit(String data) {
        final String savedata = data;
        String URL = loginurl;
        final LoadingDialog ld = new LoadingDialog(getActivity());
        ld.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ld.show();
        ld.setCanceledOnTouchOutside(getRetainInstance());

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ld.dismiss();
                try {
                    JSONObject objres = new JSONObject(response);
                    String newEntry = objres.getString("token");
                    r = newEntry;
                    AddData(newEntry);
                    parsejsonstring();
                    final Intent intent = new Intent(getActivity(), MainActivity.class);
                    SQLiteDatabase db = dbhelper.getReadableDatabase();
                    db.execSQL("UPDATE " + "login" + " SET loggedin = 1 WHERE loggedin = 0");
                    Toast.makeText(getActivity().getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                    ld.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ld.dismiss();
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }, 3000);


                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                    System.out.println("wrong password or account");
                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                        ColorStateList colorStateList = ColorStateList.valueOf(Color.RED);
                        loginpassword.setBackgroundTintList(colorStateList);
                        loginphone.setBackgroundTintList(colorStateList);
                        credwarning.setVisibility(View.VISIBLE);

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
    }

    public void AddUserData(String name, String id, String phone) {
        boolean insertData = dbhelper.addUserData(name, id, phone);
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void parsejsonstring() {
        final String ACCESS_TOKEN = "Token " + r;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginapiurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = response.getString("name");
                    String id = response.getString("id");
                    String phone = response.getString("phone");
                    AddUserData(name, id, phone);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);

            }

        }) {
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
}
