package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.Temporal;
import org.hibernate.validator.constraints.NotBlank;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;

@Points("W3E01.1")
public class A_DomainTest {

    private final String DATE_FORMAT = "d.M.y H.m";
    private final String SLEEP_CLASSNAME = "wad.domain.Sleep";

    private final Map<String, Class> sleepAttributesAndTypes = new TreeMap<String, Class>() {
        {
            put("start", Date.class);
            put("end", Date.class);
            put("feeling", String.class);
        }
    };

    private final Map<String, List<Class>> sleepAttributeAnnotations = new TreeMap<String, List<Class>>() {
        {
            put("start", EntityTester.createAnnotationList(Temporal.class, DateTimeFormat.class));
            put("end", EntityTester.createAnnotationList(Temporal.class, DateTimeFormat.class));
            put("feeling", EntityTester.createAnnotationList(NotBlank.class));
        }
    };

    @Test
    public void validateSleep() {
        new EntityTester().testEntity(SLEEP_CLASSNAME, sleepAttributesAndTypes, sleepAttributeAnnotations);
    }

    @Test
    public void correctTemporalTypeForFieldStart() throws NoSuchFieldException {

        assertTrue("Field start in class " + SLEEP_CLASSNAME + " should have Temporal.DATE as the value for annotation " + Temporal.class.getName() + ".", Reflex.reflect(SLEEP_CLASSNAME).cls().getDeclaredField("start").getAnnotation(Temporal.class).value().toString().equals("DATE"));
    }

    @Test
    public void correctDateTimeFormatForFieldStart() throws NoSuchFieldException {

        assertTrue("Field start in class " + SLEEP_CLASSNAME + " should have " + DATE_FORMAT + " as the pattern for annotation " + DateTimeFormat.class.getName() + ".", Reflex.reflect(SLEEP_CLASSNAME).cls().getDeclaredField("start").getAnnotation(DateTimeFormat.class).pattern().equals(DATE_FORMAT));
    }

    @Test
    public void correctTemporalTypeForFieldEnd() throws NoSuchFieldException {

        assertTrue("Field end in class " + SLEEP_CLASSNAME + " should have Temporal.DATE as the value for annotation " + Temporal.class.getName() + ".", Reflex.reflect(SLEEP_CLASSNAME).cls().getDeclaredField("end").getAnnotation(Temporal.class).value().toString().equals("DATE"));
    }

    @Test
    public void correctDateTimeFormatForFieldEnd() throws NoSuchFieldException {

        assertTrue("Field end in class " + SLEEP_CLASSNAME + " should have " + DATE_FORMAT + " as the pattern for annotation " + DateTimeFormat.class.getName() + ".", Reflex.reflect(SLEEP_CLASSNAME).cls().getDeclaredField("end").getAnnotation(DateTimeFormat.class).pattern().equals(DATE_FORMAT));
    }
}
