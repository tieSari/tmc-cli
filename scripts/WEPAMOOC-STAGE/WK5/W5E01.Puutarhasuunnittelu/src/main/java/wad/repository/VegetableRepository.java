package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Vegetable;

public interface VegetableRepository extends JpaRepository<Vegetable, Long> {

}
