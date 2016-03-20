package flipkart;
import java.lang.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.corba.se.spi.orbutil.fsm.Action;
import com.sun.corba.se.spi.orbutil.fsm.FSM;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.sun.javafx.collections.MappingChange.Map;
import com.thoughtworks.selenium.Wait;

class Product {
	private String productName;
	private int quantity;
		
	public Product(String productName, int quantity) {
		super();
		this.productName = productName;
		this.quantity = quantity;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}

public class AutomateFlipkart {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Downloads\\chromedriver_win32\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.flipkart.com/");
		driver.manage().window().maximize();
		WebElement Mouseelement = driver.findElement(By.className("_2sYLhZ"));
		Actions builder = new Actions(driver);
		builder.moveToElement(driver.findElement(By.name("q"))).perform();
		
		
		
		
		try {
			FileInputStream file = new FileInputStream(new File("src/product.xlsx"));
			//Get the workbook instance for XLS file 
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			 
			//Get first sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);
			 
		    //Iterate through each rows from first sheet
		    Iterator<Row> rowIterator = sheet.rowIterator();
		    boolean pinCodeSubmitted = false;
		    List<Product> products = new ArrayList<Product>();
		        while(rowIterator.hasNext()) {
		        Row row = rowIterator.next();
		        // Skip Header Row
		        if(row.getRowNum() == 0) {
		        	continue;
		        }
		        Cell productNameCell = row.getCell(0);
		        String productName = productNameCell.getStringCellValue();
		        
		        Cell quantityCell = row.getCell(1);
		        int quantity = (int)quantityCell.getNumericCellValue();
		        		        
		        System.out.println(productName + " : " + quantity);
		        products.add(new Product(productName, quantity));
				WebDriverWait wait = new WebDriverWait(driver, 10);

		        WebElement firstelement = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
		        //driver.findElement(By.name("q"));
				firstelement.sendKeys(productName);

				firstelement.submit();
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				
				List<WebElement> productlist = driver.findElements(By.className("pu-visual-section"));
				System.out.println(productlist.size());
				WebElement firstproduct = productlist.get(0).findElements(By.tagName("a")).get(0);
				firstproduct.click();
//				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				
				
				if(!pinCodeSubmitted) {
					wait = new WebDriverWait(driver, 10);
					WebElement pinCodeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pincode")));

//					WebElement pinCodeInput = driver.findElement(By.name("pincode"));
					System.out.println(pinCodeInput);
					pinCodeInput.sendKeys("560100");
					System.out.println("Wating");
					//driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
					System.out.println("Wait Over");
					wait = new WebDriverWait(driver, 30);
					WebElement pinCodeCheck = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[4]/div/div[7]/div/div[3]/div/div/div[6]/div/div[1]/div/div[1]/div[1]/div[2]/div[1]/form/input[2]")));

					System.out.println(pinCodeCheck);
					pinCodeCheck.submit();					
					
					wait = new WebDriverWait(driver, 30);
					wait.until(ExpectedConditions.textToBe(By.xpath("/html/body/div[1]/div[4]/div/div[7]/div/div[3]/div/div/div[6]/div/div[1]/div/div[1]/div[2]/div/span[2]"), "560100"));

					pinCodeSubmitted = true;

				}
				
//				Actions actions = new Actions(driver);

//				actions.moveToElement(pinCodeCheck).click().perform();
//				pinCodeCheck.click();
				System.out.println("Wating");
				
				wait = new WebDriverWait(driver, 30);
				WebElement AddtoCart = wait.until(ExpectedConditions.elementToBeClickable(By.className("btn-buy-now")));

				System.out.println("Wait Over");			
//				WebElement AddtoCart = driver.findElement(By.xpath("/html/body/div[1]/div[4]/div/div[7]/div/div[3]/div/div/div[6]/div/div[2]/div[1]/div/div[2]/div/div[1]/form/input[8]"));
				System.out.println(AddtoCart.getText());
				//actions.moveToElement(AddtoCart).click().perform();
				AddtoCart.click();
				//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				WebElement cartElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("item_count_in_cart_top_displayed")));
				wait.until(ExpectedConditions.textToBePresentInElement(cartElement, row.getRowNum()+""));
		        
		    }
		    file.close();
		    	WebDriverWait wait = new WebDriverWait(driver, 10);		    	
		    	WebElement cartElement = driver.findElement(By.id("item_count_in_cart_top_displayed"));				
		    	wait.until(ExpectedConditions.textToBePresentInElement(cartElement, "5"));
		    	cartElement.click();
		    	wait = new WebDriverWait(driver, 30);
		    	WebElement AmountPaybale = driver.findElement(By.xpath("//*[@id=\"cartpage-cart-tab-content\"]/div[1]/div[1]/span[2]"));
		    	String TotalAmount = AmountPaybale.getText();
		    	String Amount = TotalAmount.substring(4);
		    	
		    	System.out.println(TotalAmount);
		    	System.out.println(Amount);
		    	
		    	if(Amount.equals("38,715")) {
		    		System.out.println("Correct");
		    	}
		    	else{
		    		System.out.println("Price of the Product Might Get Changed");
		    	}
		    	
		    	/*
		    	 * [WebElement itemcell, Webelement itemcell]
		    	 * 
		    	 * */
		    List<WebElement> verifyCartContant = driver.findElements(By.xpath("//tbody[contains(@class, 'cart-body')]//td[contains(@class, 'item-cell')]"));
		    int i = 0;
		    System.out.println(verifyCartContant.size());
		    for(WebElement element : verifyCartContant) {
		    	WebElement titleElement = element.findElement(By.xpath(".//span"));
		    	String productName = products.get(i++).getProductName();
		    	if(titleElement.getText().equalsIgnoreCase(productName)) {
		    		System.out.println("Product : " + productName + " is valid");
		    	} else {		    		
		    		System.out.println("Product : " + productName + " is invalid");
			    	
		    	}
		    }
		    
		    List<WebElement> verifyCartQuantity = driver.findElements(By.className("cart-qty-panel-read"));
		    int j = 0;
		    System.out.println(verifyCartQuantity.size());
		    for(WebElement element : verifyCartQuantity) {
		    	WebElement Quantity = element.findElement(By.tagName("a"));
		    	int quantity = products.get(j++).getQuantity();
		    	if(Quantity.getText().equalsIgnoreCase(quantity+"")) {
		    		System.out.println("Quantity = " + quantity + " is valid");
		    	} else {		    		
		    		System.out.println("Quantity = " + quantity + " is invalid");
			    	
		    	}
		    }
		    	
		    	
		    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}

}
