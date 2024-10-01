package com.bigoval.foodfever;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class AddaddressFragment extends Fragment {
    RequestQueue requestQueue;
    RequestQueue rq;
    Button addaddress;
    EditText name;
    EditText pincode;
    EditText phone;
    String regphone;
    String customer;
    EditText address;
    EditText town;
    EditText landmark;
    EditText housename;
    DatabaseHelper dbhelper;
    String r;
    String addaddressurl="http://dpend.pythonanywhere.com/accounts/addaddress/";
    String loginapiurl="http://dpend.pythonanywhere.com/accounts/Loginapi/";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mview=inflater.inflate(R.layout.fragment_addaddress, container, false);

        dbhelper=new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String details[] = {"loggedin"};
        final Cursor c = db.query("login",details,null,null,null,null,null);
        c.moveToFirst();
        rq= Volley.newRequestQueue(getActivity());
        addaddress=mview.findViewById(R.id.addaddress);
        name=mview.findViewById(R.id.addaddressname);
        pincode=mview.findViewById(R.id.addaddresspincode);
        phone=mview.findViewById(R.id.addaddressphone);
        address=mview.findViewById(R.id.addaddressaddress);
        town=mview.findViewById(R.id.addaddresstown);
        landmark=mview.findViewById(R.id.addaddresslandmark);
        housename=mview.findViewById(R.id.addaddresshousename);



        String token[] = {"token"};
        Cursor c2 = db.query("tokens",token,null,null,null,null,null);
        c2.moveToFirst();
        if(c.getInt(0)==1){
            try {
                r=c2.getString(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parsejsonstring();
        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String namet=name.getText().toString();
                final String pincodet=pincode.getText().toString();
                final String phonet=phone.getText().toString();
                final String addresst=address.getText().toString();
                final String townt=town.getText().toString();
                final String landmarkt=landmark.getText().toString();
                final String housenamet=housename.getText().toString();
                ColorStateList colorStateList = ColorStateList.valueOf(Color.RED);
                if(namet.equals("")){
                    name.setBackgroundResource(R.drawable.input_background_red);
                }
                else if(pincodet.equals("")){
                    pincode.setBackgroundResource(R.drawable.input_background_red);
                }
                else if(phonet.equals("")){
                    phone.setBackgroundResource(R.drawable.input_background_red);
                }
                else if(landmarkt.equals("")){
                    landmark.setBackgroundResource(R.drawable.input_background_red);
                }
                else if(housenamet.equals("")){
                    housename.setBackgroundResource(R.drawable.input_background_red);
                }
                else if(townt == ""){
                    town.setBackgroundResource(R.drawable.input_background_red);
                }
                else if(addresst == ""){
                    address.setBackgroundResource(R.drawable.input_background_red);
                }
                else {
                    String data = "{"+
                            "\"pincode\"" + ":" + "\"" + pincode.getText().toString() + "\","+
                            "\"mobilenumber\"" + ":" + "\"" + phone.getText().toString() + "\","+
                            "\"address1\"" + ":" + "\"" + address.getText().toString() + "\","+
                            "\"landmark\"" + ":" + "\"" + landmark.getText().toString() + "\","+
                            "\"housename\"" + ":" + "\"" + housename.getText().toString() + "\","+
                            "\"townname\"" + ":" + "\"" + town.getText().toString() + "\","+
                            "\"customer\"" + ":" + "\"" + customer + "\","+
                            "\"regphone\"" + ":" + "\"" + regphone + "\","+
                            "\"name\"" + ":" + "\"" + name.getText().toString() + "\""+
                            "}";
                    Submit(data);
                }

            }
        });
        return mview;
    }
    private void Submit(String data)
    {
        final String savedata= data;
        String URL=addaddressurl;

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres=new JSONObject(response);
                    String responsecode = objres.getString("responsecode");
                    if(responsecode.equals("0")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Address added successfully", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(),"Delivery not available for the pincode",Toast.LENGTH_LONG).show();
                        pincode.setBackgroundResource(R.drawable.input_background_red);
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();

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
                Toast.makeText(getActivity().getApplicationContext(),"error",Toast.LENGTH_LONG).show();

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

}
