import org.junit.Test;
import org.apache.tools.ant.util.DOMElementWriter;
// manually written based on http://www.evocrash.org/experimental-case-ant-33446.html
 
public class Crash {
@Test
public void test0() throws Throwable {
    DOMElementWriter dOMElementWriter0 = new DOMElementWriter();
    dOMElementWriter0.encode((String) null);
}
}

