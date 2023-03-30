package ada.spotify.backend.service;

import ada.spotify.backend.model.track.Music;
import ada.spotify.backend.repository.MusicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicService {

    private final MusicRepository musicRepository;

    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    public Music save(Music music) {
        return musicRepository.save(music);
    }

    public List<Music> findByName(String nome){
        return musicRepository.findByNameContains(nome);
    }

    public Music findById(String id) {
        return musicRepository.findById(id);
    }
}
