package wad.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.FileObject;
import wad.domain.Image;
import wad.repository.FileObjectRepository;
import wad.repository.ImageRepository;
import wad.service.ImageService;

@Controller
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FileObjectRepository fileRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String getImages(Model model) {
        model.addAttribute("images", imageRepository.findAll());
        return "index";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String addFile(
            @RequestParam("metadata") String metadata,
            @RequestParam("file") MultipartFile file) throws IOException {
        Image image = new Image();
        image.setMetadata(metadata);

        imageService.add(image, file.getContentType(), file.getOriginalFilename(), file.getBytes());
return "redirect:/images";
    }

    @RequestMapping(value = {"/thumbnails/{id}", "/originals/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(
            @PathVariable String id) {

        return createResponseEntity(fileRepository.findOne(id));
    }

    private ResponseEntity<byte[]> createResponseEntity(FileObject fo) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fo.getContentType()));
        headers.setContentLength(fo.getContentLength());
        headers.setCacheControl("public");
        headers.setExpires(Long.MAX_VALUE);

        return new ResponseEntity<>(fo.getContent(), headers, HttpStatus.CREATED);
    }
}
