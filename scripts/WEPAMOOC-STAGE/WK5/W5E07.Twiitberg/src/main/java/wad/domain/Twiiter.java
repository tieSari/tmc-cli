package wad.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Twiiter extends AbstractPersistable<Long> {

    private String name;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Twiit> twiits;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Twiiter> favourite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Twiit> getTwiits() {
        if(twiits == null) {
            twiits = new ArrayList<>();
        }
        
        return twiits;
    }

    public void setTwiits(List<Twiit> twiits) {
        this.twiits = twiits;
    }

    public List<Twiiter> getFavourite() {
        return favourite;
    }

    public void setFavourite(List<Twiiter> favourite) {
        this.favourite = favourite;
    }
}
