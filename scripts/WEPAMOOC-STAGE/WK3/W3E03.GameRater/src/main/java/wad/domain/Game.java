package wad.domain;

// ei tietokantaolio; käytössä vain datan siirtämisessä

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Game {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nickname) {
        this.name = nickname;
    }

    // db objects have a new-field by default -- let's ignore it
    @JsonIgnore
    public boolean isNew() {
        return false;
    }
}
