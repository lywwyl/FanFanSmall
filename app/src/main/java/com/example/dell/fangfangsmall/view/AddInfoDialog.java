package com.example.dell.fangfangsmall.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.listener.OnConfimListener;

/**
 * Created by dell on 2017/9/13.
 */

public class AddInfoDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnConfimListener mOnConfimListener;

    public AddInfoDialog(Context context, OnConfimListener onConfimListener) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
        this.mOnConfimListener = onConfimListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_common_confirm, null);
        setContentView(view);
        EditText et_content = (EditText) view.findViewById(R.id.et_content);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (dm.widthPixels * 0.8);
        window.setAttributes(lp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                dismiss();
                mOnConfimListener.onConfim();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
