package com.frank.addams;

import com.frank.emojis.Emogis;
import com.frank.exceptions.DataFileErrorException;
import com.frank.exceptions.InvalidMenuResponseException;
import com.frank.types.AddamsSearchCriteria;
import com.frank.types.Person;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
/********************************************************************************************
 * Class representing the Addams Family members and various manipulation methods
 ********************************************************************************************/
public class AddamsFamilyApplication {

        /********************************************************************************************
         *  Keyboard object to get user input
         ********************************************************************************************/
        private static Scanner userKeyboardDevice = new Scanner(System.in);

        /********************************************************************************************
         * Constants representing menu options
         ********************************************************************************************/
        private static final String DISPLAY_ALL_ADDAMS_OPTION       = "Display all Addams'";
        private static final String DISPLAY_BY_NAME_OPTION          = "Find an Addams";
        private static final String CHANGE_AN_ADDAMS_NAME_OPTION    = "Change an Addams name";
        private static final String REMOVE_AN_ADDAMS_OPTION         = "Remove an Addams";
        private static final String DISPLAY_ADDAMS_IN_REVERSE_ORDER = "Display all Addams in reverse order";
        private static final String REFRESH_DATA_FROM_FILE          = "Refresh data from file";
        private static final String EXIT_OPTION                     = "Exit";

        /********************************************************************************************
         * Array of menu options display to users as needed
         ********************************************************************************************/
        private static final String[] mainMenuOptions = { DISPLAY_ALL_ADDAMS_OPTION
                , DISPLAY_BY_NAME_OPTION
                , CHANGE_AN_ADDAMS_NAME_OPTION
                , REMOVE_AN_ADDAMS_OPTION
                , DISPLAY_ADDAMS_IN_REVERSE_ORDER
                , REFRESH_DATA_FROM_FILE
                , EXIT_OPTION
        };
        /********************************************************************************************
         * List of Addams Family members
         ********************************************************************************************/
        private List<Person> theAddamsFamily;

        /********************************************************************************************
         * Constructor for this application
         ********************************************************************************************/
        public AddamsFamilyApplication() throws FileNotFoundException {
                // Note: use of LinkedList rather than ArrayList due to efficiency when adding/removing
                theAddamsFamily = new LinkedList<>();  // Instantiate structure to hold family members
                loadFamilyMembersFromFile();           // Load data structure with family members in a file
        }
        /********************************************************************************************
         * Application controller
         ********************************************************************************************/
        public void run() throws FileNotFoundException {

                startOfApplicationProcessing();           // Display greeting
                String whatTheyChose = new String("");    // Hold response from user prompt
                boolean shouldLoop = true;                // Main processing loop control variable
                /********************************************************************************************
                 * main processing loop
                 ********************************************************************************************/
                while(shouldLoop) {
                        try {
                                whatTheyChose = displayMenuAndGetResponse();          // Display main menu and get response
                                System.out.println("\nYou chose: " + whatTheyChose);  // Display menu option chosen

                                switch (whatTheyChose) {                              // Process based on menu option chosen
                                        case DISPLAY_ALL_ADDAMS_OPTION: {
                                                displayAllPeople();
                                                break;
                                        }
                                        case DISPLAY_BY_NAME_OPTION: {
                                                displayByName();
                                                break;
                                        }
                                        case CHANGE_AN_ADDAMS_NAME_OPTION: {
                                                changePersonName();
                                                break;
                                        }
                                        case REMOVE_AN_ADDAMS_OPTION: {
                                                removeAnAddams();
                                                break;
                                        }
                                        case DISPLAY_ADDAMS_IN_REVERSE_ORDER: {
                                                displayAllInReverseOrder();
                                                break;
                                        }
                                        case REFRESH_DATA_FROM_FILE: {
                                                theAddamsFamily.removeAll(theAddamsFamily);  // Remove all app data structure current entries
                                                loadFamilyMembersFromFile();                 // Reload app data structure
                                                break;
                                        }
                                        case EXIT_OPTION: {
                                                shouldLoop = false;
                                                break;
                                        }
                                        default: {    // if somehow an unexpected option was returned - throw an exception
                                                throw new InvalidMenuResponseException("Invalid menu option " + whatTheyChose + " entered: ");
                                        }
                                }
                        }
                        catch(InvalidMenuResponseException exceptionObject) {
                                System.out.println("\nUh-Oh, Looks like you entered an invalid response, please try again");
                        }
                }
                endOfApplicationProcessing();     // Perform any clean up at end of the application
        }  // End of main processing method - run()

/**********************************************************************************************************
 * main processing helper methods
 *********************************************************************************************************/

