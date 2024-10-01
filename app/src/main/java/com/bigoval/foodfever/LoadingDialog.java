package com.bigoval.foodfever;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

public class LoadingDialog extends Dialog {


    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);


    }

}