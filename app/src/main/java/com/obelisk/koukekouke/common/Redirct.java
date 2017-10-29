package com.obelisk.koukekouke.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/24.
 */

public class Redirct {

    public static void openActivity(Context context, Class<?> pClass, Bundle pBundle){
        Intent intent = new Intent(context, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        context.startActivity(intent);
    }
    public static void openActivity(Context context, Class<?> pClass) {
        openActivity(context, pClass, null);
    }
    public static void makeToast(final Context context, final String text){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void makeToast(final Context context, int id){
        final String text = context.getResources().getString(id);
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
