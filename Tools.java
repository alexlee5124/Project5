import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Tools {

    public Tools() {
    }

    /** Take a file name string and return the entire text file as a single string
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/14/2022
     */
    public String loadTextFile(String filename) {
        ArrayList<String> textFileLines = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                textFileLines.add(line);
            }
        } catch (IOException ie) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }
        String entireFile = "";
        for ( int i = 0 ; i < textFileLines.size() ; i++ ) {
            entireFile += textFileLines.get(i);
        }
        return entireFile;
    }

    /** Get a list of all questions in the question document
     * CS1800 Spring 2022, Project 4
     * @author Quinn Bell0
     * @version 4/30/2022
     */
    public ArrayList<String> getQuestionList()
    {
        ArrayList<String> questionList = new ArrayList<String>();

        try (BufferedReader bfr = new BufferedReader(new FileReader("Questions.txt")))
        {
            String line;

            while ((line = bfr.readLine()) != null)
                questionList.add(line);

        } catch (IOException ie)
        {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }

        return questionList;
    }

    /** Take maximum number of values and a size and return an integer array of random values of size and
     * less than maximum value
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/14/2022
     */
    public int[] randomGenerator(int max, int size) {
        int[] random = new int[size];
        ArrayList<Integer> pool = new ArrayList<>();
        for (int i = 0 ; i < max ; i++) {
            pool.add(i);
        }
        Collections.shuffle(pool);
        for ( int i = 0 ; i < size ; i++) {
            random[i] = pool.get(i);
        }
        return random;
    }

    /** Take in a minimum value and run the program until a valid integer response is provided
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/14/2022
     */
    public int receiveValidInt(int minValue, Scanner scan) {
        int response = 0;
        boolean flag;
        do {
            flag = false;
            String responseString = scan.nextLine();
            try {
                response = Integer.parseInt(responseString);
                if (response < minValue) {
                    System.out.printf("Please enter a value greater than %d\n", minValue);
                    flag = true;
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid response!");
                flag = true;
            }
        } while (flag);
        return response;
    }

    /** Take in a minimum value and run the program until a valid integer response is provided
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/14/2022
     */
    public int receiveValidInt(int minValue, int maxValue, Scanner scan) {
        int response = 0;
        boolean flag;
        do {
            flag = false;
            String responseString = scan.nextLine();
            try {
                response = Integer.parseInt(responseString);
                if (response < minValue) {
                    System.out.printf("Please enter a value greater than %d\n", minValue);
                    flag = true;
                } else if (response > maxValue) {
                    System.out.printf("Please enter a value less than %d\n", maxValue);
                    flag = true;
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid response!");
                flag = true;
            }
        } while (flag);
        return response;
    }

    /** Take in a minimum value and run the program until a valid integer response is provided
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/14/2022
     */
    public String receiveValidString(String[] validResponses , Scanner scan) {
        String response = "";
        boolean valid;
        do {
            valid = false;
            response = scan.nextLine();
            if (Arrays.asList(validResponses).contains(response)) {
                valid = true;
            } else {
                System.out.println("Please enter a valid response!");
            }
        } while (!valid);
        return response;
    }

    /** Take a string array list and return it as an array
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/14/2022
     */
    public String[] listToArrayString(ArrayList<String> stringArrayList) {
        String[] returnArray = new String[stringArrayList.size()];
        for (int i = 0 ; i < returnArray.length ; i++) {
            returnArray[i] = stringArrayList.get(i);
        }
        return returnArray;
    }

    /** Take an int array list and return it as an array
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/14/2022
     */
    public int[] listToArrayInt(ArrayList<Integer> stringArrayList) {
        int[] returnArray = new int[stringArrayList.size()];
        for (int i = 0 ; i < returnArray.length ; i++) {
            returnArray[i] = stringArrayList.get(i);
        }
        return returnArray;
    }

    /** retrieve number of questions in the pool
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/14/2022
     */
    public int getQuestionPoolSize() {
        int size = 0;
        String questionFile = loadTextFile("Questions.txt");
        String[] lines = questionFile.split("/");
        return lines.length;
    }
}
