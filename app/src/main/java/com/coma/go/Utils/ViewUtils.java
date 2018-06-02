package com.coma.go.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.Toast;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.coma.go.R;

import java.util.Random;



/**
 * Created by Koma on 02.04.2018.
 */

public class ViewUtils {

    private static ProgressDialog progressDialog;
    public static void showDialogLoading(Context context) {
        progressDialog = ProgressDialog.show(context,"","Loading...", true, true);
    }
    public static void hideDialogLoading() {
        if(progressDialog != null)
            progressDialog.dismiss();
    }

    private static AlertDialog mErrorDialog;

    public static void showDialogError(Context context, String message) {
        message = translateErrorToMessage(message);
        mErrorDialog = new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(message)
                .setOnCancelListener(dialog -> hideDialogError())
                .show();
    }
    public static void showDialogError(Context context, Integer message) {
        mErrorDialog = new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage("Error with code:" + message)
                .setOnCancelListener(dialog -> hideDialogError())
                .show();
    }

    private static void hideDialogError() {
        if (mErrorDialog != null && mErrorDialog.isShowing()) {
            mErrorDialog.cancel();
        }
    }

    private static String translateErrorToMessage(String message){
        if(message == null)
            return "Unknown error";
        if(message.contains("IOException"))
            message = "Error establishing connection to server";
        if(message.contains("UnknownHostException"))
            message = "Exception occurred.";
        if(message.contains("Bad Gateway"))
            message = "Exception occurred.";
        if(message.contains("SocketTimeoutException"))
            message = "Server connection timeout.";
        if(message.contains("ConnectException"))
            message = "Failed to connect to server.";

        return message;
    }


    public static void showToast(Context context, String message){
        message = translateErrorToMessage(message);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, Integer code){
        String message = "Error with code:";
        Toast.makeText(context, message + code, Toast.LENGTH_SHORT).show();
    }

    public static int getColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(55) + 10, rnd.nextInt(20), rnd.nextInt(50) + 20);
    }

    public static void openMarket(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static RequestOptions getAvatarOptions(){
        return new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .circleCrop()
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_account_circle_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public static RequestOptions getImageOptions(){
        return new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_menu_camera)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

}
