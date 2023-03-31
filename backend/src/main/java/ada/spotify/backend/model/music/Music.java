package ada.spotify.backend.model.music;

import ada.spotify.backend.model.playlist.Playlist;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "music")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Music {

    private String album;

    //@ElementCollection
    //private List<String> artists;
    private String href;
    @Id
    private String id;
    private String name;
    private String img;

    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "MUSIC_PLAYLIST",
         joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private Set<Playlist> playlists = new LinkedHashSet<>();

    public Music(String album, String href, String id, String name, String img) {
        this.album = album;
        this.href = href;
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public void addPlaylistToMusic(Playlist playlist)
    {
        playlists.add(playlist);
    }

    public void removePlaylistFromMusic(Playlist playlist)
    {
        playlists.remove(playlist);
    }
}
