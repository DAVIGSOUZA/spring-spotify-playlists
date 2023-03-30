package ada.spotify.backend.Controller;

import ada.spotify.backend.model.music.Music;
import ada.spotify.backend.model.playlist.Playlist;
import ada.spotify.backend.service.MusicService;
import ada.spotify.backend.service.PlaylistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class PlaylistController {

    private final PlaylistService playlistService;

    private final MusicService musicService;

    public PlaylistController(PlaylistService playlistService, MusicService musicService) {
        this.playlistService = playlistService;
        this.musicService = musicService;
    }

    @GetMapping(value = "playlists")
    public String findAllPlaylist(Model model){
        model.addAttribute("playlists", playlistService.findAll());
        return "playlists";
    }

    @GetMapping(value="new-playlist")
    public String createPlaylist(){
        return("new-playlist");
    }

    @GetMapping(value="/playlist-details/{id}")
    public String detalharPlaylist(@PathVariable("id") Long id, Model model){
        model.addAttribute("musics", musicService.findAll());
        return("playlist-details");
    }

    @GetMapping(value="descobertas-da-semana")
    public String descobertas(){
        return("redirect:/api/descobertas-da-semana");
    }

    @PostMapping("/add")
    public String addNewPlaylist(@RequestParam("name") String name)
    {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlistService.save(playlist);
        return "redirect:playlists";
    }

    @PostMapping("/delete/{id}")
    public String deletePlaylist(@PathVariable("id") Long id)
    {
        playlistService.deleteById(id);
        return "redirect:/user/playlists";
    }
}
