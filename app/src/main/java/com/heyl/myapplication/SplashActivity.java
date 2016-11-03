package com.heyl.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heyl.myapplication.utils.Content;
import com.heyl.myapplication.utils.IOUtils;

import net.youmi.android.AdManager;
import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SplashViewSettings;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends Activity {

    private PermissionHelper mPermissionHelper;

    private static final String TAG = "SplashActivity";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);
        TextView tv_version = (TextView)findViewById(R.id.tv_version);
        String pkName = this.getPackageName();
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(pkName,0).versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        tv_version.setText(versionName);
        mContext = this;
        permission();
        //拷贝数据库
        copyDb();
        init();
    }

    private void init() {
        RelativeLayout rl_welcome_bg = (RelativeLayout) findViewById(R.id.rl_welcome_bg);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);

        rl_welcome_bg.startAnimation(animationSet);
        // 监听动画
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(mContext,MainActivity.class));
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * 拷贝数据库
     */
    private void copyDb() {
        File file = new File(getFilesDir(), "dictionary.db");
        //file.exists() 判断文件是否存在
        if (!file.exists()) {
            //设置广告条
            AssetManager am = getAssets();//获取asset管理者
            InputStream in = null;
            FileOutputStream out = null;
            try {
                //Log.e("数据库", "file");
                in = am.open("dictionary.db");
                //getCacheDir() : 获取缓存目录     getFilesDir():获取文件路径
                out = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int len = -1;
                while ((len = in.read(b)) != -1) {
                    out.write(b, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //关流的操作
//				out.close();
//				in.close();
                IOUtils.closeQuietly(out);
                IOUtils.closeQuietly(in);
            }
        }
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
        AdManager.getInstance(this).init(Content.AppID, Content.AppSecrt, false, false);
    }
}
