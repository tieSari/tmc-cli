package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
