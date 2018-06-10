```
java -cp ../eu.stamp_project.evocrash-lib/eu.stamp_project.evocrash-1.0.0.jar:../src/eu.stamp_project.evocrash:"$(cat xwikiclasspath.txt)" org.junit.runner.JUnitCore com.xpn.xwiki.objects.classes.BaseClass_ESTest


for i in ANT-bins/ANT-1.6.2/*jar; do echo -n $i":"; done > classpath.txt

javac -cp ../../eu.stamp_project.evocrash-lib/eu.stamp_project.evocrash-1.0.0.jar:"$(cat classpath.txt)"  Crash.java

```
