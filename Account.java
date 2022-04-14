import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Account super class for all types of accounts
 *
 * CS1800 Spring 2022, Project 4
 *
 * @author Alex Lee, Taylor Graham, Quinn Bello
 * @version 4/5/2022
 */

public class Account {
    private String username; //account identifier
    private boolean isLogged; //isLogged == true (logged in) isLogged == false (logged out)

    // create scanner object
    Scanner scan = new Scanner(System.in);

    PrintWriter out = null;

    /** Constructor,Scanner, Fields, Getter & Setter methods
     * CS1800 Spring 2022, Project 4
     * @author Taylor Graham
     * @version 4/5/2022
     */
    public Account(String username, boolean isLogged) throws FileNotFoundException {
        this.username = username;
        this.isLogged = isLogged;
    }

    public Account() {
        this.username = null;
        this.isLogged = false;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    /** Retrieve existing usernames from text file and return a string array of usernames
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public String[] retrieveUsernames() {
        //// load the existing usernames and save it in a string array ////
        ArrayList<String> existing = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("AccountInformation.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                existing.add(line);
            }
        } catch (IOException ie ) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }
        String[] existingUsernames = new String[existing.size()];
        for ( int i = 0 ; i < existingUsernames.length ; i++) {
            String existingUsername = (existing.get(i).split(":"))[0];
            existingUsernames[i] = existingUsername;
        }
        return existingUsernames;
    }

    /** prompts user to enter username, log in if the username exists, output error message if not
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public String logIn() {
        String accountType = "false";
        // load the existing usernames and save it in a string array
        ArrayList<String> existing = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("AccountInformation.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                existing.add(line);
            }

        } catch (IOException ie ) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }
        String[] existingUsernames = retrieveUsernames();
        if ( existingUsernames.length > 0 ) {
            for ( int i = 0 ; i < existingUsernames.length ; i++) {
                String username = (existing.get(i).split(":"))[0];
                existingUsernames[i] = username;
            }
            // check to see if the username exists
            for ( int i = 0 ; i < existingUsernames.length ; i++ ) {
                if ( existingUsernames[i].equals(this.getUsername())) {
                    this.setLogged( true );
                    accountType = (existing.get(i).split(":"))[1];
                }
            }
        }
        if ( this.isLogged() ) {
            System.out.println("Logged in!");
        } else {
            System.out.println("The username doesn't exist!");
        }
        return accountType;
    }

    /** creates a new account by taking user input and setting it to a new username
     *  (checks to see if it already exists)
     * account usernames and account type (teacher/student) are stored in a text file
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public boolean createAccount() {
        boolean created = false;

        // retrieve usernames
        String[] existingUsernames = retrieveUsernames();

        // check to see if the username already exists
        boolean alreadyExists = false;
        for ( int i = 0 ; i < existingUsernames.length ; i++ ) {
            if ( existingUsernames[i].equals(this.getUsername())) {
                alreadyExists = true;
            }
        }

        // Add new account if the username doesn't already exist
        if (alreadyExists) {
            System.out.println("This account already exists!");
        } else {
            try {
                out = new PrintWriter(new FileOutputStream("AccountInformation.txt", true));
                out.println( this.toString() );
                out.close();
                System.out.println("Account created!");
                created = true;
            } catch ( Exception e ) {
                System.out.println("ERROR CREATING NEW ACCOUNT");
            }
        }

        return created;
    }

    /** deletes an account by setting the username to an empty string and logging them out of the system
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void deleteAccount() {
        // retrieve existing usernames
        String[] existingUsernames = retrieveUsernames();

        // retrieve username text file lines (string arraylist)
        ArrayList<String> existing = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("AccountInformation.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                existing.add(line);
            }
        } catch (IOException ie ) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }

        //find corresponding account index
        int accountIndex = 0;
        for ( int i = 0 ; i < existingUsernames.length ; i++ ) {
            if ( existingUsernames[i].equals(this.getUsername())) {
                accountIndex = i;
            }
        }
        // remove the corresponding account and write over the existing information text file
        existing.remove( accountIndex );
        try {
            out = new PrintWriter(new FileOutputStream("AccountInformation.txt", false));
            for ( int i = 0 ; i < existing.size() ; i++ ) {
                out.println(existing.get(i));
            }
            out.close();
            System.out.println("Account successfully deleted!");
        } catch ( Exception e ) {
            System.out.println("ERROR DELETING ACCOUNT");
        }
    }

    /** allows users to change their username
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public boolean modifyAccount( String username ) {
        boolean newUsernameAlreadyExists = false;
        boolean result = false;

        // retrieve existing usernames (string array)
        String[] existingUsernames = retrieveUsernames();

        // retrieve username text file lines (string arraylist)
        ArrayList<String> existing = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("AccountInformation.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                existing.add(line);
            }
        } catch (IOException ie ) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }

        // check to see if the newly entered username already exists
        for ( int i = 0 ; i < existingUsernames.length ; i++) {
            if (existingUsernames[i].equals(username)) {
                System.out.println("The username is already in use. Please enter a new username");
                newUsernameAlreadyExists = true;
            }
        }
        ///
        if ( !newUsernameAlreadyExists ) {
            // find the username index in the list of accounts
            int accountIndex = 0;
            for ( int i = 0 ; i < existingUsernames.length ; i++ ) {
                if ( existingUsernames[i].equals(this.getUsername())) {
                    accountIndex = i;
                }
            }
            //
            // set current username to new username
            this.setUsername(username);
            // remove the current account and add the modified account
            existing.remove( accountIndex );
            existing.add( this.toString() );
            // write the new modified account to the information file
            try {
                out = new PrintWriter(new FileOutputStream("AccountInformation.txt", false));
                for ( int i = 0 ; i < existing.size() ; i++ ) {
                    out.println(existing.get(i));
                }
                out.close();
            } catch ( IOException ie ) {
                System.out.println("ERROR FILE NOT FOUND");
            }
            System.out.println("Account modified!");
            result = true;
        }
        return result;
    }


    /** toString method
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public String toString() {
        String output = "";
        if ( this instanceof Teacher ) {
            output = String.format("%s:teacher", this.getUsername() );
        } else if ( this instanceof Student ) {
            output = String.format("%s:student", this.getUsername());
        }
        return output;
    }
}