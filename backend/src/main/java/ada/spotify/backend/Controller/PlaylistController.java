package ada.spotify.backend.Controller;

import ada.spotify.backend.model.playlist.Playlist;
import ada.spotify.backend.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistRepository playlistRepository;

}
