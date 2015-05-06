package searchAPI.bikeAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import classicDriver.ClassicDriverGenericMethods;
//TODO needs to work on this, it is not completed
/**
 * @author Deepshikha
 *
 */
public class SearchBike extends ClassicDriverGenericMethods  {
	List<WebElement> titles;
	//declaring multidimensional Arraylist to store values
	ArrayList<List<String>> listoflists = new ArrayList<List<String>>();

	/**
	 * this method is for searching bike by applying different filters
	 * @throws Exception
	 */
	@Test
	public void searchBike() throws Exception{
		login();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		click("Bikes");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		enterTextIntoTextField("Search field", getCellValue(1, 0));
		filters();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		click("Search Cars");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		String result = driver.findElement(By.xpath("//div[@id='main-content']//h1")).getText();
		System.out.println("The result is " + result);
		captureNodes();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		clickNext();

	}

	/**
	 * this method is for pagination, clicking on next button to move to next page
	 * @throws Exception
	 */
	public void clickNext() throws Exception{

		//to check if the next button link is present for pagination
		System.out.println("I am in clicknext method");
		while(driver.findElements(By.xpath("//li[@class='pager-next']/a")).size()!=0)
		{			
			//System.out.println("I am in clicknext method and size is not zero");
			driver.findElement(By.xpath("//li[@class='pager-next']/a")).click();
			//System.out.println("I am in clicknext method and I have clicked");
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			captureNodes();

		}
	}

	/**
	 * this method is for capturing the result obtained after search action
	 * @throws Exception
	 */
	public void captureNodes() throws Exception{
		System.out.println("I m in capture nodes method");
		titles =driver.findElements(By.xpath("//div[@id='main-content']//div[@class='panel-pane pane-node-title']"));
		System.out.println("====>" + titles.size());
		for (int i=1; i<=titles.size();i++ ){
			ArrayList<String> listOfValFromUI = new ArrayList<String>();

			//for fetching price of every node
			WebElement element = driver.findElement(By.xpath("//div[@class='panel-panel panel-content']//div[@class='view-content']/div["+i+"]//div[@class='price']"));
			String varValue = element.getText();
			//System.out.println("var Valuefor price--->>"+varValue);
			listOfValFromUI.add(varValue);

			//for fetching title of every node
			element=driver.findElement(By.xpath("//div[@class='panel-panel panel-content']//div[@class='view-content']/div["+i+"]//a[contains(@class,'jquery-once')]"));
			varValue = element.getText();
			//System.out.println("var Value for title--->>"+varValue);
			listOfValFromUI.add(varValue);

			//for fetching image url of every node
			element=driver.findElement(By.xpath("//div[@class='panel-panel panel-content']//div[@class='view-content']/div["+i+"]//div[@class='panel-pane pane-entity-field pane-node-field-add-images']//div[@class='field-items']//img"));
			varValue = element.getAttribute("src");
			if(varValue.contains("itok")){
				varValue=varValue.substring(0, varValue.indexOf("?itok"));
			}
			//System.out.println("var Value for image url--->>"+varValue);
			listOfValFromUI.add(varValue);

			//for fetching nid of every node
			element=driver.findElement(By.xpath("//div[@class='panel-panel panel-content']//div[@class='view-content']/div["+i+"]//div[@class='panel-pane pane-entity-field pane-node-field-add-images']//div[@class='field-items']//a"));
			varValue=element.getAttribute("href");
			String temp[]=varValue.split("/");
			varValue = temp[temp.length-1];
			//System.out.println("var Value for nid--->>"+varValue);
			listOfValFromUI.add(varValue);
			listoflists.add(listOfValFromUI);

		}
		for(int temp =0 ;temp<listoflists.size();temp++){
			ArrayList<String> str= new ArrayList<String>();
			str=(ArrayList<String>) listoflists.get(temp);
			System.out.println("*********"+listoflists.size());
			for(int temp1=0;temp1<str.size();temp1++){
				System.out.println(str.get(temp1));
			}
		}
	}
	/**
	 * this method is for applying different filters
	 * @throws Exception
	 */
	public void filters() throws Exception{
		
		dropDown("Make Filter", getCellValue(1, 1));
		Thread.sleep(10000);
	/*	dropDown("PriceFrom Filter", getCellValue(1, 3));
		Thread.sleep(5000);
		dropDown("PriceTo Filter", getCellValue(1, 4));
		Thread.sleep(5000);*/
		dropDown("YearFrom", getCellValue(1, 6));
		Thread.sleep(10000);
		dropDown("YearTo", getCellValue(1, 7));
		Thread.sleep(10000);
		dropDown("MileageFrom", getCellValue(1, 8));
		Thread.sleep(10000);
		dropDown("MileageTo", getCellValue(1, 8));
		Thread.sleep(10000);
		System.out.println("Filters selected");
		//dropDown("Model Filter", getCellValue(1, 2));


	}
}







