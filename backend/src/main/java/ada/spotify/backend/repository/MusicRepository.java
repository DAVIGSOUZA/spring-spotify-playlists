package ada.spotify.backend.repository;
import ada.spotify.backend.model.track.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
