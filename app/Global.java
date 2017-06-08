import models.UserDB;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        UserDB.addUser("presto", "228");
    }
}
