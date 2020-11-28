import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
/**
 * This project is a quick website automation script to sort through our alumni list website and create a mailing/contact list. 
 * Comments are not nearly extensive and javadoc was not created for this specific project. The point was to have a quick way to do work
 * that would have taken a lot longer manually, so doing things to add more dev time would not have made sense. Total dev time was approximately 4-5 hours.
 * The Selenium web automation library and the java.io.File were the main tools used.
 * 
 * This class creates CSV Files using the data using java.io.File once compiled from web.
 * @author Brendan Hood, github: brehood
 *
 */
//CREATES THE ACTUAL FILE - USES JAVA.IO.FILE,FILEWRITER
public class CSVFileCreator {

	File csvFile;
	//private String fileDirectory = "";
	private WebsiteIndexer webIndexer; //website name removed from name for security purposes - probably not necessary
	private FileWriter writer;

	public CSVFileCreator(String directory) {

		//fileDirectory = directory;
		csvFile = new File(directory);
		webIndexer = new WebsiteIndexer();

	}

	public void buildCSV() {

		try {
			csvFile.createNewFile();
			writer = new FileWriter(csvFile);

			String singleEntry;

			Iterator<String> alumListIterator = webIndexer.alumList.iterator();
			while(alumListIterator.hasNext()) {

				singleEntry = alumListIterator.next().toString() + "\n";
				writer.write(singleEntry);
			}

			writer.close();

		} catch (IOException e) {

			System.err.println("I/O Error with File");
		}

	}


	//WEBSITE INDEXER SUB CLASS - USES SELENIUM
	private class WebsiteIndexer{

		private final String link = ""; //link removed for security - probably not necessary
		private final String rootProjectPath = System.getProperty("user.dir");

		private HashSet<String> alumList;
		private WebDriver driver;
		private int currentAlumInList;
		private boolean exitKey;

		public WebsiteIndexer() {

			System.setProperty("webdriver.chrome.driver", rootProjectPath + "\\drivers\\chromedriver.exe");

			alumList = new HashSet<String>();
			driver = new ChromeDriver();

			currentAlumInList = 0;
			exitKey = false;
			
			indexAllAlumni();
		}

		private void indexAllAlumni() {


			logIntoWebsite();
			getToAlumniPage();

			indexVisibleOnPage();
			while(hasMorePages() && !exitKey) {

				WebElement nextPageButton = driver.findElement(By.cssSelector("#memberresults_next"));
				nextPageButton.click();
				currentAlumInList = 0;

				indexVisibleOnPage();
			}
		}

		private void logIntoWebsite() {

			driver.get(link);

			WebElement userNameBox = driver.findElement(By.xpath("//*[@id=\"username\"]"));
			userNameBox.sendKeys(""); //keys removed for security purposes

			WebElement passwordBox = driver.findElement(By.xpath("//*[@id=\"password\"]"));
			passwordBox.sendKeys(""); //keys removed for security purposes

			WebElement logInButton = driver.findElement(By.xpath("//*[@id=\"omegafi-login\"]/form/div[2]/div[2]/input"));
			logInButton.click();

			driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);

			WebElement websiteLink = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div/div/div[2]/a/span"));
			websiteLink.click();

			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

			WebElement alumSearchLink = driver.findElement(By.xpath("//*[@id=\"uiSearch\"]/div[2]/ul/li[3]/a"));
			alumSearchLink.click();

			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}


