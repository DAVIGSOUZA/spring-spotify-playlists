package ada.spotify.backend.repository;

import ada.spotify.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
