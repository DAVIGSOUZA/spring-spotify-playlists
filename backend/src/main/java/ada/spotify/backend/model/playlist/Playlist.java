package ada.spotify.backend.model.playlist;

import ada.spotify.backend.model.music.Music;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    @ManyToMany(mappedBy = "playlists")
    private List<Music> playlistTracks = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long idUser;

    public Playlist(String name, Long idUser) {
        this.name = name;
        this.idUser = idUser;
    }

    public Playlist(String name) {
        this.name = name;
    }

    public void addMusicToPlaylist(Music music){
        playlistTracks.add(music);
    }
}
