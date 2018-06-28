# EvoCrash: a crash replication tool for Java

What is EvoCrash?
---------------
In order to aid software developers in debugging, we developed EvoCrash, a search-based approach to automated crash reproduction. EvoCrash receives a Java crash stack trace, and searches for a unit test case that can reproduce the crash.


How does EvoCrash work?
---------------
EvoCrash defines the crash replication problem to a search problem. It uses a guided genetic algorithm to find a test which replicates the stack trace which is passed as input.

Output of EvoCrash
---------------
EvoCrash produces 2 outputs:
* A CSV file which reports the process of search.
* The test which replicates the input stack trace.

Getting Started with Demo
---------------

```
git clone https://github.com/STAMP-project/EvoCrash-demo.git
cd EvoCrash-demo/Demo
```

# XWIKI_13916 example
## Generating the test in GGA-tests directory

### Under Linux or Mac
```./gradlew run```

### Under Windows
```gradlew.bat run```

## Looking at the generated tests

```
cat GGA-tests/com/xpn/xwiki/objects/classes/BaseClass_ESTest.java
```
```
cat GGA-tests/com/xpn/xwiki/objects/classes/BaseClass_ESTest_scaffolding.java
```

## To run the generated test
### Under Linux or Mac
```./gradlew test```

### Under Windows
```gradlew.bat test```

## now some manual work, you have to open the BaseClass_ESTest.java in an editor and
1) remove the annotations RunWith and EvoRunnerParameters
2) remove the try catch block in the generated test


The console output is:

```
* Permissions denied during test execution:
  - java.lang.RuntimePermission:
         writeFileDescriptor: 3
* Writing JUnit test case 'BaseClass_ESTest' to ../GGA-tests
* Done!

* Computation finished

Time: 313.527
```

The generated test in `../GGA-tests/com/xpn/xwiki/objects/classes/BaseClass_ESTest.java`:

```java
// THIS LINE SHOULD BE REMOVED MANUALLY (FOR NOW, see https://github.com/STAMP-project/EvoCrash/issues/2)
@RunWith(EvoRunner.class) @EvoRunnerParameters(useVFS = true, useJEE = true)
public class BaseClass_ESTest extends BaseClass_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      BaseClass baseClass0 = new BaseClass();
      baseClass0.addUsersField((String) null, (String) null, true);
      TextAreaClass.ContentType textAreaClass_ContentType0 = TextAreaClass.ContentType.VELOCITY_CODE;
      // Undeclared exception!

      // THE TRY-CATCH SHOULD BE REMOVED MANUALLY (for now, see https://github.com/STAMP-project/EvoCrash/issues/3)
      try {
        baseClass0.addTextAreaField((String) null, "&A,#Km@=kjd1", 1695, 1695, textAreaClass_ContentType0);
        fail("Expecting exception: ClassCastException");

      } catch(ClassCastException e) {
         //
         // com.xpn.xwiki.objects.classes.UsersClass cannot be cast to com.xpn.xwiki.objects.classes.TextAreaClass
         //
         verifyException("com.xpn.xwiki.objects.classes.BaseClass", e);
      }
  }
}
```

Running the Evocrash-generated test produces the exception on the console:
```
JUnit version 4.12
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/afs/kth.se/home/m/o/monp/EvoCrash/Demo/evocrash-lib/evocrash-1.0.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/afs/kth.se/home/m/o/monp/EvoCrash/Demo/resources/targetedSoftware/XWIKI-bins/XWIKI-8.4/logback-classic-1.1.7.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [ch.qos.logback.classic.util.ContextSelectorStaticBinder]
.E
Time: 0.503
There was 1 failure:
1) test0(com.xpn.xwiki.objects.classes.BaseClass_ESTest)
java.lang.ClassCastException: com.xpn.xwiki.objects.classes.UsersClass cannot be cast to com.xpn.xwiki.objects.classes.TextAreaClass
	at com.xpn.xwiki.objects.classes.BaseClass.addTextAreaField(BaseClass.java:890)
	at com.xpn.xwiki.objects.classes.BaseClass.addTextAreaField(BaseClass.java:878)
	at com.xpn.xwiki.objects.classes.BaseClass.addTextAreaField(BaseClass.java:868)
	at com.xpn.xwiki.objects.classes.BaseClass_ESTest.test0(BaseClass_ESTest.java:25)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.internal.runners.statements.FailOnTimeout$CallableStatement.call(FailOnTimeout.java:298)
	at org.junit.internal.runners.statements.FailOnTimeout$CallableStatement.call(FailOnTimeout.java:292)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.lang.Thread.run(Thread.java:748)

FAILURES!!!
Tests run: 1,  Failures: 1

```

# Running on your own project
---------------
1. Put your software libraries and dependencies in a dedicated directory in `resources/targetedSoftware/`. Then modify the value of the "path.libs" properties in `src/resources/config.properties` to target the libs your software libs directory.
2. Put your log file in `src/resources/logs/` directory. Then modify the value of the "path.log" properties in `src/resources/config.properties` to target your log file.
3. Set "frame.level" properties in `src/resources/config.properties` to the frame that you want to have as the target frame.
4. Run the software.
5. Find the generated test at `GGA-tests` directory.
