Real Estate Web Scraper

Overview

This project is a Real Estate Web Scraper that extracts property listings from the Realtor website using Selenium in Java. The program takes a city and state as input, scrapes property data such as price, address, and meta information (square footage, etc.), and saves the data in CSV format.

The scraper navigates through multiple pages of listings and handles dynamic loading of elements using JavaScript execution. This project provides a robust example of how to use web automation tools like Selenium for data extraction, while also demonstrating how to handle challenges like pagination and stale element references.

Features

	•	Scrapes real estate data including price, address, and key metadata like square footage.
	•	Automatically handles pagination and dynamically loads new pages.
	•	Gracefully handles stale element reference exceptions.
	•	Saves the scraped data into a CSV file for further analysis.

Project Structure

	•	Scraper.java: Main class that handles the initialization of the web driver, URL navigation, data scraping, and file writing.

Technologies Used

	•	Selenium WebDriver: For interacting with the webpage and automating browser actions.
	•	Java: The programming language used to build the scraper.
	•	OpenCSV: For writing the scraped data to a CSV file.

How It Works

	1.	User Input: The program prompts the user to enter a city and state (e.g., “San Francisco CA”). The input is formatted to match the URL structure of the Realtor website.
	2.	Web Scraping:
	•	Selenium navigates to the appropriate page on the Realtor website.
	•	It scrapes data such as price, address, and metadata for each property.
	•	The scraper handles scrolling and pagination to move through multiple pages of listings.
	3.	CSV Output: The collected data is saved to a CSV file for further use.

Setup and Installation

Prerequisites

	•	Java: Ensure you have Java installed on your system.
	•	Selenium: Download and configure Selenium WebDriver for Firefox (geckodriver).
	•	OpenCSV: Add OpenCSV to your project dependencies.
	•	Firefox Browser: The scraper is set up to work with the Firefox browser.

Steps

	1.	Clone the repository:
git clone https://github.com/yourusername/real-estate-scraper.git
	2.	Set up WebDriver:
	•	Download the geckodriver for Firefox and place it in a suitable directory.
	•	In the Scraper.java file, update the path to the geckodriver in the following line:
System.setProperty(“webdriver.firefox.driver”, “/path/to/geckodriver”);
	3.	Run the program:
	•	Compile and run the program using a Java IDE or terminal.
	4.	CSV Output:
	•	The scraped data will be saved in a CSV file located at c:\test\test.csv.

Usage

	1.	Run the program.
	2.	Enter the location:
The program will prompt you to enter the city and state (e.g., “San Francisco CA”).
	3.	Scraping in progress:
	•	The program will begin scraping property data, handling pagination, and saving it to the CSV file.
	•	It will display the current number of listings being processed.

Key Code Snippets

Scraping the Data

The scraper extracts data like price, address, and meta information (e.g., square footage) from the property listings:

prices = driver.findElements(By.cssSelector("[data-testid='card-price']"));
address = driver.findElements(By.cssSelector("[data-testid='card-address']"));
meta = driver.findElements(By.cssSelector("[data-testid='card-meta']"));

for (int j = 0; j < housesCount; j++) {
    String Meta = meta.get(j).getText();
    String singleLineMeta = Meta.replace("\n", " ");
    int index = singleLineMeta.indexOf("sqft");
    String newMeta = singleLineMeta.substring(0, index + 4);
    String[] record = {String.valueOf(number), prices.get(j).getText(), address.get(j).getText(), newMeta};
    list.add(record);
    number++;
}

Handling Pagination

The scraper uses JavaScript to handle scrolling and clicking the next page button:

while (button.getAttribute("disabled") == null) {
    Thread.sleep(2000);
    scrollPage(js, button);
    // Click the next page button
    button.click();
}

Writing Data to CSV

The extracted data is saved into a CSV file using OpenCSV:

try (CSVWriter writer = new CSVWriter(new FileWriter("c:\\test\\test.csv"))) {
    writer.writeAll(list);
}

Troubleshooting

	•	Stale Element Reference: If a stale element reference exception occurs (due to dynamic loading), the program will automatically retry fetching the data.
	•	Driver Path: Ensure that the path to geckodriver is correctly set based on your system’s configuration.
	•	Page Structure Changes: If Realtor.com changes its page structure, you may need to update the CSS selectors used in the scraper.

Future Enhancements

	•	Add more robust error handling for network or site downtime issues.
	•	Implement multi-threading to speed up the scraping process.
	•	Extend the scraper to extract additional property details such as images, agent contact information, etc.

License

This project is licensed under the MIT License.





