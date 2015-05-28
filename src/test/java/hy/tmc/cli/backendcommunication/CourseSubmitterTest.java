//package hy.tmc.cli.backendcommunication;
//
//import hy.tmc.cli.configuration.ClientData;
//import hy.tmc.cli.domain.Course;
//import hy.tmc.cli.zipping.DefaultRootDetector;
//import hy.tmc.cli.zipping.ProjectRootFinder;
//import static org.junit.Assert.assertEquals;
//import org.junit.Before;
//import org.junit.Test;
//
//public class CourseSubmitterTest {
//
//    private CourseSubmitter courseSubmitter;
//    private String testFilePath = "/home/chang/Desktop/2014-mooc-no-deadline/viikko1";
//    private final String kurssinNimi = "2014-mooc-no-deadline";
//    private final String tehtavanNimi = "Viikko1_020.Kayttajatunnukset";
//
//    @Before
//    public void setup() {
//        this.courseSubmitter = new CourseSubmitter(new ProjectRootFinder(new DefaultRootDetector()));
//        ClientData.setUserData("test", "1234");
//    }
//
//    @Test
//    public void firstTest() {
//        Course ex2 = this.courseSubmitter.getCurrentCourse(testFilePath);
//        assertEquals(kurssinNimi, ex2.getName());
//    }
//
//    @Test
//    public void submission() {
//        
//        this.courseSubmitter.submit(testFilePath, tehtavanNimi);
//        
//    }
//}
