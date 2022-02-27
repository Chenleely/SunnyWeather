package ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.sunnyweather.R;

import java.util.List;
import java.util.Random;

import mode.Base;
import mode.MomentModel;
import tools.HttpUtil;
import tools.NetRepo;
import tools.Util;

public class CommentActivity extends BaseActivity {
    private int momentId;
    private EditText editText;
    private TextView send;
    private LinearLayout commentList;
    private MomentModel.MomentComment mMomentComment;
    private ImageView back;
    private int offset=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list);
        setStatus();
        Intent intent=getIntent();
        momentId=intent.getIntExtra("moment_id",-1);
        if (momentId==-1){finish();}
        editText=(EditText) findViewById(R.id.comment_edit);
        send=(TextView) findViewById(R.id.comment_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editString=editText.getText().toString();
                if (TextUtils.isEmpty(editString)){
                    showToast(CommentActivity.this,"说点啥吧");
                }else{
                    sendComment(editString,momentId);
                }
            }
        });
        commentList=(LinearLayout) findViewById(R.id.comment_listview);
        back=(ImageView) findViewById(R.id.comment_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mHandler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        mProgressDialog.show();
                        break;
                    case 2:
                        updateUI();
                        mProgressDialog.dismiss();
                        break;
                    case 3:
                        mProgressDialog.dismiss();
                        Toast.makeText(CommentActivity.this,"稍后重试",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        queryCommentList(offset);
    }
    private void updateUI(){
        editText.setText("");
        editText.clearFocus();
        if (mMomentComment.getCommentData().getCommmentList()!=null && mMomentComment.getCommentData().getCommmentList().size()>0){
            commentList.removeAllViews();
            for(MomentModel.Comment comment:mMomentComment.getCommentData().getCommmentList()){
                View view=LinearLayout.inflate(this,R.layout.comment_item,null);
                TextView name=(TextView) view.findViewById(R.id.comment_item_user_name);
                TextView updateTime=(TextView) view.findViewById(R.id.comment_update_time);
                TextView context=(TextView) view.findViewById(R.id.comment_context);
                ImageView userAvatar=(ImageView) view.findViewById(R.id.moment_user_avatar);
                name.setText(comment.getUser().getName());
                updateTime.setText(Util.formatUtcTime(comment.getUpdateTime()));
                context.setText(comment.getContext());
                commentList.addView(view);

                Random random=new Random();
                int i=random.nextInt(5)+1;
                Context ctc=getBaseContext();
                int resId=getResources().getIdentifier("user"+i,"drawable",ctc.getPackageName());
                userAvatar.setImageResource(resId);

            }
        }
    }
    private void queryCommentList(final int offset){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url= NetRepo.base_url+"ListComment"+"&Limit=10&Offset="+offset+"&MomentId="+momentId;
                NetRepo.GetRequest request=new NetRepo.GetRequest(url);
                NetRepo.Param param=new NetRepo.Param(NetRepo.Authorization,NetRepo.token);
                request.setParam(param);
                sendProMsg();
                HttpUtil.doGet(request, new HttpUtil.CallbackListener() {
                    @Override
                    public void success(String msg) {
                        MomentModel.MomentComment momentComment=null;
                        try{
                            momentComment= (MomentModel.MomentComment) JSON.parseObject(msg, MomentModel.MomentComment.class);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if (momentComment!=null && momentComment.getStatus().equals("Success")){
                            mMomentComment=momentComment;
                            sendSuccessMsg();
                        }else{
                            sendFaliedMsg();
                        }
                    }

                    @Override
                    public void failed(int code) {
                        sendFaliedMsg();
                    }
                });
            }
        }).run();
    }
    private void sendComment(String context,int id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url=NetRepo.base_url+"CreateComment";
                JSONObject object=new JSONObject();
                object.put("Context",context);
                object.put("MomentId",id);
                NetRepo.PostRequest request=new NetRepo.PostRequest(url);
                NetRepo.Param param=new NetRepo.Param(NetRepo.Authorization, NetRepo.token);
                request.setParam(param);
                request.setJsonString(object.toJSONString());
                request.setType(NetRepo.JSON);
                sendProMsg();
                HttpUtil.doPost(request, new HttpUtil.CallbackListener() {
                    @Override
                    public void success(String msg) {
                        Base base=JSON.parseObject(msg,Base.class);
                        if (base!=null && base.getStatus().equals("Success")){
                            queryCommentList(offset);
                        }else{
                            sendFaliedMsg();
                        }
                    }
                    @Override
                    public void failed(int code) {
                        sendFaliedMsg();
                    }
                });
            }
        }).run();
    }
}