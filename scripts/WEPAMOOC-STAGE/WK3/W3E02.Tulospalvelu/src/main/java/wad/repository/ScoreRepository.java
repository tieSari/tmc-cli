package wad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Game;
import wad.domain.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByGame(Game game);
    Score findByGameAndId(Game game, Long id);
}
