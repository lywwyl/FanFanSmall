package com.example.dell.fangfangsmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.bean.Tab;
import com.example.dell.fangfangsmall.fragment.ActionFragment;
import com.example.dell.fangfangsmall.fragment.DataFragment;
import com.example.dell.fangfangsmall.fragment.OtherFragment;
import com.example.dell.fangfangsmall.fragment.SysSetFragment;

import java.util.ArrayList;
import java.util.List;

public class DataManagementActivity extends AppCompatActivity implements View.OnClickListener {


    private LayoutInflater mInflater;

    public FragmentTabHost mTabhost;
    private ImageView img_add, img_back;

    private List<Tab> mTabs = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamanagent);
        initView();
        initTab();

        // mTabhost.getCurrentTab();
    }

    private void initView() {
        img_add = (ImageView) findViewById(R.id.img_add);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_add.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    private void initTab() {


        Tab tab_home = new Tab(DataFragment.class, R.string.data, 0);
        Tab tab_hot = new Tab(ActionFragment.class, R.string.action, 0);

        Tab tab_cart = new Tab(SysSetFragment.class, R.string.sysset, 0);
        Tab tab_mine = new Tab(OtherFragment.class, R.string.other, 0);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);

        mTabs.add(tab_cart);
        mTabs.add(tab_mine);


        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (Tab tab : mTabs) {

            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec, tab.getFragment(), null);

        }

        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);


    }

    private View buildIndicator(Tab tab) {


        View view = mInflater.inflate(R.layout.tab_indicator, null);

        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        text.setText(tab.getTitle());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add:
                // addData();
                Intent toAdd = new Intent(DataManagementActivity.this, AddDataActivity.class);
                toAdd.putExtra("type", String.valueOf(mTabhost.getCurrentTab()));

                startActivity(toAdd);
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

}
