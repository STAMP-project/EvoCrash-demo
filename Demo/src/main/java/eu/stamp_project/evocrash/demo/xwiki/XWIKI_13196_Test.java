package eu.stamp_project.evocrash.demo.xwiki;


import net.jcip.annotations.NotThreadSafe;
import eu.stamp_project.evocrash.demo.SystemBase;

import org.crash.client.log.LogParser;
import org.crash.master.EvoSuite;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.result.TestGenerationResult;
import org.evosuite.testcase.TestChromosome;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@NotThreadSafe
public class XWIKI_13196_Test extends SystemBase {

	private int frameLevel = 6;
	private String ExceptionType = "java.lang.IllegalArgumentException";
	private String issueNumber = "XWIKI-13196";
	private String softwareVersion = "7.4.2";
	private Path logPath;
	private String dependencies ="";
	private Path testPath;
	
	public XWIKI_13196_Test() {
		String user_dir = System.getProperty("user.dir");
		Path binpath = Paths.get(user_dir, "EvoCrash/Demo/src/eu.stamp_project.evocrash/resources", "targetedSoftware", "XWIKI-bins");
		String bin_path = binpath.toString();
		logPath = Paths.get(user_dir, "EvoCrash/Demo/src/eu.stamp_project.evocrash/resources", "logs","XWIKI", issueNumber , issueNumber+".log");
		
    		File depFolder = new File(bin_path,"XWIKI-"+softwareVersion);
    		File[] listOfFilesInSourceFolder = depFolder.listFiles();
    		for(int i = 0; i < listOfFilesInSourceFolder.length; i++){
    			if(listOfFilesInSourceFolder[i].getName().charAt(0) !='.') {
    				Path depPath = Paths.get(depFolder.getAbsolutePath(), listOfFilesInSourceFolder[i].getName());
    				String dependency = depPath.toString();
    				
    				dependencies += (dependency+":");
    			}
    		}
    		dependencies = dependencies.substring(0, dependencies.length() - 1);
    		
    		testPath = Paths.get(user_dir, "GGA-tests");
	}
	
	
	@Test @Ignore
	public void runTest() {

		String targetClass = LogParser.getTargetClass(logPath.toString(), frameLevel);
		
		String[] command = {
				"-generateTests",
				"-Dcriterion=CRASH", 
				"-Dsandbox=TRUE",
				"-Dtest_dir="+ testPath.toString(),
				"-Drandom_tests=1",
				"-Dminimize=TRUE",
				"-Dheadless_chicken_test=FALSE",
				"-Dp_functional_mocking=1.0",
				"-Dfunctional_mocking_percent=1.0",
				"-Dmax_recursion=50",
				"-Dpopulation=80",
				"-Dsearch_budget=1800",
				"-Dtarget_frame="+frameLevel,
				"-Dvirtual_fs=FALSE",
				"-Dreplace_calls=FALSE",
				"-Dtools_jar_location=/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib",
				"-Dreset_static_fields=FALSE",
				"-Dvirtual_net=FALSE",
				"-Dtarget_exception_crash="+ExceptionType,
				"-DEXP="+ logPath.toString(),
				"-projectCP",
				dependencies,
				"-class", 
				targetClass
				};
		
		EvoSuite evosuite = new EvoSuite();
		
		try {
			Object result = evosuite.parseCommandLine(command);
			List<List<TestGenerationResult>> results = (List<List<TestGenerationResult>>)result;
			GeneticAlgorithm<?> ga = getGAFromResult(results);
			if (ga == null){
				// ga is null when during bootstrapping the ideal eu.stamp_project.evocrash is found!
				Assert.assertTrue(true);
			}
			else{
				TestChromosome best = (TestChromosome) ga.getBestIndividual();
				Assert.assertEquals(0.0, best.getFitness(), 0);
			}
		}catch(Exception e){
			e.printStackTrace(System.out);
		}

	}

}