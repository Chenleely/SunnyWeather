package user;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sunnyweather.R;

import mode.UserInfo;
import okhttp3.MediaType;
import tools.HttpUtil;
import tools.NetRepo;
import tools.PrefTools;
import ui.BaseActivity;
import ui.MainActivity;

public class Register extends BaseActivity implements View.OnClickListener{
    //声明变量
    private EditText emailEdit,password;
    private Button LoginButton,LoginError,LoginRegister;
    private Button bt_Email_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_visible;
    private TextWatcher Email_watcher;
    private TextWatcher password_watcher;//文本监视器

    @Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);

    emailEdit =(EditText) findViewById(R.id.Email);
    password=(EditText) findViewById(R.id.password);
    bt_Email_clear=(Button) findViewById(R.id.bt_Email_clear);
    bt_pwd_clear=(Button)findViewById(R.id.bt_pwd_clear);
    bt_pwd_visible=(Button) findViewById(R.id.bt_pwd_visible);

    bt_Email_clear.setOnClickListener(this);
    bt_pwd_clear.setOnClickListener(this);
    bt_pwd_visible.setOnClickListener(this);

    LoginButton=(Button) findViewById(R.id.login);
    LoginError=(Button) findViewById(R.id.login_error);
    LoginRegister=(Button) findViewById(R.id.register);
    LoginButton.setOnClickListener(this);
    LoginError.setOnClickListener(this);
    LoginRegister.setOnClickListener(this);
    String email= (String) PrefTools.get(Register.this,"userEmail","");
    String pass= (String) PrefTools.get(Register.this,"userPass","");
    if (email!=null && pass!=null && !email.equals("") && !pass.equals("")){
           emailEdit.setText(email);
           password.setText(pass);
           login();
        }
    }
    private void initWatcher(){
     Email_watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                emailEdit.setText("");
                if (editable.toString().length()>0){
                    bt_Email_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_Email_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password.setText("");
                if(password.getText().toString().length()>0){
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_pwd_clear.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login:
                login();
                break;
            case R.id.login_error:
                login_error();
                break;
            case R.id.register:
                register();
                break;
        }
    }
    private UserInfo.RegisterModel registerModel=new UserInfo.RegisterModel("","");
    private void getPassAndEmail(){
        String inputEmail= emailEdit.getText().toString();
        String inputPass=password.getText().toString();
        if(TextUtils.isEmpty(inputEmail) || TextUtils.isEmpty(inputPass)){
            Toast.makeText(this,"账户或者密码为空",Toast.LENGTH_SHORT).show();
        }else{
            registerModel.setEmail(inputEmail);
            registerModel.setPassword(inputPass);
        }
    }
    private void login(){
        getPassAndEmail();
        String jsonString= JSON.toJSONString(registerModel);

        NetRepo.PostRequest request=new NetRepo.PostRequest("http://louis296.top:8081/login");
        request.setJsonString(jsonString);
        request.setType(MediaType.parse("application/json;charset=utf-8"));

        HttpUtil.doPost(request, new HttpUtil.CallbackListener() {
            @Override
            public void success(String msg) {
                UserInfo.BackModel backModel =JSON.parseObject(msg, UserInfo.BackModel.class);
                Log.d("test",msg+"");
                if (backModel.Status.equals("Error")){
                    error();
                }else{
                    userStateInfo.setUserToken(backModel.Data.Token);
                    userStateInfo.setState(true);
                    userStateInfo.login();
                    PrefTools.put(Register.this,"userEmail", emailEdit.getText().toString());
                    PrefTools.put(Register.this,"userPass",password.getText().toString());
                    Intent intent=new Intent(Register.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void failed(int code) {

            }

        });
    }
    private void login_error(){
    }
    private void register(){
        getPassAndEmail();
    }
    public void error(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(Register.this).setTitle("错误").setMessage("邮箱或密码错误")

                        .setPositiveButton("确定",null)

                        .setNegativeButton("取消",null).show();
            }
        });
    }
}

