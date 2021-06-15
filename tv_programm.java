package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
// class - �������� ��������� ������
import main.tv_chanel;
import main.tv_show;

public class tv_programm {
	static ArrayList<tv_chanel> listchanel= new ArrayList<>();  // ��������� ������ �������
	
	public static void selenium_parser(WebDriver driver, String href_TV) throws InterruptedException {
		 driver.navigate().to(href_TV); //�������� �������� �� ������
	     driver.navigate().refresh(); // ���������� �������� ��� �������� JS
	     Thread.sleep(5000); // �������� �������� �������� JS
	     JavascriptExecutor jse = (JavascriptExecutor) driver; // ��� ���������� ����������� JS 
	 	 // ������������� ������ ��������� �������� Xpath    
	     List<WebElement> num_chanel_box = driver.findElements(By.xpath("//section[1]/div[1]/div[@class='grid__item']")); // ����� ������� �� ������� JS
	     List<WebElement> title_chanel_box = driver.findElements(By.xpath("//section[1]/div[1]/div[1]/div[@class='grid-channel']")); // �������� ������
	     List<WebElement> time_TV_box = driver.findElements(By.xpath("//section[1]/div[1]/div[1]/div[@class='grid-events']//time[@class='grid-event__item-time']"));  //����� �������
	     List<WebElement> title_TV_box = driver.findElements(By.xpath("//section[1]/div[1]/div[1]/div[@class='grid-events']//div[@class='grid-event__item-title-wrap']"));  //�������� �������
	     // ������� � ����� ���������� �� ������� 
	     System.out.println("**************************************************************");
	     for (int l=1; l<=4; ++l) { // 4 ������ � ��������
	  	 num_chanel_box = driver.findElements(By.xpath("//section[1]/div["+l+"]/div[@class='grid__item']")); // ����� ������� �� ������� JS
		 for (int k=1; k<=num_chanel_box.size(); ++k) { // ����� �������� ������ � ������ �������
		    	title_chanel_box = driver.findElements(By.xpath("//section[1]/div["+l+"]/div["+k+"]/div[@class='grid-channel']")); // �������� ������
		    	time_TV_box = driver.findElements(By.xpath("//section[1]/div["+l+"]/div["+k+"]/div[@class='grid-events']//time[@class='grid-event__item-time']"));  //����� �������
			    title_TV_box = driver.findElements(By.xpath("//section[1]/div["+l+"]/div["+k+"]/div[@class='grid-events']//div[@class='grid-event__item-title-wrap']"));  //�������� �������
		    	for (int i=0; i<title_chanel_box.size(); ++i) {
		    		ArrayList<tv_show> listshow= new ArrayList<>();
			    	System.out.println("--------------------------------------------------�����: <<"+title_chanel_box.get(i).getText()+">>--------------------------------------------------"); //����� �������� ������
			    	for (int j=0; j<time_TV_box.size(); ++j) { 
				    	 String buf=time_TV_box.get(j).getText();
				    	 if (!buf.isEmpty()) {	 
				    		 System.out.println(time_TV_box.get(j).getText()+"---"+title_TV_box.get(j).getText());  // ����� ��������� ������� + �����
				    		 listshow.add(new tv_show(time_TV_box.get(j).getText(), title_TV_box.get(j).getText())); // ���������� ��������� ������ �� ���������           
				    		 } 
			    	}
			    	listchanel.add(new tv_chanel(title_chanel_box.get(i).getText(), listshow)); // ���������� ��������� ������ �� �������
			    	
			     }
		     } 
		 jse.executeScript("scroll(0, document.body.scrollHeight)"); // ��������� �������� ��� �������� JS �� ������ ������
		 Thread.sleep(5000);  // �������� �������� ��������
	    	      } 
	     driver.close(); //�������� �������� ��������
	}
public static String TV_data_set() {  // ����� ������� ���� ������������� ������������� + �������� ���������� ����������
	Scanner in_d=new Scanner(System.in);
	GregorianCalendar Calendar=new GregorianCalendar(); //������� ����
	GregorianCalendar BeforCalendar=new GregorianCalendar(); // ���� ��
	GregorianCalendar AfterCalendar=new GregorianCalendar(); // ���� �����
	DateFormat df = new SimpleDateFormat("dd.MM.yyyy"); // ������ ���� ��� �����������
	DateFormat df_parser = new SimpleDateFormat("yyyy-MM-dd"); //������ ���� ��� �����
	BeforCalendar.add(Calendar.WEEK_OF_YEAR, -1); //��������� ���� �� �������
	AfterCalendar.add(Calendar.WEEK_OF_YEAR, +1); // ��������� ���� ����� �������
	System.out.println("������� ����: "+df.format(Calendar.getTime()));
	System.out.println("��������� ����� ��������� ������ � ������� �� ������� ����: � "+df.format(BeforCalendar.getTime())+" �� "+df.format(AfterCalendar.getTime()));
	String data_site="";
while (true) {		
	System.out.println("������� ����, �� ������� ��������� ������������ ������������� (��� ��.��.����):");
	String enterdata=in_d.nextLine();
	Date date=Calendar.getTime();
	try {
		  date = df.parse(enterdata);
		  data_site=df_parser.format(date);
	    }
		catch (ParseException e) { System.out.println("���� ������� �����������! ������ ����� ��������� �� ������� ����!"); data_site=df_parser.format(Calendar.getTime());}
	if ((date.compareTo(BeforCalendar.getTime()) >0) && (date.compareTo(AfterCalendar.getTime()) <0)) { System.out.println("��! ��������� ���� ������������ ��������� ���������!"); break; }
	else {System.out.println("NO! ��������� ���� �� ������������ ��������� ���������!");}
 }
     
return data_site; // ����� ������ ���� � ������� ����� ��� ������������ ������
}	

public static void saveinfo_TV(ArrayList<tv_chanel> list, String filename, String data_set) throws IOException {
	 Workbook excelbook = new XSSFWorkbook(); // ����� 
	    Sheet sheet1=excelbook.createSheet("TV-��������� "+data_set);  //�������� ����� � �����
	    int k=0;       
	    for (int i=0; i<list.size(); ++i) {
	    	Row CHrow = sheet1.createRow(i+k); // �������� ������ �� ����� ��������� ���������� � ����
	    	Cell titlechanel = CHrow.createCell(0); // �������� ������ � ������ - �������� ���������
	    	titlechanel.setCellValue(list.get(i).getChtitle());  //���������� �������� �����
	    	
	    	ArrayList<tv_show> listshow= new ArrayList<>(); // ������ ������������ ������
	    	listshow=list.get(i).getlistshow(); // ��������� ������ �������� �� ������ �������
	    	for (int j=0; j<listshow.size(); ++j) {
	    		Row TVrow = sheet1.createRow(i+k+j+1); // �������� ������ �� ����� ��������� ���������� � ����
	    		Cell timeTV = TVrow.createCell(1); //�������� ������ � ������ - ����� ���������
		    	timeTV.setCellValue(listshow.get(j).getTVtime()); //���������� �������� �����
	    		Cell titleTV = TVrow.createCell(2); // �������� ������ � ������ - �������� ���������
		    	titleTV.setCellValue(listshow.get(j).getTVtitle()); //���������� �������� �����
		    }
	    	k=k+listshow.size();	    	    	
	       	}
	    // ������ ������ �������
	    for (int j=0; j<=2; ++j ) { sheet1.autoSizeColumn(j); }
   	// ���������� �� � ����
       excelbook.write(new FileOutputStream(filename));
       System.out.println("������ ��������� � ����: "+filename);
       excelbook.close();
}

public static void Control_TV() throws InterruptedException, IOException {   // ����� ���������� ��������� �� ��������� 
	System.out.println("��������� ������������ ���� � ����� ���������� � ��������� �����������, ������ �� ��.");
	System.out.println("� �������� ����� ��� ���������� ���������� ������������ ���� ������������� ������ � https://tv.yandex.ru/");
	System.out.println("������ ��������� �� ������� � ����� ���� ��������� � MS EXCEL.");
	System.out.println("��� ������ ��������� ����������, ����� �� �� ��� ���������� ���� �� ���������: Firefox, Chrome ��� Internet Explorer.");
	String data_site=TV_data_set();
	String href_TV="https://tv.yandex.ru/?date="+data_site+"&period=all-day&utm_source=main_stripe_big";
	Scanner in = new Scanner(System.in);
	System.out.println("�������� ������� ��� ��������� ������ � ������������ � ������������� ���������(0-ChromeDriver; 1-FirefoxDriver, 2-InternetExplorerDriver):");
	int n=in.nextInt();
	in.nextLine(); // ������� �������
	if (n==0) { 
		WebDriver driver = new ChromeDriver();
		selenium_parser(driver, href_TV);
		}
	if (n==1) { 
		WebDriver driver = new FirefoxDriver();
		selenium_parser(driver, href_TV);
		}
	if (n==2) { 
		WebDriver driver = new InternetExplorerDriver();
		selenium_parser(driver, href_TV);
		}
	System.out.println("-----------------------------------------------------------------------------������������ ����� ������:---------------------------------------------------------------------------");
	System.out.println("��������� ���������� ������? (0-���; 1-��):");
	n=in.nextInt();
	in.nextLine(); // ������� �������
	if (n==1) {	saveinfo_TV(listchanel, "TV_yandex.xlsx", data_site); }
	else {System.out.println("������ �� ���� ���������!");}
	in.close();      
}

public static void main(String[] args) throws IOException, InterruptedException {
	Control_TV(); // ����� ���������� ��������� �� ���������
	}
}