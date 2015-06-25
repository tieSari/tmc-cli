package wad.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByGameName(String gameName);
    List<Rating> findByGameName(String gameName, Sort sort);
}
