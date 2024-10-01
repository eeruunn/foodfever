package com.bigoval.foodfever;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryFragment extends Fragment implements CategoryAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private CategoryAdapter mExampleAdapter;
    private ArrayList<CategoryItem> mExampleList;
    private RequestQueue mRequestQueue;

    Boolean grandparent = false;
    String parent;

    String categorylisturl="http://dpend.pythonanywhere.com/products/getcategories/";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_category, container, false);

        mRecyclerView = mview.findViewById(R.id.category_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExampleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        try {
            Bundle bundle = new Bundle(this.getArguments());
            parent = bundle.getString("parent");

        } catch (Exception e) {
            parent = "None";
        }
        if(isNetworkAvailable()){
            parseJSON();
        }
        else {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ErrorFragment()).addToBackStack(null).commit();

        }

        return mview;
    }
    private void parseJSON() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, categorylisturl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject category = response.getJSONObject(i);
                        if(category.getString("parent").equals(parent)){
                            CategoryItem item = new CategoryItem();
                            item.setname(category.getString("name").toString());
                            item.setchildcount(category.getString("childcount").toString());
                            item.setimage("http://dpend.pythonanywhere.com/media/"+category.getString("image").toString());
                            item.setparent(category.getString("parent").toString());
                            System.out.println(category.getString("parent"));
                            item.setid(category.getString("id").toString());
                            mExampleList.add(item);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mExampleAdapter = new CategoryAdapter(getActivity().getApplicationContext(),mExampleList);
                mRecyclerView.setAdapter(mExampleAdapter);
                mExampleAdapter.setOnItemClickListener(CategoryFragment.this);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        mRequestQueue.add(jsonArrayRequest);

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onItemClick(int position) {
        CategoryItem citem = mExampleList.get(position);
        Bundle bundle = new Bundle();
        if(citem.getparent().equals("None")){
            bundle.putString("parent",citem.getmname());

        }
        else {
            bundle.putString("parent",citem.getparent() + " -> "+citem.getmname());
        }
        if(citem.getchildcount().equals("0")){
            Bundle b2 = new Bundle();
//            b2.putString("search",citem.getparent() + " -> "+citem.getmname());
            b2.putString("type","category");
            b2.putString("search",citem.getparent() + " -> "+citem.getmname());
            Fragment fragment = new SearchResultFragment();
            fragment.setArguments(b2);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        }
        else{
            Fragment fragment = new CategoryFragment();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        }

    }
}
