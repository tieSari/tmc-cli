package hy.tmc.cli.frontend.communication.commands;

import com.google.common.util.concurrent.ListenableFuture;

import fi.helsinki.cs.tmc.core.communication.UrlHelper;
import fi.helsinki.cs.tmc.core.configuration.TmcSettings;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;

import hy.tmc.cli.TmcCli;
import hy.tmc.cli.frontend.communication.server.ProtocolException;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

public class SetCourse extends Command<String> {

    private String course;

    public SetCourse(TmcCli cli, String newCourse) {
        super(cli);
        this.course = newCourse;
    }

    @Override
    public String call() throws Exception {
        TmcSettings settings = checkLoginAndGetSettings();
        ListenableFuture<Course> courseFuture = downloadCurrentCourse(settings);
        Course currentCourse = courseFuture.get();
        if (currentCourse == null) {
            return "Could not find course by given param: " + course;
        }
        tmcCli.setCurrentCourse(currentCourse);
        return currentCourse.getName() + " is set as current course.";
    }

    private TmcSettings checkLoginAndGetSettings()
        throws ProtocolException, IllegalStateException, ParseException, IOException {
        TmcSettings settings = tmcCli.defaultSettings();
        if (!settings.userDataExists()) {
            throw new ProtocolException("Please login first.");
        }
        return settings;
    }

    private ListenableFuture<Course> downloadCurrentCourse(TmcSettings settings)
        throws TmcCoreException, IOException {
        if (StringUtils.isNumeric(course)) {
            int courseId = Integer.parseInt(course);
            URI courseUrl = null;
            try {
                courseUrl = new URI(new UrlHelper(settings).getCourseUrl(courseId));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
            return tmcCli.getCore().getCourse(courseUrl);
        } else {
            return tmcCli.getCore().getCourseByName(course);
        }
    }
}
