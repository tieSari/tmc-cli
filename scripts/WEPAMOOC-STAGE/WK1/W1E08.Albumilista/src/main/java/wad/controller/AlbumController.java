package wad.controller;

import wad.repository.AlbumRepository;
import wad.domain.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.domain.Track;
import wad.repository.TrackRepository;

@Controller
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;


    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("albums", albumRepository.findAll());
        return "/WEB-INF/views/page.jsp";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createAlbum(@ModelAttribute Album album) {
        String name = album.getName();
        if (name != null && !name.trim().isEmpty()) {
            albumRepository.save(album);
        }

        return "redirect:/albums";
    }

}
