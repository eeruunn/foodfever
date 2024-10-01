package com.bigoval.foodfever;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CartFragment extends Fragment implements CartAdapter.OnItemClickListener {
    int totalprice = 0;
    TextView totalpricetext;
    private Button addaddressbtn;
    Button proceedtobuy;
    TextView result;
    private RecyclerView mRecyclerView;
    Fragment frg;
    private CartAdapter mAdapter;
    private ArrayList<CartItem> mExampleList;
    private RequestQueue mRequestQueue;
    RequestQueue requestQueue;
    RequestQueue requestQueue2;
    RequestQueue rq;
    DatabaseHelper dbhelper;
    String customerid;
    String r;
    String viewcarturl = "http://dpend.pythonanywhere.com/products/viewcart/";
    String deletefromcarturl = "http://dpend.pythonanywhere.com/products/deletefromcart/";
    String addtocarturl = "http://dpend.pythonanywhere.com/products/addtocart/";
    String imageurl = "http://dpend.pythonanywhere.com/media/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_cart, container, false);
        rq = Volley.newRequestQueue(getActivity());
        dbhelper = new DatabaseHelper(getActivity());
        totalpricetext = mview.findViewById(R.id.totalprice);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoggedinData", Context.MODE_PRIVATE);
        customerid = sharedPreferences.getString("Customerid","");
        if (isNetworkAvailable()) {
            try {
                String data = "{" +
                        "\"customerid\"" + ":" + "\"" + customerid + "\"" +
                        "}";
                Submit(data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ErrorFragment()).addToBackStack(null).commit();
        }

        mRecyclerView = mview.findViewById(R.id.cart_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExampleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
        proceedtobuy = mview.findViewById(R.id.proceedtobuybtn);
        proceedtobuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("many", true);
                Fragment fragment = new OrderAddressFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        });
        result = mview.findViewById(R.id.result);
        return mview;
    }

    private void Submit(String data) {
        totalprice = 0;
        final LoadingDialog pd = new LoadingDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final String savedata = data;
        String URL = viewcarturl;

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        pd.show();
        pd.setCanceledOnTouchOutside(getRetainInstance());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {
                        result.setVisibility(View.VISIBLE);
                    }
                    else {
                        proceedtobuy.setEnabled(true);
                        proceedtobuy.setBackgroundResource(R.drawable.button_background);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject data = jsonArray.getJSONObject(i);
                            CartItem item = new CartItem();
                            item.setImageUrl(imageurl + data.getString("image"));
                            item.setsize(data.getString("size"));
                            item.setprice(data.getInt("price"));
                            item.setquantity(data.getInt("quantity"));
                            item.setseller(data.getString("seller"));
                            item.setsellerid(data.getString("sellerid"));
                            item.setname(data.getString("name"));
                            item.setid(data.getString("id"));
                            item.setdeliverablewithin(data.getInt("deliverablewithin"));
                            item.setctext(data.getString("ctext"));
                            totalprice += data.getInt("price") * data.getInt("quantity");
                            mExampleList.add(item);


                        } catch (JSONException e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                        }
                    }
                    mAdapter = new CartAdapter(getActivity().getApplicationContext(), mExampleList);
                    mRecyclerView.setAdapter(mAdapter);
                    totalpricetext.setText("₹" + totalprice);
                    mAdapter.setOnItemClickListener(CartFragment.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                try {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ServerErrorFragment()).commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onDeleteItemClick(int position) {
        CartItem item = mExampleList.get(position);
        String itemid = item.getid();
        String size = item.getsize();
        String seller = item.getsellerid();
        String ctext = item.getctext();
        System.out.println(ctext);
        String data = "{" +
                "\"customerid\"" + ":" + "\"" + customerid + "\"," +
                "\"quantity\"" + ":" + "\"" + 1 + "\"," +
                "\"size\"" + ":" + "\"" + size + "\"," +
                "\"sellerid\"" + ":" + "\"" + seller + "\"," +
                "\"caketext\"" + ":" + "\"" + ctext + "\"," +
                "\"product\"" + ":" + "\"" + itemid + "\"" +
                "}";
        Submit2(data, deletefromcarturl);
        frg = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    @Override
    public void onSubQtyClick(int position) {
        CartItem item = mExampleList.get(position);
        String itemid = item.getid();
        String ctext = item.getctext();
        String size = item.getsize();
        String seller = item.getsellerid();
        System.out.println(ctext);
        if (item.getquantity() < 2) {
            String data = "{" +
                    "\"customerid\"" + ":" + "\"" + customerid + "\"," +
                    "\"quantity\"" + ":" + "\"" + 1 + "\"," +
                    "\"size\"" + ":" + "\"" + size + "\"," +
                    "\"sellerid\"" + ":" + "\"" + seller + "\"," +
                    "\"caketext\"" + ":" + "\"" + ctext + "\"," +
                    "\"product\"" + ":" + "\"" + itemid + "\"" +
                    "}";
            Submit2(data, deletefromcarturl);
            removeItem(position);
            frg = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        } else {
            String data = "{" +
                    "\"customerid\"" + ":" + "\"" + customerid + "\"," +
                    "\"quantity\"" + ":" + "\"" + -1 + "\"," +
                    "\"size\"" + ":" + "\"" + size + "\"," +
                    "\"sellerid\"" + ":" + "\"" + seller + "\"," +
                    "\"caketext\"" + ":" + "\"" + ctext + "\"," +
                    "\"product\"" + ":" + "\"" + itemid + "\"" +
                    "}";
            Submit2(data, addtocarturl);

            frg = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }


    }

    @Override
    public void onAddQtyClick(int position) {
        CartItem item = mExampleList.get(position);
        String itemid = item.getid();
        String size = item.getsize();
        String ctext = item.getctext();
        String seller = item.getsellerid();
        System.out.println(itemid);
        String data = "{" +
                "\"customerid\"" + ":" + "\"" + customerid + "\"," +
                "\"quantity\"" + ":" + "\"" + 1 + "\"," +
                "\"size\"" + ":" + "\"" + size + "\"," +
                "\"sellerid\"" + ":" + "\"" + seller + "\"," +
                "\"caketext\"" + ":" + "\"" + ctext + "\"," +
                "\"product\"" + ":" + "\"" + itemid + "\"" +
                "}";
        Submit2(data, addtocarturl);

        totalprice += item.getprice();
        frg = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    private void Submit2(String data, String URL) {
        final String savedata = data;
//        String URL="http://192.168.1.107:8000/products/addtocart/";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                    String msg = objres.getString("response");
                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

