package ada.spotify.backend.Controller;

import ada.spotify.backend.APIs.Keys;
import ada.spotify.backend.model.playlist.Playlist;
import ada.spotify.backend.model.music.Music;
import ada.spotify.backend.repository.MusicRepository;
import ada.spotify.backend.repository.PlaylistRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api")
public class SpotifyController {

    private final PlaylistRepository playlistRepository;

    private final MusicRepository musicRepository;

    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/get-user-code/");
    private String code = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(Keys.CLIENT_ID)
            .setClientSecret(Keys.CLIENT_SECRET)
            .setRedirectUri(redirectUri)
            .build();

    public SpotifyController(PlaylistRepository playlistRepository, MusicRepository musicRepository) {
        this.playlistRepository = playlistRepository;
        this.musicRepository = musicRepository;
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
        response.sendRedirect("http://localhost:8080/user/playlists");
        return spotifyApi.getAccessToken();
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
    public List<Music> searchMusicName(@RequestParam("q") String q, Model model) throws IOException, ParseException, SpotifyWebApiException {
        final SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(q)
                .offset(0)
                .build();
        return Arrays.asList(searchTracksRequest.execute().getItems()).stream()
                .map(t -> {
            return new Music(t.getAlbum().getName(), t.getHref(), t.getId(), t.getName());
        })
                .toList();
    }
    @GetMapping(value="search/music/{id}")
    public Music searchMusicId(@PathVariable String id) throws IOException, ParseException, SpotifyWebApiException {
        final GetTrackRequest getTrackRequest = spotifyApi.getTrack(id).build();
        final Track track = getTrackRequest.execute();
        return new Music(track.getAlbum().getName(), track.getHref(), track.getId(), track.getName());

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
    public Playlist descobertasDaSemana(HttpServletResponse response) throws IOException, ParseException, SpotifyWebApiException {
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
        playlistRepository.save(playlist);
        for (String id: musicsid) {
                if (musicRepository.findById(id) == null)
                {
                    Music music = searchMusicId(id);
                    music.addPlaylistToMusic(playlist);
                    musicRepository.save(music);
                }
                else {
                    Music music = musicRepository.findById(id);
                    music.addPlaylistToMusic(playlist);
                    musicRepository.save(music);
                }
        }
        response.sendRedirect("http://localhost:8080/user/playlists");
        return playlist;
    }

}
