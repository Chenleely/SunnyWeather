package ui;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.sunnyweather.R;

import java.util.ArrayList;
import java.util.List;

import mode.Base;
import mode.MomentModel;
import tools.HttpUtil;
import tools.NetRepo;
import tools.Util;

public class MomentsActivity extends BaseActivity {
    private Button shareBtn;
    private Button backBtn;
    private MyScrollView scrollView;
    private TextView gotoTopBtn;
    private RelativeLayout topLayout;
    private LinearLayout momentsListView;
    private List<MomentModel.Moment> momentItemList;
    private MomentModel.AllMoments allMoments;
    private int offset=1;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onRestart() {
        super.onRestart();
        queryAllmoments(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        setStatus();
        shareBtn=(Button) findViewById(R.id.moment_share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MomentsActivity.this,CreateMentActivity.class);
                startActivity(intent);
            }
        });
        backBtn=(Button) findViewById(R.id.moment_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gotoTopBtn=(TextView) findViewById(R.id.moment_circle);
        gotoTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0,0);
            }
        });
        topLayout=(RelativeLayout) findViewById(R.id.moment_top_bar);
        scrollView=(MyScrollView) findViewById(R.id.moment_scroll);

        momentsListView=(LinearLayout) findViewById(R.id.moments_list);
        momentItemList=new ArrayList<>();
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.moment_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryAllmoments(1);
            }
        });
        mHandler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        mProgressDialog.show();
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                    case 2:
                        //请求成功，更新UI
                        momentItemList=allMoments.getData().getList();
                        swipeRefreshLayout.setRefreshing(false);
                        offset++;
                        updateUI();
                        mProgressDialog.dismiss();
                        break;
                    case 3:
                        //请求失败
                        mProgressDialog.dismiss();;
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                    case 4:
                        String toastMsg=msg.getData().getString("toast_msg");
                        Toast.makeText(MomentsActivity.this,toastMsg,Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
            }
        };
        queryAllmoments(offset);
    }
    private void updateUI(){
        momentsListView.removeAllViews();
        for(MomentModel.Moment moment:momentItemList){
            View view= LayoutInflater.from(this).inflate(R.layout.moment_item,null);
            TextView userNameTv=(TextView) view.findViewById(R.id.moment_user_name);
            TextView userCommentTv=(TextView) view.findViewById(R.id.moment_user_comment);
            TextView userUpdateTimeTv=(TextView) view.findViewById(R.id.moment_user_update_time);
            ImageView userLikeBtn=(ImageView) view.findViewById(R.id.moment_user_like);
            TextView userLikenums=(TextView) view.findViewById(R.id.moment_like_nums);
            ImageView userCommentBtn=(ImageView) view.findViewById(R.id.moment_user_comment_btn);
            TextView userCommentnums=(TextView) view.findViewById(R.id.moment_comment_nums);
            userNameTv.setText(moment.getUser().getName());
            userCommentTv.setText(moment.getContext());
            userUpdateTimeTv.setText(Util.formatUtcTime(moment.getCreateTime()));
            userLikenums.setText(moment.getLike()+"");
            userCommentnums.setText(15+"");
            userLikeBtn.setTag(moment);
            userCommentBtn.setTag(moment.getId());
            userCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转评论界面
                    Intent intent=new Intent(MomentsActivity.this,CommentActivity.class);
                    intent.putExtra("moment_id",(int) view.getTag());
                    startActivity(intent);
                }
            });
            userCommentnums.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MomentsActivity.this,CommentActivity.class);
                    intent.putExtra("moment_id",(int) view.getTag());
                    startActivity(intent);
                }
            });
            userLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //调用点赞接口
                    userLike((MomentModel.Moment) view.getTag());
                }
            });
            userLikenums.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userLike((MomentModel.Moment) view.getTag());

                }
            });
            momentsListView.addView(view);
        }
    }
    private void queryAllmoments(int offset){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendProMsg();
                String url= NetRepo.base_url+"ListMoment&Limit=10&Offset="+offset;
                NetRepo.GetRequest request=new NetRepo.GetRequest(url);
                NetRepo.Param param=new NetRepo.Param(NetRepo.Authorization,NetRepo.token);
                request.setParam(param);
                HttpUtil.doGet(request, new HttpUtil.CallbackListener() {
                    @Override
                    public void success(String msg) {
                        Log.d("MomentAcivity",msg);
                        allMoments= (MomentModel.AllMoments) JSON.parseObject(msg,MomentModel.AllMoments.class);
                        if (allMoments.getData().getList()==null || allMoments.getData().getList().size()==0){
                            sendFaliedMsg();
                        }else{
                            Bundle bundle=new Bundle();
                            sendSuccessMsg(bundle);
                        }
                    }

                    @Override
                    public void failed(int code) {
                        Log.d("MomentAcivity",""+code);
                        sendFaliedMsg();
                    }
                });
            }
        }).run();
    }
    private void userLike(final MomentModel.Moment clickMoment){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(clickMoment!=null){
                    String url=NetRepo.base_url+"ThumbUpMoment";
                    NetRepo.PostRequest request=new NetRepo.PostRequest(url);
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("MomentId",clickMoment.getId());
                    request.setJsonString(jsonObject.toJSONString());
                    request.setParam(new NetRepo.Param(NetRepo.Authorization,NetRepo.token));
                    request.setType(NetRepo.JSON);
                    Log.d("testLike",request.getRequest().toString());
                    HttpUtil.doPost(request, new HttpUtil.CallbackListener() {
                        @Override
                        public void success(String msg) {
                            Base base=JSON.parseObject(msg,Base.class);
                            if(base.getStatus().equals("Success")){
                                showToast(MomentsActivity.this,"点赞成功");
                            }else{
                                showToast(MomentsActivity.this,"你点过赞了！");
                            }
                        }

                        @Override
                        public void failed(int code) {
                            showToast(MomentsActivity.this,"请求失败");
                        }
                    });
                }else{
                    showToast(MomentsActivity.this,"请稍后重试");
                }
            }
        }).start();
    }


}