        /********************************************************************************************
         * Display main menu, get response and return response
         ********************************************************************************************/
        public String displayMenuAndGetResponse() {

                int response = -1;  // initialize response to invalid value to be sure we store what user enters

                System.out.println("\nYou rang??? WattaYaWannaDo? (enter number of option)\n");
                // CANNOT use an iterator with a simple array (must use for or for each)
                for (int i = 0; i < mainMenuOptions.length; i++) {              // Loop through menu option array
                        System.out.println(i + 1 + ". " + mainMenuOptions[i]);  //     display a menu option
                }
                System.out.print("\nYour choice: ");                               // Ask user for choice
                try {
                        response = Integer.parseInt(userKeyboardDevice.nextLine());// Get user choice and convert to int value
                        return mainMenuOptions[response - 1];                      // Return menu option from option array
                }
                catch (NumberFormatException exceptionObject) {
                        throw new InvalidMenuResponseException("Invalid menu option " + response + " entered: ");
                }
                catch (ArrayIndexOutOfBoundsException exceptionObject) {
                        throw new InvalidMenuResponseException("Invalid menu option " + response + " entered");
                }
        }  // End of displayMenuAndGetResponse()
        /********************************************************************************************
         * Starting of application setup processing - display welcome screen
         ********************************************************************************************/
        public void startOfApplicationProcessing() throws FileNotFoundException{

                // Assign error messages to be written a file instead of the screen
                // 1. Instantiate a PrintStream object to assign file to the PrintStream object
                PrintStream fileProcessingErrorLogFile = new PrintStream("fileProcessingError.log");
                // 2. Tell Java to use the PrintStream object for error messages
                //       use System.setErr() method
                //    Also you may assign System.out output to a file using setOut() method
                System.setErr(fileProcessingErrorLogFile);

                System.out.println(Emogis.BLACK_SPIDER_WITH_EIGHT_LEGS.repeat(40));
                System.out.printf("%1s %s \n", Emogis.BLACK_SPIDER_WITH_EIGHT_LEGS,"Welcome to the Addams Family app!");
                System.out.println(Emogis.BLACK_SPIDER_WITH_EIGHT_LEGS.repeat(40));
        }

        /********************************************************************************************
         * End of application takedown processing - display goodbye message
         ********************************************************************************************/
        public void endOfApplicationProcessing() {
                System.out.println("-".repeat(60) + "\nThank you for using our app!\n" + "-".repeat(60));
        }

