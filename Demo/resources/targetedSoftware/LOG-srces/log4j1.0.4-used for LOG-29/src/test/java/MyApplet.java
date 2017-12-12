import java.applet.Applet;

import org.apache.log4jb.BasicConfigurator;
import org.apache.log4jb.Category;

public class MyApplet extends Applet {

    private static Category cat = Category.getInstance(MyApplet.class.getName());

    public void init() {
        super.init();
        BasicConfigurator.configure();
        cat.info("Hello, World!");
    }

}
