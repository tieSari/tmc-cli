package wad.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import wad.domain.Twiit;
import wad.domain.TwiitPic;
import wad.repository.TwiitRepository;

@Controller
@RequestMapping("/twiits")
public class TwiitController {

    @Autowired
    private TwiitRepository twiitRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String listTwiits(Model model) {
        Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC, "created");
        model.addAttribute("twiits", twiitRepository.findAll(pageable).getContent());

        return "twiits";
    }

    @RequestMapping(value = "{id}/imagedata/*", method = RequestMethod.GET)
    public ResponseEntity<byte[]> listTwiits(@PathVariable Long id, Model model) {
        TwiitPic pic = twiitRepository.findOne(id).getTwiitPic();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(pic.getContentType()));

        return new ResponseEntity<>(pic.getContent(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addTwiit(@RequestParam String content, @RequestParam("file") MultipartFile file) throws IOException {
        Twiit twiit = new Twiit();
        twiit.setContent(content);

        if (file != null
                && file.getContentType() != null
                && file.getContentType().contains("image")) {
            
            TwiitPic pic = new TwiitPic();
            pic.setName(file.getOriginalFilename());
            pic.setContentType(file.getContentType());
            pic.setContent(file.getBytes());
            twiit.setTwiitPic(pic);
        }

        twiitRepository.save(twiit);

        return "redirect:/twiits";
    }
}
