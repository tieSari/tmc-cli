package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Twiiter;

public interface TwiiterRepository extends JpaRepository<Twiiter, Long> {

}
