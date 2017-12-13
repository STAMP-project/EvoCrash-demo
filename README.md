# EvoCrash

Getting Started
---------------

```
git clone https://github.com/STAMP-project/EvoCrash.git
cd EvoCrash/Demo
cd src/

# XWIKI_13916
# compiling the code
javac -cp ../evocrash-lib/evocrash-1.0.0.jar test/SystemBase.java test/XWIKI_13916_Test.java

# executing Evocrash, this creates tests in ../GGA-tests
java -cp ../evocrash-lib/evocrash-1.0.0.jar:. org.junit.runner.JUnitCore test.XWIKI_13916_Test

# looking at the generated tests
cat ../GGA-tests/com/xpn/xwiki/objects/classes/BaseClass_ESTest.java

```

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

```
@RunWith(EvoRunner.class) @EvoRunnerParameters(useVFS = true, useJEE = true) 
public class BaseClass_ESTest extends BaseClass_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      BaseClass baseClass0 = new BaseClass();
      baseClass0.addUsersField((String) null, (String) null, true);
      TextAreaClass.ContentType textAreaClass_ContentType0 = TextAreaClass.ContentType.VELOCITY_CODE;
      // Undeclared exception!
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
