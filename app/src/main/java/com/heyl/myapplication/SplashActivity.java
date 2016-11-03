package com.heyl.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heyl.myapplication.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends Activity {

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
}
