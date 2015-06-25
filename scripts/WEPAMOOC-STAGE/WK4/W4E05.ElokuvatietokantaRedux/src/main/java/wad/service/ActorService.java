package wad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.domain.Actor;
import wad.domain.Movie;
import wad.repository.ActorRepository;
import wad.repository.MovieRepository;

@Service
public class ActorService  {

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private MovieRepository movieRepository;

    public Iterable<Actor> list() {
        return actorRepository.findAll();
    }

    @Transactional
    public void add(String name) {
        Actor actor = new Actor();
        actor.setName(name);

        actorRepository.save(actor);
        
    }

    @Transactional
    public void remove(Long actorId) {
        Actor actor = actorRepository.findOne(actorId);
        for (Movie movie : actor.getMovies()) {
            movie.getActors().remove(actor);
        }

        actorRepository.delete(actorId);
    }

    @Transactional
    public void addActorToMovie(Long actorId, Long movieId) {
        Actor actor = actorRepository.findOne(actorId);
        Movie movie = movieRepository.findOne(movieId);

        actor.getMovies().add(movie);
        movie.getActors().add(actor);
    }

    public Actor findById(Long actorId) {
        return actorRepository.findOne(actorId);
    }
}
