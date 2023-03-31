package ada.spotify.backend.Controller;

import ada.spotify.backend.Session;
import ada.spotify.backend.model.music.Music;
import ada.spotify.backend.model.playlist.Playlist;
import ada.spotify.backend.service.MusicService;
import ada.spotify.backend.service.PlaylistService;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class PlaylistController {

    private final PlaylistService playlistService;

    private final MusicService musicService;

    private static SpotifyApi spotifyApi;

    public PlaylistController(PlaylistService playlistService, MusicService musicService) {
        this.playlistService = playlistService;
        this.musicService = musicService;
    }
    //cadastrar playlist com usuário
    @GetMapping(value = "playlists")
    public String findAllPlaylist(Model model) throws IOException, ParseException, SpotifyWebApiException {
        model.addAttribute("playlists", playlistService.findAll());
        /*PlaylistSimplified[] spotifyPlaylists = Session.spotifyApi.getListOfCurrentUsersPlaylists().build().execute().getItems();


        List<Playlist> playlists = new ArrayList<>();

        for (PlaylistSimplified spotifyPlaylist : spotifyPlaylists) {
            Playlist p = new Playlist(spotifyPlaylist.getName(),Session.user.getId());
            playlistService.save(p);

        }

        model.addAttribute("playlists", playlists);*/

        return "playlists";
    }

    @GetMapping(value="new-playlist")
    public String createPlaylist(){
        return("new-playlist");
    }

    @GetMapping(value="/playlist-details/{id}")
    public String detalharPlaylist(@PathVariable("id") Long id, Model model){
        model.addAttribute("musics", playlistService.findById(id.intValue()).getPlaylistTracks());
        model.addAttribute("playlist", playlistService.findById(id.intValue()));
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
        playlist.setIdUser(Session.user.getId());
        playlistService.save(playlist);
        return "redirect:playlists";
    }

    @PostMapping("/delete/{id}")
    public String deletePlaylist(@PathVariable("id") Long id)
    {
        Playlist playlist =  playlistService.findById(id.intValue());
        for (Music music : playlist.getPlaylistTracks())
        {
            music.removePlaylistFromMusic(playlist);
            musicService.save(music);
        }
        playlistService.deleteById(id);
        return "redirect:/user/playlists";
    }

    @PostMapping("/deleteMusic/{idPlaylist}/{idMusic}")
    public String deleteMusicFromPlaylist(@PathVariable("idPlaylist") Long idPlaylist, @PathVariable("idMusic") String idMusic)
    {
        Music music = musicService.findById(idMusic);
        music.removePlaylistFromMusic(playlistService.findById(idPlaylist.intValue()));
        musicService.save(music);
        return "redirect:/user/playlist-details/{idPlaylist}";
    }

}
