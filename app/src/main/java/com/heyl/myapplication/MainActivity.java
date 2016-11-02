package com.heyl.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import net.youmi.android.AdManager;
import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;
import net.youmi.android.normal.spot.SpotManager;
import net.youmi.android.normal.video.VideoAdManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private PermissionHelper mPermissionHelper;
    private LinearLayout bannerLayout;
    private EditText originalText;
    private ImageView delete;
    private Button confirm;
    private TextView translationText;
    private boolean isHavebanner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        //初始化布局
        init();
        // 当系统为6.0以上时，需要申请权限
        permission();
        //设置广告条
        setupBannerAd();
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
    }
    /**
     * 申请权限
     */
    private void permission() {
        // 当系统为6.0以上时，需要申请权限
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
                runApp();
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
            runApp();
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
                Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
                runApp();
            } else {
                // 如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void runApp() {
        AdManager.getInstance(this).init("90317fa210f0bc30", "159f21e7dfb37468", false, true);
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
                if(TextUtils.isEmpty(chinese)){
                    Toast.makeText(this,"没有查询到",Toast.LENGTH_SHORT).show();
                    translationText.setText("");
                }else{
                    translationText.setText(chinese);
                }
                Log.e("查询",  chinese+"bh");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //bannerLayout.setVisibility(View.VISIBLE);
        // 视频广告
        VideoAdManager.getInstance(this).onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 插播广告
        SpotManager.getInstance(this).onPause();
        // 视频广告
        VideoAdManager.getInstance(this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 插播广告
        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 插播广告
        SpotManager.getInstance(this).onDestroy();
        // 视频广告
        VideoAdManager.getInstance(this).onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
        super.onBackPressed();
    }

}
