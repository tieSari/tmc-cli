package wad.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Rating extends AbstractPersistable<Long> {

    @NotNull
    @Min(0)
    @Max(5)
    private Integer rating;

    // Pelin nimeä ei validoida, koska ohjelma asettaa sen REST URL:n perusteella
    private String gameName;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @JsonIgnore
    public String getGameName() {
        return gameName;
    }

    @JsonIgnore
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
