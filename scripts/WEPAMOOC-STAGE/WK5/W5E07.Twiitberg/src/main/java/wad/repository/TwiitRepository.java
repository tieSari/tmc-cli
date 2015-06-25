package wad.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.*;

public interface TwiitRepository extends JpaRepository<Twiit, Long> {

    List<Twiit> findByTwiiter(Twiiter twiiter, Pageable pageable);

}
