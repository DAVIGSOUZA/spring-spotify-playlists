package ada.spotify.backend.Controller;

import ada.spotify.backend.APIs.Keys;
import ada.spotify.backend.model.playlist.Playlist;
import ada.spotify.backend.model.music.Music;
import ada.spotify.backend.service.MusicService;
import ada.spotify.backend.service.PlaylistService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class SpotifyController {

    private final PlaylistService playlistService;

    private final MusicService musicService;



    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/user/get-user-code/");
    private String code = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(Keys.CLIENT_ID)
            .setClientSecret(Keys.CLIENT_SECRET)
            .setRedirectUri(redirectUri)
            .build();

    public SpotifyController(PlaylistService playlistService, MusicService musicService) {
        this.playlistService = playlistService;
        this.musicService = musicService;
    }

    @GetMapping("login")
    @ResponseBody
    public ResponseEntity<Void> spotifyLogin() {
        AuthorizationCodeUriRequest autorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-private, user-read-email, user-top-read")
                .show_dialog(true)
                .build();
        final URI uri = autorizationCodeUriRequest.execute();
        return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
    }

    @GetMapping(value="get-user-code/")
    public String getSpotifyUserCode(@RequestParam("code") String userCode, HttpServletResponse response) throws IOException {
        code = userCode;
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                .build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return("redirect:/user/playlists");
    }

    @GetMapping(value="user-top-tracks")
    public void getUserTopTracks() {
            final GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks()
                .limit(10)
                .offset(0)
                .build();
        try {
            final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();
            Track[] topTracks = trackPaging.getItems();
            for (Track track : topTracks)
                System.out.println(track);
        } catch (Exception e) {
            System.out.println("We're sorry, something went wrong :(\n" + e.getMessage());
        }
    }

    @GetMapping(value="search/music")
    public String searchMusicName(@RequestParam("q") String q, Model model) throws IOException, ParseException, SpotifyWebApiException {
        final SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(q)
                .limit(5)
                .offset(0)
                .build();
        List<Music> list = Arrays.asList(searchTracksRequest.execute().getItems()).stream()
                .map(t -> {
                    return new Music(t.getAlbum().getName(), t.getHref(), t.getId(), t.getName(), Arrays.stream(t.getAlbum().getImages()).findFirst().get().getUrl());
                })
                .toList();
        for (Music music: list) {
            musicService.save(music);
        }
        model.addAttribute("musicquery", list);
        model.addAttribute("playlists", playlistService.findAll());
        Long playlistEscolhida = 0L;
        model.addAttribute("playlistEscolhida", playlistEscolhida);
        return "music-search";
    }


    @PostMapping(value="save-in-playlist/{musicId}")
    public String saveInPlaylist(@PathVariable String musicId, @RequestParam(value="playlistEscolhida") String playlistEscolhida){
        Music music = musicService.findById(musicId);
        music.addPlaylistToMusic(playlistService.findById(Integer.parseInt(playlistEscolhida)));
        musicService.save(music);
        return "redirect:/user/playlists";
    }


//    @PostMapping(value="save-in-playlist/{playlistId}/{musicId}")
//    public String saveInPlaylist(@PathVariable Long playlistId, @PathVariable String musicId){
//       Music music = musicService.findById(musicId);
//       music.addPlaylistToMusic(playlistService.findById(playlistId.intValue()));
//        return "redirect:/user/playlist-details/{playlistId}";
//    }




    @GetMapping(value="search/music/{id}")
    public Music searchMusicId(@PathVariable String id) throws IOException, ParseException, SpotifyWebApiException {
        final GetTrackRequest getTrackRequest = spotifyApi.getTrack(id).build();
        final Track track = getTrackRequest.execute();
        return new Music(track.getAlbum().getName(), track.getHref(), track.getId(), track.getName(), Arrays.stream(track.getAlbum().getImages()).findFirst().get().getUrl());

    }

    @GetMapping(value="search/artist")
    public void searchArtist(@RequestParam("q") String q) {
        final SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(q)
                .limit(5)
                .offset(0)
                .build();
        try {
            final Paging<Artist> artistPaging = searchArtistsRequest.execute();
            Artist[] artistsQuery = artistPaging.getItems();
            for (Artist artist : artistsQuery)
                System.out.println(artist);
        } catch (IOException | SpotifyWebApiException |org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @GetMapping(value="descobertas-da-semana")
    public String descobertasDaSemana(HttpServletResponse response) throws IOException, ParseException, SpotifyWebApiException {
        final SearchPlaylistsRequest searchPlaylistsRequest = spotifyApi.searchPlaylists("Discover Weekly")
                .limit(1)
                .offset(0)
                .build();


        final Paging<PlaylistSimplified> playlistSimplifiedPaging = searchPlaylistsRequest.execute();
        PlaylistSimplified[] playlists = playlistSimplifiedPaging.getItems();
        Optional<PlaylistSimplified> result = Arrays.stream(playlists).findFirst();
        PlaylistSimplified descobertasDaSemana = result.orElse(null);
        final Paging<PlaylistTrack> playlistsItems = spotifyApi.getPlaylistsItems(descobertasDaSemana.getId()).build().execute();
        PlaylistTrack[] playlistTracks = playlistsItems.getItems();
        List<String> musicsid = Arrays.asList(playlistTracks).stream()
                .map(t -> t.getTrack().getId()).toList();
        Playlist playlist = new Playlist("Descobertas da Semana");
        playlistService.save(playlist);
        for (String id: musicsid) {
                if (musicService.findById(id) == null)
                {
                    Music music = searchMusicId(id);
                    music.addPlaylistToMusic(playlist);
                    musicService.save(music);
                }
                else {
                    Music music = musicService.findById(id);
                    music.addPlaylistToMusic(playlist);
                    musicService.save(music);
                }
        }
        return("redirect:/user/playlists");
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
        model.addAttribute("musics", playlistService.findById(id.intValue()).getPlaylistTracks());
        model.addAttribute("playlist", playlistService.findById(id.intValue()));
        return("playlist-details");
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
