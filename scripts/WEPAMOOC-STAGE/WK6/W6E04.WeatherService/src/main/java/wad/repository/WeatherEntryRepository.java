package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.WeatherEntry;

public interface WeatherEntryRepository extends JpaRepository<WeatherEntry, Long> {

}
