package searchAPI.createListing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import classicDriver.JSONAndExcelConversionTOArrayList;

public class CreatingContentUsingphp  extends JSONAndExcelConversionTOArrayList{
	WebDriver driver = new FirefoxDriver();

	@Test
	public void createContent() {

		System.out.println(System.getProperty("user.dir"));
		File file = new File(System.getProperty("user.dir")+"\\src\\searchAPI\\createListing");

		String batFileContent=null;
		batFileContent= "cd System.getProperty(\"user.dir\")";
		for(File s : file.listFiles())
		{
			if(s.getName().equals("client_1.bat"))
			{
				DefaultExecutor executor = new DefaultExecutor();
				executor.setWorkingDirectory(file);
				CommandLine commandLine = new CommandLine(s.getAbsolutePath());

				try

				{
					executor.execute(commandLine);
				} catch (ExecuteException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}


	/*@Test
	public void test() throws Exception{
		System.out.println(">>"+readFromFile(System.getProperty("user.dir")+"\\src\\searchAPI\\createListing\\demo_response.txt"));
		getJson();
	}*/


	public String readFromFile(String fileName){
		String resultString=null;
		try{
			File file= new File(fileName);
			if(file.exists()){

				Scanner fileScan = new Scanner(file);
				while(fileScan.hasNext()){
					resultString =fileScan.nextLine();
				}
			}
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
		return resultString;
	}
	
@Test
	public void getJson() throws Exception {
		driver.navigate().to("http://dev.classicdriver.com/en/api/node/" + readFromFile(System.getProperty("user.dir")+ "\\src\\searchAPI\\createListing\\demo_response.txt"));
		//driver.wait(5000);
		System.out.println("I am logged in");
	}
}