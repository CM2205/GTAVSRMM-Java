/*
 * GTAVSRMM-GUI 
 * Grand Theft Auto V Self Radio Music Manager (now with GUI)
 * 
 *  // FileOp.java \\ 
 * Class(es): FileOp
 * Subclass(es): [private] DirAccessor, [private] DirMutator
 * Dependenc(y/ies): java.io.File, java.nio.file.Files
 * 
 * This class is responsible for any file 
 * operations (i.e. listing and copying playlists). 
 * Calls methods from Java87 Files class. 
 * 
 * Last Edit: 11 Sept 2022
 *  
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class FileOp {

    DirAccessor dirAcc; 
    DirMutator dirMut; 

    private static File[] userMusic; 
    private static File[] playlists; 
    private static File userMusicDir;
    private static File playlistsDir;
    private static String username; 
    private static String playlistsPath; 
    private static long playlistsTotalSize;

    
    public FileOp() {

        this.dirAcc = new DirAccessor();
        this.dirMut = new DirMutator();

        username = System.getProperty("user.name");
        playlistsPath = "c:/Users/" + username + "/Documents/Rockstar Games/GTA V/";
        playlistsDir = new File(playlistsPath + "playlists/");
        userMusicDir = new File(playlistsPath + "User Music/");
        
        playlistsTotalSize = playlistsDir.getTotalSpace();
        
        try {
            playlists = dirAcc.getPlaylists(playlistsPath + "playlists/");
        } catch (FileNotFoundException e) {}

        try {
            userMusic = dirAcc.getUserMusicContents();
        } catch (FileNotFoundException e) {}
    }

    public FileOp(int num) {}

    public File[] getPlaylists() {
        return playlists;
    }

    public File[] getUserMusic() {
        return userMusic;
    }

    public static String getUsername() { 
        return username;
    }

    public long getPlaylistsTotalSize() {
        return playlistsTotalSize;
    }

    public void copyOp(File playlist) throws IOException{
        dirMut.copyPlaylist(playlist);
    }


    private class DirAccessor {
        
        File[] getPlaylists(String path) throws FileNotFoundException {

            File[] playlists; 
            File playlistDir = new File(path); 

            if (!playlistDir.isDirectory()) {
                // playlists dir is not found!
                throw new FileNotFoundException(
                    "Playlists directory missing " +
                    "from GTA V Documents Directory.");
            }

            playlists = playlistDir.listFiles();

            if (playlists.length == 0 || playlistDir == null) {
                throw new FileNotFoundException(
                    "No playlist found in playlist directory."
                );
            }

            return playlists;
        }

        File[] getUserMusicContents() throws FileNotFoundException {

            File userMusicDir = new File(playlistsPath + "User Music/");

            if (userMusicDir == null || !(userMusicDir.isDirectory())) {
                System.err.println("User Music directory unavailable.");
                System.exit(-1);
            }

            return userMusicDir.listFiles();
        }
    }

    private class DirMutator {

        // /playlist/[selected]/ -> /User Music/
        void copyPlaylist(File playlistSelected) throws IOException{

            File[] playlistElems = playlistSelected.listFiles();
            
            for (File f : playlistElems) {
                Files.copy(f.toPath(), userMusicDir.toPath());
            }

        }

    }
}


