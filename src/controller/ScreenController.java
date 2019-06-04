package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import data.Disk;
import data.Raid;
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
			changeSecondDiskButton, changeThirdDiskButton;
	@FXML
	ListView<String> firstDiskList, secondDiskList, thirdDiskList;
	@FXML
	Label inputLabel, lostDataLabel, infoLabel;
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
	}

	@FXML
	public void readData() {
		String input = "";
		File file = new File("input.txt");
		Scanner in;
		try {
			in = new Scanner(file);
			input = in.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// dodanie 0 gdy ci¹g jest zbyt krótki
		if (input.length() % 8 != 0) {
			while (input.length() % 8 != 0) {
				input += "0";
			}
		}
		inputLabel.setText(input);
		inputLabel.setVisible(true);
		infoLabel.setVisible(true);
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
		readDataButton.setDisable(true);
	}

	@FXML
	public void firstFault() {

		int num = 0;
		try {
			num = Integer.parseInt(firstFaultField.getText());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		disk1.setData(Raid.makeError(disk1.getData(), num));
		ObservableList<String> obsDisk1 = FXCollections.observableArrayList(disk1.getData());
		firstDiskList.setItems(obsDisk1);

	}

	@FXML
	public void secondFault() {
		int num = 0;
		try {
			num = Integer.parseInt(secondFaultField.getText());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		disk2.setData(Raid.makeError(disk2.getData(), num));
		ObservableList<String> obsDisk2 = FXCollections.observableArrayList(disk2.getData());
		secondDiskList.setItems(obsDisk2);

	}

	@FXML
	public void thirdFault() {
		int num = 0;
		try {
			num = Integer.parseInt(thirdFaultField.getText());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		parityDisk.setData(Raid.makeError(parityDisk.getData(), num));
		ObservableList<String> obsDisk3 = FXCollections.observableArrayList(parityDisk.getData());
		thirdDiskList.setItems(obsDisk3);
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

		if (Raid.checkDisk(firstDisk) || Raid.checkDisk(secondDisk) || Raid.checkDisk(thirdDisk)) {
			lostDataLabel.setVisible(true);
		}
	}

	@FXML
	public void changeFirstDisk() {

		String firstDisk = Raid.diskAsString(disk1.getData());

		disk1.setData(Raid.changeDisk(firstDisk));
		ObservableList<String> obsDisk1 = FXCollections.observableArrayList(disk1.getData());
		firstDiskList.setItems(obsDisk1);

	}

	@FXML
	public void changeSecondDisk() {
		String secondDisk = Raid.diskAsString(disk2.getData());

		disk2.setData(Raid.changeDisk(secondDisk));
		ObservableList<String> obsDisk2 = FXCollections.observableArrayList(disk2.getData());
		secondDiskList.setItems(obsDisk2);

	}

	@FXML
	public void changeThirdDisk() {
		String thirdDisk = Raid.diskAsString(parityDisk.getData());

		parityDisk.setData(Raid.changeDisk(thirdDisk));
		ObservableList<String> obsDisk3 = FXCollections.observableArrayList(parityDisk.getData());
		thirdDiskList.setItems(obsDisk3);

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
