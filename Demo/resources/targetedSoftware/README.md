```
java -cp ../evocrash-lib/evocrash-1.0.0.jar:../src/test:"$(cat xwikiclasspath.txt)" org.junit.runner.JUnitCore com.xpn.xwiki.objects.classes.BaseClass_ESTest


for i in ANT-bins/ANT-1.6.2/*jar; do echo -n $i":"; done > classpath.txt

javac -cp ../../evocrash-lib/evocrash-1.0.0.jar:"$(cat classpath.txt)"  Crash.java

```
