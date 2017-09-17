package com.example.dell.fangfangsmall.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.bean.Tab;
import com.example.dell.fangfangsmall.fragment.HomePageFragment;
import com.example.dell.fangfangsmall.fragment.TrainFragment;
import com.example.dell.fangfangsmall.fragment.VideoFragment;
import com.example.dell.fangfangsmall.fragment.VoiceFragment;
import com.example.dell.fangfangsmall.util.FragmentTabHost;
import com.example.dell.fangfangsmall.util.PermissionsChecker;

import java.util.ArrayList;
import java.util.List;

public class MainTwoActivity extends AppCompatActivity {


    private LayoutInflater mInflater;
    private FragmentTabHost mFragmentTabHost;
    private FrameLayout mFrameLayout;

    private List<Tab> mTabs = new ArrayList<>();
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private PermissionsChecker mChecker;
    private boolean isRequireCheck; // 是否需要系统权限检测
    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝

    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_two);
        initView();
        initData();
        mChecker = new PermissionsChecker(this);
        isRequireCheck = true;
    }

    private void initView() {
        (mFragmentTabHost) = (FragmentTabHost) findViewById(android.R.id.tabhost);
        (mFrameLayout) = (FrameLayout) findViewById(R.id.realtabcontent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            if (mChecker.lacksPermissions(PERMISSIONS)) {
                requestPermissions(PERMISSIONS); // 请求权限
            } else {
                allPermissionsGranted(); // 全部权限都已获取
            }
        } else {
            isRequireCheck = true;
        }

    }

    private void initData() {
        Tab tab_home = new Tab(HomePageFragment.class, R.string.main_first);
        Tab tab_video = new Tab(VideoFragment.class, R.string.main_two);

        Tab tab_voice = new Tab(VoiceFragment.class, R.string.main_three);
        Tab tab_train = new Tab(TrainFragment.class, R.string.main_four);
        mTabs.add(tab_home);
        mTabs.add(tab_video);
        mTabs.add(tab_voice);
        mTabs.add(tab_train);


        mInflater = LayoutInflater.from(this);
        mFragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (Tab tab : mTabs) {

            TabHost.TabSpec tabSpec = mFragmentTabHost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mFragmentTabHost.addTab(tabSpec, tab.getFragment(), null);

        }
        //取消默认的Tab间的竖线显示
        mFragmentTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mFragmentTabHost.setCurrentTab(0);
    }

    private View buildIndicator(Tab tab) {


        View view = mInflater.inflate(R.layout.tab_item, null);
        TextView text = (TextView) view.findViewById(R.id.iv_title);

        text.setText(tab.getTitle());

        return view;
    }


    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {
//        setResult(PERMISSIONS_GRANTED);
    }
    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            allPermissionsGranted();
        } else {
            isRequireCheck = false;
        }
    }


}