        /********************************************************************************************
         * Display a entries in data sturcture holding application data
         ********************************************************************************************/
        public void displayAllPeople() {
                int personCount = 0;
                String borderIcon = Emogis.TELEVISION;

                System.out.println("\n"+ (borderIcon + " ").repeat(13)) ;
                // for (Person anAddams : theAddamsFamily) {
                Iterator<Person> personIterator = theAddamsFamily.iterator(); // Define an Iterator for the list
                while(personIterator.hasNext()) { // Loop while the Iterator has a next value
                        Person anAddams = personIterator.next(); // Instantiate an object and get the next value from Iterator
                        personCount++;
                        System.out.printf("%s %2d. %-30s %-8s",borderIcon,personCount,anAddams.getName(),borderIcon);
                        if (personCount != theAddamsFamily.size()) {
                                System.out.println("");
                        }
                }
                System.out.println("\n"+ (borderIcon + " ").repeat(13)) ;
        }
        /********************************************************************************************
         * Display selected entries from data structure holding application data
         ********************************************************************************************/
        public void displayByName() {

                List<Person> listOfAddams = new LinkedList<>();                    // Hold selected entries from data structure

                AddamsSearchCriteria whatTheyWant = solicitAddamsSearchCriteria(); // Ask user for search criteria

                // Extract entries from data structure based on user seacrh criterial
                listOfAddams = findAnAddamsByName(whatTheyWant.getSearchValue().strip(),whatTheyWant.isCaseSensitiveSearch());

                // Display number of entries extracted from data structure
                System.out.println("\nNumber of Addams' found containing " + whatTheyWant.getSearchValue() + " in name: " + listOfAddams.size());

                // Loop through extracted entries and display them one at a time
                // for(Person anAddams : listOfAddams) {
                Iterator<Person> anIterator = listOfAddams.iterator(); //Instantiate an Iterator for the list
                while(anIterator.hasNext()) { // Loop while the Iterator has a next object
                        Person anAddams = anIterator.next(); // Get the next object from the Iterator
                        System.out.printf("%10d %-30s\n",anAddams.getId(),anAddams.getName());
                }
        }
        /********************************************************************************************
         * Allow user to change the name of selected entries in the data structure
         ********************************************************************************************/
        public void changePersonName() {
                // Hold entries to be changed
                List<Person> listOfAddams = new LinkedList<>();

                // Ask user for search criteria for entries to be changed
                AddamsSearchCriteria whatTheyWant = solicitAddamsSearchCriteria();

                // Extract entries based on user entered search criteria
                listOfAddams = findAnAddamsByName(whatTheyWant.getSearchValue().strip(),whatTheyWant.isCaseSensitiveSearch());

                // Loop through extracted entries, display each one and ask for new values
                //for(Person anAddams : listOfAddams) { // Replaced by an Iterator
                ListIterator<Person> cousinIterator = listOfAddams.listIterator(); // Define an iterator for the list
                while(cousinIterator.hasNext()) { // Loop while the Iterator has a next object
                        Person anAddams = cousinIterator.next(); // Get the next object from the Iterator
                        // Show user the current name from extracted entries
                        System.out.println("Found: " + anAddams);

                        // Ask user if they would like to change this entries value
                        System.out.println("Do you want to change their name? (Y or N default is No");
                        String changeResponse = userKeyboardDevice.nextLine().strip().toUpperCase();

                        if (changeResponse.startsWith("Y")) {                           // if user wants to change value
                                System.out.println("Please enter new name: ");          //   Ask for new value
                                String newName = userKeyboardDevice.nextLine().strip(); //   Get new value from user
                                findAnAddamsById(anAddams.getId()).setName(newName);    //   change value in data structure
                                System.out.println("----- Name changed to: " + newName);// Confirm to user change was made
                        }
                        else {                                                          // If user does not want to change
                                System.out.println("----- Name is unchanged -----");    //    display message to that effect
                        }
                }

        }
        /********************************************************************************************
         * Allow user to remove selected entries in the data structure
         ********************************************************************************************/
        public void removeAnAddams() {
                // Hold entries to be changed
                List<Person> aListOfAddams = new LinkedList<>();

                // Ask user for search criteria for entries to be changed
                AddamsSearchCriteria whatTheyWant = solicitAddamsSearchCriteria();

                // Extract entries based on user entered search criteria
                aListOfAddams = findAnAddamsByName(whatTheyWant.getSearchValue().strip(), whatTheyWant.isCaseSensitiveSearch());

                // Loop through extracted entries, display each one and ask if user wants to delete it
                // for (Person anAddams : aListOfAddams) { // Replaced by an Iterator to avoid the concurrentModificatoinException
                ListIterator<Person> anotherIterator = aListOfAddams.listIterator(); // Define an Iterator for the list
                while(anotherIterator.hasNext()) { // Loop while the Iterator has a next object
                        Person anAddams = anotherIterator.next(); // Get the next object from the Iterator
                        // Show user the current entry from extracted entries
                        System.out.println("Found: " + anAddams);

                        // Ask user if they would like to delete this entry from the data structure
                        System.out.println("Do you want to delete this Addams from the database? (Y or N default is No");
                        String deleteResponse = userKeyboardDevice.nextLine().strip().toUpperCase();

                        if (deleteResponse.startsWith("Y")) {             // If user wants to delete entry...
                                if (theAddamsFamily.remove(anAddams)) {   //    remove it from the data structure
                                        // aListOfAddams.remove(anAddams);       //       and from the extracted entries
                                        anotherIterator.remove(); // Use Iterator.remove instead of List.remove
                                        System.out.println("----- Removal of " + anAddams.getName() + " was successful");
                                } else {                                  // if user does not want to delete entry...
                                        System.out.println("----- Removal of " + anAddams.getName() + " failed");
                                }
                        }
                }
        }
        /********************************************************************************************
         * Obtain search criteria for extacting entries from the data structure
         ********************************************************************************************/
        public AddamsSearchCriteria solicitAddamsSearchCriteria() {
                String response = "";                // Hold response from user
                String personToFind = "";            // Hold neme of entry to find
                boolean wantsCaseSensitiveSearch;    // Hold if search should be case sensitivity

                System.out.println("\nPlease enter part or all of the person would like to find");
                response = userKeyboardDevice.nextLine();                                          // Get response from user
                personToFind = response.strip();                                                   // Remove extraneous spaces

                System.out.println("\nWould you like the search to be case sensitive? (Y or N) - default is No");
                response = userKeyboardDevice.nextLine();       // Get response from user

                if (response.toUpperCase().startsWith("Y")) {  // if user wants case sensitive search...
                        wantsCaseSensitiveSearch = true;       //    set case sensitive search indicator to true
                }
                else {                                          // if user does not wants case sensitive search..
                        wantsCaseSensitiveSearch = false;       //    set case senitive search indicator to false
                }
                // Return search criterial object
                return new AddamsSearchCriteria(personToFind, wantsCaseSensitiveSearch);
        }
        /********************************************************************************************
         * Load application data structure from data in a file
         ********************************************************************************************/
        private void loadFamilyMembersFromFile() throws FileNotFoundException, DataFileErrorException {

                String aLine = null;                                         // Hold a line from the file
                String MEMBERS_FILE_NAME = "theAddamsFamilyMembers.txt";     // Name of file holding data to be loaded
                File theAddamsFamilyFile = null;                             // File object to represent file to be loaded
                Scanner memberFileReader  = null;                            // Scanner object to read the file

                try {
                        theAddamsFamilyFile = new File(MEMBERS_FILE_NAME);   // Instantiate File object resprenenting data
                        memberFileReader  = new Scanner(theAddamsFamilyFile);// Instantiate Scanner to read file
                        while (memberFileReader.hasNextLine()) {             // Loop as long as there is data in the file
                                aLine = memberFileReader.nextLine().strip(); //      Get a line from the file and store it
                                theAddamsFamily.add(new Person(aLine));      //      Instantiate a Person and add to data structure
                        }
                }
                catch(FileNotFoundException exceptionObj) {
                        System.err.println(exceptionObj.getMessage());
                        exceptionObj.printStackTrace();
                        throw new DataFileErrorException(MEMBERS_FILE_NAME + " not found - see error log for details");
                }
                catch (IllegalStateException exceptionObject) {
                        System.err.println("Error processing family member file: " + MEMBERS_FILE_NAME);
                        System.err.println("Call stack:");
                        exceptionObject.printStackTrace();

                        System.out.println("Error processing family member file: " + MEMBERS_FILE_NAME);
                        System.out.println("Please see error log file for details");
                        System.err.println("System message: " + exceptionObject.getMessage() );
                }
                finally {   // Whether there is an exception
                        memberFileReader.close();
                }
        }  // End of loadMembers()

