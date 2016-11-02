package com.heyl.myapplication;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

import com.heyl.myapplication.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by heyl on 2016/11/1.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //拷贝数据库
        copyDb();
    }

    /**
     * 拷贝数据库
     */
    private void copyDb() {
        File file = getDatabasePath("dictionary.db");
        //file.exists() 判断文件是否存在
        if (!file.exists()) {
            AssetManager am = getAssets();//获取asset管理者
            InputStream in = null;
            FileOutputStream out = null;
            try {
                Log.e("数据库","file");
                in = am.open("dictionary.db");
                //getCacheDir() : 获取缓存目录     getFilesDir():获取文件路径
                out = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int len = -1;
                while((len = in.read(b)) != -1){
                    out.write(b, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                //关流的操作
//				out.close();
//				in.close();
                IOUtils.closeQuietly(out);
                IOUtils.closeQuietly(in);
            }
        }
    }
}
