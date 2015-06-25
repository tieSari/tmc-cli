package wad;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Points("W5E03.1 W5E03.2")
public class FileManagerTest {

    private final String API_URI = "/files";

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void getFilesReturnsSite() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("files"))
                .andReturn();

        assertTrue(res.getResponse().getContentAsString().contains("File manager"));
    }

    @Test
    public void canPostAFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "bac.on", "burgerz/mmm", "yay".getBytes());

        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        Long largestFileId = largestFileId(res.getResponse().getContentAsString());
        mockMvc.perform(fileUpload(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        assertTrue(largestFileId < largestFileId(res.getResponse().getContentAsString()));
    }

    @Test
    public void filenameVisibleAfterPost() throws Exception {
        String randomName = UUID.randomUUID().toString().substring(0, 6);

        MockMultipartFile multipartFile = new MockMultipartFile("file", randomName, "burgerz/mmm", "yay".getBytes());

        mockMvc.perform(fileUpload(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        assertTrue(res.getResponse().getContentAsString().contains(randomName));
    }

    @Test
    public void contentTypeVisibleAfterPost() throws Exception {
        String randomName = UUID.randomUUID().toString().substring(0, 6);
        String randomContentType = UUID.randomUUID().toString().substring(0, 6);

        MockMultipartFile multipartFile = new MockMultipartFile("file", randomName, randomContentType, "yay".getBytes());

        mockMvc.perform(fileUpload(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        assertTrue(res.getResponse().getContentAsString().contains(randomContentType));
    }

    @Test
    public void canDeleteAFile() throws Exception {
        String randomContent = UUID.randomUUID().toString().substring(0, 6);
        String randomName = UUID.randomUUID().toString().substring(0, 6);
        String randomContentType = UUID.randomUUID().toString().substring(0, 6);

        MockMultipartFile multipartFile = new MockMultipartFile("file", randomName, randomContentType, randomContent.getBytes());

        mockMvc.perform(fileUpload(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        Long largestFileId = largestFileId(res.getResponse().getContentAsString());

        mockMvc.perform(delete(API_URI + "/" + largestFileId))
                .andExpect(status().is3xxRedirection());

        res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        assertTrue(largestFileId > largestFileId(res.getResponse().getContentAsString()));
    }
    
    @Test
    public void downloadedFileAvailableAndCorrect() throws Exception {
        String randomContent = UUID.randomUUID().toString().substring(0, 6);
        String randomName = UUID.randomUUID().toString().substring(0, 6);

        MockMultipartFile multipartFile = new MockMultipartFile("file", randomName, MediaType.IMAGE_GIF_VALUE, randomContent.getBytes());

        mockMvc.perform(fileUpload(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        Long largestFileId = largestFileId(res.getResponse().getContentAsString());

        res = mockMvc.perform(get(API_URI + "/" + largestFileId))
                .andExpect(status().is2xxSuccessful()).andReturn();
        
        assertEquals(MediaType.IMAGE_GIF_VALUE, res.getResponse().getContentType());
        assertEquals(randomContent, new String(res.getResponse().getContentAsByteArray()));
    }

    private Long largestFileId(String content) {
        Pattern p = Pattern.compile("(files\\/[0-9]*)");
        Matcher matcher = p.matcher(content);

        Long largest = 0L;
        while (matcher.find()) {
            String res = matcher.group();
            res = res.substring(res.indexOf("/") + 1).trim();

            try {
                Long curr = Long.parseLong(res);
                if (curr > largest) {
                    largest = curr;
                }
            } catch (Throwable t) {
            }
        }

        return largest;
    }
}
