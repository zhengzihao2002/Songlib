package songlib.app;

public class Song {
    private String name;
    private String year;
    private String artist;
    private String album;

    public Song(String name, String year, String artist, String album) {
        this.name = name;
        this.year = year;
        this.artist = artist;
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "Song [name=" + name + ", year=" + year + ", artist=" + artist + ", album=" + album + "]";
    }
}
