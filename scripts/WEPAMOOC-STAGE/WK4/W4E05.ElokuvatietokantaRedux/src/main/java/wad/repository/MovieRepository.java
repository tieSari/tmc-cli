package wad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wad.domain.Actor;
import wad.domain.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE :actor NOT MEMBER OF m.actors")
    List<Movie> findMoviesWithoutActor(@Param("actor") Actor actor);
}
