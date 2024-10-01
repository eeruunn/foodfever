package com.bigoval.foodfever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Splash_Screen extends AppCompatActivity {
    RequestQueue rq;
    DatabaseHelper dbhelper;
    Boolean loggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        dbhelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String details[] = {"loggedin"};
        final Cursor c = db.query("login", details, null, null, null, null, null);
        if (c.getCount() == 0) {
            SQLiteDatabase db2 = dbhelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("loggedin", 0);
            db2.insert("login", null, values);
        }

        c.moveToFirst();
        loggedin = c.getInt(0) == 1;
        rq = Volley.newRequestQueue(this);
        Checklist();

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
                    if(appenabled.equals("False")){
                        AlertDialog.Builder Abuilder = new AlertDialog.Builder(Activity_Splash_Screen.this);
                        View vview = getLayoutInflater().inflate(R.layout.appdisabledalert, null);
                        final Button exit = vview.findViewById(R.id.appdisalex);
                        final TextView textView = vview.findViewById(R.id.appdisaltext);
                        textView.setText(appdisabledmsg);
                        Abuilder.setView(vview);
                        final AlertDialog dialog = Abuilder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.show();
                        exit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                    }else {
                        if(loggedin){
                            Intent intent =  new Intent(Activity_Splash_Screen.this,MainActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent =  new Intent(Activity_Splash_Screen.this,LoginActivity.class);
                            startActivity(intent);
                        }

                    }



                } catch (JSONException | PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
                if(error instanceof NetworkError){
                    Toast.makeText(Activity_Splash_Screen.this,"no internet connection",Toast.LENGTH_SHORT).show();
                    Shownetworkdialog();
                }
                else if(error instanceof ServerError){
                    Toast.makeText(Activity_Splash_Screen.this,"oops! something went wrong",Toast.LENGTH_SHORT).show();
                    Showservererrordialog();
                }
            }
        });

        rq.add(jsonObjectRequest);

    }
    private void Shownetworkdialog(){
        AlertDialog.Builder Abuilder = new AlertDialog.Builder(Activity_Splash_Screen.this);
        View vview = getLayoutInflater().inflate(R.layout.nonetworkdialog, null);
        final Button retry = vview.findViewById(R.id.nonetretry);
        final Button exit = vview.findViewById(R.id.nonetexit);

        Abuilder.setView(vview);
        final AlertDialog dialog = Abuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Activity_Splash_Screen.this,"Retrying",Toast.LENGTH_SHORT).show();
                Checklist();
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Showservererrordialog(){
        AlertDialog.Builder Abuilder = new AlertDialog.Builder(Activity_Splash_Screen.this);
        View vview = getLayoutInflater().inflate(R.layout.nonetworkdialog, null);
        final Button retry = vview.findViewById(R.id.nonetretry);
        final Button exit = vview.findViewById(R.id.nonetexit);
        final TextView nontext = vview.findViewById(R.id.nonettext);

        nontext.setText("Something went wrong");

        Abuilder.setView(vview);
        final AlertDialog dialog = Abuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Activity_Splash_Screen.this,"Retrying",Toast.LENGTH_SHORT).show();
                Checklist();
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}