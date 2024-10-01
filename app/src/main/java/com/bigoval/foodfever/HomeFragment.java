package com.bigoval.foodfever;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements Adapter.OnItemClickListener, ViewPagerAdapter.OnItemClickListener, HomescreenimageAdapter.OnItemClickListener,homescreenshopesadapter.OnItemClickListener {
    private Timer timer;
    private int current_position = 0;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private TextView rtextview;
    private ImageView[] dots;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private RecyclerView shopsRecyclerview;
    private homescreenshopesadapter mhomescreenshopesadapter;
    private Adapter mAdapter;
    private HomescreenimageAdapter mAdapter2;
    private ArrayList<card> mExampleList;
    private ArrayList<HomescreenimageItem> mExampleList2;
    private ArrayList<homescreenshopsitem> mshopitemlist;
    private RequestQueue mRequestQueue;
    private RequestQueue mshopRequestQueue;
    RequestQueue rq;
    private RequestQueue mRequestQueue2;
    List<SliderUtils> sliderImg;
    ViewPagerAdapter viewPagerAdapter;

    String request_url = "http://dpend.pythonanywhere.com/headerimages/";
    String imageurl="http://dpend.pythonanywhere.com";
    String homescreencardsurl="http://dpend.pythonanywhere.com/homescreencards/";
    String homescreenimageurl="http://dpend.pythonanywhere.com/headerimages2/";
    String homescreenshopinfourl="http://dpend.pythonanywhere.com/shopsinfo/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview= inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = mview.findViewById(R.id.recycler_view2);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearlayoutmanager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearlayoutmanager);
        shopsRecyclerview=mview.findViewById(R.id.homeshoprecyclerview);
        shopsRecyclerview.setHasFixedSize(true);
        mExampleList = new ArrayList<>();
        mshopitemlist=new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        mshopRequestQueue = Volley.newRequestQueue(getActivity());
        mRecyclerView2 = mview.findViewById(R.id.homescreenimagesrecyclerview);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExampleList2 = new ArrayList<>();
        mRequestQueue2 = Volley.newRequestQueue(getActivity());
        rq = CustomVolleyRequest.getInstance(getActivity()).getRequestQueue();
        rtextview=mview.findViewById(R.id.restaurantstext);
        sliderImg = new ArrayList<>();

        viewPager = mview.findViewById(R.id.viewPager);

        sliderDotspanel = mview.findViewById(R.id.SliderDots);
        if(isNetworkAvailable()){
            try {
                sendRequest();
                parseJSON();
                parseJSON2();
                parseJSON3();
            }catch (Exception e){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ErrorFragment()).commit();

            }

        }
        else {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ErrorFragment()).commit();
        }



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return  mview;
    }
    public void sendRequest(){
        final LoadingDialog ld=new LoadingDialog(getActivity());
        ld.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ld.show();
        ld.setCanceledOnTouchOutside(getRetainInstance());


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ld.dismiss();
                rtextview.setVisibility(View.VISIBLE);

                for(int i = 0; i < response.length(); i++){

                    SliderUtils sliderUtils = new SliderUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        sliderUtils.setSliderImageUrl(imageurl+jsonObject.getString("image"));
                        sliderUtils.setProduct(jsonObject.getString("product"));
                        sliderUtils.setName(jsonObject.getString("name"));
                        sliderUtils.setType(jsonObject.getString("type"));
                        sliderUtils.setCategory(jsonObject.getString("category"));
                        sliderUtils.setSeller(jsonObject.getString("seller"));
                        sliderUtils.setLink(jsonObject.getString("link"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sliderImg.add(sliderUtils);

                }

                viewPagerAdapter = new ViewPagerAdapter(sliderImg, getActivity());
                viewPagerAdapter.setOnItemClickListener(HomeFragment.this);
                viewPager.setAdapter(viewPagerAdapter);
                createSlideShow();

                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for(int i = 0; i < dotscount; i++){

                    dots[i] = new ImageView(getActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ld.dismiss();
                System.out.println(error);
                try {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ServerErrorFragment()).commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        CustomVolleyRequest.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);

    }
    private void createSlideShow(){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (current_position==sliderImg.size())
                    current_position=0;
                viewPager.setCurrentItem(current_position++,true);

            }
        };
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },250,8000);
    }
    private void parseJSON() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, homescreencardsurl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject hcard = response.getJSONObject(i);

                        card item = new card();
                        item.setImagename1(hcard.getString("image1title").toString());
                        item.setImagename2(hcard.getString("image2title").toString());
                        item.setImagename3(hcard.getString("image3title"));
                        item.setImagename4(hcard.getString("image4title"));
                        item.setcheading(hcard.getString("heading"));
                        item.setImageUrl1(imageurl+hcard.getString("image1"));
                        item.setImageUrl2(imageurl+hcard.getString("image2"));
                        item.setImageUrl3(imageurl+hcard.getString("image3"));
                        item.setImageUrl4(imageurl+hcard.getString("image4"));
                        item.setImage1type(hcard.getString("image1type"));
                        item.setImage2type(hcard.getString("image2type"));
                        item.setImage3type(hcard.getString("image3type"));
                        item.setImage4type(hcard.getString("image4type"));
                        item.setLink1(hcard.getString("link1"));
                        item.setLink2(hcard.getString("link2"));
                        item.setLink3(hcard.getString("link3"));
                        item.setLink4(hcard.getString("link4"));
                        try{
                            item.setImage1category(hcard.getString("image1category"));
                            item.setImage2category(hcard.getString("image2category"));
                            item.setImage3category(hcard.getString("image3category"));
                            item.setImage4category(hcard.getString("image4category"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            item.setImage1product(hcard.getString("image1product"));
                            item.setImage2product(hcard.getString("image2product"));
                            item.setImage3product(hcard.getString("image3product"));
                            item.setImage4product(hcard.getString("image4product"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        item.setImage1seller(hcard.getString("image1seller"));
                        item.setImage2seller(hcard.getString("image2seller"));
                        item.setImage3seller(hcard.getString("image3seller"));
                        item.setImage4seller(hcard.getString("image4seller"));



                        mExampleList.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mAdapter = new Adapter(getActivity().getApplicationContext(),mExampleList);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(HomeFragment.this);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        mRequestQueue.add(jsonArrayRequest);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    private void parseJSON2() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, homescreenimageurl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject himage = response.getJSONObject(i);

                        HomescreenimageItem item = new HomescreenimageItem();
                        item.setImageUrl(imageurl+himage.getString("image"));
                        item.settype(himage.getString("type"));
                        item.setcategory(himage.getString("category"));
                        item.setlink(himage.getString("link"));
                        try {
                            item.setproduct(himage.getString("product"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        item.setname(himage.getString("name"));
                        item.setseller(himage.getString("seller"));
                        mExampleList2.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mAdapter2 = new HomescreenimageAdapter(getActivity().getApplicationContext(),mExampleList2);
                mRecyclerView2.setAdapter(mAdapter2);
                mAdapter2.setOnItemClickListener(HomeFragment.this);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        mRequestQueue2.add(jsonArrayRequest);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void parseJSON3() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, homescreenshopinfourl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);

                        homescreenshopsitem item = new homescreenshopsitem();
                        item.setShopImage(object.getString("image"));
                        item.setShopname(object.getString("name"));
                        item.setId(object.getString("id"));
                        item.setShopBanner(object.getString("banner"));
                        item.setShopAddress(object.getString("address"));
                        mshopitemlist.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mhomescreenshopesadapter = new homescreenshopesadapter(getActivity().getApplicationContext(),mshopitemlist);
                shopsRecyclerview.setAdapter(mhomescreenshopesadapter);
                mhomescreenshopesadapter.setOnItemClickListener(HomeFragment.this);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        mshopRequestQueue.add(jsonArrayRequest);

    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        card clickedItem = mExampleList.get(position);
        String type = clickedItem.getImage1type();
        switch (type) {
            case "product": {
                bundle.putString("type", "card");
                bundle.putString("productid", clickedItem.getImage1product());
                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "category": {
                bundle.putString("type", "category");
                bundle.putString("search", clickedItem.getImage1category());
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "seller": {
                bundle.putString("type", "seller");
                bundle.putString("seller", clickedItem.getImage1seller());
                bundle.putString("search", clickedItem.getImage1category());
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "link":{
                String url = clickedItem.getLink1();
                if (!url.startsWith("https://") && !url.startsWith("http://")){
                    url = "http://" + url;
                }
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (openUrlIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(openUrlIntent);
                }
            }
        }

    }
    public void onItemClick2(int position) {
        Bundle bundle = new Bundle();
        card clickedItem = mExampleList.get(position);
        String type = clickedItem.getImage2type();
        switch (type) {
            case "product": {
                bundle.putString("type", "card");
                bundle.putString("productid", clickedItem.getImage2product());
                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "category": {
                bundle.putString("type", "category");
                bundle.putString("search", clickedItem.getImage2category());
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "seller": {
                bundle.putString("type", "seller");
                bundle.putString("seller", clickedItem.getImage2seller());
                bundle.putString("search", clickedItem.getImage2category());
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "link":{
                String url = clickedItem.getLink2();
                if (!url.startsWith("https://") && !url.startsWith("http://")){
                    url = "http://" + url;
                }
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (openUrlIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(openUrlIntent);
                }
            }
        }
    }

    @Override
    public void onItemClick3(int position) {
        Bundle bundle = new Bundle();
        card clickedItem = mExampleList.get(position);
        String type = clickedItem.getImage3type();
        switch (type) {
            case "product": {
                bundle.putString("type", "card");
                bundle.putString("productid", clickedItem.getImage3product());
                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "category": {
                bundle.putString("type", "category");
                bundle.putString("search", clickedItem.getImage3category());
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "seller": {
                bundle.putString("type", "seller");
                bundle.putString("seller", clickedItem.getImage3seller());
                bundle.putString("search", clickedItem.getImage3category());
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "link":{
                String url = clickedItem.getLink3();
                if (!url.startsWith("https://") && !url.startsWith("http://")){
                    url = "http://" + url;
                }
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (openUrlIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(openUrlIntent);
                }
            }
        }
    }

    @Override
    public void onItemClick4(int position) {
        Bundle bundle = new Bundle();
        card clickedItem = mExampleList.get(position);
        String type = clickedItem.getImage4type();
        switch (type) {
            case "product": {
                bundle.putString("type", "card");
                bundle.putString("productid", clickedItem.getImage4product());
                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "category": {
                bundle.putString("type", "category");
                bundle.putString("search", clickedItem.getImage4category());
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "seller": {
                bundle.putString("type", "seller");
                bundle.putString("seller", clickedItem.getImage4seller());
                bundle.putString("search", clickedItem.getImage4category());
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "link":{
                String url = clickedItem.getLink4();
                if (!url.startsWith("https://") && !url.startsWith("http://")){
                    url = "http://" + url;
                }
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (openUrlIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(openUrlIntent);
                }
            }
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onImageClick(int position) {
        SliderUtils item = sliderImg.get(position);
        Bundle bundle = new Bundle();
        String type = item.gettype();
        String category = item.getcategory();
        String seller = item.getseller();
        switch (type) {
            case "product": {
                bundle.putString("type", "sliderimage");
                bundle.putString("productid", item.getproduct());
                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "seller": {
                bundle.putString("type", "seller");
                bundle.putString("seller", seller);
                bundle.putString("search", category);
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "link":{
                String url = item.getlink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }
    }

    @Override
    public void Onimageitemclick(int position) {
        HomescreenimageItem item = mExampleList2.get(position);
        Bundle bundle = new Bundle();
        String name = item.getname();
        String type = item.gettype();
        String category = item.getcategory();
        String seller = item.getseller();
        String product = item.getproduct();
        switch (type) {
            case "product": {
                bundle.putString("type", "homescreenimage");
                bundle.putString("productid", product);
                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "seller": {
                bundle.putString("type", "seller");
                bundle.putString("seller", seller);
                bundle.putString("search", category);
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                break;
            }
            case "link":{
                String url = item.getlink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }
    }

    @Override
    public void onShopItemClick(int position) {
        homescreenshopsitem hssci = mshopitemlist.get(position);
        String id = hssci.getId();
        String name = hssci.getShopname();
        String banner = hssci.getShopBanner();
        String address =hssci.getShopAddress();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("name",name);
        bundle.putString("banner",banner);
        bundle.putString("address",address);
        ShopFragment shopFragment = new ShopFragment();
        shopFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, shopFragment).addToBackStack(null).commit();
    }
}
