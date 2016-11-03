package com.heyl.myapplication;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heyl.myapplication.utils.AddressDao;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
                //Log.e("查询", chinese + "bh");
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
