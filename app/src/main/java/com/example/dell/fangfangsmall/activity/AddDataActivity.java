package com.example.dell.fangfangsmall.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.dao.UserDao;
import com.example.dell.fangfangsmall.dao.UserInfo;


public class AddDataActivity extends Activity implements View.OnClickListener {
    Button add;
    EditText ed_type, ed_question, ed_content;
    private String currentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        Intent getCurrentTab = getIntent();
        currentTab = getCurrentTab.getStringExtra("type");
        initView();
    }

    private void initView() {

        add = (Button) findViewById(R.id.bt_add_fin);
        add.setOnClickListener(this);

        ed_type = (EditText) findViewById(R.id.ed_type);

        ed_question = (EditText) findViewById(R.id.ed_question);
        ed_question.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//文本显示的位置在EditText的最上方
        ed_question.setGravity(Gravity.TOP);

        ed_content = (EditText) findViewById(R.id.ed_content);
        ed_content.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//文本显示的位置在EditText的最上方
        ed_content.setGravity(Gravity.TOP);

    }

    /**
     * 添加数据
     */
    @SuppressLint("WrongConstant")
    private void addData() {
        UserInfo userInfo = new UserInfo();

        if (currentTab.equals("0")) {
            userInfo.setType("Data");
        } else if (currentTab.equals("1")) {
            userInfo.setType("Action");
        } else if (currentTab.equals("2")) {
            userInfo.setType("SysSet");
        } else if (currentTab.equals("3")) {
            userInfo.setType("Other");
        }
        if (ed_type.getText().toString().trim().equals("") || ed_question.getText().toString().trim().equals("") || ed_content.getText().toString().trim().equals("")) {
            Toast.makeText(AddDataActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();

        } else {

            userInfo.setSendtype(ed_type.getText().toString().trim());
            userInfo.setQuestion(ed_question.getText().toString().trim());
            userInfo.setContent(ed_content.getText().toString().trim());
            UserDao.getInstance().insertUserData(userInfo);
            finish();
        }

        //    queryListData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_fin:
                addData();

                break;
        }
    }
}
