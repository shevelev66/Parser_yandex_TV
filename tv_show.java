package main;
public class tv_show {  //ТВ-передача
	private String time_tv;
	private String title_tv;
	// конструктор класса
	public tv_show(String time_tv, String title_tv) {
		this.time_tv=time_tv;
		this.title_tv=title_tv;
	}
	
	public String getTVtime() {
		return this.time_tv;
	}
	
	public String getTVtitle() {
		return this.title_tv;
	}
	
	@Override
    public String toString() {
        return time_tv+"---"+title_tv+"\n";
    }	
}