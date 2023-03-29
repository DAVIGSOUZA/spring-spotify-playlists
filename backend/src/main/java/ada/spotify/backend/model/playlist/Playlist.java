package ada.spotify.backend.model.playlist;

import ada.spotify.backend.model.track.Music;
import ada.spotify.backend.model.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "playlist")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Playlist {
    @JsonManagedReference
    @ManyToMany(mappedBy = "playlist")
    private List<Music> playlistTracks;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private User user;

    public Playlist(List<Music> playlistTracks, String name, User user) {
        this.playlistTracks = playlistTracks;
        this.name = name;
        this.user = user;
    }
}
