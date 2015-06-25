package wad;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import wad.domain.Image;
import wad.repository.ImageRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ImageServiceTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private ImageRepository imageRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Points("W6E05.1")
    public void addingAnImageRedirectsToImageSpecificPage() throws Exception {
        String metadata = UUID.randomUUID().toString().substring(0, 6);
        String imageName = UUID.randomUUID().toString().substring(0, 6);
        String content = UUID.randomUUID().toString().substring(0, 6);
        MockMultipartFile multipartFile = new MockMultipartFile("file", imageName, "image/png", content.getBytes());

        MvcResult res = mockMvc.perform(fileUpload("/images").file(multipartFile).param("metadata", metadata))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        try {
            Thread.sleep(100);
        } catch (Throwable t) {

        }

        for (Image image : imageRepository.findAll()) {

            res = mockMvc.perform(get("/images/{id}", image.getId()))
                    .andExpect(model().attributeExists("image"))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            Image i = (Image) res.getModelAndView().getModel().get("image");
            assertEquals(image.getMetadata(), i.getMetadata());
        }
    }

    @Test
    @Points("W6E05.2")
    public void retrievingAnImageHasETagHeader() throws Exception {
        String metadata = UUID.randomUUID().toString().substring(0, 6);
        String imageName = UUID.randomUUID().toString().substring(0, 6);
        String content = UUID.randomUUID().toString().substring(0, 6);
        MockMultipartFile multipartFile = new MockMultipartFile("file", imageName, "image/png", content.getBytes());

        MvcResult res = mockMvc.perform(fileUpload("/images").file(multipartFile).param("metadata", metadata))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        try {
            Thread.sleep(100);
        } catch (Throwable t) {

        }

        for (Image image : imageRepository.findAll()) {
            String originalId = image.getOriginal().getId();
            mockMvc.perform(get("/images/originals/{id}", originalId))
                    .andExpect(header().string("ETag", "\"" + originalId + "\""))
                    .andExpect(status().is(HttpStatus.CREATED.value()));
        }
    }

    @Test
    @Points("W6E05.2")
    public void retrievingAnImageWithIfNoneMatchReturnsNotChanged() throws Exception {
        String metadata = UUID.randomUUID().toString().substring(0, 6);
        String imageName = UUID.randomUUID().toString().substring(0, 6);
        String content = UUID.randomUUID().toString().substring(0, 6);
        MockMultipartFile multipartFile = new MockMultipartFile("file", imageName, "image/png", content.getBytes());

        MvcResult res = mockMvc.perform(fileUpload("/images").file(multipartFile).param("metadata", metadata))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        try {
            Thread.sleep(100);
        } catch (Throwable t) {

        }

        for (Image image : imageRepository.findAll()) {
            String originalId = image.getOriginal().getId();
            mockMvc.perform(get("/images/originals/{id}", originalId))
                    .andExpect(header().string("ETag", "\"" + originalId + "\""))
                    .andExpect(status().is(HttpStatus.CREATED.value()));
            
            res = mockMvc.perform(get("/images/originals/{id}", originalId).header("If-None-Match", "\"" + originalId + "\""))
                    .andExpect(status().is(HttpStatus.NOT_MODIFIED.value()))
                    .andReturn();
            
            assertTrue(res.getResponse().getContentAsByteArray().length == 0);
        }
    }

}
