package ada.spotify.backend.repository;

import ada.spotify.backend.model.playlist.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    List<Playlist> findByName(String nome);
    List<Playlist> findByIdUser(Long idUser);
}
