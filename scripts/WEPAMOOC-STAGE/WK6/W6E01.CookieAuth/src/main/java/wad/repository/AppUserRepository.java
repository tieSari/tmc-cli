package wad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    List<AppUser> findByUsername(String username);
}
