package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Raid {

	public static String xor(String val1, String val2) {
		String temp = "";
		for (int i = 0; i < val1.length(); i++) {
			if (val1.charAt(i) == '0' && val2.charAt(i) == '0') {
				temp += "0";
			} else if (val1.charAt(i) == '0' && val2.charAt(i) == '1') {
				temp += "1";
			} else if (val1.charAt(i) == '1' && val2.charAt(i) == '0') {
				temp += "1";
			} else if (val1.charAt(i) == '1' && val2.charAt(i) == '1') {
				temp += "0";
			}
		}
		return temp;
	}

	public static String xor(char val1, char val2) {
		String temp = "";
		if (val1 == '0' && val2 == '0') {
			temp += "0";
		} else if (val1== '0' && val2 == '1') {
			temp += "1";
		} else if (val1 == '1' && val2== '0') {
			temp += "1";
		} else if (val1 == '1' && val2 == '1') {
			temp += "0";
		}
		else {
			temp+="X";
		}
		return temp;
	}

	public static List<String> createParityDisk(List<String> disk1, List<String> disk2) {
		List<String> disk3 = new ArrayList<String>();

		for (int i = 0; i < disk1.size(); i++) {
			disk3.add(xor(disk1.get(i), disk2.get(i)));
		}
		return disk3;
	}

	public static List<String> makeError(List<String> list, int num) {
		String input = diskAsString(list);
		Random r = new Random();
		int temp = 0;
		System.out.println(input);
		for (int i = 0; i < num; i++) {
			temp = r.nextInt(input.length());
			if (input.charAt(temp) != 'X') {
				String left = input.substring(0, temp);
				String right = input.substring(temp + 1, input.length());
				input = left + 'X' + right;
			} else {
				i--;
			}
		}
		return stringAsDisk(input);
	}

	public static ArrayList<String> stringAsDisk(String input) {
		ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i < input.length(); i += 4) {
			list.add(input.substring(i, i + 4));
		}
		return list;
	}

	public static String diskAsString(List<String> list) {
		String input = "";
		for (String s : list) {
			input += s;
		}
		return input;
	}

	public static String backUp(String firstDisk, String secondDisk, String thirdDisk) {
		
		for(int i = 0;i<firstDisk.length();i++)
		{
			if(firstDisk.charAt(i)=='X')
			{
				String left = firstDisk.substring(0, i);
				String right = firstDisk.substring(i + 1, firstDisk.length());
				String temp = xor(secondDisk.charAt(i),thirdDisk.charAt(i));
				firstDisk = left + temp + right;
			}
		}
		return firstDisk;
	}

	public static boolean checkDisk(String firstDisk) {
		for(int i =0;i<firstDisk.length();i++)
		{
			if(firstDisk.charAt(i)=='X')
			{
				return true;
			}
		}
		return false;
	}

	public static List<String> changeDisk(String firstDisk) {
		String temp = "";
		for(int i=0;i<firstDisk.length();i++)
		{
			temp+="X";
		}
		return stringAsDisk(temp);
	}

}
