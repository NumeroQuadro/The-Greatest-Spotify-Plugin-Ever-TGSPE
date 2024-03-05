package src;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;

@Setter
@Getter
@Entity
@Table(name = "songs_info")
public class SongInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "song_name")
    private String songName;

    public SongInfo() {
    }

    public SongInfo(String name, String artist) {
        this.id = 1L;
        this.songName = name;
        this.artistName = artist;
    }
}
