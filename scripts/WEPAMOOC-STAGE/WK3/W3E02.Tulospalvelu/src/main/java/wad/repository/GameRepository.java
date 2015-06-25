package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByName(String name);
}
