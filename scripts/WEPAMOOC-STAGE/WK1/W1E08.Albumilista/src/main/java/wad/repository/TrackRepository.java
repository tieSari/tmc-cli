package wad.repository;

import wad.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {

}
