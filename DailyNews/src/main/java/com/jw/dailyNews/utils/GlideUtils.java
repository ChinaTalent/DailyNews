package com.jw.dailyNews.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jw.dailyNews.BaseApplication;

import Lib.MyNews;

/**
 * 创建时间：2017/8/16
 * 更新时间：2017/11/11 0011 上午 12:31
 * 作者：Mr.jin
 * 描述：Glide工具类，判断网络环境是否为wifi，应用是否配置只在wifi条件下加载图片
 */
public class GlideUtils {
    private static boolean isvImageDownloadOnlyWifi=CacheUtils.getCacheBoolean(
            "isvImageDownloadOnlyWifi",true);
    private static boolean isWIFI=NetUtils.isNetworkAvailable(MyNews.getInstance().getContext()).equals("WIFI");

    /**
     * 如果当前网络环境不为wifi，且应用配置为只在wifi下加载图片，则不加载传入链接的图片
     * @param context
     * @param url 图片链接
     * @param view 加载图片的view
     */
    public static void load(Context context, String url, ImageView view){
        if((isvImageDownloadOnlyWifi&&isWIFI)||!isvImageDownloadOnlyWifi)
            Glide.with(context).load(url).apply(BaseApplication.options).into(view);
    }
}
