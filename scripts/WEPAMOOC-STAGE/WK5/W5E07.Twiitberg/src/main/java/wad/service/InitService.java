package wad.service;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wad.domain.Twiit;
import wad.domain.Twiiter;
import wad.repository.TwiitRepository;
import wad.repository.TwiiterRepository;

@Component
public class InitService {

    @Autowired
    private TwiiterRepository twiiterRepository;

    @Autowired
    private TwiitRepository twiitRepository;

    @PostConstruct
    public void init() {
        Twiiter t = new Twiiter();
        t.setName("Twiitberg");

        t = twiiterRepository.save(t);

        Twiit tt = new Twiit();
        tt.setContent("viestin sisältö");
        tt = twiitRepository.save(tt);

        t.getTwiits().add(tt);
        twiiterRepository.save(t);

        tt.setTwiiter(t);
        twiitRepository.save(tt);
    }
}
