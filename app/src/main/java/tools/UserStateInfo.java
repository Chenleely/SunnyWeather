package tools;
//管理用户登录状态
public class UserStateInfo {
    private volatile static UserStateInfo userStateInfo;
    private UserStateInfo(){}
    private String userToken;
    private boolean state;//检测token是否有效
    private boolean isLogin;//判断登录状态 true:已经登录  false:未登录

    public void login(){
        this.isLogin=true;
    }
    public void logout(){
        this.isLogin=false;
    }
    public static UserStateInfo getUserStateInfo() {
        synchronized (UserStateInfo.class){
            if (userStateInfo==null){
                userStateInfo=new UserStateInfo();
            }
        }
        return userStateInfo;
    }
    public String getUserToken() {
        return userToken;
    }
    public void setUserToken(String userToken) {
        this.userToken = userToken;
        NetRepo.token=this.userToken;
    }
    public boolean isState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }

    public static void setUserStateInfo(UserStateInfo userStateInfo) {
        UserStateInfo.userStateInfo = userStateInfo;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
