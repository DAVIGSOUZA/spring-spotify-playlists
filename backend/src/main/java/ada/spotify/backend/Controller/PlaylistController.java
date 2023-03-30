package ada.spotify.backend.Controller;

import ada.spotify.backend.model.playlist.Playlist;
import ada.spotify.backend.model.track.Music;
import ada.spotify.backend.service.PlaylistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import java.util.List;

@Controller
@RequestMapping("/user")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping(value = "playlists")
    public String findAllPlaylist(Model model){
        model.addAttribute("playlists", playlistService.findAll());
        return "playlists";
    }
}
