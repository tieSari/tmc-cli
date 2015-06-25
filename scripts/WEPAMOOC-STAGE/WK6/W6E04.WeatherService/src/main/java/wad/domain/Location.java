package wad.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Location extends AbstractPersistable<Long> {

    private String name;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
    private List<WeatherEntry> weatherEntries;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<WeatherEntry> getWeatherEntries() {
        return weatherEntries;
    }

    public void setWeatherEntries(List<WeatherEntry> weatherEntries) {
        this.weatherEntries = weatherEntries;
    }

}
