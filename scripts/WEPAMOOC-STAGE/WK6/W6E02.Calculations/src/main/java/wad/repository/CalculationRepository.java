package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Calculation;

public interface CalculationRepository extends JpaRepository<Calculation, Long> {

}
