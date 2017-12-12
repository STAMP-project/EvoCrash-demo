import static org.junit.Assert.fail;

import java.io.File;

import org.apache.log4jb.Appender;
import org.apache.log4jb.AsyncAppender;
import org.apache.log4jb.Category;
import org.apache.log4jb.FileAppender;
import org.apache.log4jb.Hierarchy;
import org.apache.log4jb.Priority;
import org.apache.log4jb.spi.LoggingEvent;
import org.apache.log4jb.spi.RootCategory;
import org.apache.xerces.readers.DefaultReaderFactory;
import org.junit.Test;

import junit.framework.TestCase;

public class crash_29 extends TestCase {

	@Test
	public void test0() throws Throwable {
		Class<Integer> class0 = Integer.class;
		Category category0 = Category.getInstance(class0);
		Hierarchy hierarchy0 = Category.getDefaultHierarchy();
		category0.debug((Object) hierarchy0);
	}
}
