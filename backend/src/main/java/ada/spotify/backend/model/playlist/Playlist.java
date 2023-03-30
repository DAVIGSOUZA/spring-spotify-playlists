package ada.spotify.backend.model.playlist;

import ada.spotify.backend.model.track.Music;
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

    private Long idUser;

    public Playlist(List<Music> playlistTracks, String name, Long idUser) {
        this.playlistTracks = playlistTracks;
        this.name = name;
        this.idUser = idUser;
    }

    public Playlist(String name, List<Music> playlistTracks) {
        this.playlistTracks = playlistTracks;
        this.name = name;
    }
}
