package Controller;

import APIs.Keys;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final URI redirectUri = SpotifyHttpManager.makeUri("http:localhost:8080/api/get-user-code/");
    private String code = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(Keys.CLIENT_ID)
            .setClientSecret(Keys.CLIENT_SECRET)
            .setRedirectUri(redirectUri)
            .build();
}
