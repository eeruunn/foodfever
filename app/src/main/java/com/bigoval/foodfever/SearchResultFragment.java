package com.bigoval.foodfever;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class SearchResultFragment extends Fragment implements ExampleAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    private String query;
    private String type;
    TextView result;
    String imageurl="http://dpend.pythonanywhere.com";
    String productlisturl="http://dpend.pythonanywhere.com/products/productlist/";
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKECOUNT = "likecount";
    public static final String EXTRA_STOCK = "stock";
    public static final String EXTRA_SELLER = "seller";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview= inflater.inflate(R.layout.fragment_searchres, container, false);
        Bundle cbundle = this.getArguments();
        query = cbundle.getString("search");
        type = cbundle.getString("type");
        result = mview.findViewById(R.id.result);
        mRecyclerView = mview.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExampleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        if(isNetworkAvailable()){
            if(!type.equals("seller")){
                String data = "{" +
                        "\"type\"" + ":" + "\"" + type + "\"," +
                        "\"query\"" + ":" + "\"" + query + "\"" +
                        "}";
                Submit(data);
            }
            else {
                String seller = cbundle.getString("seller");
                String data = "{" +
                        "\"sellerid\"" + ":" + "\"" + seller + "\"," +
                        "\"type\"" + ":" + "\"" + type + "\"," +
                        "\"query\"" + ":" + "\"" + query + "\"" +
                        "}";
                Submit(data);
            }
        }
        else {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ErrorFragment()).addToBackStack(null).commit();

        }


        return mview;
    }
//    private void parseJSON() {
//
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, productlisturl, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject product = response.getJSONObject(i);
//                        if(product.getString(type).toLowerCase().contains(query.toLowerCase())) {
//                            ExampleItem item = new ExampleItem();
//                            item.setCreator(product.getString("name").toString());
//                            item.setseller(product.getString("seller").toString());
//                            item.setdescription(product.getString("description").toString());
//                            item.setcategory(product.getString("category").toString());
//                            item.setLikeCount(product.getInt("price"));
//                            item.setStock(product.getInt("stock"));
//                            item.setImageUrl(imageurl + product.getString("image"));
//                            item.setImageUrl2(imageurl + product.getString("image2"));
//                            item.setImageUrl3(imageurl + product.getString("image3"));
//                            item.setImageUrl4(imageurl + product.getString("image4"));
//                            item.setImageUrl5(imageurl + product.getString("image5"));
//                            item.setImageUrl6(imageurl + product.getString("image6"));
//                            item.setImageUrl7(imageurl + product.getString("image7"));
//                            item.setid(product.getString("id"));
//
//                            mExampleList.add(item);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                mExampleAdapter = new ExampleAdapter(getActivity().getApplicationContext(),mExampleList);
//                mRecyclerView.setAdapter(mExampleAdapter);
//                mExampleAdapter.setOnItemClickListener(SearchResultFragment.this);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", "onErrorResponse: " + error.getMessage());
//            }
//        });
//
//        mRequestQueue.add(jsonArrayRequest);
//
//    }
private void Submit(String data) {
    final String savedata = data;
    String URL =productlisturl;
    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                System.out.println(jsonArray);
                if (jsonArray.length()==0 & !type.equals("category")){
                    result.setText("Sorry could'nt find : "+query);
                    result.setVisibility(View.VISIBLE);
                }
                else if(jsonArray.length()==0 & type.equals("category")) {
                    result.setText("Sorry no products found");
                    result.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                            JSONObject product = jsonArray.getJSONObject(i);
                            ExampleItem item = new ExampleItem();
                            item.setCreator(product.getString("name").toString());
                            item.setseller(product.getString("seller").toString());
                            item.setdescription(product.getString("description").toString());
                            item.setcategory(product.getString("category").toString());
                            item.setLikeCount(product.getInt("price"));
                            item.setrealprice(product.getInt("realprice"));
                            item.setStock(product.getInt("stock"));
                            item.setsellerid(product.getString("sellerid"));
                            item.setImageUrl(imageurl + product.getString("image"));
                            item.setImageUrl2(imageurl + product.getString("image2"));
                            item.setImageUrl3(imageurl + product.getString("image3"));
                            item.setImageUrl4(imageurl + product.getString("image4"));
                            item.setImageUrl5(imageurl + product.getString("image5"));
                            item.setImageUrl6(imageurl + product.getString("image6"));
                            item.setImageUrl7(imageurl + product.getString("image7"));
                            item.setwriteable(product.getBoolean("writeable"));
                            item.setid(product.getString("id"));

                            mExampleList.add(item);
                    } catch (JSONException e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                    }
                }
                mExampleAdapter = new ExampleAdapter(getActivity().getApplicationContext(),mExampleList);
                mRecyclerView.setAdapter(mExampleAdapter);
                mExampleAdapter.setOnItemClickListener(SearchResultFragment.this);
            } catch (JSONException e) {
                e.printStackTrace();
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
    mRequestQueue.add(stringRequest);
}

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        ExampleItem clickedItem = mExampleList.get(position);
        bundle.putString(EXTRA_CREATOR,clickedItem.getCreator());
        bundle.putString(EXTRA_SELLER,clickedItem.getseller());
        bundle.putString("image",clickedItem.getImageUrl());
        bundle.putString("image2",clickedItem.getImageUrl2());
        bundle.putString("image3",clickedItem.getImageUrl3());
        bundle.putString("image4",clickedItem.getImageUrl4());
        bundle.putString("image5",clickedItem.getImageUrl5());
        bundle.putString("image6",clickedItem.getImageUrl6());
        bundle.putString("image7",clickedItem.getImageUrl7());
        bundle.putString("description",clickedItem.getdescription());
        bundle.putString("productid",clickedItem.getid());
        bundle.putString("sellerid",clickedItem.getsellerid());
        bundle.putBoolean("writeable",clickedItem.getwriteable());
        bundle.putString("type","productfromproducts");
        bundle.putString(EXTRA_STOCK,Integer.toString(clickedItem.getStock()));
        bundle.putString(EXTRA_LIKECOUNT,Integer.toString(clickedItem.getLikeCount()));
        bundle.putString("realprice",Integer.toString(clickedItem.getrealprice()));
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (myImg != null) {
//            ViewGroup parentViewGroup = (ViewGroup) myImg.getParent();
//            if (parentViewGroup != null) {
//                parentViewGroup.removeAllViews();
//            }
//        }
//    }
}
