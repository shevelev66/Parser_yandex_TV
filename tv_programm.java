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
// class - элементы структуры данных
import main.tv_chanel;
import main.tv_show;

public class tv_programm {
	static ArrayList<tv_chanel> listchanel= new ArrayList<>();  // структура данных каналов
	
	public static void selenium_parser(WebDriver driver, String href_TV) throws InterruptedException {
		 driver.navigate().to(href_TV); //загрузка страницы по ссылке
	     driver.navigate().refresh(); // обновление страницы для загрузки JS
	     Thread.sleep(5000); // ожидание загрузки страницы JS
	     JavascriptExecutor jse = (JavascriptExecutor) driver; // для управления скроллингом JS 
	 	 // инициализация поиска элементов запросом Xpath    
	     List<WebElement> num_chanel_box = driver.findElements(By.xpath("//section[1]/div[1]/div[@class='grid__item']")); // поиск каналов по секциям JS
	     List<WebElement> title_chanel_box = driver.findElements(By.xpath("//section[1]/div[1]/div[1]/div[@class='grid-channel']")); // НАЗВАНИЕ канала
	     List<WebElement> time_TV_box = driver.findElements(By.xpath("//section[1]/div[1]/div[1]/div[@class='grid-events']//time[@class='grid-event__item-time']"));  //время передач
	     List<WebElement> title_TV_box = driver.findElements(By.xpath("//section[1]/div[1]/div[1]/div[@class='grid-events']//div[@class='grid-event__item-title-wrap']"));  //название передач
	     // парсинг и вывод информации на консоль 
	     System.out.println("**************************************************************");
	     for (int l=1; l<=4; ++l) { // 4 секции с каналами
	  	 num_chanel_box = driver.findElements(By.xpath("//section[1]/div["+l+"]/div[@class='grid__item']")); // поиск каналов по секциям JS
		 for (int k=1; k<=num_chanel_box.size(); ++k) { // поиск названия канала и списка передач
		    	title_chanel_box = driver.findElements(By.xpath("//section[1]/div["+l+"]/div["+k+"]/div[@class='grid-channel']")); // НАЗВАНИЕ канала
		    	time_TV_box = driver.findElements(By.xpath("//section[1]/div["+l+"]/div["+k+"]/div[@class='grid-events']//time[@class='grid-event__item-time']"));  //время передач
			    title_TV_box = driver.findElements(By.xpath("//section[1]/div["+l+"]/div["+k+"]/div[@class='grid-events']//div[@class='grid-event__item-title-wrap']"));  //название передач
		    	for (int i=0; i<title_chanel_box.size(); ++i) {
		    		ArrayList<tv_show> listshow= new ArrayList<>();
			    	System.out.println("--------------------------------------------------КАНАЛ: <<"+title_chanel_box.get(i).getText()+">>--------------------------------------------------"); //вывод названия канала
			    	for (int j=0; j<time_TV_box.size(); ++j) { 
				    	 String buf=time_TV_box.get(j).getText();
				    	 if (!buf.isEmpty()) {	 
				    		 System.out.println(time_TV_box.get(j).getText()+"---"+title_TV_box.get(j).getText());  // вывод программы передач + время
				    		 listshow.add(new tv_show(time_TV_box.get(j).getText(), title_TV_box.get(j).getText())); // заполнение структуры данных по передачам           
				    		 } 
			    	}
			    	listchanel.add(new tv_chanel(title_chanel_box.get(i).getText(), listshow)); // заполнение структуры данных по каналам
			    	
			     }
		     } 
		 jse.executeScript("scroll(0, document.body.scrollHeight)"); // прокрутка страницы для загрузки JS на каждой секции
		 Thread.sleep(5000);  // выдержка загрузки страницы
	    	      } 
	     driver.close(); //закрытие драйвера браузера
	}
public static String TV_data_set() {  // метод запроса даты запрашиваемой телепрограммы + контроль временного промежутка
	Scanner in_d=new Scanner(System.in);
	GregorianCalendar Calendar=new GregorianCalendar(); //текущая дата
	GregorianCalendar BeforCalendar=new GregorianCalendar(); // дата ДО
	GregorianCalendar AfterCalendar=new GregorianCalendar(); // дата ПОСЛЕ
	DateFormat df = new SimpleDateFormat("dd.MM.yyyy"); // формат даты для отображения
	DateFormat df_parser = new SimpleDateFormat("yyyy-MM-dd"); //формат даты для сайта
	BeforCalendar.add(Calendar.WEEK_OF_YEAR, -1); //установка даты ДО текущей
	AfterCalendar.add(Calendar.WEEK_OF_YEAR, +1); // установка даты ПОСЛЕ текущей
	System.out.println("Текущая дата: "+df.format(Calendar.getTime()));
	System.out.println("Программа может загрузить данные в периоде от текущей даты: с "+df.format(BeforCalendar.getTime())+" по "+df.format(AfterCalendar.getTime()));
	String data_site="";
while (true) {		
	System.out.println("Введите дату, за которую требуется сформировать телепрограмму (вид дд.мм.гггг):");
	String enterdata=in_d.nextLine();
	Date date=Calendar.getTime();
	try {
		  date = df.parse(enterdata);
		  data_site=df_parser.format(date);
	    }
		catch (ParseException e) { System.out.println("Дата введена некорректно! Данные будут загружены за текущую дату!"); data_site=df_parser.format(Calendar.getTime());}
	if ((date.compareTo(BeforCalendar.getTime()) >0) && (date.compareTo(AfterCalendar.getTime()) <0)) { System.out.println("ОК! Введенная дата соответсвует заданному диапазону!"); break; }
	else {System.out.println("NO! Введенная дата НЕ соответсвует заданному диапазону!");}
 }
     
return data_site; // метод выдает дату в формате сайта для формирования ссылки
}	

public static void saveinfo_TV(ArrayList<tv_chanel> list, String filename, String data_set) throws IOException {
	 Workbook excelbook = new XSSFWorkbook(); // книга 
	    Sheet sheet1=excelbook.createSheet("TV-программа "+data_set);  //создание листа в книге
	    int k=0;       
	    for (int i=0; i<list.size(); ++i) {
	    	Row CHrow = sheet1.createRow(i+k); // создание строки на листе Нумерация начинается с нуля
	    	Cell titlechanel = CHrow.createCell(0); // создание ячейки в строке - название программы
	    	titlechanel.setCellValue(list.get(i).getChtitle());  //установить значение ячеки
	    	
	    	ArrayList<tv_show> listshow= new ArrayList<>(); // список телепрограмм канала
	    	listshow=list.get(i).getlistshow(); // извлекаем список программ из списка каналов
	    	for (int j=0; j<listshow.size(); ++j) {
	    		Row TVrow = sheet1.createRow(i+k+j+1); // создание строки на листе Нумерация начинается с нуля
	    		Cell timeTV = TVrow.createCell(1); //создание ячейки в строке - время программы
		    	timeTV.setCellValue(listshow.get(j).getTVtime()); //установить значение ячеки
	    		Cell titleTV = TVrow.createCell(2); // создание ячейки в строке - название программы
		    	titleTV.setCellValue(listshow.get(j).getTVtitle()); //установить значение ячеки
		    }
	    	k=k+listshow.size();	    	    	
	       	}
	    // Меняем размер столбца
	    for (int j=0; j<=2; ++j ) { sheet1.autoSizeColumn(j); }
   	// Записываем всё в файл
       excelbook.write(new FileOutputStream(filename));
       System.out.println("Данные сохранены в файл: "+filename);
       excelbook.close();
}

public static void Control_TV() throws InterruptedException, IOException {   // метод управления парсингом ТВ программы 
	System.out.println("Программа осуществляет сбор и вывод информации о программе телепередач, идущих по ТВ.");
	System.out.println("В качестве сайта для извлечения информации используется сайт телепрограммы Яндекс – https://tv.yandex.ru/");
	System.out.println("Данные выводятся на консоль и могут быть сохранены в MS EXCEL.");
	System.out.println("Для работы программы необходимо, чтобы на ПК был установлен один из браузеров: Firefox, Chrome или Internet Explorer.");
	String data_site=TV_data_set();
	String href_TV="https://tv.yandex.ru/?date="+data_site+"&period=all-day&utm_source=main_stripe_big";
	Scanner in = new Scanner(System.in);
	System.out.println("Выберете драйвер для получения данных в соответствии с установленным браузером(0-ChromeDriver; 1-FirefoxDriver, 2-InternetExplorerDriver):");
	int n=in.nextInt();
	in.nextLine(); // очистка сканера
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
	System.out.println("-----------------------------------------------------------------------------Формирование файла данных:---------------------------------------------------------------------------");
	System.out.println("Сохранить полученные данные? (0-НЕТ; 1-ДА):");
	n=in.nextInt();
	in.nextLine(); // очистка сканера
	if (n==1) {	saveinfo_TV(listchanel, "TV_yandex.xlsx", data_site); }
	else {System.out.println("Данные НЕ были сохранены!");}
	in.close();      
}

public static void main(String[] args) throws IOException, InterruptedException {
	Control_TV(); // метод управления парсингом ТВ программы
	}
}