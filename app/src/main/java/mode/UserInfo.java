package mode;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfo {
    public static class RegisterModel{
        @JSONField(name = "Email")
        public String Email;

        @JSONField(name = "Password")
        public String Password;


        public RegisterModel(String email,String password){
            this.Email=email;
            this.Password=password;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            Password = password;
        }
    }
    public static class BackModel{
        @JSONField(name = "Status")
        public String Status;

        @JSONField(name = "Time")
        public String Time;

        @JSONField(name = "Data")
        public Data Data;

        public BackModel() {
        }

        public BackModel(String status, String time, Data data){
            this.Status=status;
            this.Time=time;
            this.Data=data;
        }
        public static class Data{
            @JSONField(name = "Token")
            public String Token;

            public String getToken() {
                return Token;
            }

            public void setToken(String token) {
                Token = token;
            }



            public Data() {
            }

            public Data(String token){
                this.Token=token;
            }


    }



        }
    }




