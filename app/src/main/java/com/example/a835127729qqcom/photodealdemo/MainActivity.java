package com.example.a835127729qqcom.photodealdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a835127729qqcom.photodealdemo.util.SaveBitmap2File;
import com.example.a835127729qqcom.photodealdemo.widget.RotatableTextCloudLayout;
import com.example.a835127729qqcom.photodealdemo.widget.StickerView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xinlan.imageeditlibrary.editimage.view.CropImageView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    CropImageView cropImageView;
    ActionImageView guaKa;
    String testurl = "http://www.iteye.com/upload/logo/user/254048/1468917d-4784-3baa-a365-68315ed82ebb.jpg?1274705681";
    StickerView stickerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageLoader();
        cropImageView = (CropImageView) findViewById(R.id.crop);
        guaKa = (ActionImageView) findViewById(R.id.guagua);

        ImageLoader.getInstance().displayImage(testurl,guaKa,new ImageLoadingListener(){

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.i("cky",failReason.toString());
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.i("cky","com");
                guaKa.init();
                guaKa.invalidate();
                cropImageView.setRatioCropRect(guaKa.getmRect(),1);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        stickerView = (StickerView) findViewById(R.id.stick);

        guaKa.setmBackTextActionListener(stickerView);
        stickerView.setmTextsControlListener(guaKa);
    }

    /**
     * 初始化图片载入框架
     */
    private void initImageLoader() {
        File cacheDir = StorageUtils.getCacheDirectory(this);
        int MAXMEMONRY = (int) (Runtime.getRuntime().maxMemory());

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).memoryCacheExtraOptions(480, 800).defaultDisplayImageOptions(defaultOptions)
                .diskCacheExtraOptions(480, 800, null).threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(MAXMEMONRY / 5))
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()).build();

        ImageLoader.getInstance().init(config);
    }


    public void back(View view){
        Log.i("tag","back");
        guaKa.back();
    }

    public void crop(View view){
        if(guaKa.getMode()!=4) return;
        Log.i("cky","crop="+cropImageView.getCropRect().toString());
        guaKa.crop(cropImageView.getCropRect());
    }

    public void change(View view){
        guaKa.setEnabled(true);
        cropImageView.setVisibility(View.GONE);
        stickerView.setVisibility(View.GONE);
        if(guaKa.getMode()==4){
            guaKa.setMode(1);
        }else{
            guaKa.setMode(guaKa.getMode()+1);
            if(guaKa.getMode()==3) {
                stickerView.setVisibility(View.VISIBLE);
                guaKa.setEnabled(false);
            }
            if(guaKa.getMode()==4){
                cropImageView.setVisibility(View.VISIBLE);
                cropImageView.setCropRect(guaKa.getRotatedmRect());
                guaKa.setEnabled(false);
            }
        }
        Toast.makeText(this,guaKa.getMode()+"",Toast.LENGTH_SHORT).show();
    }

    public void rotate(View view){
        Log.i("tag","rotate");
        guaKa.rotate(guaKa.mCurrentAngle+90);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

}
