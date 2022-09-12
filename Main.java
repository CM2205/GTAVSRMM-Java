import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/*
 * GTAVSRMM
 * Grand Theft Auto V Self Radio Music Manager (GUI Pending)
 * 
 *  // Main.java \\ 
 * Class(es): Main
 * Subclass(es): N/A
 * Dependenc(y/ies): FileOp.java, GUI.java
 * 
 * This class holds the programs main "running loop." Calls 
 * methods from FileOp to handle directory manipulation and 
 * GUI to render visual elements.
 * 
 * Last Edit: 11 Sept 2022
 *  
 */

public class Main {

    public static void main(String[] args) {

        int playlistIdx = 1; 
        int playlistSelection; 

        FileOp fileOp = new FileOp();
        Scanner scnr = new Scanner(System.in);

        System.out.println( "\n" + 
            "Hello " + FileOp.getUsername());

        System.out.println("\n" + "Playlists Found (" + 
            fileOp.getPlaylists().length + ")" + "\n");

        for (File f : fileOp.getPlaylists()) {
            System.out.println("["+ playlistIdx + "] " + f.getName());
            playlistIdx++;
        }

        System.out.println("\n" + "Enter the playlist's number to select");
        System.out.println("Note: multi-selection is not supported... yet." + "\n");

        playlistSelection = scnr.nextInt() - 1;

        if (playlistSelection > fileOp.getPlaylists().length - 1) {
            System.err.println("\n" + "Invalid entry! Selection number" + 
            "must match an option listed" + "\n");
            scnr.close();
            System.exit(-1);
        }

        System.out.println("\n" + "You've selected the playlist \""+ 
            fileOp.getPlaylists()[playlistSelection].getName() + "\"." + "\n" );

        System.out.println("Purging User Music");
        File[] UserMusic = fileOp.getUserMusic();

        if (UserMusic.length == 0) {
            System.out.println("No files to purge" + "\n");
        }
        else {
            System.out.println("Found " + UserMusic.length + " files to delete.");
            for (File f : UserMusic) {
                System.out.println("Deleted" + "\"" + f.getName() + "\"");
            }

            System.out.println("Purge complete." + "\n" );
        }

        System.out.println("Copying initiated");
        try {
            fileOp.copyOp(fileOp.getPlaylists()[playlistSelection]);
        } catch (IOException e) {}

        System.out.println("Copying successful" + "\n" + "Playlist \"" + 
            fileOp.getPlaylists()[playlistSelection].getName() + "\" has been selected." + "\n");
        System.out.println("Thanks for using GTAVSRMM :)" + "\n" + "Press any letter then enter too exit." + "\n");

        if (scnr.next() != null) {
            scnr.close();
            System.out.println("\n" + "Bye!" + "\n");
            System.exit(1); 
        }

    }

}