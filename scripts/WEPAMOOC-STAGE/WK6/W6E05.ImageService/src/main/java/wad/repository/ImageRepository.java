package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

}
