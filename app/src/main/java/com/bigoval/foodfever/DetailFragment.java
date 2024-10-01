package com.bigoval.foodfever;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private int current_position = 0;
    ViewPager viewPager;

    Button addtocartbutton;
    Button buynowbtn;
    private TextView name;
    private TextView seller;
    private TextView realprice;
    private TextView price;
    private TextView stock;
    private TextView description;
    Boolean sized = true;
    String r;
    String loginapiurl = "http://dpend.pythonanywhere.com/accounts/Loginapi/";
    String addtocarturl = "http://dpend.pythonanywhere.com/products/addtocart/";
    String nullstring = "http://dpend.pythonanywhere.comnull";
    String checkcarturl = "http://dpend.pythonanywhere.com/products/checkcartseller/";
    RequestQueue rq;
    RequestQueue requestQueue;
    RequestQueue requestQueue2;
    RequestQueue requestQueue3;
    RequestQueue requestQueue4;
    TextView spinnertext;
    String quantity;
    String id;
    DatabaseHelper dbhelper;
    ArrayList<String> imageurls;
    ArrayList<String> sizes;
    Spinner sizespinner;
    String size;
    String imageurl1;
    String imageurl2;
    String imageurl3;
    String imageurl4;
    String imageurl5;
    String imageurl6;
    String imageurl7;
    String Description;
    String productid;
    String parentproductid;
    String namedata;
    String sellerdata;
    String pricedata;
    String realpricedata;
    String stockdata;
    String sellerid;
    Boolean writeable;
    Boolean writeabledata;
    ArrayList<String> cquantity;
    Boolean sameseller;
    Boolean loggedin;
    RequestQueue RQ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.detailed_fragment, container, false);
        if (!isNetworkAvailable()) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ErrorFragment()).addToBackStack(null).commit();

        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoggedinData", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("Customerid","");
        sizes = new ArrayList<String>();
        cquantity = new ArrayList<String>();
//        dbhelper = new DatabaseHelper(getActivity());
//        SQLiteDatabase db = dbhelper.getReadableDatabase();
        viewPager = mview.findViewById(R.id.viewpager2);
        spinnertext = mview.findViewById(R.id.spinnertext);
        name = mview.findViewById(R.id.name2);
        seller = mview.findViewById(R.id.seller2);
        price = mview.findViewById(R.id.price2);
        realprice = mview.findViewById(R.id.realprice);
        stock = mview.findViewById(R.id.stock2);
        stock.setTextColor(Color.GREEN);
        description = mview.findViewById(R.id.description2);
        addtocartbutton = mview.findViewById(R.id.addtocartbtn);
        buynowbtn = mview.findViewById(R.id.buynowbtn);
        loggedin = ((MainActivity) getActivity()).loggedin;
        Spinner spinner = mview.findViewById(R.id.spinner);
        sizespinner = mview.findViewById(R.id.spinnersize);
