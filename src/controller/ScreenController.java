package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import data.Disk;
import data.Raid;
import data.Raport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ScreenController {
	@FXML
	Button readDataButton, firstFaultButton, secondFaultButton, thirdFaultButton, backDataButton, changeFirstDiskButton,
			changeSecondDiskButton, changeThirdDiskButton, resetButton;
	@FXML
	ListView<String> firstDiskList, secondDiskList, thirdDiskList;
	@FXML
	Label inputLabel, lostDataLabel, infoLabel, firstDiskWarningLabel, secondDiskWarningLabel, thirdDiskWarningLabel;
	@FXML
	TextField firstFaultField, secondFaultField, thirdFaultField;
	Disk disk1;
	Disk disk2;
	Disk parityDisk;

	public ScreenController() {
		super();
		disk1 = new Disk(1);
		disk2 = new Disk(2);
		parityDisk = new Disk(3);
	}

	@FXML
	public void initialize() {
		setDisable(true);
		resetButton.setDisable(true);
	}

	@FXML
	public void readData() {
		String input = "";
		File file = new File("input.txt");
		Raport.makeRaport("Wykorzystany plik wejsciowy: " + file.getPath());
		Scanner in;
		try {
			in = new Scanner(file);
			input = in.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Raport.makeRaport("Dane odczytane z pliku: " + input);
		// dodanie 0 gdy ci¹g jest zbyt krótki
		if (input.length() % 8 != 0) {
			while (input.length() % 8 != 0) {
				input += "0";
			}
		}
		inputLabel.setText(input);
		inputLabel.setVisible(true);
		infoLabel.setVisible(true);

		Raport.makeRaport("Dane uzyte w programie: " + input);
		for (int i = 0; i < input.length(); i += 8) {
			disk1.addData(input.substring(i, i + 4));
			disk2.addData(input.substring(i + 4, i + 8));
		}

		ObservableList<String> obsDisk1 = FXCollections.observableArrayList(disk1.getData());
		firstDiskList.setItems(obsDisk1);
		ObservableList<String> obsDisk2 = FXCollections.observableArrayList(disk2.getData());
		secondDiskList.setItems(obsDisk2);
		parityDisk.setData(Raid.createParityDisk(disk1.getData(), disk2.getData()));
		ObservableList<String> obsDisk3 = FXCollections.observableArrayList(parityDisk.getData());

		thirdDiskList.setItems(obsDisk3);
		setDisable(false);
		resetButton.setDisable(false);
		readDataButton.setDisable(true);

		Raport.makeRaport("Zawartosc dysku 1:  " + Raid.diskAsString(disk1.getData()));
		Raport.makeRaport("Zawartosc dysku 2:  " + Raid.diskAsString(disk2.getData()));
		Raport.makeRaport("Zawartosc dysku 3:  " + Raid.diskAsString(parityDisk.getData()));
	}

	@FXML
	public void firstFault() {

		int num = 0;
		try {
			num = Integer.parseInt(firstFaultField.getText());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (num > disk1.getData().size() * 4) {
			firstDiskWarningLabel.setVisible(true);
			Raport.makeRaport("Liczba wstrzyknietych bledow wieksza niz rozmiar dysku!");
		} else {

			secondDiskWarningLabel.setVisible(false);
			disk1.setData(Raid.makeError(disk1.getData(), num));
			ObservableList<String> obsDisk1 = FXCollections.observableArrayList(disk1.getData());
			firstDiskList.setItems(obsDisk1);

			Raport.makeRaport("Liczba wstrzyknietych bledow na dysku 1:  " + num);
			Raport.makeRaport("Dysk 1 po wstrzyknieciu bledow:  " + Raid.diskAsString(disk1.getData()));
		}
	}

	@FXML
	public void secondFault() {
		int num = 0;
		try {
			num = Integer.parseInt(secondFaultField.getText());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (num > disk2.getData().size() * 4) {
			secondDiskWarningLabel.setVisible(true);
			Raport.makeRaport("Liczba wstrzyknietych bledow wieksza niz rozmiar dysku!");
		} else {
			secondDiskWarningLabel.setVisible(false);
			disk2.setData(Raid.makeError(disk2.getData(), num));
			ObservableList<String> obsDisk2 = FXCollections.observableArrayList(disk2.getData());
			secondDiskList.setItems(obsDisk2);

			Raport.makeRaport("Liczba wstrzyknietych bledow na dysku 2:  " + num);
			Raport.makeRaport("Dysk 2 po wstrzyknieciu bledow:  " + Raid.diskAsString(disk2.getData()));
		}
	}

	@FXML
	public void thirdFault() {
		int num = 0;
		try {
			num = Integer.parseInt(thirdFaultField.getText());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (num > parityDisk.getData().size() * 4) {
			thirdDiskWarningLabel.setVisible(true);
			Raport.makeRaport("Liczba wstrzyknietych bledow wieksza niz rozmiar dysku!");
		} else {

			thirdDiskWarningLabel.setVisible(false);
			parityDisk.setData(Raid.makeError(parityDisk.getData(), num));
			ObservableList<String> obsDisk3 = FXCollections.observableArrayList(parityDisk.getData());
			thirdDiskList.setItems(obsDisk3);

			Raport.makeRaport("Liczba wstrzyknietych bledoww na dysku 3:  " + num);
			Raport.makeRaport("Dysk 3 po wstrzyknieciu bledow:  " + Raid.diskAsString(parityDisk.getData()));
		}
	}

	@FXML
	public void backData() {
		String firstDisk = Raid.diskAsString(disk1.getData());
		String secondDisk = Raid.diskAsString(disk2.getData());
		String thirdDisk = Raid.diskAsString(parityDisk.getData());

		firstDisk = Raid.backUp(firstDisk, secondDisk, thirdDisk);
		disk1.setData(Raid.stringAsDisk(firstDisk));
		ObservableList<String> obsDisk1 = FXCollections.observableArrayList(disk1.getData());
		firstDiskList.setItems(obsDisk1);

		secondDisk = Raid.backUp(secondDisk, firstDisk, thirdDisk);
		disk2.setData(Raid.stringAsDisk(secondDisk));
		ObservableList<String> obsDisk2 = FXCollections.observableArrayList(disk2.getData());
		secondDiskList.setItems(obsDisk2);

		thirdDisk = Raid.backUp(thirdDisk, secondDisk, firstDisk);
		parityDisk.setData(Raid.stringAsDisk(thirdDisk));
		ObservableList<String> obsDisk3 = FXCollections.observableArrayList(parityDisk.getData());
		thirdDiskList.setItems(obsDisk3);

		Raport.makeRaport("Zawartosc dysku 1 po odzyskaniu danych: " + Raid.diskAsString(disk1.getData()));
		Raport.makeRaport("Zawartosc dysku 2 po odzyskaniu danych: " + Raid.diskAsString(disk2.getData()));
		Raport.makeRaport("Zawartosc dysku 3 po odzyskaniu danych: " + Raid.diskAsString(parityDisk.getData()));

		if (Raid.checkDisk(firstDisk) || Raid.checkDisk(secondDisk) || Raid.checkDisk(thirdDisk)) {
			lostDataLabel.setVisible(true);
			Raport.makeRaport("UTRACONE DANE!!!");
			setDisable(true);

		}

	}

	@FXML
	public void changeFirstDisk() {

		String firstDisk = Raid.diskAsString(disk1.getData());

		disk1.setData(Raid.changeDisk(firstDisk));
		ObservableList<String> obsDisk1 = FXCollections.observableArrayList(disk1.getData());
		firstDiskList.setItems(obsDisk1);

		Raport.makeRaport("Zawartosc dysku 1 po wymianie: " + Raid.diskAsString(disk1.getData()));

	}

	@FXML
	public void changeSecondDisk() {
		String secondDisk = Raid.diskAsString(disk2.getData());

		disk2.setData(Raid.changeDisk(secondDisk));
		ObservableList<String> obsDisk2 = FXCollections.observableArrayList(disk2.getData());
		secondDiskList.setItems(obsDisk2);

		Raport.makeRaport("Zawartosc dysku 2 po wymianie: " + Raid.diskAsString(disk2.getData()));

	}

	@FXML
	public void changeThirdDisk() {
		String thirdDisk = Raid.diskAsString(parityDisk.getData());

		parityDisk.setData(Raid.changeDisk(thirdDisk));
		ObservableList<String> obsDisk3 = FXCollections.observableArrayList(parityDisk.getData());
		thirdDiskList.setItems(obsDisk3);

		Raport.makeRaport("Zawartosc dysku 3 po wymianie: " + Raid.diskAsString(parityDisk.getData()));

	}

	@FXML
	public void reset() {
		setDisable(true);
		disk1.getData().clear();
		ObservableList<String> obsDisk1 = FXCollections.observableArrayList(disk1.getData());
		firstDiskList.setItems(obsDisk1);
		disk2.getData().clear();
		ObservableList<String> obsDisk2 = FXCollections.observableArrayList(disk2.getData());
		secondDiskList.setItems(obsDisk2);
		parityDisk.getData().clear();
		ObservableList<String> obsDisk3 = FXCollections.observableArrayList(parityDisk.getData());
		thirdDiskList.setItems(obsDisk3);
	
		Raport.makeRaport("RESET DANYCH!");


		readDataButton.setDisable(false);
		lostDataLabel.setVisible(false);

	}

	public void setDisable(boolean flag) {
		firstFaultButton.setDisable(flag);
		secondFaultButton.setDisable(flag);
		thirdFaultButton.setDisable(flag);
		backDataButton.setDisable(flag);
		changeFirstDiskButton.setDisable(flag);
		changeSecondDiskButton.setDisable(flag);
		changeThirdDiskButton.setDisable(flag);
	}

}
