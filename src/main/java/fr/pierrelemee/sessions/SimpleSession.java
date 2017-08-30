package fr.pierrelemee.sessions;

import fr.pierrelemee.Session;

public class SimpleSession extends Session {

    protected User user;

    public User getUser() {
        return user;
    }

    public void setUser(String login, String password) {
        this.user = new User();
        this.user.login = login;
        this.user.password = password;
    }

    public static class User {
        protected String login;
        protected String password;

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

}