//        String token[] = {"token"};
//        Cursor c2 = db.query("tokens", token, null, null, null, null, null);
//        c2.moveToFirst();
//        try {
//            r = c2.getString(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        rq = Volley.newRequestQueue(getActivity());
//        parsejsonstring();
        ArrayAdapter<CharSequence> sadapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinnerarray, R.layout.spinnerbox);
        sadapter.setDropDownViewResource(R.layout.spinnertemplate);
        spinner.setAdapter(sadapter);
        spinner.setOnItemSelectedListener(this);
        Bundle bundle = this.getArguments();
        productid = bundle.getString("productid");
        parentproductid = bundle.getString("productid");
        String type = bundle.getString("type");
        if (type.equals("productfromproducts")) {
            namedata = bundle.getString("creatorName");
            stockdata = bundle.getString("stock");
            sellerdata = bundle.getString("seller");
            sellerid = bundle.getString("sellerid");
            Description = bundle.getString("description");
            pricedata = bundle.getString("likecount");
            realpricedata = bundle.getString("realprice");
            writeabledata = bundle.getBoolean("writeable");
            imageurl1 = bundle.getString("image");
            imageurl2 = bundle.getString("image2");
            imageurl3 = bundle.getString("image3");
            imageurl4 = bundle.getString("image4");
            imageurl5 = bundle.getString("image5");
            imageurl6 = bundle.getString("image6");
            imageurl7 = bundle.getString("image7");

            imageurls = new ArrayList<String>();
            if (!imageurl1.contains(nullstring)) {
                imageurls.add(imageurl1);
            }
            if (!imageurl2.contains(nullstring)) {
                imageurls.add(imageurl2);
            }
            if (!imageurl3.contains(nullstring)) {
                imageurls.add(imageurl3);
            }
            if (!imageurl4.contains(nullstring)) {
                imageurls.add(imageurl4);
            }
            if (!imageurl5.contains(nullstring)) {
                imageurls.add(imageurl5);
            }
            if (!imageurl6.contains(nullstring)) {
                imageurls.add(imageurl6);
            }
            if (!imageurl7.contains(nullstring)) {
                imageurls.add(imageurl7);
            }
            name.setText(namedata);
            seller.setText("Sold by: " + sellerdata);
            if (Integer.parseInt(stockdata) != 0) {
                stock.setText("Only " + stockdata + " Stocks left");

            } else {
                stock.setText("Out of stock");
                stock.setTextColor(Color.RED);
            }
            price.setText("₹" + pricedata);
            description.setText(Description);
            if (price != realprice){
                realprice.setText("₹" + realpricedata);
                realprice.setPaintFlags(realprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else {
                realprice.setVisibility(View.GONE);
            }


            String[] stockArr = new String[imageurls.size()];
            stockArr = imageurls.toArray(stockArr);
            ProductViewPagerAdapter adapter = new ProductViewPagerAdapter(getActivity(), stockArr);
            viewPager.setAdapter(adapter);

            if (Integer.parseInt(stockdata) < 1) {
                addtocartbutton.setEnabled(false);
                addtocartbutton.setVisibility(View.GONE);
                buynowbtn.setEnabled(false);
                buynowbtn.setVisibility(View.GONE);
            }
        } else {
            String data = "{" +
                    "\"id\"" + ":" + "\"" + productid + "\"" +
                    "}";
            Submit4(data);
        }


        try {
            String data = "{" +
                    "\"id\"" + ":" + "\"" + productid + "\"" +
                    "}";
            Submit2(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addtocartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loggedin) {
                    if (sized) {
                        if (size.equals("")) {
                            Toast.makeText(getActivity().getApplicationContext(), "select a size", Toast.LENGTH_SHORT).show();
                        } else {
//                            String data = "{" +
//                                    "\"customerid\"" + ":" + "\"" + id + "\"," +
//                                    "\"quantity\"" + ":" + "\"" + quantity + "\"," +
//                                    "\"size\"" + ":" + "\"" + size + "\"," +
//                                    "\"product\"" + ":" + "\"" + productid + "\"" +
//                                    "}";
//                            Submit(data);
//                            cartctextcheck();
                            String data = "{" +
                                            "\"id\"" + ":" + "\"" + id + "\"" +
                                            "}";
                            getcartdetails(data);
                        }
                    } else {
//                        String data = "{" +
//                                "\"customerid\"" + ":" + "\"" + id + "\"," +
//                                "\"quantity\"" + ":" + "\"" + quantity + "\"," +
//                                "\"size\"" + ":" + "\"" + size + "\"," +
//                                "\"product\"" + ":" + "\"" + productid + "\"" +
//                                "}";
//                        Submit(data);
//                        cartctextcheck();
                        String data = "{" +
                                "\"id\"" + ":" + "\"" + id + "\"" +
                                "}";
                        getcartdetails(data);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        buynowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loggedin) {
                    if (sized) {
                        if (size.equals("")) {
                            Toast.makeText(getActivity().getApplicationContext(), "select a size", Toast.LENGTH_SHORT).show();
                        } else {
                            ctextcheck();
                        }
                    } else {
                        ctextcheck();
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
//        String data = "{" +
//                "\"id\"" + ":" + "\"" + id + "\"" +
//                "}";
//        getcartdetails(data);

        return mview;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        quantity = adapterView.getItemAtPosition(i).toString();
//        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void Submit(String data) {
        final LoadingDialog pd = new LoadingDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final String savedata = data;
        String URL = addtocarturl;

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        pd.show();
        pd.setCanceledOnTouchOutside(getRetainInstance());

        pd.setContentView(R.layout.dialog);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject objres = new JSONObject(response);
                    String responsecode = objres.getString("responsecode");
                    if (!responsecode.equals("0")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorry not enough stock.Try decreasing quantity", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "product added to cart", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), "Add to cart failed", Toast.LENGTH_LONG).show();
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

    private void Submit2(String data) {
        final LoadingDialog pd = new LoadingDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final String savedata = data;
        String URL = "http://dpend.pythonanywhere.com/products/getvariatedproduct/";

        requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
        pd.show();
        pd.setCanceledOnTouchOutside(getRetainInstance());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {
                        sizespinner.setVisibility(View.INVISIBLE);
                        spinnertext.setVisibility(View.INVISIBLE);
                    } else if (jsonArray.length() == 1) {
                        sizespinner.setVisibility(View.INVISIBLE);
                        spinnertext.setVisibility(View.INVISIBLE);
                        sized = false;
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String sizeb = data.getString("size");
                            sizes.add(sizeb);


                        } catch (JSONException e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sizes);
                        adapter.setDropDownViewResource(R.layout.spinnertemplate2);
                        sizespinner.setAdapter(adapter);
                        sizespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String text = adapterView.getItemAtPosition(i).toString();
                                Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
                                size = text;
                                if (!size.equals("")) {
                                    String data = "{" +
                                            "\"size\"" + ":" + "\"" + size + "\"," +
                                            "\"productid\"" + ":" + "\"" + parentproductid + "\"" +
                                            "}";
                                    Submit3(data);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
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

    private void Submit3(String data) {
        final LoadingDialog pd = new LoadingDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final String savedata = data;
        String URL = "http://dpend.pythonanywhere.com/products/featurechangedproduct/";

        requestQueue3 = Volley.newRequestQueue(getActivity().getApplicationContext());
        pd.show();
        pd.setCanceledOnTouchOutside(getRetainInstance());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject item = new JSONObject(response);
                    pricedata = item.getString("price");
                    realpricedata = item.getString("realprice");
                    stockdata = item.getString("stock");
                    productid = item.getString("productid");
                    price.setText("₹ " + pricedata);
                    realprice.setText("₹ " + realpricedata);
                    realprice.setPaintFlags(realprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    if (Integer.parseInt(stockdata) > 0) {
                        stock.setText("Only " + stockdata + " Stocks left");
                        stock.setTextColor(Color.GREEN);
                        addtocartbutton.setEnabled(true);
                        addtocartbutton.setVisibility(View.VISIBLE);
                        buynowbtn.setEnabled(true);
                        buynowbtn.setVisibility(View.VISIBLE);
                    } else if (Integer.parseInt(stockdata) < 1) {
                        stock.setText("Out of Stock");
                        stock.setTextColor(Color.RED);
                        addtocartbutton.setEnabled(false);
                        addtocartbutton.setVisibility(View.GONE);
                        buynowbtn.setEnabled(false);
                        buynowbtn.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
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
        requestQueue3.add(stringRequest);
    }

    private void Submit4(String data) {
        final LoadingDialog pd = new LoadingDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final String savedata = data;
        String URL = "http://dpend.pythonanywhere.com/products/getproduct/";

        requestQueue4 = Volley.newRequestQueue(getActivity().getApplicationContext());
        pd.show();
        pd.setCanceledOnTouchOutside(getRetainInstance());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    namedata = object.getString("name");
                    stockdata = object.getString("stock");
                    sellerdata = object.getString("seller");
                    sellerid = object.getString("sellerid");
                    Description = object.getString("description");
                    pricedata = object.getString("price");
                    realpricedata = object.getString("realprice");
                    writeabledata = object.getBoolean("writeable");
                    imageurl1 = "http://dpend.pythonanywhere.com" + object.getString("image");
                    imageurl2 = "http://dpend.pythonanywhere.com" + object.getString("image2");
                    imageurl3 = "http://dpend.pythonanywhere.com" + object.getString("image3");
                    imageurl4 = "http://dpend.pythonanywhere.com" + object.getString("image4");
                    imageurl5 = "http://dpend.pythonanywhere.com" + object.getString("image5");
                    imageurl6 = "http://dpend.pythonanywhere.com" + object.getString("image6");
                    imageurl7 = "http://dpend.pythonanywhere.com" + object.getString("image7");
                    imageurls = new ArrayList<String>();
                    if (!imageurl1.contains(nullstring)) {
                        imageurls.add(imageurl1);
                    }
                    if (!imageurl2.contains(nullstring)) {
                        imageurls.add(imageurl2);
                    }
                    if (!imageurl3.contains(nullstring)) {
                        imageurls.add(imageurl3);
                    }
                    if (!imageurl4.contains(nullstring)) {
                        imageurls.add(imageurl4);
                    }
                    if (!imageurl5.contains(nullstring)) {
                        imageurls.add(imageurl5);
                    }
                    if (!imageurl6.contains(nullstring)) {
                        imageurls.add(imageurl6);
                    }
                    if (!imageurl7.contains(nullstring)) {
                        imageurls.add(imageurl7);
                    }
                    name.setText(namedata);
                    seller.setText("Sold by: " + sellerdata);
                    stock.setText("Only " + stockdata + " Stocks left");
                    price.setText("₹" + pricedata);
                    if(!pricedata.equals(realpricedata)){
                        realprice.setText("₹" + realpricedata);
                        realprice.setPaintFlags(realprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }else {
                        realprice.setVisibility(View.GONE);
                    }
                    description.setText(Description);
                    String[] stockArr = new String[imageurls.size()];
                    stockArr = imageurls.toArray(stockArr);
                    ProductViewPagerAdapter adapter = new ProductViewPagerAdapter(getActivity(), stockArr);
                    viewPager.setAdapter(adapter);
                    if (Integer.parseInt(stockdata) < 1) {
                        addtocartbutton.setEnabled(false);
                        addtocartbutton.setVisibility(View.GONE);
                        buynowbtn.setEnabled(false);
                        buynowbtn.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        System.out.println(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
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
        requestQueue4.add(stringRequest);
    }

    private void parsejsonstring() {
        final String ACCESS_TOKEN = "Token " + r;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginapiurl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    id = response.getString("id");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void continuewithorder() {
        Bundle bundle1 = new Bundle();
        bundle1.putBoolean("many", false);
        bundle1.putString("name", namedata);
        bundle1.putString("imageurl", imageurl1);
        bundle1.putString("seller", sellerdata);
        bundle1.putString("price", pricedata);
        bundle1.putString("quantity", quantity);
        bundle1.putString("id", productid);
        bundle1.putString("size", size);
        bundle1.putString("caketext", "");

        Fragment fragment = new OrderAddressFragment();
        fragment.setArguments(bundle1);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

    }

    public void continuewithorder2(String caketext) {
        Bundle bundle1 = new Bundle();
        bundle1.putString("caketext", caketext);
        bundle1.putBoolean("many", false);
        bundle1.putString("name", namedata);
        bundle1.putString("imageurl", imageurl1);
        bundle1.putString("seller", sellerdata);        bundle1.putString("price", pricedata);
        bundle1.putString("quantity", quantity);
        bundle1.putString("id", productid);
        bundle1.putString("size", size);

        Fragment fragment = new OrderAddressFragment();
        fragment.setArguments(bundle1);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

    }

    public void ctextcheck() {
        if (writeabledata.equals(true)) {
            AlertDialog.Builder Abuilder = new AlertDialog.Builder(getActivity());
            View vview = getLayoutInflater().inflate(R.layout.doyouwannawrite, null);
            final Button yes = vview.findViewById(R.id.dyww_yes);
            final Button no = vview.findViewById(R.id.dyww_no);
            Abuilder.setView(vview);
            final AlertDialog dialog = Abuilder.create();
            dialog.show();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    AlertDialog.Builder Abuilder2 = new AlertDialog.Builder(getActivity());
                    View vview2 = getLayoutInflater().inflate(R.layout.nameoncake, null);
                    Button continue1 = vview2.findViewById(R.id.noc_continue);
                    Button cancel = vview2.findViewById(R.id.noc_cancel);
                    final EditText edittext = vview2.findViewById(R.id.caketext);

                    Abuilder2.setView(vview2);
                    final AlertDialog dialog2 = Abuilder2.create();
                    dialog2.show();
                    continue1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String text = edittext.getText().toString();
                            if (text.equals("")) {
                                Toast.makeText(getActivity().getApplicationContext(), "Enter text to continue", Toast.LENGTH_SHORT).show();

                            } else {
                                dialog2.dismiss();
                                continuewithorder2(text);
                            }


                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog2.dismiss();

                        }
                    });
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    continuewithorder();

                }
            });
        } else {
            continuewithorder();
        }
    }
    private void cartctextcheck(){
        if (writeabledata.equals(true)) {
            AlertDialog.Builder Abuilder = new AlertDialog.Builder(getActivity());
            View vview = getLayoutInflater().inflate(R.layout.doyouwannawrite, null);
            final Button yes = vview.findViewById(R.id.dyww_yes);
            final Button no = vview.findViewById(R.id.dyww_no);
            Abuilder.setView(vview);
            final AlertDialog dialog = Abuilder.create();
            dialog.show();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    AlertDialog.Builder Abuilder2 = new AlertDialog.Builder(getActivity());
                    View vview2 = getLayoutInflater().inflate(R.layout.nameoncake, null);
                    Button continue1 = vview2.findViewById(R.id.noc_continue);
                    Button cancel = vview2.findViewById(R.id.noc_cancel);
                    final EditText edittext = vview2.findViewById(R.id.caketext);

                    Abuilder2.setView(vview2);
                    final AlertDialog dialog2 = Abuilder2.create();
                    dialog2.show();
                    continue1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String text = edittext.getText().toString();
                            if (text.equals("")) {
                                Toast.makeText(getActivity().getApplicationContext(), "Enter text to continue", Toast.LENGTH_SHORT).show();

                            } else {
                                dialog2.dismiss();
                                String data = "{" +
                                        "\"customerid\"" + ":" + "\"" + id + "\"," +
                                        "\"quantity\"" + ":" + "\"" + quantity + "\"," +
                                        "\"size\"" + ":" + "\"" + size + "\"," +
                                        "\"caketext\"" + ":" + "\"" + text + "\"," +
                                        "\"sellerid\"" + ":" + "\"" + sellerid + "\"," +
                                        "\"product\"" + ":" + "\"" + productid + "\"" +
                                        "}";
                                Submit(data);
                            }


                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog2.dismiss();

                        }
                    });
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    String text = "";
                    String data = "{" +
                            "\"customerid\"" + ":" + "\"" + id + "\"," +
                            "\"quantity\"" + ":" + "\"" + quantity + "\"," +
                            "\"size\"" + ":" + "\"" + size + "\"," +
                            "\"sellerid\"" + ":" + "\"" + sellerid + "\"," +
                            "\"caketext\"" + ":" + "\"" + text + "\"," +
                            "\"product\"" + ":" + "\"" + productid + "\"" +
                            "}";
                    Submit(data);

                }
            });
        } else {
            String text = "";
            String data = "{" +
                    "\"customerid\"" + ":" + "\"" + id + "\"," +
                    "\"quantity\"" + ":" + "\"" + quantity + "\"," +
                    "\"size\"" + ":" + "\"" + size + "\"," +
                    "\"sellerid\"" + ":" + "\"" + sellerid + "\"," +
                    "\"caketext\"" + ":" + "\"" + text + "\"," +
                    "\"product\"" + ":" + "\"" + productid + "\"" +
                    "}";
            Submit(data);
        }
    }

    private void getcartdetails(String data) {
        final LoadingDialog pd = new LoadingDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final String savedata = data;
        String URL = checkcarturl;
        RQ = Volley.newRequestQueue(getActivity().getApplicationContext());
        pd.show();
        pd.setCanceledOnTouchOutside(getRetainInstance());

        pd.setContentView(R.layout.dialog);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject objres = new JSONObject(response);
                    String responsecode =  objres.getString("responsecode");
                    if(responsecode.equals("1")){
                        String seller = objres.getString("sellerid");
                        sameseller = seller.equals(sellerid);
                        if(!sameseller){
                            showwarning();
                        }
                        else {
                            cartctextcheck();
                        }
                    }else if(responsecode.equals("2")){
                        sameseller=true;
                        cartctextcheck();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();

                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
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
        RQ.add(stringRequest);
    }

    private void showwarning(){
            AlertDialog.Builder Abuilder = new AlertDialog.Builder(getActivity());
            View vview = getLayoutInflater().inflate(R.layout.cartalert, null);
            final Button ok = vview.findViewById(R.id.cartalertok);
            final Button cancel = vview.findViewById(R.id.cartalertcancel);
            Abuilder.setView(vview);
            final AlertDialog dialog = Abuilder.create();
            dialog.show();
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    cartctextcheck();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

    }
}
