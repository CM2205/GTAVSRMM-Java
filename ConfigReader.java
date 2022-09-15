import java.util.Properties;
import java.util.Scanner;
import java.nio.file.Files;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigReader {

    private final String[] CFG_DEFAULTS = {
        "  ___  _____  ___ __   __ ___  ___  __  __  __  __ \n", 
        " / __||_   _|/   \\\\ \\ / // __|| _ \\|  \\/  ||  \\/  |\n", 
        "| (_ |  | |  | - | \\   / \\__ \\|   /| |\\/| || |\\/| |\n",
        " \\___|  |_|  |_|_|  \\_/  |___/|_|_\\|_|  |_||_|  |_|\n", 
        "\n\n", "[game exe] ~placeholder~", "\n", "[color] a\n"};

    private final String GAME_PATH_MARKER = "[game exe]";
    private final String UNINITIALIZED = " ~placeholder~";
    private final String DO_NOT_EXECUTE_GAME = " ~veto~";
    private final String CFG_FILENAME = "config.txt";

    private File config;
    private File gameExecutable;
    private String customPath ="n/a";
    private boolean executeGameOption;
    private Scanner scnr; 
    

    public ConfigReader() throws FileNotFoundException,IOException{

        if (!hasConfig()) {
            generateCFG(0);
        }
        this.scnr = new Scanner(config);

        switch (this.getGameDir()) {
            case DO_NOT_EXECUTE_GAME:
                System.out.println("launch veto'd");
                break;
        
            case UNINITIALIZED: 
                boolean pathProvided = promptForPath();
                System.out.println("initialized");
                if (!pathProvided) {
                    return;
                }
                executeGame(this.getGameDir());
                break;

            default:

                if (this.getGameDir().contains("PlayGTAV.exe")) {
                    executeGame(this.getGameDir());
                }
                System.err.println("corrupted cfg file found. Regenerating...");
                this.deleteCFG();
                this.generateCFG(0);
                break;
        }


    }

    public boolean hasConfig() {

        this.config = new File(CFG_FILENAME);

       if (config.exists()) {
        System.out.println("Found config");
        return true;
       }
       System.err.println("Config file not found. TODO: Generate cfg");
        return false;
    }

    /**
     * Method to create a new CFG file in the event that a file is unreadable.
     * @param option
     * @throws IOException
     */
    private void generateCFG(int option) throws IOException{

        FileWriter fw;

        this.config = new File(CFG_FILENAME);

        if (!this.config.createNewFile()) {
            // System.err.println("Unable to generate new CFG file. Most likely insufficient permissions.");
        }

        fw = new FileWriter(this.config);

        for (String str: CFG_DEFAULTS) {
            if (str.contains(UNINITIALIZED) && option == -1) {
                fw.write(GAME_PATH_MARKER + DO_NOT_EXECUTE_GAME);
                continue;
            }
            if (str.contains(UNINITIALIZED) && option == 1) {
                fw.write(GAME_PATH_MARKER + " " + customPath);
                System.out.println("Custom path added to cfg");
                System.out.println(customPath);
                continue;
            }
            fw.write(str);
        }

        System.out.println("wrote contents to config");
        fw.close();
    }

    private void deleteCFG() {

        if(config.exists()) {
            config.delete();
            System.out.println("Deleted previous corrupted config");
            return;
        }
        System.err.println("No CFG to delete");
    }

    private boolean promptForPath() throws IOException{

        Scanner scnr = new Scanner(System.in); 
        FileWriter fw;
        boolean providedPath = false;

        System.out.println("\nGame directory not specified. \n" 
            + "Would you like to launch the game after selecting a playlist by providing game path?\n"
            + "(y/n)\n");

        String option = scnr.next();

        switch (option) {
            case "y":
                
                setGameExecutable(this.acceptPath());
                providedPath = true;
                break;

            case "n":
            Scanner cfgRead = new Scanner(CFG_FILENAME);

                String cfgLine = cfgRead.next(); 
                while (!cfgLine.contains(GAME_PATH_MARKER) && cfgRead.hasNext()) {
                    cfgLine = cfgRead.next();
                }

                fw = new FileWriter(config);

                deleteCFG();
                generateCFG(-1);

                System.out.println("Wrote veto string.");
                break; 
            default:
                System.out.println("Invalid selection provided. Please try again.\n");
                this.promptForPath();
                break;
        }

        scnr.close();
        return providedPath;
    }

    private String acceptPath() {

        Scanner scnr = new Scanner(System.in); 

        System.out.println("\nPlease paste the game's executable 'absolute' path");
        System.out.println("Ending with PlayGTAV.exe\n");
        
        String gamePathStr = scnr.nextLine(); 

        if (!gamePathStr.contains("PlayGTAV.exe")) {
                System.err.println("Path provided is invalid. Please try again\n\n\n\n");
                acceptPath();
        }

        this.customPath = gamePathStr;
        return gamePathStr;
    }

    private void setGameExecutable(String path) throws IOException{


        this.gameExecutable = new File(path); 

        generateCFG(1);

        if (!this.gameExecutable.exists()) {
            System.out.println("Cannot find game executable @ " + this.gameExecutable.getAbsolutePath());
        }
    }

    private void executeGame(String path) {

        this.gameExecutable = new File(path);

        if (this.gameExecutable.exists()) {
            System.out.println("launching app...");
        }
    }

    public String getGameDir() throws IOException{

        Scanner cfgRead = new Scanner(config);

        String path = cfgRead.nextLine();

        while (cfgRead.hasNext() && !path.contains(GAME_PATH_MARKER)) {
            path = cfgRead.nextLine(); 
            System.out.println("Scanner found: " + path);
        }

        if (path.length() < 11) {
            System.out.println("Path is " + path);
            System.err.println("invalid CFG. Regenerating...");
            generateCFG(0);
            cfgRead.close();
            return UNINITIALIZED;
        }

        path = path.substring(10);

        System.out.println("Game @\n" + path);

        cfgRead.close();
        return path;
    }
}