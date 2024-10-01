package com.bigoval.foodfever;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    TextView welcome;
    ImageView actionbarlogo;
    SearchView searchView;
    Button logout;
    DatabaseHelper dbhelper;
    DatabaseHelper dbhelper2;
    String r;
    RequestQueue rq;
    RequestQueue rqueue;
    Boolean loggedin = false;
    String Customername;
    String Customerphone;
    String Token;
    public static final int a = R.id.nav_order;
    public LinearLayout searchtoolbar;
    String id;
    String appid ="1234";
    String loginapiurl = "http://dpend.pythonanywhere.com/accounts/Loginapi/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        dbhelper = new DatabaseHelper(this);
        dbhelper2 = new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        SQLiteDatabase db3 = dbhelper.getReadableDatabase();

        String details[] = {"loggedin"};
        final Cursor c = db.query("login", details, null, null, null, null, null);

        if (c.getCount() == 0) {
            SQLiteDatabase db2 = dbhelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("loggedin", 0);
            db2.insert("login", null, values);
        }
        c.moveToFirst();
        rq = Volley.newRequestQueue(this);
        rqueue = Volley.newRequestQueue(this);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        logout = header.findViewById(R.id.logoutbtn);
        welcome = header.findViewById(R.id.usersname);
        actionbarlogo = findViewById(R.id.actionbarlogo);

        String token[] = {"token"};
        Cursor c2 = db3.query("tokens", token, null, null, null, null, null);
        c2.moveToFirst();

        if (c.getInt(0) == 1) {
            loggedin = true;

            try {
                r = c2.getString(0);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                c.close();
                c2.close();
            }
        }
        else {
            SharedPreferences preferences = getSharedPreferences("LoggedinData",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("Loggedin",false);
            editor.apply();
        }
//    welcome.setText(b);

        if (!loggedin) {
            logout.setText("Login");
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawer) {
                closeKeyboard();
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Checklist();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_message);
        }
        searchtoolbar = findViewById(R.id.searchtoolbar);
        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = (String) "" + searchView.getQuery();
                Bundle cbundle = new Bundle();
                cbundle.putString("search", query);
                cbundle.putString("type", "search");
                Fragment filteredproducts = new SearchResultFragment();
                filteredproducts.setArguments(cbundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, filteredproducts).addToBackStack(null).commit();
                searchView.clearFocus();
                closeKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

//        if (searchView.isF){
//            fragment2 = new SearchResultFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment2).addToBackStack(null).commit();
//        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loggedin) {
                    SQLiteDatabase db = dbhelper.getReadableDatabase();
                    db.execSQL("UPDATE " + "login" + " SET loggedin = 0 WHERE loggedin = 1");
                    db.execSQL("DELETE FROM " + "tokens");
                    db.execSQL("DELETE FROM " + "user_data");
                    finish();
                    startActivity(getIntent());
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);

                }

            }
        });

        if (loggedin) {
            String[] userdetails = {"name", "phone","id"};
            Cursor c3 = db.query("user_data", userdetails, null, null, null, null, null);
            c3.moveToFirst();
            SharedPreferences preferences = getSharedPreferences("LoggedinData",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("Loggedin",true);
            editor.putString("Customerid",c3.getString(2));
            editor.apply();
            try {
                Customername = c3.getString(0);
                Customerphone = c3.getString(1);
                welcome.setText("Welcome " + Customername);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                c3.close();
            }

        }
        actionbarlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowHome();
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (currentFragment instanceof HomeFragment) {
            MainActivity.this.finish();
        }
//        else if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
//            finish();
//        }
//        else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        }
        else {
            super.onBackPressed();
        }


    }

    public void ShowHome() {
        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }


    Fragment fragment = null;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getNumericShortcut()+"") {

            case "1":
                ShowHome();
                break;
            case "2":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryFragment()).addToBackStack(null).commit();
                break;
            case "3":
                if (loggedin) {
                    Bundle bundle = new Bundle();
                    bundle.putString("customername", Customername);
                    bundle.putString("customerphone", Customerphone);
                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case "6":
                if (loggedin) {
                    Bundle addbundle = new Bundle();
                    addbundle.putString("customerphone", Customerphone);
                    Fragment fragment = new AddressFragment();
                    fragment.setArguments(addbundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case "5":
                if (loggedin) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("loggedin", loggedin);
                    Fragment fragment = new CartFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case "4":
                if (loggedin) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdereditemsFragment()).addToBackStack(null).commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case "7":
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Food Fever");
                    String shareMessage = "\nOrder food from you favorite restaurants and bakeries\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + appid + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Food Fever"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case "8":
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+919895491556"));
                startActivity(intent);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getNumericShortcut()+"") {
            case "0":
                if (loggedin) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).addToBackStack(null).commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }

    private void Checklist() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://dpend.pythonanywhere.com/androidappinfo/", null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String latestversion = response.getString("latestversion");
                    int updatenumber = response.getInt("updatenumber");
                    String updatecompulsary = response.getString("appupdatecompulsary");
                    String appenabled = response.getString("enabled");

                    String appdisabledmsg;
                    try{
                        appdisabledmsg = response.getString("disabledmsg") + "";
                    }catch (Exception e){
                        appdisabledmsg ="";
                    }
                    PackageManager manager = getApplicationContext().getPackageManager();
                    PackageInfo info = manager.getPackageInfo(
                            getApplicationContext().getPackageName(), 0);
                    String version = info.versionName;
                    if(appenabled.equals("True")){
                        if (!version.equals(latestversion)) {


                            AlertDialog.Builder Abuilder = new AlertDialog.Builder(MainActivity.this);
                            View vview = getLayoutInflater().inflate(R.layout.updateappalert, null);
                            final Button upnow = vview.findViewById(R.id.upalertupnow);
                            final Button cancel = vview.findViewById(R.id.upalertcancel);

                            Abuilder.setView(vview);
                            final AlertDialog dialog = Abuilder.create();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);
                            dialog.show();
                            upnow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            });
                            if(updatecompulsary.toLowerCase().equals("true")){
                                cancel.setText("Exit");
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                });

                            }else{
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }

                            System.out.println("x" + version + "x");
                            Toast.makeText(getApplicationContext(), "Update Available..Please Update the app", Toast.LENGTH_LONG).show();
                        }

                    }
//                    else {
//                        AlertDialog.Builder Abuilder = new AlertDialog.Builder(MainActivity.this);
//                        View vview = getLayoutInflater().inflate(R.layout.appdisabledalert, null);
//                        final Button exit = vview.findViewById(R.id.appdisalex);
//                        final TextView textView = vview.findViewById(R.id.appdisaltext);
//                        textView.setText(appdisabledmsg);
//                        Abuilder.setView(vview);
//                        final AlertDialog dialog = Abuilder.create();
//                        dialog.setCanceledOnTouchOutside(false);
//                        dialog.setCancelable(false);
//                        dialog.show();
//                        exit.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                finish();
//                            }
//                        });
//                    }



                } catch (JSONException | PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        rq.add(jsonObjectRequest);

    }
    private void getid() {
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

}