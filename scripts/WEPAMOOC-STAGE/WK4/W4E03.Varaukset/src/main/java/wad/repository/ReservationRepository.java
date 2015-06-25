package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import wad.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
