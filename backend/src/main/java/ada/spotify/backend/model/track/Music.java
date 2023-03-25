package ada.spotify.backend.model.track;

import ada.spotify.backend.model.playlist.Playlist;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "track")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Music {

    private String album;

    @ElementCollection
    private List<String> artists;
    private String href;
    @Id
    private String id;
    private String name;

    @ManyToOne
    @JoinColumn(name="playlist_id")
    private Playlist playlist;

}
