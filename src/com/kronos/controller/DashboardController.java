package com.kronos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.kronos.App;
import com.kronos.api.LapRace;
import com.kronos.api.MainCar;
import com.kronos.api.Observer;
import com.kronos.api.TimeRace;
import com.kronos.global.animation.PulseTransition;
import com.kronos.global.enums.RaceState;
import com.kronos.global.util.Alerts;
import com.kronos.model.*;
import com.kronos.module.main.Main;
import com.kronos.parserXML.MainImpl.IncrementalSaveStrategy;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class manages all the logic behind the dashboard.
 * @author TeamKronos
 * @version 1.0
 */
public class DashboardController implements Initializable, Observer {

    public static DashboardController ctrl;
    private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
    private static Double meantime = 0.00;
    @FXML
    public Label i18n_raceinfo;
    @FXML
    public Label i18n_departureHour;
    @FXML
    public Label i18n_actualHour;
    @FXML
    public Label i18n_elapsedTime;
    @FXML
    public Label i18n_remainingTime;
    @FXML
    public Label i18n_elapsedLaps;
    @FXML
    public Label i18n_remainingLaps;
    @FXML
    public Label i18n_details;
    @FXML
    public Tab i18n_currentPilot;
    @FXML
    public Label i18n_currentPilotLastName;
    @FXML
    public Label i18n_currentPilotFirstname;
    @FXML
    public Label i18n_currentPilotDateOfBirth;
    @FXML
    public Tab i18n_currentCar;
    @FXML
    public Label i18n_currentCarBrand;
    @FXML
    public Label i18n_currentCarModel;
    @FXML
    public Label i18n_currentCarTeam;
    @FXML
    public Label i18n_averageLapTime;
    @FXML
    public Label i18n_timerOpponentCar;
    @FXML
    public Label i18n_type;
    @FXML
    public Label i18n_car;
    @FXML
    public Label i18n_comment;
    @FXML
    public Label i18n_raceRanking;
    @FXML
    public Label i18n_previous;
    @FXML
    public Label i18n_inProgress;
    @FXML
    public Label currentHour;
    @FXML
    public JFXButton startRace;
    @FXML
    public JFXButton pauseRace;
    @FXML
    public JFXButton stopRace;
    @FXML
    public TableColumn<TopModel, Double> col_delete;
    @FXML
    public TableColumn<TopModel, Integer> colCarNumber;
    @FXML
    public TableColumn<TopModel, String> col_typetop;
    @FXML
    public TableColumn<TopModel, String> col_comment;
    @FXML
    public TableColumn<TopModel, String> col_time;
    @FXML
    public TableColumn<TopModel, String> col_racetime;
    @FXML
    public TableColumn<TopModel, String> col_laptime;
    @FXML
    public TableColumn<TopModel, Integer> colLapNumber;
    @FXML
    public TableColumn<TopModel, Integer> colTopPosition;
    @FXML
    public JFXToggleButton toogleedit;
    PulseTransition pulseTransition;
    MainCarModel mycar;
    boolean isStartRivalTimer = true;
    @FXML
    private Label chronoRivalCar;
    @FXML
    private Label chronoTopTime;
    @FXML
    private Label labelMeanTime;
    @FXML
    private Label tempsTourEcoulé;
    @FXML
    private Label tempsTourRestant;
    @FXML
    private ProgressBar meanTimeBar;
    @FXML
    private Button TopBtn;
    @FXML
    private Label departureHour;
    @FXML
    private Label tmierSign;
    @FXML
    private Label spentTime;
    @FXML
    private Label remainingTime;
    @FXML
    private Label lastNamePilotMainCar;
    @FXML
    private Label firstNamePilotMainCar;
    @FXML
    private Label dateOfBirthPilot;
    @FXML
    private Label mainCarBrand;
    @FXML
    private Label mainCarModel;
    @FXML
    private Label mainCarTeam;
    @FXML
    private Label carNumber;
    @FXML
    private Label remainingLapsBeforeStop;
    @FXML
    private Label state;
    @FXML
    private Label time;
    @FXML
    public Label i18n_panelTitle;
    @FXML
    public Label i18n_panelRemainingLaps;
    @FXML
    public Label i18n_panelTime;
    @FXML
    public Label i18n_panelState;
    @FXML
    private TableView<TopModel> table_info;
    @FXML
    private JFXTextField topComment;
    @FXML
    private ComboBox<String> car;
    @FXML
    private ComboBox<String> topType;
    @FXML
    private ListView<Integer> listNowRank = new ListView<>();
    @FXML
    private ListView<Integer> listPastRank = new ListView<>();
    private static RaceModel raceModel;
    private CarController carController = new CarController();
    private ArrayList<TopModel> topModels = new ArrayList<>();
    private ArrayList<CarModel> carModels = new ArrayList<>();
    private Integer[] relayCycles = new Integer[3];

