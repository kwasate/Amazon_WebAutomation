package reusableFunctions;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.prolifics.ProlificsSeleniumAPI;

import utility.Generic;
import utility.ScreenShot;

public class OrangeHRM {
	Generic gen = new Generic();
	Logger log;
	ScreenShot screen = new ScreenShot();
	ProlificsSeleniumAPI oPSelFW = null;
	// Login to OrangeHRM
	String txtUserName = "//*[@id=\"txtUsername\"]";
	String txtPassword = "//*[@id=\"txtPassword\"]";
	String btnLogin = "//*[@id=\"btnLogin\"]";
	String tabDirectory = "//a[@id='menu_directory_viewDirectory']";
	String txtEmpName = "//*[contains(@id,'empName')]";
	String btnSearch = "//input[@id = 'searchBtn']";

	public void orangeHRMLogin(WebDriver driver, HashMap<String, String> envTestData,
			HashMap<String, String> XLTestData, ProlificsSeleniumAPI oPSelFW) throws Exception {

		try {

			String url = envTestData.get("ApplicationURL").toString();
			String userName = envTestData.get("UserName").toString();
			String pwd = envTestData.get("Pasword").toString();
			driver.get(url);
			gen.setText(driver, txtUserName, userName, oPSelFW);
			gen.setText(driver, txtPassword, pwd, oPSelFW);
			gen.clickElement(driver, btnLogin, oPSelFW);
			Thread.sleep(14000);
			if (driver.findElements(By.xpath("//li/a[@id='menu_dashboard_index']")).size() != 0) {
				System.out.println("User logged in Sucessfully");
				oPSelFW.reportStepDetails("User is Successfully Logged in OrangeHRM", userName, "Pass");
			} else {
				oPSelFW.reportStepDetails("User Login", "user is not successfully Logged in OrangeHRM", "Fail");
				Assert.assertEquals("user is not successfully Logged in OrangeHRM", "Login failed");
			}
		} catch (Exception e) {
			oPSelFW.reportStepDetails("Exception", e + " is displayed", "Fail");
			Assert.assertEquals("Exception", "Throwing Exception");
		}
	}

	byte counter;

	public void directorySearch(WebDriver driver, HashMap<String, String> excelData, ProlificsSeleniumAPI oPSelFW)
			throws IOException {
		try {
			gen.clickElement(driver, tabDirectory);
			String[] empNames = excelData.get(enumToString(ExcelDataFields.Name)).split(";");
			for (String empName : empNames) {
				oPSelFW.reportStepDetails("Verify Employee Details", " Employee Details -  " + (counter + 1), "Info");
				gen.setText(driver, txtEmpName, empName, "Name #", oPSelFW);
				gen.clickElement(driver, btnSearch, "submit", oPSelFW);
				Thread.sleep(20000);
				boolean isPresent = driver.findElements(By.xpath("//*[@id=\"resultTable\"]/tbody/tr")).size() > 1;
				if (isPresent) {
					oPSelFW.reportStepDetails("Verify the Directory Search with Name display in directory search ",
							"Employee name are displayed for " + empName, "Pass");
					oPSelFW.reportStepDtlsWithScreenshot("Employee details in directory Search ", "Employee details",
							"Pass");

				} else if (!isPresent) {
					String message = driver.findElement(By.xpath("//*[contains(text(),'No Records Found')]")).getText();
					oPSelFW.reportStepDetails("Employee details in directory Search ", message + " for" + empName,
							"Pass");
					oPSelFW.reportStepDtlsWithScreenshot("Employee details in directory Search", "Employee details",
							"Pass");

				}
			}

		} catch (Exception e) {
			oPSelFW.reportStepDetails("Verify if any error is displayed for directory search",
					"Exception is displayed while directory search", "Fail");
			String expectedresult = "No error message should be displayed";
			Assert.assertEquals("Error message is displayed", expectedresult);
		}
	}

	public String enumToString(ExcelDataFields enumField) {

		return String.valueOf(enumField);
	}

	enum ExcelDataFields {
		Name
	}

}
