package com.example.dell.fangfangsmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.dao.UserInfo;

import java.util.List;

/**
 * Created by lyw on 2017/9/26
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements View.OnClickListener{
    private OnItemClickListener mOnItemClickListener = null;

    private Context mContext;
    private List<UserInfo> mList;

    public UserAdapter(Context context, List<UserInfo> list){
        this.mContext =context;
        this.mList =list;
    }
    public void setData(List<UserInfo> list){
        this.mList =list;
        notifyDataSetChanged();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(mContext).inflate(R.layout.item_user,parent,false);

        UserViewHolder vh = new UserViewHolder(view);
               //将创建的View注册点击事件
              view.setOnClickListener(this);
              return vh;

    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserInfo item =(UserInfo) mList.get(position);
            holder.tvId.setText(String.valueOf(position+1));
            holder.tvType.setText(item.getSendtype());
        holder.tvQuestion.setText(item.getQuestion());
        holder.tvContent.setText(item.getContent());


               //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);

//           if(1==item.getSex()){
//               holder.tvSex.setText("男");
//           }else {
//               holder.tvSex.setText("女");
//           }

    }



    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
                      //注意这里使用getTag方法获取position
                      mOnItemClickListener.onItemClick(v,(int)v.getTag());
                  }

    }


    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView tvId,tvType,tvQuestion,tvContent;
        public UserViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvQuestion = (TextView) itemView.findViewById(R.id.tv_question);
            tvContent = (TextView) itemView.findViewById(R.id.tv_Content);
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
               this.mOnItemClickListener = listener;
            }

}
