package wad.service;

import java.util.Collection;
import wad.domain.Game;

public interface GameService {

    void setUri(String uri);

    Game create(Game game);

    Game findByName(String name);

    void deleteByName(String name);

    Collection<Game> findAll();
}