        /********************************************************************************************
         * Find entries in the data structure by id
         ********************************************************************************************/
        public Person findAnAddamsById(int requestedPersonId) {
                Person foundPerson = null;                       // Hold entry found in application data structure

                for(Person currentAddams : theAddamsFamily) {    // Loop through application data structure
                        if (currentAddams.getId() == requestedPersonId) { // if current entry id = requested Id
                                foundPerson = currentAddams;              //    remember it
                                break;                                    //    and exit loop
                        }
                }
                return foundPerson;   // Return entry found (or null)
        }
        /********************************************************************************************
         * Find entries in the data structure by full or partial name
         ********************************************************************************************/
        public List<Person>findAnAddamsByName(String requestedPersonName, boolean isCaseSensitive) {
                List<Person> foundPeople = new LinkedList<>();            // Hold entry(ies) found in application data structire
                boolean personFound;                                      // Indicate is requested entry was found or not
                List<Person> theAddamsFamilyList =new ArrayList<>();      // Hold entries based on full or partial name

                for(Person currentAddams : theAddamsFamily) {             // Loop through the app data structure
                        personFound = false;                              //      Assume entry will not be found
                        switch(Boolean.toString((isCaseSensitive))) {     //      Determine if case sensitivty is requested
                                case "false":                             //         If not, convert both values to same case and compare
                                        if (currentAddams.getName().toUpperCase().contains(requestedPersonName.toUpperCase())) {
                                                personFound = true;       //         if entry is found - indicate so
                                        }
                                        break;                            //                             and exit switch
                                case "true":                              //      if case sensitivity requested, compare values as is
                                        if (currentAddams.getName().contains(requestedPersonName)) {
                                                personFound = true;       //      if entry isfound - indicate so
                                        }
                                        break;                            //                         and exit switch

                        }
                        if (personFound) {                        // if requested entry is found in app data structure
                                foundPeople.add(currentAddams);   //    add it container holding found entries
                        }
                }
                return foundPeople;    // return container holding found entries
        }
        /********************************************************************************************
         * Display all entries in the data structure in reverse
         ********************************************************************************************/
        public void displayAllInReverseOrder() {
                // TODO: Add code to implement this feature
                // System.out.println("\n" + "-".repeat(60) +"\n----- Sorry, this feature has not been implemented yet -----\n"
                // + "-".repeat(60) + "\n");
                // Define a List iterator to start processing at the end of the list
                // listIterator(starting-position-within-the-list) - default is first element
                // list.size() - return the number of elements in the List
                // list.size()-1 - return the highest valid index in the List
                // Loop while List iterator has a previous entry
                //      retrieve the previous entry
                //      process it
                ListIterator<Person> aListIterator = theAddamsFamily.listIterator(theAddamsFamily.size());
                while(aListIterator.hasPrevious()) {
                        Person aPerson = aListIterator.previous();
                        System.out.println(aPerson);
                }
        }
} // End of ApplicationProgram class