		private void getToAlumniPage() {

			WebElement initiatedChapter = driver.findElement(By.xpath("//*[@id=\"InitiatedChapterPartyID___chosen\"]"));
			initiatedChapter.click();

			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

			WebElement marylandBeta = driver.findElement(By.xpath("//*[@id=\"InitiatedChapterPartyID___chosen\"]/div/ul/li[140]"));
			marylandBeta.click();

			WebElement deceasedCheckbox = driver.findElement(By.xpath("//*[@id=\"MemberStatusID[1]\"]"));
			deceasedCheckbox.click();

			WebElement collegianCheckbox = driver.findElement(By.xpath("//*[@id=\"MemberStatusID[2]\"]"));
			collegianCheckbox.click();

			WebElement candidateCheckbox = driver.findElement(By.xpath("//*[@id=\"MemberStatusID[3]\"]"));
			candidateCheckbox.click();

			WebElement initiationEarliest = driver.findElement(By.xpath("//*[@id=\"InitiateYearFrom[0]\"]"));
			initiationEarliest.sendKeys("1970");

			WebElement initiationLatest = driver.findElement(By.xpath("//*[@id=\"InitiateYearTo\"]"));
			initiationLatest.sendKeys("2016");

			WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"Submit\"]"));
			searchButton.click();

			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			Boolean foundFirstAlum = driver.findElements(By.xpath("//*[@id=\"memberresults\"]/tbody/tr[1]")).size() > 0;

			if(foundFirstAlum) {
				System.out.println("Alumni Loaded. First Alumnus Found.");

				Select entriesDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"memberresults_length\"]/select")));
				entriesDropdown.selectByValue("100");
			}
		}

		private void indexVisibleOnPage() {

			while(hasNextAlum() && !exitKey) {

				indexSingleAlumnus();
			}
		}

		private void indexSingleAlumnus() {

			currentAlumInList++;

			Alumnus current;
			String first = "", last = "", email = "", initiated = "", phone = "", street = "", city = "", state = "", zip = "";

			WebElement alumProfile = driver.findElement(By.xpath("//*[@id=\"memberresults\"]/tbody/tr[" + currentAlumInList + "]"));
			alumProfile.click();

			//get name
			WebElement nameOnPage = driver.findElement(By.xpath("//*[@id=\"profile-name\"]/div[1]"));
			String[] fullName = nameOnPage.getText().split("\\s+");

			first = fullName[1];
			last = fullName[fullName.length - 2];
			if(last.equals("Jr") || last.equals("I") || last.equals("II") || last.equals("III") || last.equals("IV")
					|| last.equals("USA") || last.equals("USMC")) {
				
				last = fullName[fullName.length - 3];
				last.replace(",", "");
			}
			
			//get initiation year
			WebElement initiationYear = driver.findElement(By.xpath("//*[@id=\"profile-name\"]/div[2]"));
			initiated = initiationYear.getText();
			initiated = initiated.substring(0, initiated.indexOf("\n"));
			initiated = initiated.substring(initiated.length() - 4);

			//get email
			WebElement emailOnPage = driver.findElement(By.xpath("//*[@id=\"member-profile\"]/div/div[3]/table/tbody/tr[1]/td/a"));
			email = emailOnPage.getText();

			//get phone number
			if(hasPhone()) {
				WebElement phoneOnPage = driver.findElement(By.xpath("//*[@id=\"member-profile\"]/div/div[3]/table/tbody/tr[3]"));
				phone = phoneOnPage.getText();
				phone = phone.substring(phone.indexOf(':') + 1);
			}
			//get address - lots of edge cases
			WebElement addressOnPage = driver.findElement(By.xpath("//*[@id=\"member-profile\"]/div/div[3]/table/tbody/tr[2]/td"));
			String[] address = addressOnPage.getText().split("\\n");
			
			boolean isValidAddress = false;
			
			for(String s: address)
				if(s.indexOf("Yes") != -1) isValidAddress = true; //checks for address validity before assigning

			if(isValidAddress) { //addresses were the most difficult part, lot of edge cases - some handled manually in excel for ease
				
				street = address[0]; //gets street address
				String cityStateZipInfo = "";
				for(String s:  address) 
					if(s.indexOf(",") != -1) {
						
						cityStateZipInfo = s;
						
						city = cityStateZipInfo.substring(0, cityStateZipInfo.indexOf(",")); //extracts city from line
						
						cityStateZipInfo = cityStateZipInfo.substring(cityStateZipInfo.indexOf(",") + 2); //removes city info
						
						state = state + cityStateZipInfo.substring(0,2); //takes state from what is remaining
						zip = zip + cityStateZipInfo.substring(3,8); //finally retrieves zip code
					}
			}

			current = new Alumnus(first, last, email, initiated, phone, street, city, state, zip);
			if(currentAlumInList == 2) System.out.println(current.toString());
			
			if(!alumList.contains(current.toString())) {
				alumList.add(current.toString());
	
			} else exitKey = true;
			
			WebElement closeButton = driver.findElement(By.xpath("/html/body/div[25]/div[1]/button"));
			closeButton.click();
		}

		private Boolean hasNextAlum() { //loop continuation, are the more alum on same page

			return driver.findElements(By.xpath("//*[@id=\"memberresults\"]/tbody/tr[" + (currentAlumInList + 1) + "]")).size() > 0;
		}

		private Boolean hasMorePages() { //loop continuation, is it on last page of list

			WebElement nextPageButton = driver.findElement(By.cssSelector("#memberresults_next"));
			return nextPageButton.isEnabled();
		}

		private Boolean hasPhone() { //not every alum has one

			return driver.findElements(By.xpath("//*[@id=\"member-profile\"]/div/div[3]/table/tbody/tr[3]")).size() > 0;
		}


	}
}
