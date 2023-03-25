package ada.spotify.backend.model.playlist;

import ada.spotify.backend.model.track.Music;
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
    @OneToMany(mappedBy = "playlist")
    private List<Music> playlistTracks;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long idUser;

    public Playlist(List<Music> playlistTracks, String name, Long idUser) {
        this.playlistTracks = playlistTracks;
        this.name = name;
        this.idUser = idUser;
    }
}
