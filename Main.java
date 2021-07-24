/*  Grand Theft Auto V Self Radio Music Manager (GTAVSRMM)
    Created by Conrado Martinez 
    7.24.21
*/

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
    public static void main (String [] args) {
        Scanner scnr = new Scanner(System.in);
        String userInput;
        String currUser = System.getProperty("user.name");

        System.out.print("\n");
        System.out.print("Welcome to GTAVSRMM!");
        System.out.println("\n");
        System.out.println("Current user is : " + currUser);
        String userMusicDirStr = "C:\\Users/" + currUser + "/Documents/Rockstar Games/GTA V/mm";
        File userMusicDir = new File("C:\\Users/" + currUser + "/Documents/Rockstar Games/GTA V/mm");
        File activeMusicdir = new File("C:\\Users/" + currUser + "/Documents/Rockstar Games/GTA V/User Music/");
        File [] pListDir = userMusicDir.listFiles();

        int pListCount = 1;

        System.out.println("----------------------------------------------------------------------");
        for (File pList : pListDir) {
            System.out.println("| To set active playlist to (" + pList.getName() + "), enter " + pListCount);
            pListCount ++;
        }
        System.out.println("----------------------------------------------------------------------");
        System.out.print("\n");

        int pListSelection;

        userInput = scnr.next();
        try {
            pListSelection = Integer.parseInt(userInput);
        }
        catch (NumberFormatException e) {
            pListSelection = 0;
        }

        if (pListSelection == 0) {
            // run help subroutine.
        }

        System.out.println("Setting playlist (" + pListDir[pListSelection - 1].getName() + ") as the active playlist...");
        for (File song : activeMusicdir.listFiles()) {
            song.delete();
        }
        userMusicDir = new File (userMusicDirStr + "/" + pListDir[pListSelection - 1].getName() + "/");
        File [] songsToCopy = userMusicDir.listFiles();
        //Path relocate = null;

        
        for (File song : songsToCopy) {
            Path relocate = null;
            try {
                relocate = Files.copy(Paths.get(song.getPath()), Paths.get(activeMusicdir.getPath() + "/" + song.getName()));
            } 
            catch (IOException e ) {
                System.out.println("File operation failed.");
            }
            if (relocate != null) {
                System.out.println("Successfully moved " + song.getName() + " into the active directory");
            }
            System.out.println(song.getPath());
        }
        scnr.close();
        System.out.println(activeMusicdir.getPath());

        try {
            System.out.println("Launching game...");
            Runtime runTime = Runtime.getRuntime();
            Process process = runTime.exec("C:\\Program Files/Rockstar Games/Grand Theft Auto V/PlayGTAV.exe");
            try {
                Thread.sleep(5000);
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            process.destroy();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
