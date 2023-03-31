package ada.spotify.backend;

import ada.spotify.backend.model.user.User;
import se.michaelthelin.spotify.SpotifyApi;

public class Session {

    public static SpotifyApi spotifyApi;
    public static User user;

    public static void setSpotifyApi(SpotifyApi spotifyApi) {
        Session.spotifyApi = spotifyApi;
    }
    public static void setUser(User user) {
        Session.user = user;
    }

}
