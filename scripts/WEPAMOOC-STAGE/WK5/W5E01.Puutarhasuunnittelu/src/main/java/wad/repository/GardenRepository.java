package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Garden;

public interface GardenRepository extends JpaRepository<Garden, Long> {

}
