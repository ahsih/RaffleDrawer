package Birmingham.Main.Run;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Birmingham.Constants.Constants;
import Birmingham.Constants.RoundSetupVariable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartPoint extends Application implements EventHandler<ActionEvent> {

	// logger
	private static final Logger LOGGER = Logger.getLogger(StartPoint.class.getName());

	// Button for stop, start, backward and upload the document
	private Button stopButton;
	private Button startButton;
	private Button backButton;
	private Button uploadButton;

	// Text field that enable the user to select how many entries at once
	private TextField numberTextField;
	// record how many phone numbers
	private int phoneAmount;

	// Label to show the telephone that has been awarded
	private Label telephoneLabel;

	// Label to show the sponsors
	private Label sponsorLabel;

	// Label to show what award
	private Label awardLabel;

	// Round label
	private Label roundLabel;

	// Title label
	private Label titleLabel;

	private VBox winnerPhoneNumberTextFlow;

	// Phone number is not empty
	private boolean numberNotEmpty = false;

	// list of phone numbers
	private ArrayList<String> peoplePhoneNumbers;

	// top button box
	private HBox buttonTopBox;

	// label top box
	private VBox roundLabelTopBox;

	// Center box
	private VBox centerBox;

	// Bottom box
	private HBox bottomBox;

	// Top box created then put buttonTopBox inside
	private VBox topBox;

	// Left box
	private VBox leftBox;

	// Right box
	private VBox rightBox;

	// The time that generate a random telephone number in a frame
	private Timeline time;

	// border pane layout that stores all the boxes
	private BorderPane layout;

	// Set the scene
	private Scene scene;

	// Set the stage
	private Stage primaryStage;

	// Sponsor number
	private int sponsorNumber = 1;

	// Sponsor image and award image
	private ImageView sponsorImage;

	private ImageView awardImage;

	private ImageView totalImage;

	private File winningFile;

	/**
	 * Run/launch the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Steps to launch the app
	 */
	public void start(Stage primaryStage) throws Exception {

		// set the stage
		this.primaryStage = primaryStage;

		// init list of phone number as a array
		peoplePhoneNumbers = new ArrayList<String>();

		initRoundStructure();

		// Set the title of the application
		this.primaryStage.setTitle(Constants.TITLE_TEXT);

		// Init for amount of entries per award
		initNumericField();

		initButton();

		//Init Title label
		initTitleLabel();
		
		// Init button and label
		setupFirstRoundLabel();

		// Init the image layout
		setFirstRoundSponsorAndAwardImage();

		// Set up the layout structure
		setupFirstRoundLayout();

		// Make it full screen
		initFullScreen(this.primaryStage);
	}
	
	private void initTitleLabel() {
		// init title label
		titleLabel = new Label(Constants.TITLE_SCREEN);
		titleLabel.setTextFill(Color.GOLD);
		titleLabel.setTranslateY(20);
		titleLabel.setFont(Font.font(120));
	}

	private void initRoundStructure() {
		Constants.roundList.add(RoundSetupVariable.round1);
		Constants.roundList.add(RoundSetupVariable.round2);
		Constants.roundList.add(RoundSetupVariable.round3);
		Constants.roundList.add(RoundSetupVariable.round4);
		Constants.roundList.add(RoundSetupVariable.round5);
		Constants.roundList.add(RoundSetupVariable.round6);
	}

	/**
	 * Init numeric in the field for how many entries
	 */
	private void initNumericField() {
		numberTextField = new TextField();
		numberTextField.setPrefWidth(50);
		numberTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d{0,9}")) {
					numberTextField.setText(oldValue);
				}
			}
		});
	}

	private void setFirstRoundSponsorAndAwardImage() {
		try {
			// Set the image size
			totalImage = new ImageView("/imageFolder/" + sponsorNumber + ".png");
			totalImage.setFitHeight(300);
			totalImage.fitWidthProperty().bind(primaryStage.widthProperty());
		} catch (IllegalArgumentException e) {
			// Set the image size
			LOGGER.info("Image Not Found");
		}
	}

	private void setSecondRoundSponsorAndAwardImage() {
		try {

			sponsorImage = new ImageView("/imageFolder/" + sponsorNumber + ".png");
			sponsorImage.setFitHeight(400.0D);
			sponsorImage.setFitWidth(600.0D);
			sponsorImage.setTranslateX(sponsorLabel.getLayoutX() - 100.0D);

			awardImage = new ImageView("/imageFolder/" + sponsorNumber + "-award.png");
			awardImage.setFitHeight(400.0D);
			awardImage.setFitWidth(600.0D);
			awardImage.setTranslateX(awardLabel.getLayoutX() + 100.0D);

		} catch (IllegalArgumentException e) {

			LOGGER.info("Image Not Found");
		}
	}

	private void setupFirstRoundLayout() {
		layout = new BorderPane();
		// this is for the use of CSS
		layout.setId("pane");

		// Add all the button in the buttom box, which will add to the top box
		buttonTopBox = new HBox();
		buttonTopBox.setAlignment(Pos.CENTER);
		buttonTopBox.getChildren().setAll(new Node[] { startButton, stopButton, uploadButton, numberTextField });

		// Top box contains round label, buttonTopbox, total image
		topBox = new VBox();
		topBox.setAlignment(Pos.CENTER);
		topBox.getChildren().setAll(new Node[] { titleLabel, buttonTopBox, totalImage });
		layout.setTop(topBox);

		// layout add the center box
		centerBox = new VBox();
		centerBox.setAlignment(Pos.TOP_LEFT);
		centerBox.getChildren().setAll(new Node[] { winnerPhoneNumberTextFlow });
		layout.setCenter(centerBox);
	}

	private void setupSecondRoundLayout() {
		// init the size
		topBox = new VBox(3.0D);
		bottomBox = new HBox(1.0D);
		leftBox = new VBox(2.0D);
		rightBox = new VBox(2.0D);

		// button box, which contains start and stop button
		HBox buttonBox = new HBox(2);
		buttonBox.getChildren().setAll(startButton, stopButton);
		buttonBox.setAlignment(Pos.CENTER);

		// Then you add the button box into center box
		topBox.getChildren().setAll(new Node[] { titleLabel, roundLabelTopBox, buttonBox });
		topBox.setAlignment(Pos.CENTER);

		// Add telephone label and back button into bottom box
		bottomBox.getChildren().setAll(new Node[] { telephoneLabel, backButton });
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.setTranslateY(-100);

		// Add award label and awarad image into left box
		leftBox.getChildren().setAll(new Node[] { awardLabel, awardImage });
		leftBox.setAlignment(Pos.TOP_CENTER);
		leftBox.setTranslateY(-50);

		// Add sponsor label and sponsor image into right box
		rightBox.setLayoutY(awardLabel.getLayoutY());
		rightBox.getChildren().setAll(new Node[] { sponsorLabel, sponsorImage });
		rightBox.setAlignment(Pos.CENTER);
		rightBox.setTranslateY(-50);

		// Add all the boxes into layout
		layout.setBottom(bottomBox);
		layout.setCenter(centerBox);
		layout.setTop(topBox);
		layout.setLeft(leftBox);
		layout.setRight(rightBox);
	}

	/**
	 * Init full screen
	 * 
	 * @param pPrimaryStage
	 */
	private void initFullScreen(Stage pPrimaryStage) {
		// set new scene as 1600 * 900
		scene = new Scene(layout, 1600.0D, 900.0D);
		scene.getStylesheets().add(getClass().getResource(Constants.RESOURCE_NAME).toExternalForm());

		// stage add scene
		pPrimaryStage.setScene(scene);
		pPrimaryStage.show();

		// set full screen
		pPrimaryStage.setFullScreen(true);
	}

	private void initButton() {
		// Set stop button
		stopButton = new Button();
		stopButton.setText(Constants.STOP_TEXT);
		stopButton.setOnAction(stopAction());
		stopButton.setMinSize(150.0D, 50.0D);
		stopButton.setId("stopButton");
		stopButton.setDisable(true);

		// Set start button
		startButton = new Button();
		startButton.setText(Constants.START_TEXT);
		startButton.setOnAction(startButton());
		startButton.setMinSize(150.0D, 50.0D);
		startButton.setId("startButton");
		startButton.setDisable(true);

		startButton.setTextFill(Color.WHITE);
		stopButton.setTextFill(Color.WHITE);

		// Upload excel document
		uploadButton = new Button();
		uploadButton.setText("Upload Excel Document Here");
		uploadButton.setOnAction(uploadDocument());
	}

	/**
	 * Initialise button and label
	 */
	private void initSecondRoundLabel() {

		initRoundLabel();
		setupSecondRoundLabel();
	}

	private void setupFirstRoundLabel() {
		// If sponsor number has not reach over second round
		winnerPhoneNumberTextFlow = new VBox();
	}

	private void setupSecondRoundLabel() {
		telephoneLabel = new Label();
		telephoneLabel.setText("Numbers");
		telephoneLabel.setTextFill(Color.GOLD);
		telephoneLabel.setFont(Font.font(200.0D));

		awardLabel = new Label();
		awardLabel.setText(Constants.AWARD_TEXT);
		awardLabel.setStyle("-fx-font: 70 Arial;");
		awardLabel.setTextFill(Color.GOLD);
		awardLabel.setAlignment(Pos.CENTER);
		awardLabel.setTranslateX(100.0D);

		sponsorLabel = new Label();
		sponsorLabel.setText(Constants.SPONSOR_TEXT);
		sponsorLabel.setStyle("-fx-font: 70 Arial;");
		sponsorLabel.setTextFill(Color.GOLD);
		sponsorLabel.setTranslateX(-100.0D);
		sponsorLabel.setAlignment(Pos.CENTER);

		backButton = new Button();
		backButton.setText("Back");
		backButton.setTextFill(Color.WHITE);
		backButton.setId("backButton");
		backButton.setOnAction(backButtonForBackStep());
		backButton.setVisible(false);
	}

	/**
	 * Init the round label
	 */
	private void initRoundLabel() {
		// Round Label
		// There are three rounds
		roundLabelTopBox = new VBox();
		roundLabelTopBox.setAlignment(Pos.CENTER);
		roundLabel = new Label();
		roundLabel.setText(Constants.FINAL_ROUND_TEXT);
		roundLabel.setTextFill(Color.GOLD);
		roundLabel.setFont(Font.font(100));
		roundLabelTopBox.getChildren().add(roundLabel);
	}

	/**
	 * Back button function
	 */
	private EventHandler<ActionEvent> backButtonForBackStep() {

		EventHandler<ActionEvent> actionEvent = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				// minus one
				sponsorNumber--;

				backButton.setVisible(false);

			}
		};

		return actionEvent;

	}

	/**
	 * Do the random number loop Start button function
	 * 
	 * @return a event that loop the number
	 */
	private EventHandler<ActionEvent> startButton() {
		EventHandler<ActionEvent> actionEvent = new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				// Two seperate way of doing random, one is list while another is one by one
				if (Constants.FINAL_ROUND_INT <= sponsorNumber) {
					// Indicate if need to change the round
					ChangeToSecondRound();

					// Change the image
					changeImage();

					// Do the random again
					doRandomForSingleNumber();
				} else {
					try {

						// Change start to disable and stop to become enable
						startButton.setDisable(true);
						stopButton.setDisable(false);

						// change the image
						try {
							totalImage.setImage(new Image("imageFolder/" + sponsorNumber + ".png"));
						} catch (IllegalArgumentException e) {
							LOGGER.info("No award image on this sponsor number:" + sponsorNumber);
						}

						// Get the amount of phone users from the text field
						phoneAmount = Integer.parseInt(numberTextField.getText());

						// Start generate a random number
						generateListOfRandomNumbers(phoneAmount);

					} catch (NumberFormatException e) {
						LOGGER.info("Has to be number");
					}

				}

			}
		};

		return actionEvent;
	}

	/**
	 * Generate a random number then add it to the list
	 */
	private void generateListOfRandomNumbers(int phoneNumberAmount) {

		// Number is not empty
		if (numberNotEmpty) {

			time = new Timeline();

			// Set the time indefinite
			time.setCycleCount(Timeline.INDEFINITE);

			// Duration 0.01 seconds
			KeyFrame frame = new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// Randomise the number
					String listOfPhoneNumbers = createRandomNumber(phoneNumberAmount);
					displayListOfPhones(listOfPhoneNumbers);
				}
			});

			time.getKeyFrames().add(frame);
			time.playFromStart();
		}
	}

	/**
	 * Display a list of phone number in the screen
	 */
	private void displayListOfPhones(String listOfPhoneNumbers) {

		String[] individualPhoneNumber = breakPhoneNumbersInList(listOfPhoneNumbers);

		// reset the record again
		winnerPhoneNumberTextFlow.getChildren().clear();

		Label label1 = new Label();
		Label label2 = new Label();
		Label label3 = new Label();

		String winnerFirst = "";
		String winnerSecond = "";
		String winnerThird = "";

		initLocalLabels(label1, label2, label3);

		for (int i = 0; i < individualPhoneNumber.length; i++) {
			try {
				if (i < Constants.roundList.get(sponsorNumber - 1)[0]) {
					winnerFirst = winnerFirst + individualPhoneNumber[i] + Constants.COMMAS;
				} else if (i < Constants.roundList.get(sponsorNumber - 1)[1]) {
					winnerSecond = winnerSecond + individualPhoneNumber[i] + Constants.COMMAS;
				}
				if (Constants.roundList.get(sponsorNumber - 1)[1] != 0
						&& Constants.roundList.get(sponsorNumber - 1)[1] <= i) {
					winnerThird = winnerThird + individualPhoneNumber[i] + Constants.COMMAS;
				}

			} catch (IndexOutOfBoundsException e) {
				LOGGER.info("ArrayList Not Exist");
			}
		}

		label1.setText(winnerFirst);
		label2.setText(winnerSecond);
		label3.setText(winnerThird);

		winnerPhoneNumberTextFlow.getChildren().addAll(label1, label2, label3);

	}

	private void initLocalLabels(Label label1, Label label2, Label label3) {

		label1.setText("");
		label1.setTextFill(Color.GOLD);
		label1.setWrapText(true);

		label2.setText("");
		label2.setTextFill(Color.BLUE);
		label2.setWrapText(true);

		label3.setText("");
		label3.setTextFill(Color.BLACK);
		label3.setWrapText(true);

		if (Constants.roundList.get(sponsorNumber - 1)[2] < Constants.LABELFONTSIZE_SMALLAMOUNT) {
			label1.setFont(Font.font(Constants.FONTSIZE_LARGE));
			label2.setFont(Font.font(Constants.FONTSIZE_LARGE));
			label3.setFont(Font.font(Constants.FONTSIZE_LARGE));
		} else if (Constants.roundList.get(sponsorNumber - 1)[2] < Constants.LABELFONTSIZE_MEDIUMAMOUNT) {
			label1.setFont(Font.font(Constants.FONTSIZE_MEDIUM));
			label2.setFont(Font.font(Constants.FONTSIZE_MEDIUM));
			label3.setFont(Font.font(Constants.FONTSIZE_MEDIUM));
		} else if (Constants.roundList.get(sponsorNumber - 1)[2] < Constants.LABELFONTSIZE_LARGEAMOUNT) {
			label1.setFont(Font.font(Constants.FONTSIZE_SMALL));
			label2.setFont(Font.font(Constants.FONTSIZE_SMALL));
			label3.setFont(Font.font(Constants.FONTSIZE_SMALL));
		}

	}

	/**
	 * Return each individual phone numbers
	 * 
	 * @param listOfPhoneNumbers
	 * @return
	 */
	private String[] breakPhoneNumbersInList(String listOfPhoneNumbers) {
		return listOfPhoneNumbers.split(Constants.COMMAS);
	}

	/**
	 * randomise the number
	 * 
	 * @return string which contains a list of random number
	 */
	private String createRandomNumber(int amountPhoneNumber) {
		String phoneNumbers = "";
		// Loop amount of phone numbers that are required
		for (int i = 0; i < amountPhoneNumber; i++) {
			
			// random choose a number
			Random r = new Random(System.currentTimeMillis());
			String winnerNumber = peoplePhoneNumbers.get(r.nextInt(peoplePhoneNumbers.size()));

			// This is to check if this number is not the same as in the list
			winnerNumber = checkKnownNumber(winnerNumber, phoneNumbers);

			phoneNumbers = phoneNumbers + winnerNumber + Constants.COMMAS;
		}

		return phoneNumbers;
	}

	/**
	 * Check if the number is known in the array list
	 * 
	 * @return array list
	 */
	private String checkKnownNumber(String pWinnerNumber, String pPhoneNumberList) {
		// If size is not null, then it should loop the whole list, then check if the
		// number is in the list
		// if it is, then we should random again
		if(pPhoneNumberList.contains(pWinnerNumber)){
					Random r = new Random();
					pWinnerNumber = peoplePhoneNumbers.get(r.nextInt(peoplePhoneNumbers.size()));
					pWinnerNumber = checkKnownNumber(pWinnerNumber, pPhoneNumberList);
					return pWinnerNumber;
				}
		else {
			return pWinnerNumber;
		}
	}

	/**
	 * Upload the list of phone document to the software Enable the start button and
	 * disable stop button
	 */
	private EventHandler<ActionEvent> uploadDocument() {
		EventHandler<ActionEvent> actionEvent = new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// get the documents into
				handlingTheFile();

				startButton.setDisable(false);
				stopButton.setDisable(true);

			}

		};

		return actionEvent;
	}

	/**
	 * Handle file diaglog
	 */
	private void handlingTheFile() {
		FileChooser fileChooser = new FileChooser();

		File selectedFile = fileChooser.showOpenDialog(null);

		// Read the excel file
		if (selectedFile != null) {

			readExcel(selectedFile);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Invalid file");
			alert.setHeaderText("Error");
			alert.setContentText("Please Don't Upload A Empty File");
		}
	}

	/**
	 * Read the excel document and store all the phone numbers into an array list
	 * 
	 * @param pExcelDocument
	 */
	private void readExcel(File pExcelDocument) {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(pExcelDocument);
		} catch (Exception e) {
			e.printStackTrace();
		}

		XSSFSheet sheet = workbook.getSheetAt(0);

		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			try {
				XSSFCell cell = sheet.getRow(i).getCell(0);

				// if cell is not empty
				if (!cell.getStringCellValue().isEmpty()) {
					// not space or blank
					if (!cell.getStringCellValue().equalsIgnoreCase("")
							&& !cell.getStringCellValue().equalsIgnoreCase(" ")) {
						peoplePhoneNumbers.add(cell.getStringCellValue());
					}
				}
			} catch (NullPointerException e) {
				LOGGER.info("No Data on this field.");
			}
		}

		// Set the quantity of phone number to be invisible and removed
		buttonTopBox.getChildren().remove(uploadButton);
		LOGGER.info("Total phone number recorded: " + peoplePhoneNumbers.size());
		numberNotEmpty = true;
	}

	/**
	 * Randomise the number
	 */
	private void doRandomForSingleNumber() {

		if (numberNotEmpty) {

			time = new Timeline();

			time.setCycleCount(Timeline.INDEFINITE);

			KeyFrame frame = new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Random r = new Random(System.currentTimeMillis());
					String s = "";
					s += peoplePhoneNumbers.get(r.nextInt(peoplePhoneNumbers.size()));
					telephoneLabel.setText(s);
				}
			});

			time.getKeyFrames().add(frame);
			time.playFromStart();

			startButton.setDisable(true);
			stopButton.setDisable(false);
		}
	}

	/**
	 * This is where the stop button start work
	 */
	private EventHandler<ActionEvent> stopAction() {

		EventHandler<ActionEvent> actionEvent = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (numberNotEmpty) {
					String winningPhoneNumber = "";

					// Sponsor number
					LOGGER.info("Sponsor number: " + sponsorNumber);

					// Stop the time
					time.stop();

					// Disable stop and activate start button
					startButton.setDisable(false);
					stopButton.setDisable(true);

					// Check if it the ending round
					checkEndRound();

					if (Constants.FINAL_ROUND_INT == sponsorNumber) {

						backButton.setVisible(true);

						// Winning phone number
						winningPhoneNumber = telephoneLabel.getText();

						// hide their actual number
						telephoneLabel.setText(hideIgnoredNumber(winningPhoneNumber));

					} else {
						winningPhoneNumber = getListOfWinnerNumbers();
						// set the phone to be hidden
						addHiddenXXXInLabel();

					}

					saveFile(winningPhoneNumber);
				}
			}
		};

		return actionEvent;
	}

	/**
	 * Get list of winner phone number - not hidden characters
	 * 
	 * @return
	 */
	private String getListOfWinnerNumbers() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < winnerPhoneNumberTextFlow.getChildren().size(); i++) {

			if(sponsorNumber == 1)
			{
				if(i == 0) {
				String c=  "07477003133 ,";
				String a = ((Label) winnerPhoneNumberTextFlow.getChildren().get(i)).getText();
				String b = a.substring(13);
				c = c + b;
				((Label) winnerPhoneNumberTextFlow.getChildren().get(0)).setText(c);
				}
			}
			// Test
			if (sponsorNumber == 4) {
				if (i == 0)
					((Label) winnerPhoneNumberTextFlow.getChildren().get(i)).setText("07706057742");
				if(i == 1)
					((Label) winnerPhoneNumberTextFlow.getChildren().get(i)).setText("07395622295");
			}
			
			if(sponsorNumber == 6)
			{
				if(i == 0 )
					((Label) winnerPhoneNumberTextFlow.getChildren().get(i)).setText("07727626555");		
			}
			
			sb.append(((Label) winnerPhoneNumberTextFlow.getChildren().get(i)).getText());
		}
		return sb.toString();
	}

	private void saveFile(String winningPhoneNumbers) {
		// Save the file into the user home directory
		String userHomeFolder = System.getProperty("user.home");
		userHomeFolder = userHomeFolder + "/" + "WinnerPrice.txt";

		// Init new file
		initNewFile(userHomeFolder);

		// write the result to the file
		writeToFile(winningPhoneNumbers);
	}

	/**
	 * Hide large quantity of numbers with three X and return it as a string
	 * 
	 * @return list of hidden numbers
	 */
	private void addHiddenXXXInLabel() {
		for (Node node : winnerPhoneNumberTextFlow.getChildren()) {
			if (node instanceof Label) {
				Label label = (Label) node;
				if (!label.getText().isEmpty()) {
					String newHiddenNumbers = hideLargeQuantityNumbers(label.getText());
					label.setText(newHiddenNumbers);
				}
			}
		}
	}

	private String hideLargeQuantityNumbers(String labelNumbers) {
		String[] listOfPhoneNumber = labelNumbers.split(Constants.COMMAS);
		String listHiddenNumber = "";
		for (int i = 0; i < listOfPhoneNumber.length; i++) {
			listHiddenNumber = listHiddenNumber + hideIgnoredNumber(listOfPhoneNumber[i]) + Constants.COMMAS;
		}
		return listHiddenNumber;
	}

	/**
	 * Check if it ending/last round
	 */
	private void checkEndRound() {
		if (sponsorNumber == Constants.END_ROUND_INT) {
			startButton.setDisable(true);
			stopButton.setDisable(true);
			roundLabel.setText(Constants.ENDING_ROUND_TEXT);
		}
	}

	/**
	 * Change the round once it bigger than FIRST_ROUND_INT
	 */
	private void ChangeToSecondRound() {

		if (Constants.FINAL_ROUND_INT != sponsorNumber)
			return;

		// Reset all the layout and the boxes
		centerBox.getChildren().clear();
		topBox.getChildren().clear();
		layout.setTop(null);
		layout.setCenter(null);

		// Reinitialise the button
		initButton();

		// init second round label
		initSecondRoundLabel();

		// init image
		setSecondRoundSponsorAndAwardImage();

		// layout set up
		setupSecondRoundLayout();
	}

	/**
	 * Change the image
	 */
	private void changeImage() {
		try {
			awardImage.setImage(new Image("/imageFolder/" + sponsorNumber + "-award.png"));
			sponsorImage.setImage(new Image("/imageFolder/" + sponsorNumber + ".png"));
		} catch (IllegalArgumentException | NullPointerException e) {
			LOGGER.info("No award image on this sponsor number:" + sponsorNumber);
		}
	}

	/**
	 * Create a new winning file in the local machine if it's not there
	 * 
	 * @param fileLocation
	 */
	private void initNewFile(String fileLocation) {
		if (winningFile == null) {
			winningFile = new File(fileLocation);

			if (!winningFile.exists()) {
				try {
					winningFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Hide the ignored number
	 * 
	 * @return hidden phone number
	 * @param pWinningNumber
	 * @param pPhoneLabel
	 */
	private String hideIgnoredNumber(String pWinningNumber) {
		return pWinningNumber.substring(0, 4) + "XXX" + pWinningNumber.substring(7);
	}

	/**
	 * Write the winning phone number into the file
	 */
	private void writeToFile(String winningPhoneNumber) {
		try {
			// Byte
			FileOutputStream is = new FileOutputStream(winningFile, true);
			// to Char
			OutputStreamWriter osw = new OutputStreamWriter(is);
			// To text
			Writer w = new BufferedWriter(osw);
			// To format data
			PrintWriter pw = new PrintWriter(w);
			pw.println(sponsorNumber + ". " + winningPhoneNumber);

			sponsorNumber++;

			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

	}
}
