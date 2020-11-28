/**
 * This project is a quick website automation script to sort through our alumni list website and create a mailing/contact list. 
 * Comments are not nearly extensive and javadoc was not created for this specific project. The point was to have a quick way to do work
 * that would have taken a lot longer manually, so doing things to add more dev time would not have made sense. Total dev time was approximately 4-5 hours.
 * The Selenium web automation library and the java.io.File were the main tools used.
 * 
 * This class holds Alumni information, provides a toString for CSV appending, and handles equals/hashing for data storage.
 * @author Brendan Hood, github: brehood
 *
 */
public class Alumnus {

	//extra instances added since this website doesn't list everything, but needed for formatting CSV for import
	String firstName;
	String middleName = ""; //not needed for our purposes
	String lastName;
	String informalFirstName = ""; //not needed for our purposes
	String emailAddress;
	String standing = "Alumnus"; //only looking for alumni this time
	String standingReason = "", billingGroup = "", datePledged = ""; //not needed for our purposes
	String dateInitiated; 
	String birthDate = "", gradYear = ""; //not needed for our purposes
	String phoneNumber;
	String parentsName = "", parentsEmail = "", homePhone = ""; //not needed for our purposes
	String homeStreet;
	String homeCity;
	String homeState;
	String homeZip;
	String homeCountry = "USA";

	public Alumnus(String first, String last, String email, String initiated, String phone, 
			String street, String city, String state, String zip) {
		
		firstName = first;
		lastName = last;
		emailAddress = email;
		dateInitiated = initiated;
		phoneNumber = phone;
		homeStreet = street;
		homeCity = city;
		homeState = state;
		homeZip = zip;
	
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(firstName);
		builder.append(", ");
		builder.append(middleName);
		builder.append(", ");
		builder.append(lastName);
		builder.append(", ");
		builder.append(informalFirstName);
		builder.append(", ");
		builder.append(emailAddress);
		builder.append(", ");
		builder.append(standing);
		builder.append(", ");
		builder.append(standingReason);
		builder.append(", ");
		builder.append(billingGroup);
		builder.append(", ");
		builder.append(datePledged);
		builder.append(", ");
		builder.append(dateInitiated);
		builder.append(", ");
		builder.append(birthDate);
		builder.append(", ");
		builder.append(gradYear);
		builder.append(", ");
		builder.append(phoneNumber);
		builder.append(", ");
		builder.append(parentsName);
		builder.append(", ");
		builder.append(parentsEmail);
		builder.append(", ");
		builder.append(homePhone);
		builder.append(", ");
		builder.append(homeStreet);
		builder.append(", ");
		builder.append(homeCity);
		builder.append(", ");
		builder.append(homeState);
		builder.append(", ");
		builder.append(homeZip);
		return builder.toString();
	}


	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Alumnus other = (Alumnus) obj;
		
		//comparing emails
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		
		//comparing last names
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		
		//comparing first names
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		
		return true;
	}
	
	

}
