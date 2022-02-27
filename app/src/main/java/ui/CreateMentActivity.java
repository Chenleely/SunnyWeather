package ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.sunnyweather.R;

import tools.HttpUtil;
import tools.NetRepo;

public class CreateMentActivity extends BaseActivity {
    private ImageView backBtn;
    private TextView sendBtn;
    private EditText msgEdit;
    private AlertDialog.Builder alertDialogBuilder;
    private String editMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_moment);
        setStatus();
        backBtn=(ImageView) findViewById(R.id.create_moment_back);
        sendBtn=(TextView) findViewById(R.id.create_moment_send);
        msgEdit=(EditText) findViewById(R.id.create_moment_edit);
        alertDialogBuilder=new AlertDialog.Builder(this);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createComment();
            }
        });
        alertDialogBuilder.setTitle("确定发送吗？");
        alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                sendMsg();
                mProgressDialog.show();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }
    private void createComment(){
        String info=msgEdit.getText().toString();
        if(TextUtils.isEmpty(info)){
            Toast.makeText(this,"写点啥吧...",Toast.LENGTH_SHORT).show();
        }else{
            editMsg=info;
            alertDialogBuilder.show();
        }
    }
    private void sendMsg(){
        String url= NetRepo.base_url+"CreateMoment";
        NetRepo.PostRequest request=new NetRepo.PostRequest(url);
        NetRepo.Param param=new NetRepo.Param("Context",editMsg);
        NetRepo.Param param1=new NetRepo.Param(NetRepo.Authorization,NetRepo.token);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("Context",editMsg);
        request.setJsonString(jsonObject.toJSONString());
        request.setParam(param1);
        request.setType(NetRepo.JSON);
        HttpUtil.doPost(request, new HttpUtil.CallbackListener() {
            @Override
            public void success(String msg) {
                mProgressDialog.dismiss();
                finish();
            }

            @Override
            public void failed(int code) {
                mProgressDialog.dismiss();
                showTost("发送失败，稍后再试试");
            }
        });
    }
    private void showTost(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}