    /**
     * This {@link Callback} triggers the removal of a top from the top history and updates the UI afterwards.
     */
    Callback<TableColumn<TopModel, Double>, TableCell<TopModel, Double>> cellFactory = new Callback<TableColumn<TopModel, Double>, TableCell<TopModel, Double>>() {
        @Override
        public TableCell<TopModel, Double> call(final TableColumn<TopModel, Double> param) {
            final TableCell<TopModel, Double> cell = new TableCell<TopModel, Double>() {

                private final Button btn = new Button("Delete");
                private final Button btn1 = new Button("Edit");

                {
                    btn.setOnAction((ActionEvent event) -> {
                        TopModel top = getTableView().getItems().get(getIndex());
                        removeTop(top.getCarNumber(), top.getId());
                        table_info.getItems().remove(top);
                        table_info.refresh();
                    });
                }

                @Override
                public void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        }
    };
    private HashMap<Integer, ArrayList<TopModel>> topsMaps = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> rivalCarTopsTimes = new HashMap<>();
    private HashMap<Integer, ArrayList<Double>> rivalCarListOfMeantime = new HashMap<>();
    private boolean isExtancier = true, breakThread = true;
    private Thread thread, threadChrono, threadChronoRivalCar;
    private int munites = 0, secondes = 0, millisecondes = 0, hours = 0, decimalpartTosecond = 0, intergerpart = 0, numberOfLapsDone = 0, remainingLaps;
    private int rivalTimerMunites = 0, rivalTimerHours = 0, rivalTimerSecondes = 0, rivalTimerMillisecondes = 0, numberMainCar = 0;
    private boolean isStartTimer, isSetTimerBar = true, istartRace = false, timerIsInitialize = true, firstTop = true, isInitialize = true;
    private double decimalpart = 0.0, lapTimeForMeanTime = 0.0, rivalCarLapTimeForMeanTime = 0.0;
    private Timeline spentTimeline;
    private Timeline remainingTimeline;
    private LocalTime localRemainningTime = LocalTime.parse("00:00:00");
    private LocalTime localSpentTime = LocalTime.parse("00:00:00");
    private LocalTime time2 = LocalTime.parse("00:00");
    private LocalTime timebar = LocalTime.parse("00:00:00");
    private LocalTime chronoTime = LocalTime.parse("00:00:00");
    private LocalTime chronoTimeRival = LocalTime.parse("00:00:00");
    private LocalTime timeTocompare = LocalTime.parse("00:00:00");
    private LocalTime oldRivalCarTime = LocalTime.parse("00:00:00");
    private LocalTime newRivalCarTime = LocalTime.parse("00:00:00");
    private LocalTime currentTime;
    private LocalTime departureTime;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("mm:ss:nn");
    private DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("HH:mm:ss:nn");

    /**
     * Default constructor.
     */
    public DashboardController() {

    }

    /**
     * Initializes the dashboard values on startup.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb  The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ctrl = this;
        App.getDataManager().attach(this);
        col_racetime.setVisible(false);
        colLapNumber.setVisible(false);
        topType.setDisable(true);
        localSpentTime = LocalTime.parse("00:00:00");
        time2 = LocalTime.parse("00:00");
        col_delete.setVisible(false);
        for (CarModel carModel : getFollowedCars()) {
            if (carModel instanceof MainCar) {
                carNumber.setText(Integer.toString(carModel.getNumber()));
            }
        }
        if (!getRace().isEmpty()) {
            raceModel = getRace().get(0);
        }

        if (raceModel instanceof TimeRace) {
            col_racetime.setVisible(true);
            if (raceModel.getRaceState().equals(RaceState.CREATION)) {
                long duration = ((TimeRace) raceModel).getDuration();
                int heureDuration = (int) (duration / 60);
                localRemainningTime = LocalTime.of(heureDuration, (int) (duration - (60 * heureDuration)), 0);
                spentTime.setText(localSpentTime.format(dtf));
                remainingTime.setText(localRemainningTime.format(dtf));
                computeNumberOfStops();
                listOfMeanTime.add((double) raceModel.getDefaultMeanLapTime());
            } else {
                startRace.setText("Restart");
                localRemainningTime = LocalTime.parse(raceModel.getTimeLapsRemaining());
                localSpentTime = LocalTime.parse(raceModel.getTimeLapsSpent());
                spentTime.setText(localSpentTime.format(dtf));
                remainingTime.setText(localRemainningTime.format(dtf));
                setRaceInformations(RaceState.BREAK);
                computeNumberOfStops();

            }
            spentTimeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
            spentTimeline.setCycleCount(Animation.INDEFINITE);
            remainingTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> decrementTime()));
            remainingTimeline.setCycleCount(Animation.INDEFINITE);
        } else {
            colLapNumber.setVisible(true);
            i18n_elapsedTime.setVisible(false);
            i18n_remainingTime.setVisible(false);
            i18n_elapsedLaps.setVisible(true);
            i18n_remainingLaps.setVisible(true);

            if (raceModel.getRaceState().equals(RaceState.CREATION)) {
                remainingLaps = ((LapRaceModel) raceModel).getNumberOfLaps();
                numberOfLapsDone = Integer.parseInt(raceModel.getTimeLapsSpent());
                raceModel.setTimeLapsRemaining(String.valueOf(remainingLaps));
                raceModel.setTimeLapsSpent(String.valueOf(numberOfLapsDone));
                computeNumberOfStops();
                listOfMeanTime.add((double) raceModel.getDefaultMeanLapTime());


            } else {
                startRace.setText("Restart");
                remainingLaps = Integer.parseInt(raceModel.getTimeLapsRemaining());
                numberOfLapsDone = Integer.parseInt(raceModel.getTimeLapsSpent());
                computeNumberOfStops();
                setRaceInformations(RaceState.BREAK);

            }
            remainingTime.setText(String.valueOf(remainingLaps));
            spentTime.setText(String.valueOf(numberOfLapsDone));

        }
        topType.setItems(FXCollections.observableArrayList("I", "O", "R"));
        topType.setValue("O");
        carModels.addAll(getFollowedCars());
        car.setItems(FXCollections.observableArrayList(getFollowedCarsNumbers(getFollowedCars())));
        initTable();


        /* Refresh table after import */
        // initialised table of rival Car
        List<TopModel> topModelList = (List<TopModel>) (List<?>) App.getDataManager().getModels(TopModel.class);
        ArrayList<CarModel> followedCar = getFollowedCars();
        for (CarModel carmodel : followedCar) {
            ArrayList<TopModel> mytopmodelList = new ArrayList<>();
            topsMaps.put(carmodel.getNumber(), mytopmodelList);
            if (!(carmodel instanceof MainCarModel)) {
                ArrayList<String> mytable = new ArrayList<>();
                ArrayList<Double> mytableOflaptime = new ArrayList<>();
                rivalCarTopsTimes.put(carmodel.getNumber(), mytable);
                rivalCarListOfMeantime.put(carmodel.getNumber(), mytableOflaptime);
                rivalCarTopsTimes.get(carmodel.getNumber()).add(newRivalCarTime.format(dtf1));
                rivalCarListOfMeantime.get(carmodel.getNumber()).add(0.0);

                if (!topModelList.isEmpty()) {
                    for (TopModel topModel : topModelList) {
                        int muniteteToLoad = Integer.parseInt(topModel.getLapTime().substring(0, 2));
                        int secondeToLoad = Integer.parseInt(topModel.getLapTime().substring(3, 5));
                        int milliToLoad = Integer.parseInt(topModel.getLapTime().substring(6, 8));
                        lapTimeForMeanTime = (muniteteToLoad + (secondeToLoad / 60.0) + (milliToLoad / 100) / 60.0);
                        if (topModel.getCarNumber() == carmodel.getNumber()) {
                            topsMaps.get(carmodel.getNumber()).add(topModel);
                            carmodel.getTopList().add(topModel);
                        }
                        if (topModel.getCarNumber() == carmodel.getNumber() && (topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {
                            rivalCarListOfMeantime.get(carmodel.getNumber()).add(lapTimeForMeanTime);

                        }
                    }
                }
            } else {
                numberMainCar = carmodel.getNumber();
                car.getSelectionModel().select(Integer.toString(numberMainCar));
                if (!topModelList.isEmpty()) {
                    for (TopModel topModel : topModelList) {
                        topModels.add(topModel);
                        int muniteteToLoad = Integer.parseInt(topModel.getLapTime().substring(0, 2));
                        int secondeToLoad = Integer.parseInt(topModel.getLapTime().substring(3, 5));
                        int milliToLoad = Integer.parseInt(topModel.getLapTime().substring(6, 8));
                        lapTimeForMeanTime = (muniteteToLoad + (secondeToLoad / 60.0) + (milliToLoad / 100) / 60.0);
                        if (topModel.getCarNumber() == carmodel.getNumber()) {
                            topsMaps.get(carmodel.getNumber()).add(topModel);
                            carmodel.getTopList().add(topModel);
                        }
                        if ((carmodel.getNumber() == topModel.getCarNumber()) && (topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {
                            listOfMeanTime.add(lapTimeForMeanTime);
                        }
                    }
                }
            }
            if(!topModelList.isEmpty()) {
                for(TopModel top : raceModel.getTopsMap().get(carmodel.getNumber())) {
                    loadData(top);
                }
            }
            table_info.getSortOrder().addAll(col_time, colTopPosition);
            table_info.sort();
            table_info.getSortOrder().removeAll(col_time,colTopPosition);
            table_info.refresh();
        }

        maincarinformation();
        departureHour.setText(time2.format(dtf));
        Timeline clock = new Timeline(new KeyFrame(Duration.millis(1000), e -> getCurrentTime()));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();


        Scene scene = App.getDecorator().getScene();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                KeyCode keyCode = event.getCode();

                File file = new File("top.properties");
                Properties properties = new Properties();
                try {
                    if (!file.exists()) {

                        //file.createNewFile();
                    } else {

                        FileInputStream fileInputStream = new FileInputStream(file);
                        properties.load(fileInputStream);
                        if (keyCode.toString().equals(properties.getProperty("key"))) {
                            if(!Main.isControlSet && raceModel.getRaceState().equals(RaceState.IN_PROGRESS)) {
                                handleNewTop();
                                displayNewRank();
                            }
                            else if(!Main.isControlSet) {
                                //Alerts.info("INFORMATION", "veuillez demarrer/continuer la course ");
                                Alerts.AlertWarning("INFORMATION", "veuillez demarrer/continuer la course ");
                            }

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });


    }

    private boolean compareTops(ArrayList<TopModel> oldTops, ArrayList<TopModel> newTops, int index) {
        return  oldTops.get(index).equals(newTops.get(index));
    }

    /**
     * This function calculates the initial state of the board visualization.
     * For lap race : makes an Euclidian division : lap number / lap interval
     * The integer result is the number of relays.
     * The remaining is the number of remaining laps when the last relay has a smaller number of laps.
     * For time race : the relay interval repeats indefinitley until the time is elapsed.
     * There is no need to make an euclidian division.
     * The board figures are stored in an int array data structure.
     * The first column are the remaining laps before the next relay.
     * The second column are the remaining relays.
     * The last column are the remaining laps.
     */
    private void computeNumberOfStops() {
        relayCycles[0] = raceModel.getRelayInterval();
        try {
            if (raceModel instanceof LapRace) {
                int euclidResult = remainingLaps / raceModel.getRelayInterval();
                int remaining = remainingLaps % raceModel.getRelayInterval();
                relayCycles[0] = raceModel.getRelayInterval();
                relayCycles[1] = euclidResult;
                relayCycles[2] = remaining;

            } else {
                relayCycles[0] = raceModel.getRelayInterval();
                relayCycles[1] = -1;
                relayCycles[2] = 0;
            }
            updatePannel();
        }catch (Exception logArithm){
            logArithm.printStackTrace();
        }

    }

    /**
     * Updates the data structures which encapsulates the panel figures.
     * Decrements the remaining laps before the next relay.
     *
     * @param lapTime the time of the last lap done by the {@link MainCar}
     */
    private void decrementPanel(String lapTime) {
        int carNumber = Integer.parseInt(car.getSelectionModel().getSelectedItem());
        CarModel carModel = carController.findCar(carModels, carNumber);
        if (carModel instanceof MainCarModel) {
            time.setText(lapTime);
            if (relayCycles[0] > 1 && relayCycles[1] != 0) {
                relayCycles[0]--;
            } else if (relayCycles[0] == 1 && relayCycles[1] != 0) {
                relayCycles[0] = raceModel.getRelayInterval();
                relayCycles[1]--;
            } else if (relayCycles[0] == 1) {
                relayCycles[0] = raceModel.getRelayInterval();
            } else {
                relayCycles[2]--;
            }
            updatePannel();
        }
    }

    /**
     * Updates the data structures which encapsulates the panel figures.
     * Increments the remaining laps before the next relay.
     *
     * @param lapTime the time of the last lap done by the {@link MainCar}
     */
    private void incrementPanel(String lapTime) {
        time.setText(lapTime);
        if (raceModel instanceof LapRace) {
            if (relayCycles[1] != -1 && remainingLaps - 1 < ((LapRace) raceModel).getNumberOfLaps() % raceModel.getRelayInterval()) {
                relayCycles[2]++;
            } else {
                if (relayCycles[1] == 0) {
                    relayCycles[1]++;
                }
                if (relayCycles[0] == raceModel.getRelayInterval()) {
                    relayCycles[0] = 1;
                    if (relayCycles[1] != -1) {
                        relayCycles[1]++;
                    }
                } else {
                    relayCycles[0]++;
                }
            }
        } else {
            if (relayCycles[0] == raceModel.getRelayInterval()) {
                relayCycles[0] = 1;
            } else {
                relayCycles[0]++;
            }
        }
        updatePannel();
    }

    /**
     * Updates the pannel acording to the figures in the date structure.
     * In normal time, the pannel displays :
     * - the last {@link MainCar main car} lap time
     * - the remaining laps before the next relay
     * - an empty message "-"
     * When one lap remains before the next relay, the pannel displays :
     * - the last {@link MainCar main car} lap time
     * - the remaining laps are displayed as "-"
     * - the message displayed is "IN" (go to the stands at the end of your lap)
     * When one lap remains before the end of the race (if it is a lap race)
     * - the last {@link MainCar main car} lap time
     * - the remaining laps are displayed as "-"
     * - the message displayed is "LAST" (go to the stands at the end of your lap)
     * When the race is finished and no laps remain (if it is a lap race)
     * - the last {@link MainCar main car} lap time
     * - the remaining laps are displayed as "-"
     * - the message displayed is "END" (go to the stands at the end of your lap)
     */
    private void updatePannel() {

        if (relayCycles[0] > 1 && relayCycles[1] != 0) {
            remainingLapsBeforeStop.setText(Integer.toString(relayCycles[0]));
            state.setText("-");
        } else if (relayCycles[1] == 0 && relayCycles[2] > 1) {
            remainingLapsBeforeStop.setText(Integer.toString(relayCycles[2]));
            state.setText("-");
        } else {
            if (relayCycles[0] == 1 || relayCycles[2] == 1) {
                if (relayCycles[1] == 0) {
                    remainingLapsBeforeStop.setText("-");
                    state.setText("LAST");
                } else {
                    remainingLapsBeforeStop.setText("-");
                    state.setText("IN");
                }
            } else {
                remainingLapsBeforeStop.setText("-");
                state.setText("END");
            }
        }
    }

    /**
     * Handles the click event on a top button.
     * A top cannot be triggered if the race has not started.
     *
     * @param event the {@link ActionEvent button click event}
     */
    @FXML
    public void handleTopButtonClick(ActionEvent event) {
        if (startRace.isDisable() && raceModel.getRaceState().equals(RaceState.IN_PROGRESS)) {
            handleNewTop();
            displayNewRank();
        } else {
            //Alerts.info("INFORMATION", "veuillez demarrer/continuer la course ");
            Alerts.AlertWarning("INFORMATION", "veuillez demarrer/continuer la course ");
        }
    }

    /**
     * Handles a top for the selected car (except for the first top of the {@link com.kronos.api.RivalCar rival cars}).
     */
    private void handleNewTop() {

        String type = topType.getSelectionModel().getSelectedItem();
        int carNumber = Integer.parseInt(car.getSelectionModel().getSelectedItem());
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        String raceTime = "";
        String lapTime = "";
        int lap = 0;
        TopModel topModel = null;
        String comment = topComment.getText();
        CarModel carModel = carController.findCar(carModels, carNumber);
        if (timerIsInitialize) {
            timerIsInitialize = false;
            carNumber = numberMainCar;
            carModel = carController.findCar(carModels, carNumber);
            lapTime = LocalTime.of(0, 0, 0, 0).format(dtf2);
            listOfMeanTime.add(getMeanTime(listOfMeanTime));
            setMeanTimeBar();
            startTimerBar();
        } else {
            if (carModel instanceof MainCarModel) {
                lapTime = chronoTime.format(dtf2);
                lapTimeForMeanTime = (munites + (secondes / 60.0) + (millisecondes / 100) / 60.0);
                resetTimerBar();
                setMeanTimeBar();

            } else {
                ArrayList<String> timeOfRivalCarTops = rivalCarTopsTimes.get(carModel.getNumber());
                int listLength = (timeOfRivalCarTops.size() - 1);
                String timeOfLastTop = timeOfRivalCarTops.get(listLength);
                int hh = Integer.parseInt(timeOfLastTop.substring(0, 2));
                int mm = Integer.parseInt(timeOfLastTop.substring(3, 5));
                int ss = Integer.parseInt(timeOfLastTop.substring(6, 8));
                int nn = Integer.parseInt(timeOfLastTop.substring(9, 11));
                newRivalCarTime = LocalTime.of(rivalTimerHours, rivalTimerMunites, rivalTimerSecondes, rivalTimerMillisecondes);
                oldRivalCarTime = LocalTime.of(hh, mm, ss, nn);
                java.time.Duration duration = java.time.Duration.between(newRivalCarTime, oldRivalCarTime).abs();
                hh = (int) (duration.getSeconds() / 3600);
                mm = (int) (duration.getSeconds() / 60) - (hh * 60);
                ss = (int) (duration.getSeconds() - ((hh * 3600) + (mm * 60)));
                nn = Math.abs(nn - rivalTimerMillisecondes);
                lapTime = LocalTime.of(hh, mm, ss, nn).format(dtf2);
                rivalCarTopsTimes.get(carModel.getNumber()).add(newRivalCarTime.format(dtf1));
                rivalCarLapTimeForMeanTime = (mm + (ss / 60.0) + (nn / 100) / 60.0);
            }
        }

        boolean b = (carModel instanceof MainCarModel) && (type.equals("R") || type.equals("O"));
        if (findPreviousTop(carNumber) == null || checkTopLogic(type, findPreviousTop(carNumber).getTopType())) {
            //Case where top respects logical top type order
            if (raceModel instanceof TimeRaceModel) {
                raceTime = localSpentTime.format(dtf);
                topModel = new TopModel(carNumber,0, dateTime, type, raceTime, lapTime, comment);
                handleTopTimeRace(topModel, carNumber);
                decrementPanel(lapTime);
                numberOfLapsDone++;
            } else {
                if (firstTop) {
                    lap = numberOfLapsDone;
                    firstTop = false;
                    decrementPanel(lapTime);
                } else {
                    if (b) {
                        remainingLaps--;
                        numberOfLapsDone++;
                        lap = numberOfLapsDone;
                        decrementPanel(lapTime);
                    } else {
                        lap = numberOfLapsDone;
                    }
                    if (!(carModel instanceof MainCarModel)) {
                        int sizeOfArray = carModel.getTopList().size();
                        sizeOfArray = sizeOfArray - 1;
                        TopModel topModel1 = carModel.getTopList().get(sizeOfArray);
                        if (type.equals("R") || type.equals("O")) {
                            lap = topModel1.getLap() + 1;
                        } else {
                            lap = topModel1.getLap();
                        }
                    }
                }
                topModel = new TopModel(carNumber,0, dateTime, type, lap, lapTime, comment);
                handleTopLapRace(topModel, carNumber);
            }
        } else {
            //Case where top does not respect logical top type order
            if (raceModel instanceof TimeRaceModel) {
                raceTime = localSpentTime.format(dtf);
                if (findPreviousTop(carNumber).getTopType().equals("I") && (type.equals("I") || type.equals("R"))) {
                    topModel = new TopModel(carNumber,0, dateTime, "O", raceTime, lapTime, comment + "-Top O système");
                    decrementPanel(lapTime);
                    numberOfLapsDone++;
                } else if (!findPreviousTop(carNumber).getTopType().equals("I") && type.equals("O")) {
                    topModel = new TopModel(carNumber,0, dateTime, "R", raceTime, lapTime, comment + "-Top R système");
                    decrementPanel(lapTime);
                    numberOfLapsDone++;
                }
                handleTopTimeRace(topModel, carNumber);
            } else {
                lap = numberOfLapsDone;
                if (firstTop) {
                    lap = numberOfLapsDone;
                    firstTop = false;
                } else {
                    if (b) {
                        remainingLaps--;
                        numberOfLapsDone++;
                        lap = numberOfLapsDone;
                    } else {
                        lap = numberOfLapsDone;
                    }
                    if (!(carModel instanceof MainCarModel)) {
                        int sizeOfArray = carModel.getTopList().size();
                        sizeOfArray = sizeOfArray - 1;
                        TopModel topModel1 = carModel.getTopList().get(sizeOfArray);
                        if (type.equals("R") || type.equals("O")) {
                            lap = topModel1.getLap() + 1;
                        } else {
                            lap = topModel1.getLap();
                        }
                    }
                }
                if (findPreviousTop(carNumber).getTopType().equals("I") && (type.equals("I") || type.equals("R"))) {
                    topModel = new TopModel(carNumber,0, dateTime, "O", lap, lapTime, comment + "-Top O système");
                    decrementPanel(lapTime);
                } else if (!findPreviousTop(carNumber).getTopType().equals("I") && type.equals("O")) {
                    topModel = new TopModel(carNumber,0, dateTime, "R", lap, lapTime, comment + "-Top R système");
                    decrementPanel(lapTime);
                }
                handleTopLapRace(topModel, carNumber);
            }
        }
        carModel.getTopList().add(topModel);
        loadData(topModel);
        //Save Top list of Object to persist
        saveTopModel(topModel);


        //CHECK THE END OF THE RACE

        if ((carModel instanceof MainCarModel)) {
            if ((topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {
                checkEndOfRace();
                handleMeanTimeBar(1);
                listOfMeanTime.add(lapTimeForMeanTime);
            } else {
                checkEndOfRace();
                handleMeanTimeBar(1);
            }
        } else {
            if ((topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {

                rivalCarListOfMeantime.get(carModel.getNumber()).add(rivalCarLapTimeForMeanTime);
            }
        }
        topComment.clear();

    }

    /**
     * Handles the first top for {@link com.kronos.api.RivalCar rival cars}.
     * The first top is always a "O" top.
     */
    private void handleFirstTopRivalCars() {
        LocalTime localTimeFirstTop = LocalTime.parse("00:00:00");
        String dateTimeFirstTop = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        ArrayList<CarModel> followedCars = getFollowedCars();
        for (CarModel carModel : followedCars) {
            if (!(carModel instanceof MainCarModel)) {
                if (raceModel instanceof TimeRace) {
                    TopModel topModelFirstTop = new TopModel(carModel.getNumber(),0, dateTimeFirstTop, "O", localSpentTime.format(dtf), localTimeFirstTop.format(dtf), "");
                    handleTopTimeRace(topModelFirstTop, carModel.getNumber());
                    loadData(topModelFirstTop);
                    carModel.getTopList().add(topModelFirstTop);
                    saveTopModel(topModelFirstTop);

                } else {
                    TopModel topModelFirstTop = new TopModel(carModel.getNumber(),0, dateTimeFirstTop, "O", 0, localTimeFirstTop.format(dtf), "");
                    handleTopLapRace(topModelFirstTop, carModel.getNumber());
                    loadData(topModelFirstTop);
                    carModel.getTopList().add(topModelFirstTop);
                    saveTopModel(topModelFirstTop);

                }
            }
        }

    }

    /**
     * Adds the top to the top data structure for {@link LapRace lap race} tops.
     * The data structures is a {@link HashMap map} with the car number in key and its associated tops in value.
     * This data structure is essential to find back previous tops at all time during the execution of the app.
     *
     * @param topModel  the top.
     * @param carNumber the car number.
     */
    private void handleTopTimeRace(TopModel topModel, int carNumber) {
        topModels.add(topModel);
        if (raceModel.getTopsMap().containsKey(carNumber)) {
            raceModel.getTopsMap().get(carNumber).add(topModel);
            topModel.setTopPosition(raceModel.getTopsMap().get(carNumber).size() - 1);
        } else {
            raceModel.getTopsMap().put(carNumber, new ArrayList<>());
            raceModel.getTopsMap().get(carNumber).add(topModel);
            topModel.setTopPosition(raceModel.getTopsMap().get(carNumber).size() - 1);
        }
    }

    /**
     * Adds the top to the top data structure for {@link TimeRace time race} tops.
     * The data structures is a {@link HashMap map} with the car number in key and its associated tops in value.
     * This data structure is essential to find back previous tops at all time during the execution of the app.
     *
     * @param topModel  the top.
     * @param carNumber the car number.
     */
    private void handleTopLapRace(TopModel topModel, int carNumber) {
        topModels.add(topModel);
        if (raceModel.getTopsMap().containsKey(carNumber)) {
            raceModel.getTopsMap().get(carNumber).add(topModel);
            topModel.setTopPosition(raceModel.getTopsMap().get(carNumber).size() -1);
        } else {
            raceModel.getTopsMap().put(carNumber, new ArrayList<>());
            raceModel.getTopsMap().get(carNumber).add(topModel);
            topModel.setTopPosition(raceModel.getTopsMap().get(carNumber).size() -1);
        }
    }

    /**
     * Loads the {@link TopModel} on the UI.
     */
    private void loadData(TopModel topModel) {
        table_info.getItems().add(0, topModel);
    }


    /**
     * Persists and saves the new top into the save file.
     *
     * @param topModel the new top.
     */
    public void saveTopModel(TopModel topModel) {
        IncrementalSaveStrategy.logTopToSave(topModel);
        IncrementalSaveStrategy.executeIncrementalSave();
        //App.getDataManager().persist(topModel);
        //App.getDataManager().saveFile();
    }

    /**
     * Loads the timer progressbar according to the average time.
     *
     * @param firstTime the first value for loading
     */
    private void handleMeanTimeBar(int firstTime) {

        if (isExtancier)
            pulseTransition = new PulseTransition(meanTimeBar);
        meantime = getMeanTime(listOfMeanTime);
        double timeToUpload = meantime * 60;
        stopanimation();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                isExtancier = false;
                meanTimeBar.setStyle("-fx-accent: blue;");
                updateProgress(firstTime, timeToUpload);
                for (int i = firstTime; i < timeToUpload; i++) {
                    updateProgress(i + 1, timeToUpload);
                    Thread.sleep(1000);
                    if (timeToUpload - (i + 30) < 1) {
                        meanTimeBar.setStyle("-fx-accent: red;");
                    }

                }
                pulseTransition.setCycleCount(PulseTransition.INDEFINITE);
                pulseTransition.play();
                return null;
            }
        };

        meanTimeBar.progressProperty().unbind();
        meanTimeBar.progressProperty().bind(task.progressProperty());
        thread = new Thread(task);
        thread.start();
    }

    /**
     * Sets the mean time bar if the top is for the {@link MainCar main car}
     */
    public void setMeanTimeBar() {

        decimalpart = getMeanTime(listOfMeanTime);
        intergerpart = (int) getMeanTime(listOfMeanTime);
        decimalpart = decimalpart - intergerpart;
        decimalpartTosecond = (int) (decimalpart * 60);
        timebar = LocalTime.of(0, intergerpart, decimalpartTosecond);
        labelMeanTime.setText(timebar.format(dtf));
    }

    /**
     * Checks if {@link com.kronos.model.TopModel top} for this car respects the job logic.
     * A "RACE" top should only happen after a "RACE" or an "OUT" top.
     * An "IN" top should only happen after a "RACE" or an "OUT" top.
     * An "OUT" top should only happen after an "IN" top.
     *
     * @param lastTopType     the last top type entered by the board man
     * @param previousTopType the previous top entered by the board man
     * @return true if the job logic is respected, false otherwise
     */
    public boolean checkTopLogic(String lastTopType, String previousTopType) {
        boolean respectsLogic = false;
        if (previousTopType.equals("R") && (lastTopType.equals("R") || lastTopType.equals("I"))) {
            respectsLogic = true;
        } else if (previousTopType.equals("I") && lastTopType.equals("O")) {
            respectsLogic = true;
        } else if (previousTopType.equals("O") && (lastTopType.equals("R") || lastTopType.equals("I"))) {
            respectsLogic = true;
        } else if (lastTopType == null) {
            respectsLogic = true;
        }
        return respectsLogic;
    }

    /**
     * Inits the {@link TopModel top} history.
     */
    private void initTable() {
        initCols();
    }

    /**
     * Inits the columbs of the {@link TopModel top} history.
     */
    private void initCols() {
        colCarNumber.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        col_typetop.setCellValueFactory(new PropertyValueFactory<>("topType"));
        col_racetime.setCellValueFactory(new PropertyValueFactory<>("raceTime"));
        col_laptime.setCellValueFactory(new PropertyValueFactory<>("lapTime"));
        colLapNumber.setCellValueFactory(new PropertyValueFactory<>("lap"));
        col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        col_comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        colTopPosition.setCellValueFactory(new PropertyValueFactory<>("topPosition"));
        col_time.setSortType(TableColumn.SortType.DESCENDING);
        colTopPosition.setSortType(TableColumn.SortType.DESCENDING);
        col_delete.setCellFactory(cellFactory);

        editableCols();
    }

    /**
     * Sets the {@link TopModel top} history to editable when the edit toggle is selected and to read only when unselected.
     */
    @FXML
    public void editable() {
        if (toogleedit.isSelected()) {
            table_info.setEditable(true);
            col_delete.setVisible(true);
        } else {
            table_info.setEditable(false);
            col_delete.setVisible(false);
        }
    }

    /**
     * Lists all the editable columns in the {@link TopModel top} history.
     * Sets factories to the cells to convert their values to the appropriate type.
     * Triggers the correct editing method when an edit action has been detected.
     */
    private void editableCols() {

        colCarNumber.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCarNumber.setOnEditCommit(this::editCarNumber);

        col_typetop.setCellFactory(TextFieldTableCell.forTableColumn());
        col_typetop.setOnEditCommit(this::editTopType);

        col_time.setCellFactory(TextFieldTableCell.forTableColumn());
        col_time.setOnEditCommit(this::editTopTime);

        col_laptime.setCellFactory(TextFieldTableCell.forTableColumn());
        col_laptime.setOnEditCommit(this::editLapTime);

        //We decided to let these columns are non editable for now, their edition makes it very difficult to correct potential mistakes of the board man
        //We preferred to keep robustness with a reliable correction.
        /*col_racetime.setCellFactory(TextFieldTableCell.forTableColumn());
        col_racetime.setOnEditCommit(this::editRaceTime);*/

        /*colLapNumber.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colLapNumber.setOnEditCommit(this::editLap);*/

        col_comment.setCellFactory(TextFieldTableCell.forTableColumn());
        col_comment.setOnEditCommit(this::editComment);

    }

    /**
     * Makes and controls the edition of a cell in the "Top type" column.
     *
     * @param event the {@link ActionEvent edit event}.
     */
    private void editTopType(TableColumn.CellEditEvent<TopModel, String> event) {
        int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = event.getTableView().getItems().get(row).getCarNumber();
        long topId = event.getTableView().getItems().get(row).getId();
        int index = findTopIndexWithId(carNumber, topId);
        String oldTopType = event.getOldValue();
        String newTopType = event.getNewValue();
        ArrayList<TopModel> oldTops = raceModel.getTopsMap().get(carNumber);
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        if (!oldTopType.equals(newTopType)) {
            if ((newTopType.equals("I") || newTopType.equals("O") || newTopType.equals("R"))) {
                topModels.get(index).setTopType(newTopType);
                if (!checkTopLogicOnEdit(carNumber, topId)) {
                    updateTopLogic(carNumber, findTopIndexWithId(carNumber, topId), false);
                }
                topModels = raceModel.getTopsMap().get(carNumber);
                if (raceModel instanceof LapRaceModel) {
                    recalculateLaps(topModels, carNumber);
                } else {
                    if (carNumber == numberMainCar) {
                        if (row != table_info.getItems().size() - 1 && !newTopType.equals("R")) {
                            numberOfLapsDone++;
                            incrementPanel(topModels.get(topModels.size() - 1).getLapTime());
                        } else if (row != table_info.getItems().size() - 1) {
                            numberOfLapsDone--;
                            decrementPanel(topModels.get(topModels.size() - 1).getLapTime());

                        }
                    }
                }
                topModels = raceModel.getTopsMap().get(carNumber);
                table_info.refresh();
                for(TopModel top : topModels) {
                    System.out.println(top.getTopType());
                }
                int i = 0;
                while(i < topModels.size()) {
                    App.getDataManager().persist(topModels.get(i));
                    i++;
                }
            } else {
                table_info.refresh();
                //Alerts.error("ERREUR", "Type de top invalide");
                Alerts.AlertError("ERREUR", "Type de top invalide");
            }
        }
    }

    /**
     * Makes and controls the edition of a cell in the "Car number" column.
     * (Especially usefull for the user to correct the car number if he made a top with the wrong car).
     *
     * @param event the {@link ActionEvent edit event}.
     */
    private void editCarNumber(TableColumn.CellEditEvent<TopModel, Integer> event) {
        try {
            int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
            int oldCarNumber = event.getOldValue();
            int newCarNumber = event.getNewValue();
            long topId = event.getTableView().getItems().get(row).getId();
            int index = findTopIndexWithId(oldCarNumber, topId);
            boolean carExists = carExists(newCarNumber);
            ArrayList<TopModel> oldCarTops = raceModel.getTopsMap().get(oldCarNumber);
            ArrayList<TopModel> topModels = raceModel.getTopsMap().get(newCarNumber);
            if (index > 0) {
                if (carExists) {
                    TopModel top = findTop(oldCarNumber, topId);
                    removeTop(oldCarNumber, topId);
                    top.setCarNumber(newCarNumber);
                    int newPos = findTopNewPositionOnCarNumberChange(topModels, top.getTime());
                    if (newPos < topModels.size()) {
                        topModels.add(newPos, top);
                    } else {
                        topModels.add(top);
                        top.setTopPosition(raceModel.getTopsMap().get(newCarNumber).size() -1);
                    }
                    recalculateLapTime(topModels);
                    updateTopLogic(newCarNumber, newPos, false);
                    table_info.refresh();
                    int i = 0;
                    while(i < topModels.size()) {
                        topModels.get(i).setTopPosition(i);
                        App.getDataManager().persist(topModels.get(i));
                        i++;
                    }
                    App.getDataManager().saveFile();
                } else {
                    table_info.refresh();
                    //Alerts.error("ERREUR", "Cette voiture n'existe pas");
                    Alerts.AlertError("ERREUR", "Cette voiture n'existe pas");
                }
            } else {
                table_info.refresh();
                //Alerts.error("ERREUR", "Top initial : modification numéro voiture impossible");
                Alerts.AlertError("ERREUR", "Top initial : modification numéro voiture impossible");

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            table_info.refresh();
            //Alerts.error("ERREUR", "Seuls les nombres entiers sont autorisés");
            Alerts.AlertError("ERREUR", "Seuls les nombres entiers sont autorisés");

        }

    }

    /**
     * Makes and controls the edition of a cell in the "Top time" column.
     *
     * @param event the {@link ActionEvent edit event}.
     */
    private void editTopTime(TableColumn.CellEditEvent<TopModel, String> event) {
        try {
            int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
            int carNumber = event.getTableView().getItems().get(row).getCarNumber();
            ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
            long topId = event.getTableView().getItems().get(row).getId();
            int index = findTopIndexWithId(carNumber, topId);
            String oldTopTime = event.getOldValue();
            String newTopTime = event.getNewValue();
            TopModel top = topModels.get(index);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (index > 0) {
                long newTopTimeMillis = df.parse(newTopTime).getTime();
                long raceStartTimeMillis = raceModel.getStartingTime().getTime();
                if (newTopTimeMillis < raceStartTimeMillis) {
                    top.setTime(df.format(raceStartTimeMillis));
                    top.setComment("-Heure du top doit être supérieure à heure de départ");
                } else if (newTopTimeMillis > System.currentTimeMillis()) {
                    top.setComment("-Heure du top doit être inférieure à heure actuelle");
                } else {
                    top.setTime(newTopTime);
                }
                int newPos = findTopNewPositionOnTopTimeChange(topModels, index, oldTopTime, newTopTime);
                if (newPos < topModels.size()) {
                    topModels.remove(index);
                    topModels.add(newPos, top);
                    updateTopLogic(carNumber, newPos, false);

                } else {
                    topModels.remove(index);
                    topModels.add(top);
                    updateTopLogic(carNumber, newPos, false);
                }

                top = topModels.get(newPos);
                recalculateLapTime(topModels);
                if (raceModel instanceof LapRaceModel) {
                    recalculateLaps(topModels, carNumber);
                } else {
                    recalculateRaceTime(topModels, newPos);
                }
                table_info.refresh();
                table_info.getSortOrder().add(col_time);
                table_info.sort();
                table_info.getSortOrder().remove(col_time);
                int i = 0;
                while(i < topModels.size()) {
                    topModels.get(i).setTopPosition(i);
                    App.getDataManager().persist(topModels.get(i));
                    i++;
                }
                App.getDataManager().saveFile();

            } else {
                table_info.refresh();
                //Alerts.error("ERROR", "Top initial: modification du temps du top impossible");
                Alerts.AlertError("ERROR", "Top initial: modification du temps du top impossible");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            table_info.refresh();
            //Alerts.error("ERREUR", "Format de date à respecter: jj-mm-aaaa hh:mm:ss");
            Alerts.AlertError("ERREUR", "Format de date à respecter: jj-mm-aaaa hh:mm:ss");
        }

    }

    /**
     * Makes and controls the edition of a cell in the "Lap Time" column.
     *
     * @param event the {@link ActionEvent edit event}.
     */
    private void editLapTime(TableColumn.CellEditEvent<TopModel, String> event) {
        try {
            int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
            int carNumber = event.getTableView().getItems().get(row).getCarNumber();
            ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
            long topId = event.getTableView().getItems().get(row).getId();
            int index = findTopIndexWithId(carNumber, topId);
            TopModel top = topModels.get(index);
            String oldLapTime = event.getOldValue();
            String newLapTime = event.getNewValue();
            String oldTopTime = top.getTime();
            SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat df2 = new SimpleDateFormat("mm:ss:SS");
            if (index > 0) {
                int i = index;
                while (i < topModels.size() && i != 0) {
                    recalculateTopTime(topModels, i, oldLapTime, newLapTime, false);
                    if (raceModel instanceof TimeRaceModel) {
                        recalculateRaceTime(topModels, i);
                    }
                    i++;
                }
                top = topModels.get(index);
                Calendar previousTopTimeCalendar = Calendar.getInstance();
                TopModel previousTop = topModels.get(index - 1);
                Calendar newLapTimeCalendar = Calendar.getInstance();
                previousTopTimeCalendar.setTime(df1.parse(previousTop.getTime()));
                newLapTimeCalendar.setTime(df2.parse(newLapTime));
                previousTopTimeCalendar.add(Calendar.HOUR_OF_DAY, newLapTimeCalendar.get(Calendar.HOUR_OF_DAY));
                previousTopTimeCalendar.add(Calendar.MINUTE, newLapTimeCalendar.get(Calendar.MINUTE));
                previousTopTimeCalendar.add(Calendar.SECOND, newLapTimeCalendar.get(Calendar.SECOND));
                previousTopTimeCalendar.add(Calendar.MILLISECOND, newLapTimeCalendar.get(Calendar.MILLISECOND));

                if (previousTopTimeCalendar.getTime().getTime() > System.currentTimeMillis()) {
                    recalculateLapTime(topModels);
                } else {
                    top.setLapTime(newLapTime);
                }
                table_info.refresh();
                i = 0;
                while(i < topModels.size()) {
                    App.getDataManager().persist(topModels.get(i));
                    i++;
                }
                App.getDataManager().saveFile();

            } else {
                table_info.refresh();
                //Alerts.error("ERREUR", "Top initial: temps au tour non modifiable");
                Alerts.AlertError("ERREUR", "Top initial: temps au tour non modifiable");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            table_info.refresh();
            //Alerts.error("ERREUR", "Format de temps à respecter: mm:ss:mm");
            Alerts.AlertError("ERREUR", "Format de temps à respecter: mm:ss:mm");
        }
    }

    /**
     * Edit the {@link TopModel top} comment.
     * @param event the {@link ActionEvent edit event}.
     */
    private void editComment(TableColumn.CellEditEvent<TopModel, String> event) {
        int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = event.getTableView().getItems().get(row).getCarNumber();
        long topId = event.getTableView().getItems().get(row).getId();
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        int index = findTopIndexWithId(carNumber, topId);
        String newComment = event.getNewValue();
        topModels.get(index).setComment(newComment);
        table_info.refresh();
        saveTopModel(topModels.get(index));
    }

    /**
     * UNUSED METHOD. NOT WORKING PROPERLY
     * Makes and controls the edition of a cell in the "Lap" column.
     * @param event the {@link ActionEvent edit event}.
     */
    /*private void editLap(TableColumn.CellEditEvent<TopModel, Integer> event) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = event.getTableView().getItems().get(row).getCarNumber();
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        Integer oldLap = event.getOldValue();
        Integer newLap = event.getNewValue();
        long topId = event.getTableView().getItems().get(row).getId();
        int index = findTopIndexWithId(carNumber, topId);
        TopModel top = topModels.get(index);
        top.setLap(newLap);
        int newPos = findTopNewPositionOnLapChange(topModels, index, oldLap, newLap);
        if (newPos < topModels.size() - 1) {
            topModels.remove(index);
            topModels.add(newPos, top);
            top = topModels.get(newPos);
            if (newPos == 0) {
                top.setTime(df.format(raceModel.getStartingTime()));
            } else {
                top.setTime(topModels.get(newPos + 1).getTime());
            }
            updateTopLogic(carNumber, newPos, false);
            recalculateLapTime(topModels);
        } else {
            topModels.add(top);
            if (newPos == 0) {
                top.setTime(df.format(raceModel.getStartingTime()));
            } else {
                top.setTime(df.format(System.currentTimeMillis()));
            }
            updateTopLogic(carNumber, newPos, false);
            recalculateLapTime(topModels);
        }
        colLapNumber.setSortType(TableColumn.SortType.DESCENDING);
        table_info.getSortOrder().remove(col_time);
        table_info.getSortOrder().add(colLapNumber);
        table_info.sort();
        table_info.refresh();
        table_info.getSortOrder().remove(colLapNumber);
        table_info.getSortOrder().add(col_time);
    }*/

    /**
     * UNUSED METHOD. NOT WORKING PROPERLY.
     * Makes and controls the edition of a cell in the "Race Time" (time elapsed since the beginning of the race) column.
     * @param event the {@link ActionEvent edit event}.
     */
    /*private void editRaceTime(TableColumn.CellEditEvent<TopModel, String> event) {
        int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = event.getTableView().getItems().get(row).getCarNumber();
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        long topId = event.getTableView().getItems().get(row).getId();
        int index = findTopIndexWithId(carNumber, topId);
        TopModel top = topModels.get(index);
        String oldRaceTime = event.getOldValue();
        String newRaceTime = event.getNewValue();
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        int newPos = 0;
        top.setRaceTime(newRaceTime);
        try {
            Date oldTopTimeMillis = df1.parse(top.getTime());
            Date oldRaceTimeMillis = df2.parse(oldRaceTime);
            Date newRaceTimeMillis = df2.parse(newRaceTime);
            long newTopTimeMillis = oldTopTimeMillis.getTime() + (newRaceTimeMillis.getTime() - oldRaceTimeMillis.getTime());
            String newTopTime = df1.format(newTopTimeMillis);
            String oldTopTime = top.getTime();
            if (newTopTimeMillis < raceModel.getStartingTime().getTime()) {
                top.setRaceTime("00:00:00");
                top.setTime(df1.format(raceModel.getStartingTime()));
                newPos = findTopNewPositionOnRaceTimeChange(topModels, index, oldRaceTime, "00:00:00");
            } else if (newTopTimeMillis > System.currentTimeMillis()) {
                long currentRaceTime = System.currentTimeMillis() - raceModel.getStartingTime().getTime();
                top.setRaceTime(df2.format(currentRaceTime));
                top.setTime(df1.format(System.currentTimeMillis()));
                newPos = findTopNewPositionOnRaceTimeChange(topModels, index, oldRaceTime, df2.format(currentRaceTime));
            } else {
                top.setRaceTime(newRaceTime);
                top.setTime(newTopTime);
                newPos = findTopNewPositionOnRaceTimeChange(topModels, index, oldRaceTime, newRaceTime);

            }
            if (newPos < topModels.size()) {
                topModels.remove(index);
                topModels.add(newPos, top);
                updateTopLogic(carNumber, newPos, false);

            } else {
                topModels.remove(index);
                topModels.add(top);
                updateTopLogic(carNumber, newPos, false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        col_racetime.setSortType(TableColumn.SortType.DESCENDING);
        table_info.getSortOrder().remove(col_time);
        table_info.getSortOrder().add(col_racetime);
        table_info.sort();
        table_info.refresh();
        table_info.getSortOrder().remove(col_racetime);
        table_info.getSortOrder().add(col_time);
        recalculateTopTime(topModels, newPos, oldRaceTime, newRaceTime, true);
        recalculateLapTime(topModels);
        table_info.refresh();
    }*/

    /**
     * Recalculates the date/time of the top.
     *
     * @param topModels   the {@link ArrayList list} containing the tops of the {@link CarModel car}.
     * @param index       the index of the top in the {@link ArrayList list}.
     * @param oldTime     the old lap time (or race time) made by the {@link CarModel car} (before edition).
     * @param newTime     the new lap time (or race time) made by the {@link CarModel car} (after edition).
     * @param useRaceTime when the edited column is the "Race Time" column, this boolean is set to true (currently always set to false as this column has been set back to non editable).
     */
    private void recalculateTopTime(ArrayList<TopModel> topModels, int index, String oldTime, String newTime, boolean useRaceTime) {
        SimpleDateFormat df1 = null;
        if (useRaceTime) {
            df1 = new SimpleDateFormat("HH:mm:ss");
        } else {
            df1 = new SimpleDateFormat("mm:ss:SS");
        }
        try {
            Calendar baseCalendar = Calendar.getInstance();
            Calendar newTimeCalendar = Calendar.getInstance();
            Calendar oldTimeCalendar = Calendar.getInstance();
            Date currentTime = df1.parse(newTime);
            Date formerTime = df1.parse(oldTime);
            SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date topTime = df2.parse(topModels.get(index).getTime());
            long newTopTimeInMillis = 0;
            if (useRaceTime) {
                baseCalendar.setTime(raceModel.getStartingTime());
                newTimeCalendar.setTime(currentTime);
                baseCalendar.add(Calendar.HOUR_OF_DAY, newTimeCalendar.get(Calendar.HOUR_OF_DAY));
                baseCalendar.add(Calendar.MINUTE, newTimeCalendar.get(Calendar.MINUTE));
                baseCalendar.add(Calendar.SECOND, newTimeCalendar.get(Calendar.SECOND));
            } else {
                baseCalendar.setTime(topTime);
                newTimeCalendar.setTime(currentTime);
                oldTimeCalendar.setTime(formerTime);
                if (newTimeCalendar.getTime().after(oldTimeCalendar.getTime())) {
                    newTimeCalendar.add(Calendar.MINUTE, -oldTimeCalendar.get(Calendar.MINUTE));
                    newTimeCalendar.add(Calendar.SECOND, -oldTimeCalendar.get(Calendar.SECOND));
                    newTimeCalendar.add(Calendar.MILLISECOND, -oldTimeCalendar.get(Calendar.MILLISECOND));
                    baseCalendar.add(Calendar.MINUTE, newTimeCalendar.get(Calendar.MINUTE));
                    baseCalendar.add(Calendar.SECOND, newTimeCalendar.get(Calendar.SECOND));
                    baseCalendar.add(Calendar.MILLISECOND, newTimeCalendar.get(Calendar.MILLISECOND));
                } else if (newTimeCalendar.getTime().before(oldTimeCalendar.getTime())) {
                    oldTimeCalendar.add(Calendar.MINUTE, -newTimeCalendar.get(Calendar.MINUTE));
                    oldTimeCalendar.add(Calendar.SECOND, -newTimeCalendar.get(Calendar.SECOND));
                    oldTimeCalendar.add(Calendar.MILLISECOND, -newTimeCalendar.get(Calendar.MILLISECOND));
                    baseCalendar.add(Calendar.MINUTE, -oldTimeCalendar.get(Calendar.MINUTE));
                    baseCalendar.add(Calendar.SECOND, -oldTimeCalendar.get(Calendar.SECOND));
                    baseCalendar.add(Calendar.MILLISECOND, -oldTimeCalendar.get(Calendar.MILLISECOND));
                }
            }
            if (baseCalendar.getTime().getTime() > System.currentTimeMillis()) {
                topModels.get(index).setTime(df2.format(System.currentTimeMillis()));
            } else {
                topModels.get(index).setTime(df2.format(baseCalendar.getTime()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recalculates the race time associated with the tops.
     *
     * @param topModels the {@link ArrayList list of tops} made by the {@link CarModel car}.
     * @param index the index of the top in the {@link ArrayList list of tops}.
     */
    private void recalculateRaceTime(ArrayList<TopModel> topModels, int index) {
        String raceTime = "";
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date currentTopTime = df1.parse(topModels.get(index).getTime());
            Date raceStartTime = raceModel.getStartingTime();
            long newRaceTimeInMillis = currentTopTime.getTime() - raceStartTime.getTime();
            String sec = Integer.toString((int) (newRaceTimeInMillis / 1000) % 60);
            String min = Integer.toString((int) ((newRaceTimeInMillis / (1000 * 60)) % 60));
            String hr = Integer.toString((int) ((newRaceTimeInMillis / (1000 * 60 * 60)) % 24));
            if (Integer.parseInt(sec) < 10) {
                sec = "0" + sec;
            }
            if (Integer.parseInt(min) < 10) {
                min = "0" + min;
            }
            if (Integer.parseInt(hr) < 10) {
                hr = "0" + hr;
            }
            raceTime = hr + ":" + min + ":" + sec;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        topModels.get(index).setRaceTime(raceTime);
    }

    /**
     * Recalculates the lap times associated with the tops.
     * @param topModels the {@link ArrayList list of tops} associated of the {@link CarModel car}.
     */
    private void recalculateLapTime(ArrayList<TopModel> topModels) {
        String newLapTime = "00:00:00";
        int index = 0;
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        while (index < topModels.size()) {
            try {
                Date previousTopTime;
                Date currentTopTime = df1.parse(topModels.get(index).getTime());
                if (index > 0) {
                    previousTopTime = df1.parse(topModels.get(index - 1).getTime());
                    long newLapTimeMillis = currentTopTime.getTime() - previousTopTime.getTime();

                    String ms = Integer.toString((int) newLapTimeMillis % 1000);
                    String sec = Integer.toString((int) (newLapTimeMillis / 1000) % 60);
                    String min = Integer.toString((int) ((newLapTimeMillis / (1000 * 60)) % 60));
                    if (Integer.parseInt(sec) < 10) {
                        sec = "0" + sec;
                    }
                    if (Integer.parseInt(min) < 10) {
                        min = "0" + min;
                    }
                    if (Integer.parseInt(ms) < 10) {
                        ms = "0" + ms;
                    }
                    newLapTime = min + ":" + sec + ":" + ms;
                    topModels.get(index).setLapTime(newLapTime);

                } else if (!topModels.get(index).getLapTime().equals("00:00:00")) {
                    topModels.get(index).setLapTime("00:00:00");
                    topModels.get(index).setComment("-First Top O time is always 00:00:00");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            index++;
        }
    }

    /**
     * Recalculates the laps associated with the tops.
     *
     * @param topModels the {@link ArrayList list of tops} of the {@link CarModel car}.
     * @param carNumber the number of the {@link CarModel car}.
     */
    private void recalculateLaps(ArrayList<TopModel> topModels, int carNumber) {
        int i = 0;
        int numTour = 0;
        int oldLap = topModels.get(topModels.size() - 1).getLap();
        if (raceModel instanceof LapRace) {
            while (i < topModels.size()) {
                if (i > 0 && topModels.get(i).getTopType().equals("I")) {
                    topModels.get(i).setLap(numTour);
                } else if (i > 0) {
                    numTour++;
                    topModels.get(i).setLap(numTour);
                }
                i++;
            }
            if (carNumber == numberMainCar) {
                int newLap = topModels.get(topModels.size() - 1).getLap();
                numberOfLapsDone = topModels.get(topModels.size() - 1).getLap();
                spentTime.setText(Integer.toString(numberOfLapsDone));
                remainingLaps = ((LapRace) raceModel).getNumberOfLaps() - numberOfLapsDone;
                remainingTime.setText(Integer.toString(remainingLaps));
                if (oldLap < newLap) {
                    decrementPanel(topModels.get(topModels.size() - 1).getLapTime());
                } else {
                    incrementPanel(topModels.get(topModels.size() - 1).getLapTime());
                }
            }
        }

    }

    /**
     * Updates the top logic (ORRIORR...). Browse all tops in both directions from a given index and corrects all mistakes.
     *
     * @param carNumber the {@link CarModel car} number.
     * @param origin    the {@link TopModel top} index from where the correction will start.
     * @param onRemove  if the update is done after a top removal, this boolean is set to true.
     */
    private void updateTopLogic(int carNumber, int origin, boolean onRemove) {
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        int index = origin;
        if (origin > 1) {
            while (index > 1) {
                TopModel currentTop = topModels.get(index);
                TopModel previousTop = topModels.get(index - 1);
                if (currentTop.getTopType().equals("I") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    previousTop.setTopType("R");
                    previousTop.setComment("-Top R système-" + previousTop.getComment());
                } else if (currentTop.getTopType().equals("O") && !previousTop.getTopType().equals("I")) {
                    previousTop.setTopType("I");
                    previousTop.setComment("-Top I système-" + previousTop.getComment());
                    if (raceModel instanceof LapRace) {
                        previousTop.setLap(currentTop.getLap() - 1);
                    }
                } else if (currentTop.getTopType().equals("R") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    previousTop.setTopType("R");
                    previousTop.setComment("-Top R système-" + previousTop.getComment());
                }
                index--;
            }
        } else {
            if (origin == 0) {
                topModels.get(0).setTopType("O");
                if (!onRemove) {
                    topModels.get(0).setComment("-Top O système" + topModels.get(0).getComment());
                }
            } else {
                if (!topModels.get(1).getTopType().equals("I") && !topModels.get(1).getTopType().equals("R")) {
                    topModels.get(1).setTopType("R");
                    topModels.get(1).setComment("-Top R système" + topModels.get(1).getComment());
                }
            }
        }
        index = origin + 1;
        if (origin < topModels.size() - 1) {
            while (index < topModels.size()) {
                TopModel currentTop = topModels.get(index);
                TopModel previousTop = topModels.get(index - 1);
                if (currentTop.getTopType().equals("I") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    currentTop.setTopType("O");
                    currentTop.setComment("-Top O système-" + currentTop.getComment());
                } else if (currentTop.getTopType().equals("O") && !previousTop.getTopType().equals("I")) {
                    currentTop.setTopType("R");
                    currentTop.setComment("-Top R système-" + currentTop.getComment());
                } else if (currentTop.getTopType().equals("R") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    currentTop.setTopType("O");
                    currentTop.setComment("-Top O système-" + currentTop.getComment());
                }
                index++;
            }
        }
    }

    /**
     * Used to check the top logic when the "Top type" column has been edited. Does only check the logic, does not correct anything.
     *
     * @param carNumber the {@link CarModel car number}.
     * @param lastTopId the id of the last {@link TopModel top}.
     * @return true if the logic is respected, false otherwise.
     */
    private boolean checkTopLogicOnEdit(int carNumber, long lastTopId) {
        boolean found = false;
        boolean respectsLogic = false;
        int index = findTopIndexWithId(carNumber, lastTopId);
        ArrayList<TopModel> tops = raceModel.getTopsMap().get(carNumber);
        if (index > 0) {
            respectsLogic = checkTopLogic(tops.get(index).getTopType(), tops.get(index - 1).getTopType());
            if (respectsLogic && index < tops.size() - 1) {
                respectsLogic = checkTopLogic(tops.get(index + 1).getTopType(), tops.get(index).getTopType());
            }
        }
        return respectsLogic;
    }

    /**
     * Removes a {@link TopModel top} from the top storage data structure.
     *
     * @param carNumber the {@link CarModel car} number.
     * @param topId     the {@link TopModel top} id.
     */
    private void removeTop(int carNumber, long topId) {
        if(raceModel.getRaceState().equals(RaceState.IN_PROGRESS)) {
            ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
            int index = findTopIndexWithId(carNumber, topId);
            if (index != 0) {
                TopModel currentTop = topModels.get(index);
                CarModel carModel = carController.findCar(carModels, carNumber);
                carModel.getTopList().remove(currentTop);
                topModels.remove(currentTop);
                updateTopLogic(carNumber, 0, true);
                recalculateLapTime(topModels);
                if (raceModel instanceof LapRace) {
                    recalculateLaps(topModels, carNumber);
                } else {
                    if (carModel instanceof MainCarModel) {
                        if (!currentTop.getTopType().equals("I")) {
                            numberOfLapsDone--;
                            incrementPanel(topModels.get(topModels.size() - 1).getLapTime());
                        }
                    }
                }
                IncrementalSaveStrategy.removeTop(currentTop);
                App.getDataManager().delete(currentTop, Long.toString(topId));
                int i = 0;
                while(i < topModels.size()) {
                    topModels.get(i).setTopPosition(i);
                    App.getDataManager().persist(topModels.get(i));
                    i++;
                }
                App.getDataManager().saveFile();
                table_info.refresh();
            } else {
                table_info.refresh();
                //Alerts.error("ERREUR", "Impossible de supprimer le top initial");
                Alerts.AlertError("ERREUR", "Impossible de supprimer le top initial");
            }
        }
        else {
            //Alerts.error("ERREUR", "Impossible de supprimer un top une fois la course terminée");
            Alerts.AlertError("ERREUR", "Impossible de supprimer un top une fois la course terminée");
        }
    }

    /**
     * Finds a {@link TopModel top} with its id.
     *
     * @param carNumber the {@link CarModel car} number.
     * @param topId     the {@link TopModel top} id.
     * @return the found {@link TopModel top}, null if no {@link TopModel top} has been found.
     */
    private TopModel findTop(int carNumber, long topId) {
        boolean found = false;
        TopModel topModel = null;
        ArrayList<TopModel> oldCarTops = raceModel.getTopsMap().get(carNumber);
        Iterator<TopModel> it = oldCarTops.iterator();
        while (it.hasNext() && !found) {
            TopModel top = it.next();
            if (top.getId() == topId) {
                found = true;
                topModel = top;
            }
        }
        return topModel;
    }

    /**
     * Finds the {@link TopModel top} index with its id.
     *
     * @param carNumber the {@link CarModel car} number.
     * @param topId     the {@link TopModel top} id.
     * @return the {@link TopModel top} index.
     */
    private int findTopIndexWithId(int carNumber, long topId) {
        boolean found = false;
        int index = 0;
        ArrayList<TopModel> newCarTops = raceModel.getTopsMap().get(carNumber);
        if (newCarTops != null) {
            Iterator<TopModel> it = newCarTops.iterator();
            while (it.hasNext() && !found) {
                TopModel top = it.next();
                if (top.getId() == topId) {
                    found = true;
                } else {
                    index++;
                }
            }
        }
        return index;
    }

    /**
     * Finds the {@link TopModel top} new position in the data structure when the {@link TopModel top} time has changed.
     *
     * @param topModels the {@link TopModel tops} of the car.
     * @param index     the former {@link TopModel top} index.
     * @param oldTime   the old {@link TopModel top} time.
     * @param newTime   the new {@link TopModel top} time.
     * @return the new index of the {@link TopModel top} in the data structure.
     */
    private int findTopNewPositionOnTopTimeChange(ArrayList<TopModel> topModels, int index, String oldTime, String newTime) {
        int i = index;
        boolean found = false;
        if (LocalDateTime.parse(oldTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(newTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
            TopModel newTop = topModels.get(index);
            i++;
            while (i < topModels.size() && !found) {
                if (LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(newTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))) || LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                    found = true;
                } else {
                    i++;
                }
            }
            i--;
        } else if (LocalDateTime.parse(oldTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isAfter(LocalDateTime.parse(newTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
            TopModel newTop = topModels.get(index);
            i--;
            while (i >= 0 && !found) {
                if ((LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isAfter(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) || LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                    found = true;
                } else {
                    i--;
                }
            }
            i++;
        }
        return i;
    }

    /**
     * Finds the {@link TopModel top} new position in the data structure when the {@link CarModel car} number has changed.
     * @param topModels the {@link ArrayList list} of {@link TopModel tops}.
     * @param topTime   the {@link TopModel top} time.
     * @return the {@link TopModel top} new position in the data structure.
     */
    private int findTopNewPositionOnCarNumberChange(ArrayList<TopModel> topModels, String topTime) {
        boolean found = false;
        int i = 0;
        while (i < topModels.size() && !found) {
            if (LocalDateTime.parse(topTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))) || LocalDateTime.parse(topTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                found = true;
            } else {
                i++;
            }
        }
        return i;
    }

    /**
     * UNUSED METHOD. NOT WORKING PROPERLY.
     * Finds the {@link TopModel top} new position in the data structure when the race time has changed.
     * @param topModels the {@link TopModel tops} of the car.
     * @param index the former {@link TopModel top} index.
     * @param oldTime the old race time.
     * @param newTime the new race time.
     * @return the new index of the {@link TopModel top} in the data structure.
     */
    /*private int findTopNewPositionOnRaceTimeChange(ArrayList<TopModel> topModels, int index, String oldTime, String newTime) {
        int i = index;
        boolean found = false;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        try {
            if (df.parse(oldTime).before(df.parse(newTime))) {
                TopModel newTop = topModels.get(index);
                i++;
                while (i < topModels.size() && !found) {
                    if (df.parse(newTop.getRaceTime()).before(df.parse(topModels.get(i).getRaceTime())) || df.parse(newTop.getRaceTime()).equals(df.parse(topModels.get(i).getRaceTime()))) {
                        found = true;
                    } else {
                        i++;
                    }
                }
                i--;
            } else if (df.parse(oldTime).after(df.parse(newTime))) {
                TopModel newTop = topModels.get(index);
                i--;
                while (i >= 0 && !found) {
                    if (df.parse(newTop.getRaceTime()).after(df.parse(topModels.get(i).getRaceTime())) || df.parse(newTop.getRaceTime()).equals(df.parse(topModels.get(i).getRaceTime()))) {
                        found = true;
                    } else {
                        i--;
                    }
                }
                i++;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }*/

    /**
     * UNUSED METHOD. NOT WORKING PROPERLY.
     * Finds the {@link TopModel top} new position in the data structure when lap has changed.
     * @param topModels the lap associated with the {@link TopModel top}.
     * @param index the former {@link TopModel top} index.
     * @param oldLap the old lap.
     * @param newLap the new lap.
     * @return the new index of the {@link TopModel top} in the data structure.
     */
    /*private int findTopNewPositionOnLapChange(ArrayList<TopModel> topModels, int index, int oldLap, int newLap) {
        int i = index;
        boolean found = false;
        if (newLap == 0) {
            i = 1;
        } else {
            if (oldLap < newLap) {
                i++;
                while (i < topModels.size() && !found) {
                    if (newLap < topModels.get(i).getLap() || newLap == topModels.get(i).getLap()) {
                        found = true;
                    } else {
                        i++;
                    }
                }
                i--;
            } else if (oldLap > newLap) {
                i--;
                while (i >= 0 && !found) {
                    if (newLap > topModels.get(i).getLap()) {
                        found = true;
                    } else {
                        i--;
                    }
                }
                i++;
            }
        }
        return i;
    }*/

    /**
     * Checks if the {@link CarModel car} exists.
     *
     * @param carNumber {@link CarModel car} car number.
     * @return true if the car exists, false otherwise.
     */
    private boolean carExists(int carNumber) {
        boolean exists = false;
        Set<Integer> keys = raceModel.getTopsMap().keySet();
        if (keys.contains(carNumber)) {
            exists = true;
        }
        return exists;
    }

    /**
     * Stops the pulse animation on the progressbar.
     */
    public void stopanimation() {
        pulseTransition.stop();
    }

    /**
     * Get's the mean time of the {@link MainCar main car} to upload on the progressbar.
     * In nominal cases, makes a mean with the last three lap times.
     * If there are less than three lap times to use, uses all remaining lap times available.
     *
     * @param mylistoftime list of times used to calculate the average time.
     * @return meanTime the average time.
     */
    public double getMeanTime(ArrayList<Double> mylistoftime) {
        double meantimeaux = 0.00;
        if (mylistoftime.size() == 0) {
            LocalTime tempsmoyen1 = LocalTime.now();
            double mytime = (tempsmoyen1.getHour() * 60) + tempsmoyen1.getMinute() + (tempsmoyen1.getSecond() / 60.0);
            double departtime = (departureTime.getHour() * 60) + departureTime.getMinute() + (departureTime.getSecond() / 60.0);
            meantimeaux = mytime - departtime;
        } else if (mylistoftime.size() == 1) {
            meantimeaux = mylistoftime.get(0);
        } else if (mylistoftime.size() == 2) {
            meantimeaux = ((mylistoftime.get(0) + mylistoftime.get(1)) / 2.0);
        } else {
            int start = mylistoftime.size() - 2;
            meantimeaux = ((mylistoftime.get(start - 1) + mylistoftime.get(start) + mylistoftime.get(start + 1)) / 3.0);


        }

        return meantimeaux;
    }

    /**
     * Calculates the average lap time of the {@link com.kronos.api.RivalCar rival cars} to make the rankings.
     *
     * @return HashMap<Long, Double> a {@link HashMap map} with the {@link CarModel car} number in key and its average lap time in value.
     */
    public HashMap<Integer, Double> getMeanTimeForFollowedCars() {

        HashMap<Integer, Double> follewedCarsMeantime = new HashMap<>();
        Double meanTime = 0.0;
        for (Map.Entry listOfRivalMeantime : rivalCarListOfMeantime.entrySet()) {
            ArrayList<Double> listMeantimeRicval = (ArrayList<Double>) listOfRivalMeantime.getValue();

            if (listMeantimeRicval.size() == 1) {
                meanTime = listMeantimeRicval.get(0);
            } else if (listMeantimeRicval.size() == 2) {
                meanTime = ((listMeantimeRicval.get(0) + listMeantimeRicval.get(1)) / 2.0);
            } else {
                int lengthOf = listMeantimeRicval.size();
                meanTime = ((listMeantimeRicval.get(lengthOf - 1) + listMeantimeRicval.get(lengthOf - 2) + listMeantimeRicval.get(lengthOf - 3)) / 3.0);
            }
            follewedCarsMeantime.put((Integer) listOfRivalMeantime.getKey(), meanTime);
        }
        return follewedCarsMeantime;
    }

    /**
     * Makes the ranking of the race which will then be displayed on the UI.
     */
    public void displayNewRank() {
        List<MainCarModel> maincar = (List<MainCarModel>) (List<?>) App.getDataManager().getModels(MainCarModel.class);
        listPastRank.getItems().clear();
        listPastRank.getItems().addAll(listNowRank.getItems());
        listNowRank.getItems().clear();

        HashMap<Integer, Double> rivalCarsMeanTime = getMeanTimeForFollowedCars();
        Double mainCarMeanTime = getMeanTime(listOfMeanTime);
        rivalCarsMeanTime.put(maincar.get(0).getNumber(), mainCarMeanTime);
        List<Map.Entry<Integer, Double>> list =
                new LinkedList<>(rivalCarsMeanTime.entrySet());
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

        HashMap<Integer, Double> map_apres = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : list) {
            map_apres.put(entry.getKey(), entry.getValue());
            listNowRank.getItems().add(entry.getKey());
        }


    }

    /**
     * Increment the clock  of the spent time when it's a {@link TimeRace time race}.
     */
    private void incrementTime() {
        localSpentTime = localSpentTime.plusSeconds(1);
        spentTime.setText(localSpentTime.format(dtf));
    }

    /**
     * Decrements the remaining clock when it's a {@link TimeRace time race}.
     */
    private void decrementTime() {
        localRemainningTime = localRemainningTime.minusSeconds(1);
        remainingTime.setText(localRemainningTime.format(dtf));
        if (localRemainningTime.equals(LocalTime.parse("00:00:00"))) {
            localRemainningTime = LocalTime.parse("00:00:00");
            remainingTime.setText(localRemainningTime.format(dtf));
            setRaceInformations(RaceState.DONE);
            if (raceModel instanceof TimeRace) {
                endOfTimeRace();
            }

        }

    }

    /**
     * Gets the current time.
     */
    private void getCurrentTime() {
        currentTime = LocalTime.now();
        currentHour.setText(currentTime.format(dtf));
    }

    /**
     * Starts the race timer when the user clicks on the start button.
     *
     * @param event the {@link ActionEvent button click event}.
     */
    @FXML
    private void startTimer(ActionEvent event) {
        if (!raceModel.getRaceState().equals(RaceState.DONE)) {
            istartRace = true;
            startRace.setDisable(true);
            if (raceModel instanceof TimeRace) {
                if (!localRemainningTime.equals(LocalTime.parse("00:00:00"))) {
                    departureTime = LocalTime.now();
                    LocalDateTime localDateTime = LocalDateTime.now();
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    raceModel.setStartingTime(date);
                    spentTimeline.play();
                    remainingTimeline.play();
                    departureHour.setText(currentTime.format(dtf));
                }
            } else {
                departureTime = LocalTime.now();
                LocalDateTime localDateTime = LocalDateTime.now();
                Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                raceModel.setStartingTime(date);
                departureHour.setText(departureTime.format(dtf));
                startRace.setDisable(true);
            }
            if (raceModel.getRaceState().equals(RaceState.BREAK)) {
                handleNewTop();
                if (getFollowedCars().size() >= 2)
                    startTimerForRivalCar();
                setRaceInformations(RaceState.IN_PROGRESS);
            } else {
                handleNewTop();
                if (getFollowedCars().size() >= 2) {
                    handleFirstTopRivalCars();
                    startTimerForRivalCar();
                }
                setRaceInformations(RaceState.IN_PROGRESS);
            }
            topType.setDisable(false);
        } else {
            //Alerts.info("INFORMATION", "Cette course est terminée");
            Alerts.AlertWarning("INFORMATION", "Cette course est terminée");
        }
    }

    /**
     * Manages the state of the race when the pause button is clicked.
     *
     * @param event the {@link ActionEvent button click event}.
     * @throws InterruptedException thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    @FXML
    private void pauseTimer(ActionEvent event) throws InterruptedException {
        if (istartRace) {
            if (raceModel instanceof TimeRace) {
                if (spentTimeline.getStatus().equals(Animation.Status.PAUSED)) {
                    spentTimeline.play();
                    remainingTimeline.play();
                    pauseRace.setText("Pause");
                    wakeUpThread();
                    setRaceInformations(RaceState.IN_PROGRESS);


                } else if (spentTimeline.getStatus().equals(Animation.Status.RUNNING)) {
                    spentTimeline.pause();
                    remainingTimeline.pause();
                    pauseRace.setText("Continue");
                    breakTimer();
                    setRaceInformations(RaceState.BREAK);

                }
            } else {
                if (raceModel.getRaceState().equals(RaceState.IN_PROGRESS)) {
                    pauseRace.setText("Continue");
                    breakTimer();
                    setRaceInformations(RaceState.BREAK);


                } else if (raceModel.getRaceState().equals(RaceState.BREAK)) {
                    pauseRace.setText("Pause");
                    wakeUpThread();
                    setRaceInformations(RaceState.IN_PROGRESS);


                }
            }
        } else {
            //Alerts.info("INFORMATION", "Aucune course n'a été demarrée");
            Alerts.AlertWarning("INFORMATION", "Aucune course n'a été demarrée");
        }
    }

    /**
     * Stops the timer and sets the correct information on the UI when the {@link RaceModel race} ends.
     *
     * @param event the {@link ActionEvent button click} event.
     */
    @FXML
    private void endTimer(ActionEvent event) {
        //Alerts.warning("Avertissement", "vouliez vous mettre fin a cette course");
            if (startRace.isDisable()) {
                Optional<ButtonType> confirmation = Alerts.AlertConfirmation("AVERTISSEMENT", "Voulez vous mettre fin a cette course");
                if(confirmation.get() == ButtonType.OK) {
                    setRaceInformations(RaceState.DONE);
                    if (raceModel instanceof TimeRace) {
                        endOfTimeRace();
                    } else {
                        stopRace.setDisable(true);
                        pauseRace.setDisable(true);
                        startRace.setDisable(true);
                        spentTime.setText(String.valueOf(numberOfLapsDone));
                        remainingTime.setText(String.valueOf(remainingLaps));
                        endAllThread();
                    }
                }

            } else {
                //Alerts.info("INFORMATION", "aucune course n'a été demarrer");
                Alerts.AlertWarning("INFORMATION", "Aucune course n'a été demarrée");
            }

    }

    /**
     * Sets the UI information correctly when it's the end of a {@link TimeRace time race}.
     */
    public void endOfTimeRace() {
        spentTimeline.stop();
        remainingTimeline.stop();
        startRace.setDisable(true);
        pauseRace.setDisable(true);
        stopRace.setDisable(true);
        spentTime.setText(localSpentTime.format(dtf));
        remainingTime.setText(localSpentTime.format(dtf));
        endAllThread();
    }

    /**
     * Sets the information about the {@link RaceModel race}.
     *
     * @param raceState the {@link RaceModel race} state.
     */
    public void setRaceInformations(RaceState raceState) {
        raceModel.setRaceState(raceState);
        raceModel.setTimeLapsRemaining(remainingTime.getText());
        raceModel.setTimeLapsSpent(spentTime.getText());

        App.getDataManager().persist(raceModel);
        App.getDataManager().saveFile();

    }

    /**
     * Checks if we should end the race.
     */
    public void checkEndOfRace() {

        if (raceModel instanceof TimeRace) {
            if (localRemainningTime.equals(LocalTime.parse("00:00:00"))) {
                setRaceInformations(RaceState.DONE);
                endOfTimeRace();

            }
            setRaceInformations(RaceState.IN_PROGRESS);
        } else {

            if (remainingLaps == 0) {
                remainingTime.setText(String.valueOf(remainingLaps));
                spentTime.setText(String.valueOf(numberOfLapsDone));
                setRaceInformations(RaceState.DONE);
                endAllThread();
                //Alerts.info("Information", "la course est terminée");
                Alerts.AlertWarning("Information", "la course est terminée");


            } else {

                remainingTime.setText(String.valueOf(remainingLaps));
                spentTime.setText(String.valueOf(numberOfLapsDone));
                setRaceInformations(RaceState.IN_PROGRESS);
            }
        }
    }

    /**
     * Displays main car information and the current pilot.
     * pilot information
     * first name
     * last name
     * birthday
     * <p>
     * car information
     * car model
     * car Brand
     * car Team
     */
    public void maincarinformation() {


        List<MainCarModel> maincarinformation = (List<MainCarModel>) (List<?>) App.getDataManager().getModels(MainCarModel.class);
        for (MainCarModel model : maincarinformation) {
            mycar = model;
        }
        if (mycar != null) {
            lastNamePilotMainCar.setText(mycar.getPilotModel().getLastName());
            firstNamePilotMainCar.setText(mycar.getPilotModel().getFirstName());
            if (!(mycar.getPilotModel().getDateOfBirth() == null)) {
                dateOfBirthPilot.setText(new SimpleDateFormat("dd-MM-yyyy").format(mycar.getPilotModel().getDateOfBirth()));
            }
            mainCarBrand.setText(mycar.getBrand());
            mainCarModel.setText(mycar.getModel());
            mainCarTeam.setText(mycar.getTeam());
        }

    }

    /**
     * Gets the followed {@link CarModel cars} from the model.
     *
     * @return the {@link ArrayList list} of the followed {@link CarModel cars}.
     */
    public ArrayList<CarModel> getFollowedCars() {
        ArrayList<CarModel> followedCars = new ArrayList<>();
        List<CarModel> carModels = (List<CarModel>) (List<?>) App.getDataManager().getModels(CarModel.class);
        for (CarModel carModel : carModels) {
            followedCars.add(carModel);
        }
        return followedCars;
    }

    /**
     * Gets the {@link RaceModel race} from the model.
     *
     * @return the {@link RaceModel race}.
     */
    public ArrayList<RaceModel> getRace() {
        ArrayList<RaceModel> raceModels = new ArrayList<>();
        List<TimeRaceModel> timeRaceModels = (List<TimeRaceModel>) (List<?>) App.getDataManager().getModels(TimeRaceModel.class);
        List<LapRaceModel> lapRaceModels = (List<LapRaceModel>) (List<?>) App.getDataManager().getModels(LapRaceModel.class);
        if (!timeRaceModels.isEmpty()) {
            for (TimeRaceModel model : timeRaceModels) {
                raceModels.add(model);
            }
        } else {
            for (LapRaceModel model : lapRaceModels) {
                raceModels.add(model);
            }
        }
        return raceModels;
    }

    /**
     * Gets the followed {@link CarModel car} numbers from our {@link ArrayList list} of {@link CarModel cars}.
     *
     * @param followedCars the {@link ArrayList list} of {@link CarModel cars}.
     * @return the {@link ArrayList list} of {@link CarModel car} numbers.
     */
    public ArrayList<String> getFollowedCarsNumbers(ArrayList<CarModel> followedCars) {
        ArrayList<String> followedCarsNumbers = new ArrayList<>();
        for (CarModel followedCar : followedCars) {
            followedCarsNumbers.add(Integer.toString(followedCar.getNumber()));
        }
        return followedCarsNumbers;
    }

    /**
     * Finds the previous {@link TopModel top} in the data structure.
     *
     * @param carNumber the {@link CarModel car} number associated with this top.
     * @return the previous {@link TopModel top}.
     */
    public TopModel findPreviousTop(int carNumber) {
        TopModel topModel = null;
        if (raceModel.getTopsMap() != null && raceModel.getTopsMap().containsKey(carNumber)) {
            ArrayList<TopModel> topModels;
            topModels = raceModel.getTopsMap().get(carNumber);
            if (!topModels.isEmpty()) {
                topModel = topModels.get(topModels.size() - 1);
            }
        }
        return topModel;
    }

    /**
     * Resets the timer when a {@link TopModel top} has been done on the {@link MainCar main car}.
     */
    public void resetTimerBar() {
        millisecondes = 0;
        munites = 0;
        secondes = 0;
        hours = 0;
        chronoTime = LocalTime.of(0, munites, secondes, millisecondes);
        chronoTopTime.setText(chronoTime.format(dtf2));
        isSetTimerBar = true;
        tmierSign.setVisible(false);
    }

    /**
     * Starts the timer for the next {@link TopModel top} of the {@link MainCar main car}.
     * Time format is MM:ss:nn.
     */
    public void startTimerBar() {
        isStartTimer = true;
        threadChrono = new Thread(() -> {
            while (isStartTimer) {

                try {
                    Thread.sleep(10);
                    millisecondes++;
                    if (millisecondes == 95) {
                        secondes++;

                        if (!(timebar.equals(timeTocompare)) && isSetTimerBar) {
                            timebar = timebar.minusSeconds(1);
                        } else {
                            isSetTimerBar = false;
                            timebar = timebar.plusSeconds(1);
                        }
                        millisecondes = 0;
                    }
                    if (secondes == 60) {
                        munites++;
                        secondes = 0;
                    }
                    if (munites == 60) {
                        hours++;
                        munites = 0;
                    }
                    Platform.runLater(() -> {
                        chronoTime = LocalTime.of(hours, munites, secondes, millisecondes);
                        chronoTopTime.setText(chronoTime.format(dtf2));
                        labelMeanTime.setText(timebar.format(dtf));
                        if (hours > 0)
                            chronoTopTime.setText(chronoTime.format(dtf1));
                        if (!isSetTimerBar) {
                            tmierSign.setVisible(true);
                        }
                    });


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        });
        threadChrono.start();
    }

    /**
     * Starts the timer for the rivals cars
     * Time format is HH:MM:ss:nn
     * This timer will be never stopped until the end of the race or until the user pauses or closes the software.
     */
    public void startTimerForRivalCar() {

        threadChronoRivalCar = new Thread(() -> {
            while (isStartRivalTimer) {

                try {
                    Thread.sleep(10);
                    rivalTimerMillisecondes++;
                    if (rivalTimerMillisecondes == 95) {
                        rivalTimerSecondes++;
                        rivalTimerMillisecondes = 0;
                    }
                    if (rivalTimerSecondes == 60) {
                        rivalTimerMunites++;
                        rivalTimerSecondes = 0;
                    }

                    if (rivalTimerMunites == 60) {
                        rivalTimerHours++;
                        rivalTimerMunites = 0;
                    }
                    Platform.runLater(() -> {
                        chronoTimeRival = LocalTime.of(rivalTimerHours, rivalTimerMunites, rivalTimerSecondes, rivalTimerMillisecondes);
                        chronoRivalCar.setText(chronoTimeRival.format(dtf1));
                    });


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        });
        threadChronoRivalCar.start();


    }

    /**
     * Ends all the different timers
     */
    public void endAllThread() {

        threadChrono.stop();
        thread.stop();
        if(getFollowedCars().size() > 1) {
            threadChronoRivalCar.stop();
        }
    }

    /**
     * Pauses all the different timers.
     */
    public void breakTimer() throws InterruptedException {
        if (breakThread) {
            isStartTimer = false;
            isStartRivalTimer = false;
            firstTop = false;
            breakThread = false;
            thread.interrupt();
        }

    }

    /**
     * Restarts the various timers after pausing it .
     */
    public void wakeUpThread() {

        isStartTimer = true;
        isStartRivalTimer = true;
        firstTop = true;
        breakThread = true;
        chronoTime = LocalTime.of(0, munites, secondes, millisecondes);
        chronoTopTime.setText(chronoTime.format(dtf2));
        if (!(tmierSign.isVisible())) {
            int pastTime = (munites * 60) + secondes;
            handleMeanTimeBar(pastTime);
        }
        chronoTimeRival = LocalTime.of(rivalTimerHours, rivalTimerMunites, rivalTimerSecondes, rivalTimerMillisecondes);
        chronoRivalCar.setText(chronoTimeRival.format(dtf1));
        startTimerBar();
        startTimerForRivalCar();
    }

    @Override
    public void update() {

    }

    public static RaceModel getRaceModel() {
        return raceModel;
    }
}
