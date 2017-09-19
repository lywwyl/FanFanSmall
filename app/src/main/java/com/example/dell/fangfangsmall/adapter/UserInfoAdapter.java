package com.example.dell.fangfangsmall.adapter;

import android.content.Context;

import com.example.dell.fangfangsmall.R;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/19.
 */

public class UserInfoAdapter extends SimpleAdapter<String> {
    public UserInfoAdapter(Context context, List<String> userInfos) {
        super(context, R.layout.item_person, userInfos);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, String item, int pos) {
        viewHoder.getTextView(R.id.tv_info_id).setText(item);
    }
}
