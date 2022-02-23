package mode;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfo {
    public static class Register{
        @JSONField(name = "Email")
        public String Email;

        @JSONField(name = "Password")
        public String Password;

        @JSONField(name = "Name")
        public String Name;

        public Register(String email,String password,String name){
            super();
            this.Email=email;
            this.Password=password;
            this.Name=name;
        }
    }



}
