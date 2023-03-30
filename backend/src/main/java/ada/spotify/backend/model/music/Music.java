package ada.spotify.backend.model.music;

import ada.spotify.backend.model.playlist.Playlist;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "MUSIC_PLAYLIST",
         joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private List<Playlist> playlists = new ArrayList<>();

    public Music(String album, String href, String id, String name) {
        this.album = album;
        this.href = href;
        this.id = id;
        this.name = name;
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
