/**
 * This project is a quick website automation script to sort through our alumni list website and create a mailing/contact list. 
 * Comments are not nearly extensive and javadoc was not created for this specific project. The point was to have a quick way to do work
 * that would have taken a lot longer manually, so doing things to add more dev time would not have made sense. Total dev time was approximately 4-5 hours.
 * The Selenium web automation library and the java.io.File were the main tools used.
 * 
 * This class simply was the quickest way to execute the program, especially if needed multiple times for testing/refinement of web automation process.
 * @author Brendan Hood, github: brehood
 *
 */
public class Application {

	public static void main(String[] args) {
		
		String desiredDirectory = "alumnus.csv"; //full computer directory deleted for security

		System.out.println("Executing.");
		
		CSVFileCreator fileCreator = new CSVFileCreator(desiredDirectory);
		fileCreator.buildCSV();
		
		System.out.println("Done.");
	}
}
