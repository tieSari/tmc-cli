package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Post;

public interface PostRepository extends JpaRepository<Post, String> {

}
