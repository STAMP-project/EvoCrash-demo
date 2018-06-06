package test;

import net.jcip.annotations.NotThreadSafe;

import org.apache.commons.io.FilenameUtils;
import org.crash.client.log.LogParser;
import org.crash.master.EvoSuite;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.result.TestGenerationResult;
import org.evosuite.testcase.TestChromosome;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@NotThreadSafe
public class Generic_Test extends SystemBase {

    private static final String CONFIG_PATH = "../resources/config.properties";

    private Properties properties;

    private String criterion;
    private String sandbox;
    private final Path testPath;
    private String randomTest;
    private String pFunctionalMocking;
    private String percentFunctionalMocking;
    private String minimize;
    private String headlessChickenTest;
    private String population;
    private String searchBudget;
    private Optional<String> stoppingCondition;
    private String globalTimeout;
    private final int targetFrame;
    private String virtualFs;
    private String useSeparateClassLoader;
    private String replaceCalls;
    private String maxRecursion;
    private Optional<String> toolsJarLocation;
    private String resetStaticFields;
    private String virtualNet;
    private final String exceptionType;
    private final Path logPath;
    private final String dependencies;

    public Generic_Test() {

        loadProperties();

        criterion = properties.getProperty("criterion");
        sandbox = properties.getProperty("sandbox");
        testPath = Paths.get(properties.getProperty("testsPath"));
        randomTest = properties.getProperty("randomTest");
        pFunctionalMocking = properties.getProperty("pFunctionalMocking");
        percentFunctionalMocking = properties.getProperty("percentFunctionalMocking");
        minimize = properties.getProperty("minimize");
        headlessChickenTest = properties.getProperty("headlessChickenTest");
        population = properties.getProperty("population");
        searchBudget = properties.getProperty("searchBudget");
        stoppingCondition = Optional.ofNullable(properties.getProperty("stoppingCondition"));
        globalTimeout = properties.getProperty("globalTimeout");
        targetFrame = Integer.parseInt(properties.getProperty("frameLevel"));
        virtualFs = properties.getProperty("virtualFs");
        useSeparateClassLoader = properties.getProperty("useSeparateClassLoader");
        replaceCalls = properties.getProperty("replaceCalls");
        maxRecursion = properties.getProperty("maxRecursion");
        toolsJarLocation = Optional.ofNullable(properties.getProperty("toolsJarLocation"));
        resetStaticFields = properties.getProperty("resetStaticFields");
        virtualNet = properties.getProperty("virtualNet");
        exceptionType = properties.getProperty("exceptionType");
        logPath = Paths.get(properties.getProperty("logPath"));
        dependencies = getDependencies(properties.getProperty("softwareLibsPath"));
    }


    @Test
    public void runTest() {
        List<String> commands = new ArrayList<>();
        commands.add("-generateTests");
        commands.add("-Dcriterion="+criterion);
        commands.add("-Dsandbox="+sandbox);
        commands.add("-Dtest_dir="+ testPath.toString());
        commands.add("-Drandom_tests="+randomTest);
        commands.add("-Dp_functional_mocking="+pFunctionalMocking);
        commands.add("-Dfunctional_mocking_percent="+ percentFunctionalMocking);
        commands.add("-Dminimize="+minimize);
        commands.add("-Dheadless_chicken_test="+ headlessChickenTest);
        commands.add("-Dpopulation="+population);
        commands.add("-Dsearch_budget="+searchBudget);
        stoppingCondition.filter(command -> ! command.isEmpty()).ifPresent(command -> commands.add("-Dstopping_condition="+command));
        commands.add( "-Dglobal_timeout="+ globalTimeout);
        commands.add("-Dtarget_frame="+ targetFrame);
        commands.add( "-Dvirtual_fs="+virtualFs);
        commands.add("-Duse_separate_classloader="+useSeparateClassLoader);
        commands.add("-Dreplace_calls="+replaceCalls);
        commands.add("-Dmax_recursion="+maxRecursion);
        toolsJarLocation.filter(command -> ! command.isEmpty()).ifPresent(command -> commands.add("-Dtools_jar_location="+command));
        commands.add("-Dreset_static_fields="+resetStaticFields);
        commands.add("-Dvirtual_net="+virtualNet);
        commands.add("-Dtarget_exception_crash="+ exceptionType);
        commands.add("-DEXP="+ logPath.toString());
        commands.add("-projectCP="+dependencies);
        commands.add("-class="+LogParser.getTargetClass(logPath.toString(), targetFrame));

        /*String[] command = {
                "-generateTests",
                "-Dcriterion=CRASH",
                "-Dsandbox=TRUE",
                "-Dtest_dir="+ testPath.toString(),
                "-Drandom_tests=0",
                "-Dp_functional_mocking=0.8",
                "-Dfunctional_mocking_percent=0.5",
                "-Dminimize=TRUE",
                "-Dheadless_chicken_test=FALSE",
                "-Dpopulation=80",
                "-Dsearch_budget=1800",
                //"-Dstopping_condition=MAXFITNESSEVALUATIONS",
                "-Dglobal_timeout="+(5*60*60),
                "-Dtarget_frame="+ targetFrame,
                "-Dvirtual_fs=TRUE",
                "-Duse_separate_classloader=FALSE",
                "-Dreplace_calls=FALSE",
                "-Dmax_recursion=50",
                // "-Dtools_jar_location=/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib",
                "-Dreset_static_fields=FALSE",
                "-Dvirtual_net=FALSE",
                "-Dtarget_exception_crash="+ exceptionType,
                "-DEXP="+ logPath.toString(),
                "-projectCP="+dependencies,
                "-class="+LogParser.getTargetClass(logPath.toString(), targetFrame)
        };*/



        EvoSuite evosuite = new EvoSuite();

        try {
            Object result = evosuite.parseCommandLine(commands.stream().toArray(String[]::new));
            List<List<TestGenerationResult>> results = (List<List<TestGenerationResult>>)result;
            GeneticAlgorithm<?> ga = getGAFromResult(results);
            if (ga == null){
                // ga is null when during bootstrapping the ideal test is found!
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

    /**
     * Concatenate all the dependencies of the softwareLibsPath
     * @return
     */
    private String getDependencies(String libPath){
        String dep = "";
        File lib_dir = new File(libPath);
        File[] listOfFilesInSourceFolder = lib_dir.listFiles();
        for(int i = 0; i < listOfFilesInSourceFolder.length; i++){
            String lib_file_name = listOfFilesInSourceFolder[i].getName();
            // Do not consider non jar files
            if( FilenameUtils.getExtension(lib_file_name).equals("jar")) {
                Path depPath = Paths.get(lib_dir.getAbsolutePath(), lib_file_name);
                String dependency = depPath.toString();
                dep += (dependency+":");
            }

        }
        return dep.substring(0, dep.length() - 1);
    }

    private void loadProperties(){
        properties = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(CONFIG_PATH);
            // load a properties file
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
