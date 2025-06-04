package com.crud_with_sqllite.util;

import android.content.Context;
import android.widget.Toast;

public class MessageUtil {
    public static void showMessage(String message, Context context){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
