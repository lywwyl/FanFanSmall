package com.example.dell.fangfangsmall.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.activity.DataManagementActivity;
import com.example.dell.fangfangsmall.adapter.UserAdapter;
import com.example.dell.fangfangsmall.dao.GreenDaoManager;
import com.example.dell.fangfangsmall.dao.UserDao;
import com.example.dell.fangfangsmall.dao.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by lyw on 17/9/25.
 */
public class DataFragment extends Fragment {

    private View DataView;
    private RecyclerView recyclerView;
    private List<UserInfo> lists = new ArrayList<>();
    private UserAdapter adapter;
    private DataManagementActivity dataManagementActivity;

    //问题
    private String[] voiceQuestion = null;
    //答案
    private String[] voiceAnswer = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DataView = inflater.inflate(R.layout.fragment_data, container, false);

        if (DataView != null) {
            dataManagementActivity = (DataManagementActivity) getActivity();
            //dataManagementActivity.mTabhost.getCurrentTab();
            initView();

            queryListData();
        }
        return DataView;
    }

    private void initView() {

        voiceQuestion = getActivity().getResources().getStringArray(R.array.voice_question_array);
        voiceAnswer = getActivity().getResources().getStringArray(R.array.voice_answer_array);
        recyclerView = (RecyclerView) DataView.findViewById(R.id.recycler_view);

        adapter = new UserAdapter(getActivity(), null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
//                String pos = String.valueOf(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // builder.setIcon(R.drawable.ic_launcher);

                // 指定下拉列表的显示数据
                final String[] memu = {"删除此条", "删除所有"};
                // 设置一个下拉的列表选择项
                builder.setItems(memu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                try {
                                    deleteData(position);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // TODO: handle exception
                                }


                                Toast.makeText(getActivity(), "成功删除一条数据",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                // Toast.makeText(context, "选择：" + memu[which],
                                // Toast.LENGTH_SHORT).show();
                                try {
                                    deleteAllData();

                                    Toast.makeText(getActivity(), "成功删除所有数据",
                                            Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // TODO: handle exception
                                }
                                break;
                            default:
                                break;
                        }

                    }
                });
                builder.show();
            }
        });
        addData();
    }


    /**
     * 更新数据
     */
    private void updateData(int position) {

        if (!lists.isEmpty()) {

            UserInfo userInfo = lists.get(0);
            userInfo.setName("李四");
            UserDao.getInstance().updateUserData(userInfo);
            queryListData();
        }
    }

    /**
     * 删除数据
     */
    private void deleteData(int position) {
        if (!lists.isEmpty()) {
            UserDao.getInstance().deleteUserData(lists.get(position));
            queryListData();
        }

    }

    /**
     * 删除所有数据
     */
    private void deleteAllData() {
        if (!lists.isEmpty()) {
            UserDao.getInstance().deleteAllData();
            queryListData();
        }

    }

    /**
     * 添加数据
     */
    private void addData() {
        for (int i = 0; i <= 6; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setType("Data");
            userInfo.setSendtype("本地语音");
            userInfo.setQuestion(voiceQuestion[i]);
            userInfo.setContent(voiceAnswer[i]);
            UserDao.getInstance().insertOrReplaceData(userInfo);
        }

        queryListData();
    }

    /**
     * 查询数据
     */
    private void queryListData() {
        lists = UserDao.getInstance().queryUserByType("Data");
        adapter.setData(lists);
        //  Toast.makeText(getActivity(), "查询到" + lists.size() + "条数据", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        queryListData();
    }

}
