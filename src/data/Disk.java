package data;

import java.util.ArrayList;
import java.util.List;

public class Disk {
	private int id; // 1 2 3 
	private List<String> data; // 0000
	
	public Disk(int id) {
		super();
		this.id = id;
		this.data = new ArrayList<String>();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<String> getData() {
		return data;
	}
	public void setData(List<String> data) {
		this.data = data;
	}
	public void addData(String temp)
	{
		this.data.add(temp);
	}
	public String toString()
	{
		return "Dysk " + this.id + this.getData();
	}
	
	
}
