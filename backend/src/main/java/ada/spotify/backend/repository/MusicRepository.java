package ada.spotify.backend.repository;

import ada.spotify.backend.model.music.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Integer> {

    List<Music> findByNameContains(String nome);
    Music findById(String id);
}
