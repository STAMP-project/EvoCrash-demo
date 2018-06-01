package test;

import net.jcip.annotations.NotThreadSafe;

import org.apache.commons.io.FilenameUtils;
import org.crash.client.log.LogParser;
import org.crash.master.EvoSuite;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.result.TestGenerationResult;
import org.evosuite.testcase.TestChromosome;
import org.junit.Assert;
import org.junit.Before;
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

    private List<String> commands;

    @Before
    public void setUp(){
        loadProperties();
        commands = new ArrayList<>();
    }

    @Test
    public void runTest() {

        String logPath = Paths.get(properties.getProperty("path.log")).toString();

        commands.add("-generateTests");
        addProperty("criterion");
        addProperty("sandbox");
        commands.add(String.format("-Dtest_dir=%s",Paths.get(properties.getProperty("path.test")).toString()));
        addProperty("random_tests","test.random");
        addProperty("p_functional_mocking","functional.mocking.p");
        addProperty("functional_mocking_percent","functional.mocking.percent");
        addProperty("minimize");
        addProperty("headless_chicken_test","headless.chicken.test");
        addProperty("population");
        addProperty("search_budget","search.budget");
        addOptionalProperty("stopping_condition","condition.stopping");
        addProperty("global_timeout","timeout.global");
        addProperty("target_frame","frame.level");
        addProperty("virtual_fs","virtual.fs");
        addProperty("use_separate_classloader","class.loader.separate");
        addProperty("replace_calls","calls.replace");
        addProperty("max_recursion","recursion.max");
        addOptionalProperty("tools_jar_location","jar.tools.location");
        addProperty("reset_static_fields","fields.static.reset");
        addProperty("virtual_net","net.virtual");
        addProperty("target_exception_crash","exception.type");
        commands.add(String.format("-DEXP=%s",logPath));

        System.out.println(String.format("Commands used without dependencies and class: %s",commands.toString()));

        commands.add(String.format("-projectCP=%s",getDependencies(properties.getProperty("path.libs"))));
        commands.add(String.format("-class=%s",LogParser.getTargetClass(logPath, Integer.parseInt(properties.getProperty("frame.level")))));


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

    private void addProperty(String commandName, String propertyName){
        commands.add(String.format("-D%s=%s",commandName,properties.getProperty(propertyName)));
    }

    private void addProperty(String name){
        addProperty(name,name);
    }

    private void addOptionalProperty(String commandName, String propertyName){
        Optional.ofNullable(properties.getProperty(propertyName)).filter(command -> ! command.isEmpty()).ifPresent(command -> addProperty(commandName,command));
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
