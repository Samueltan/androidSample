package com.overture.questdroid;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.overture.questdroid.fragment.ContestsFragment;
import com.overture.questdroid.utility.BitmapHelp;

/**
 * Author: xUtils
 * Date: 13-10-9
 * Time: ����5:26
 */
public class ImageActivity extends Activity {

    @ViewInject(R.id.big_img)
    private ImageView bigImage;

    private BitmapUtils bitmapUtils;

    private BitmapDisplayConfig bigPicDisplayConfig;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        ViewUtils.inject(this);

        String imgUrl = getIntent().getStringExtra("url");

        bitmapUtils = ContestsFragment.bitmapUtils;
        if (bitmapUtils == null) {
            bitmapUtils = BitmapHelp.getBitmapUtils(this.getApplicationContext());
        }

        bigPicDisplayConfig = new BitmapDisplayConfig();
        //bigPicDisplayConfig.setShowOriginal(true); // ��ʾԭʼͼƬ,��ѹ��, ������Ҫʹ��, ͼƬ̫��ʱ����OOM��
        bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
        bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(this));

        BitmapLoadCallBack<ImageView> callback = new DefaultBitmapLoadCallBack<ImageView>() {
            @Override
            public void onLoadStarted(ImageView container, String uri, BitmapDisplayConfig config) {
                super.onLoadStarted(container, uri, config);
                Toast.makeText(getApplicationContext(), uri, 300).show();
                LogUtils.d(uri);
            }

            @Override
            public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
                super.onLoadCompleted(container, uri, bitmap, config, from);
                Toast.makeText(getApplicationContext(), bitmap.getWidth() + "*" + bitmap.getHeight(), 300).show();
                LogUtils.d(bitmap.getWidth() + "*" + bitmap.getHeight());
            }
        };

        bitmapUtils.display(bigImage, imgUrl, bigPicDisplayConfig, callback);
        // ��ȡassets�е�ͼƬ
        //bitmapUtils.display(bigImage, "assets/img/wallpaper.jpg", bigPicDisplayConfig, callback);
    }
}