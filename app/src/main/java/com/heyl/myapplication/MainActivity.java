package com.heyl.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heyl.myapplication.utils.AddressDao;
import com.heyl.myapplication.utils.IOUtils;

import net.youmi.android.AdManager;
import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;
import net.youmi.android.normal.spot.SpotManager;
import net.youmi.android.normal.video.VideoAdManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private LinearLayout bannerLayout;
    private EditText originalText;
    private ImageView delete;
    private Button confirm;
    private TextView translationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        //初始化布局
        init();
    }


    private void init() {
        bannerLayout = (LinearLayout) findViewById(R.id.ll_banner);
        bannerLayout.setVisibility(View.VISIBLE);

        originalText = (EditText) findViewById(R.id.et_original);

        delete = (ImageView) findViewById(R.id.img_delete);
        delete.setOnClickListener(this);

        confirm = (Button) findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(this);

        translationText = (TextView) findViewById(R.id.tv_translation);

        //设置广告条
        setupBannerAd();
    }


    /**
     * 设置广告条广告
     */
    private void setupBannerAd() {

        /**
         * 悬浮布局
         */
        // 实例化LayoutParams
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置广告条的悬浮位置，这里示例为右下角
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        // 获取广告条控件
        final View bannerView =
                BannerManager.getInstance(this).getBannerView(new BannerViewListener
                        () {

                    @Override
                    public void onRequestSuccess() {
                        Log.i(TAG, "请求广告条成功");
                    }

                    @Override
                    public void onSwitchBanner() {
                        Log.i(TAG, "广告条切换");
                    }

                    @Override
                    public void onRequestFailed() {
                        Log.i(TAG, "请求广告条失败");
                    }
                });
        ((Activity) this).addContentView(bannerView, layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_delete:
                originalText.setText("");
                break;
            case R.id.btn_confirm:
                String chinese = AddressDao.queryAddress(originalText.getText().toString().trim(), this);
                if (TextUtils.isEmpty(chinese)) {
                    Toast.makeText(this, "没有查询到", Toast.LENGTH_SHORT).show();
                    translationText.setText("");
                } else {
                    translationText.setText(chinese);
                }
                Log.e("查询", chinese + "bh");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
        super.onBackPressed();
    }

}
