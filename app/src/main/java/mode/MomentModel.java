package mode;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

public class MomentModel {
    public static class AllMoments implements Serializable {
        private static final long serialVersionUID = 9051797102625946375L;
        private String Status;
        private String Time;
        @JSONField(name = "Data")
        private Data Data;

        public void setStatus(String Status) {
            this.Status = Status;
        }
        public String getStatus() {
            return Status;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }
        public String getTime() {
            return Time;
        }

        public void setData(Data Data) {
            this.Data = Data;
        }
        public Data getData() {
            return Data;
        }
    }

    public static class Moment {
        @JSONField(name = "Id")
        private int Id;
        private int Like;
        private String Context;
        private String UpdateTime;
        private String CreateTime;
        private User User;

        public void setId(int Id) {
            this.Id = Id;
        }
        public int getId() {
            return Id;
        }

        public void setLike(int Like) {
            this.Like = Like;
        }
        public int getLike() {
            return Like;
        }

        public void setContext(String Context) {
            this.Context = Context;
        }
        public String getContext() {
            return Context;
        }

        public void setUpdateTime(String UpdateTime) {
            this.UpdateTime = UpdateTime;
        }
        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }
        public String getCreateTime() {
            return CreateTime;
        }

        public void setUser(User User) {
            this.User = User;
        }
        public User getUser() {
            return User;
        }

    }
    public static class User {
        private String Name;
        private String Email;
        public void setName(String Name) {
            this.Name = Name;
        }
        public String getName() {
            return Name;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }
        public String getEmail() {
            return Email;
        }

    }

    public static class Data {
        @JSONField(name = "List")
        private List<Moment> MomentList;
        public void setList(List<Moment> List) {
            this.MomentList = List;
        }
        public List<Moment> getList() {
            return MomentList;
        }
    }

    public static class MomentComment{
        public String Status;
        public String Time;
        @JSONField(name = "Data")
        public CommentData commentData;
        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String time) {
            Time = time;
        }

        public CommentData getCommentData() {
            return commentData;
        }

        public void setCommentData(CommentData commentData) {
            this.commentData = commentData;
        }

        public MomentComment(String status, String time) {
            Status = status;
            Time = time;
        }

        public MomentComment() {
        }
    }
    public static class CommentData{
        public int Total;
        @JSONField(name = "List")
        public List<Comment> CommmentList;

        public int getTotal() {
            return Total;
        }

        public void setTotal(int total) {
            Total = total;
        }

        public List<Comment> getCommmentList() {
            return CommmentList;
        }

        public void setCommmentList(List<Comment> commmentList) {
            CommmentList = commmentList;
        }

        public CommentData() {
        }

        public CommentData(int total, List<Comment> commmentList) {
            Total = total;
            CommmentList = commmentList;
        }
    }
    public static class Comment{
        public String Id;
        public String Context;
        public String UpdateTime;
        public String CreateTime;
        public User User;

        public Comment() {
        }

        public Comment(String id, String context, String updateTime, String createTime, User user) {
            Id = id;
            Context = context;
            UpdateTime = updateTime;
            CreateTime = createTime;
            this.User = user;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getContext() {
            return Context;
        }

        public void setContext(String context) {
            Context = context;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String updateTime) {
            UpdateTime = updateTime;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public User getUser() {
            return User;
        }

        public void setUser(User user) {
            this.User = user;
        }
    }
}
