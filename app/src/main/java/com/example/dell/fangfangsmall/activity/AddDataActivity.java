package com.example.dell.fangfangsmall.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.dao.UserDao;
import com.example.dell.fangfangsmall.dao.UserInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


public class AddDataActivity extends Activity implements View.OnClickListener {
    private Button add;
    private EditText  ed_question, ed_content;
    private String currentTab;
    private Spinner ed_type,ed_action,ed_expression;
    private TextView ed_img;
    public static Uri imageUri1;
    private SharedPreferences sharedPreferences;
//    private String[] data;
//    private List<String> data1;
//
//    private ArrayAdapter<String> adapter;
private String ed_type_data;
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

        ed_type = (Spinner) findViewById(R.id.ed_type);

        ed_action = (Spinner) findViewById(R.id.ed_action);
        ed_expression= (Spinner) findViewById(R.id.ed_expression);
        ed_question = (EditText) findViewById(R.id.ed_question);
        ed_question.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        ed_question.setGravity(Gravity.TOP);

        ed_content = (EditText) findViewById(R.id.ed_content);
        ed_content.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        ed_content.setGravity(Gravity.TOP);
        ed_img= (TextView) findViewById(R.id.ed_img);
        ed_img.setOnClickListener(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[] { "本地语音", "在线视频", "其他" });
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[] { "微笑", "沉默", "高兴" });
        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[] { "挥手", "抬腿", "转头" });

        ed_type.setAdapter(adapter);
        ed_expression.setAdapter(adapter2);
        ed_action.setAdapter(adapter3);
        //设置下拉样式
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //设置下拉样式
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //设置下拉样式
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
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
        if ( ed_question.getText().toString().trim().equals("") || ed_content.getText().toString().trim().equals("")) {
            Toast.makeText(AddDataActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();

        } else {

            userInfo.setSendtype(ed_type.getSelectedItem().toString());
            Log.i("WWDZ","ed_type.getSelectedItem().toString()"+ed_type.getSelectedItem().toString());
            userInfo.setQuestion(ed_question.getText().toString().trim());
            userInfo.setContent(ed_content.getText().toString().trim());
            userInfo.setExpression(ed_expression.getSelectedItem().toString());
            userInfo.setAction(ed_action.getSelectedItem().toString());
            userInfo.setImg(ed_img.getText().toString());
            UserDao.getInstance().insertUserData(userInfo);

            finish();
        }

        //    queryListData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_fin:
                isExit();
                break;
            case R.id.ed_img:
                pickImageFromAlbum();
                break;
        }
    }

    public void isExit() {
        List<UserInfo> userInfos = UserDao.getInstance().queryUserByQuestion(ed_question.getText().toString().trim());
        if (!userInfos.isEmpty()) {
            Toast.makeText(AddDataActivity.this, "请不要添加相同的问题！", Toast.LENGTH_LONG).show();
        } else {
            addData();
        }
    }

    public void pickImageFromAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 222);

    }
    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 222:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(AddDataActivity.this, "点击取消从相册选择", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    imageUri1 = data.getData();
                    String ss = imageUri1.getPath();
                    //createImageFile(this).getPath();



                    Log.e("TAG", "ss : " + imageUri1);
                    Log.e("TAG", imageUri1.toString());

                    String[] pro = {MediaStore.Images.Media.DATA};
                    //好像是android多媒体数据库的封装接口，具体的看Android文档
                    Cursor cursor = managedQuery(imageUri1, pro, null, null, null);
                    Cursor cursor1 = getContentResolver().query(imageUri1, pro, null, null, null);
                    //拿到引索
                    int index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //移动到光标开头
                    cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                    String path = cursor.getString(index);
                    ed_img.setText(path);
                    Log.d("WWDZ", "path :" + path);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

            default:
                break;

        }


    }
}
