package ada.spotify.backend.model.PlaylistUser;

import ada.spotify.backend.model.playlist.Playlist;
import ada.spotify.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

//@Entity
//@Table(name = "playlist_user")
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@ToString
//public class PlaylistUser {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "playlist_id")
//    private Playlist playlist;
//
//    public PlaylistUser(User user, Playlist playlist) {
//        this.user = user;
//        this.playlist = playlist;
//    }
//}
