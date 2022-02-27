package ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sunnyweather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import mode.MomentModel;
import tools.Util;

public class MomentsAdapter extends BaseAdapter {
    private Context context;
    private List<MomentModel.Moment> momentItemList;

    public void setMomentItemList(List<MomentModel.Moment> momentItemList) {
        this.momentItemList = momentItemList;
    }

    public MomentsAdapter(Context context, List<MomentModel.Moment> momentItemList) {
        this.context = context;
        this.momentItemList = momentItemList;
    }

    @Override
    public int getCount() {
        return momentItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return momentItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null){
            holder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.moment_item,null);
            holder.userNameTv=(TextView) view.findViewById(R.id.moment_user_name);
            holder.userCommentTv=(TextView) view.findViewById(R.id.moment_user_comment);
            holder.userUpdateTimeTv=(TextView) view.findViewById(R.id.moment_user_update_time);
            holder.userLikeBtn=(ImageView) view.findViewById(R.id.moment_user_like);
            holder.userLikenums=(TextView) view.findViewById(R.id.moment_like_nums);
            holder.userCommentBtn=(ImageView) view.findViewById(R.id.moment_user_comment_btn);
            holder.userCommentnums=(TextView) view.findViewById(R.id.moment_comment_nums);
            view.setTag(holder);
        }else{
            holder=(ViewHolder) view.getTag();
        }
        MomentModel.Moment moment=momentItemList.get(i);
        holder.userNameTv.setText(moment.getUser().getName());
        holder.userCommentTv.setText(moment.getContext());
        holder.userUpdateTimeTv.setText(Util.formatUtcTime(moment.getCreateTime()));
        holder.userLikenums.setText(moment.getLike()+"");
        holder.userCommentnums.setText(15+"");
        holder.userCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转评论界面
            }
        });
        holder.userLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用点赞接口
            }
        });
        return view;
    }
    static class ViewHolder{
        TextView userNameTv;
        TextView userCommentTv;
        TextView userUpdateTimeTv;
        ImageView userLikeBtn;
        TextView userLikenums;
        ImageView userCommentBtn;
        TextView userCommentnums;
    }
}
