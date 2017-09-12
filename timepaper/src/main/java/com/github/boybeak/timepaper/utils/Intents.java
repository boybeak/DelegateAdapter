package com.github.boybeak.timepaper.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.StringRes;

import java.io.File;

/**
 * Created by gaoyunfei on 2016/12/7.
 */

public abstract class Intents {

    public static void viewMyAppOnStore (Context context) {
        viewAppOnStore(context, context.getPackageName());
    }

    public static void viewAppOnStore (Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
        context.startActivity(intent);
    }

    public static void openUrl (Context context, String url) {
        Intent it = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(url));;
        context.startActivity(it);
    }

    public static void shareText(Context context, String chooserTitle, String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, url)
                .setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, chooserTitle));
    }

    public static void shareText(Context context, @StringRes int chooserTitleRes, String url) {
        shareText(context, context.getString(chooserTitleRes), url);
    }

    public static void shareImage (Context context, String chooserTitle, Uri uri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_STREAM, uri)
                .setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, chooserTitle));
    }

    public static void shareImage (Context context, String chooserTitle, File file) {
        shareImage(context, chooserTitle, Uri.fromFile(file));
    }

    public static void shareImage (Context context, String chooserTitle, String path) {
        shareImage(context, chooserTitle, new File(path));
    }

    public static void shareImage (Context context, @StringRes int chooserTitleRes, Uri uri) {
        shareImage(context, context.getString(chooserTitleRes), uri);
    }

    public static void shareImage (Context context, @StringRes int chooserTitleRes, File file) {
        shareImage(context, chooserTitleRes, Uri.fromFile(file));
    }

    public static void shareImage (Context context, @StringRes int chooserTitleRes, String path) {
        shareImage(context, chooserTitleRes, new File(path));
    }

    public static void shareImage (Context context, String chooserTitle, Bitmap bitmap) {
        String pathofBmp = MediaStore.Images.Media.insertImage(
                context.getContentResolver(), bitmap, System.currentTimeMillis() + ".jpg", null);
        shareImage(context, chooserTitle, pathofBmp);
        new File(pathofBmp).deleteOnExit();
    }

    public static void shareImage (Context context, @StringRes int chooserTitleRes, Bitmap bitmap) {
        String pathofBmp = MediaStore.Images.Media.insertImage(
                context.getContentResolver(), bitmap, System.currentTimeMillis() + ".jpg", null);
        shareImage(context, context.getString(chooserTitleRes), pathofBmp);
    }

    public static void call (Context context, String phoneNumber) {
        Intent it = new Intent (Intent.ACTION_DIAL);
        String uri = "tel:" + phoneNumber.trim() ;
        it.setData(Uri.parse(uri));
        context.startActivity(it);
    }

    public static void emailTo (Context context, String email, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_CC, email);
        intent.putExtra(Intent.EXTRA_BCC, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        context.startActivity(Intent.createChooser(intent, "Send Email"));
    }

}