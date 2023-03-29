package ada.spotify.backend.repository;

import ada.spotify.backend.model.playlist.Playlist;
import ada.spotify.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
