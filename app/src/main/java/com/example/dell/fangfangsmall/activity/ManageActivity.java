package com.example.dell.fangfangsmall.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.adapter.BaseAdapter;
import com.example.dell.fangfangsmall.adapter.UserInfoAdapter;
import com.example.dell.fangfangsmall.camera.BaseActivity;
import com.example.dell.fangfangsmall.face.yt.YtPersonids;
import com.example.dell.fangfangsmall.face.yt.person.YtDelperson;
import com.example.dell.fangfangsmall.view.FullyLinearLayoutManager;
import com.example.dell.fangfangsmall.youtu.PersonManager;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/19.
 */

public class ManageActivity extends BaseActivity {

    RecyclerView infoRecycler;
    UserInfoAdapter userInfoAdapter;

    public Handler mHandler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage;
    }

    @Override
    protected void initView() {
        infoRecycler = (RecyclerView) findViewById(R.id.info_recycler);
    }

    @Override
    protected void initData() {
        PersonManager.getPersonIds(mHandler, new SimpleCallback<YtPersonids>(ManageActivity.this) {
            @Override
            public void onSuccess(YtPersonids ytPersonids) {
                List<String> person_ids = ytPersonids.getPerson_ids();
                setAdapter(person_ids);
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
//        PersonManager.getGroup(mHandler, new SimpleCallback<YtGroupids>(ManageActivity.this) {
//            @Override
//            public void onSuccess(YtGroupids ytGroupids) {
//                List<String> group_ids = ytGroupids.getGroup_ids();
//                setAdapter(group_ids);
//            }
//
//            @Override
//            public void onFail(int code, String msg) {
//
//            }
//        });

    }

    @Override
    protected void setListener() {

    }


    private void setAdapter(final List<String> userInfos) {
        userInfoAdapter = new UserInfoAdapter(this, userInfos);
        userInfoAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String person = userInfos.get(position);
//                PersonInfoDetailActivity.navToPersonInfoDetail(PersonListActivity.this, person);

            }
        });
        userInfoAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, final int position) {
                final String person_id = userInfos.get(position);

                final AlertDialog.Builder builder = new AlertDialog.Builder(ManageActivity.this);
                builder.setMessage("确定要删除此人脸信息吗")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                PersonManager.delperson(mHandler, person_id, new SimpleCallback<YtDelperson>(ManageActivity.this) {
                                    @Override
                                    public void onSuccess(YtDelperson ytDelperson) {
                                        showToast("删除 ： " + ytDelperson.getPerson_id() + " 下的 " + ytDelperson.getDeleted() + " 张人脸 ");
                                        userInfoAdapter.removeItem(person_id);
                                    }

                                    @Override
                                    public void onFail(int code, String msg) {
                                    }
                                });
                            }
                        })
                        .create().show();
                return false;
            }
        });
        infoRecycler.setAdapter(userInfoAdapter);

        infoRecycler.setLayoutManager(new FullyLinearLayoutManager(this));
        infoRecycler.setLayoutManager(new LinearLayoutManager(this));
        infoRecycler.setItemAnimator(new DefaultItemAnimator());
        infoRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

}
