package wad.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Vegetable extends AbstractPersistable<Long> {

    @ManyToMany(mappedBy = "vegetables", fetch = FetchType.LAZY)
    private List<Garden> gardens;

    @NotBlank(message = "thou shalt not try to add a vegetable with no name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Garden> getGardens() {
        return gardens;
    }

    public void setGardens(List<Garden> gardens) {
        this.gardens = gardens;
    }
}
