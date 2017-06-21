package actors;


public class AuthActorProtocol {
    public static class SignIn {
        public final String login;
        public final String password;

        public SignIn(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    public static class SignOut {
        public final String token;

        public SignOut(String token) {
            this.token = token;
        }
    }

    public static class SignUp {
        public final String login;
        public final String password;

        public SignUp(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }
}
