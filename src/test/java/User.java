
public class User {

        private int userId;
        private int id;
        private String title;
        private Boolean completed;

        public Integer getUserId() {
            return userId;
        }

        public Integer getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Boolean getCompleted() {
            return completed;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCompleted(Boolean completed) {
            this.completed = completed;
        }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
