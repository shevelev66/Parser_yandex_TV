package main;
import java.util.ArrayList;
import main.tv_show;

public class tv_chanel {
	private String title_chanel; // �������� ������
	ArrayList<tv_show> listshow; //������ �� �������
	// ����������� ������
	public tv_chanel (String title_chanel, ArrayList<tv_show> listshow) {
		this.title_chanel=title_chanel;
		this.listshow=listshow;
	}
	
	public String getChtitle() {
		return this.title_chanel;
	}
	
	public ArrayList<tv_show> getlistshow() {
		return this.listshow;
	}
	
	@Override
    public String toString() {
        return "�����: <<"+title_chanel+">>"+"\n"+
                listshow.toString()+"\n";
    }

}