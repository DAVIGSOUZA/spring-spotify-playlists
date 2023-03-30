package ada.spotify.backend.service;

import ada.spotify.backend.model.playlist.Playlist;
import ada.spotify.backend.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Playlist save(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public List<Playlist> findByName(String nome) {
        return playlistRepository.findByName(nome);
    }

    public Playlist findById(Integer id) {
        return playlistRepository.findById(id).orElse(null);
    }

    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }
}
