package com.example.gamedemo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.io.IOException;

import com.example.demo_2.AppMain;

public class Main extends Application {
    //Window settings
    private static final int WIDTH = 760;
    private static final int HEIGHT = 760;

    //Scenes
    private static Stage stage;
    private Scene mainScene;
    private Scene charactersScene;
    private Scene infoScene;
    private GameSounds sound;
    private Button[] MenuButtons;
    private int PacmanSelection = 1; //select which GIF to run by default

    private Scene login;
    public static Scene MSC;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        // create object to control sound effects
        sound = new GameSounds();

        setMainScene();
        setCharactersScene();
        setInfoScene();

        try{
            login = AppMain.stager();
        }catch(IOException e){
            System.out.println(e);
        }

        primaryStage.setScene(login); //login or mainscene
        primaryStage.setTitle("PacMan");
        primaryStage.getIcons().add(new Image("GameIcon.png"));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    //############################## SCENES METHODS ###############################

    // Establishing the Scene of Main Menu
    private void setMainScene() {
        Pane mainMenuPane = new Pane();

        // Main menu scene
        mainScene = new Scene(mainMenuPane, WIDTH / 1.15, HEIGHT / 1.25);

        // Background
        ImageView mainImageView = new ImageView("mainMenuPic.jpg");
        mainImageView.setFitWidth(WIDTH / 1.14);
        mainImageView.setFitHeight(HEIGHT / 1.25);

        // Panes
        Pane btns = MenuButtonsPane();
        mainMenuPane.getChildren().addAll(mainImageView, btns);
        Main.MSC = mainScene; // ADDED BY CARL
    }

    //---------Main Menu Establishing Methods---------
    // Returning main menu buttons' pane
    private Pane MenuButtonsPane() {
        Polygon buttonShape1 = new Polygon(0, 0, 200, 0, 230, 30, 30, 30);
        Polygon buttonShape2 = new Polygon(30, 0, 230, 0, 200, 30, 0, 30);

        //set the buttons' array, Vbox for them
        VBox buttonsPane = new VBox(8);
        buttonsPane.setLayoutX(39);
        buttonsPane.setLayoutY(352*1.25);

        MenuButtons = new Button[3];
        String[] btnsText = {"Play", "CHARACTERS", "INFO"};

        for (int i = 0; i < 3; i++) {
            //styling
            MenuButtons[i] = new Button(btnsText[i]);
            MenuButtons[i].setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 20));
            MenuButtons[i].setStyle("-fx-background-color:black ; -fx-border-color:#a0a0aa ;-fx-border-width: 0.7;");
            MenuButtons[i].setTextFill(Color.DARKTURQUOISE);
            MenuButtons[i].setPrefSize(200, 30);
            MenuButtons[i].setShape(i % 2 == 0 ? buttonShape1 : buttonShape2);

            buttonsPane.getChildren().add(MenuButtons[i]);

            //styling events
            int finalI = i;
            MenuButtons[i].setOnMouseEntered(e -> {
                MenuButtons[finalI].setTextFill(Color.BLACK);
                MenuButtons[finalI].setStyle("-fx-background-color:orange ;");
                MenuButtons[finalI].setPrefSize(240, 40);
                if (sound.btnSound.getStatus() == MediaPlayer.Status.PLAYING) {
                    sound.btnSound.stop();
                    sound.btnSound.play();
                } else {
                    sound.btnSound.play();
                }
            });
            MenuButtons[i].setOnMouseExited(e -> {
                MenuButtons[finalI].setTextFill(Color.DARKTURQUOISE);
                MenuButtons[finalI].setStyle("-fx-background-color:black; -fx-border-color:snow; -fx-border-width: 0.5;");
                MenuButtons[finalI].setPrefSize(200, 30);
            });
        }
        // Navigation
        MenuButtons[0].setOnAction(e -> {
            System.gc();
            stage.setScene(new GamePane(sound,PacmanSelection,stage,mainScene).getScene());
            sound.start_sound.stop();
        });
        MenuButtons[1].setOnAction(e -> {
            stage.setScene(charactersScene);
            sound.start_sound.stop();
        });

        MenuButtons[2].setOnAction(e -> {
            stage.setScene(infoScene);
            sound.start_sound.stop();
        });
        return buttonsPane;
    }

    // Scene of Characters
    private void setCharactersScene() {

        //pane for characters
        StackPane charactersPane = charactersGifsPane();

        //pane for buttons to choose a character
        VBox charactersBtnsVbox = charactersBtnsPane(charactersPane);

        //set main pane , add buttons' pane and characters' pane to it
        Pane charactersMenuPane = new Pane();
        ImageView characterBackground = new ImageView("CharacterBackground.jpg");
        setImgDimensions(characterBackground, WIDTH / 1.15, HEIGHT / 1.25);
        charactersMenuPane.getChildren().addAll(characterBackground, charactersBtnsVbox, charactersPane);

        //create the scene and add the main pane to it
        charactersScene = new Scene(charactersMenuPane, WIDTH / 1.15, HEIGHT / 1.25);

    }

    //--------Character Establishing Methods-------
    // Returning Vbox of characters' buttons at [Character Scene]
    private VBox charactersBtnsPane(StackPane charactersPane) {
        VBox charactersBtnsVbox = new VBox(25);
        charactersBtnsVbox.setAlignment(Pos.CENTER_LEFT);
        charactersBtnsVbox.setLayoutX(0);
        charactersBtnsVbox.setLayoutY(200);
        charactersBtnsVbox.setPadding(new Insets(10));


        // Setting Characters Buttons
        Button[] charactersBtns = new Button[4];
        String[] btnsText = {"Pacman", "Pacwoman", "Pacboy", "Back"};
        for (int i = 0; i < 3; i++) {
            //set button style
            charactersBtns[i] = new Button(btnsText[i]);
            charactersBtns[i].setStyle("-fx-background-color: transparent;");
            charactersBtns[i].setFont(new Font("Comic Sans MS", 30));
            charactersBtns[i].setTextFill(Color.CYAN);

            //set the button event
            int finalI = i;
            charactersBtns[i].setOnAction(event -> {
                sound.start_sound.stop();
                PacmanSelection = finalI + 1;
                stage.setScene(mainScene);
            });
            charactersBtns[i].setOnMouseEntered(event -> {
                charactersPane.getChildren().get(finalI).setVisible(true);
                charactersBtns[finalI].setTextFill(Color.MAGENTA);
                charactersBtns[finalI].setScaleX(1.2);
                charactersBtns[finalI].setScaleY(1.2);
                if (sound.btnSound.getStatus() == MediaPlayer.Status.PLAYING) {
                    sound.btnSound.stop();
                    sound.btnSound.play();
                } else {
                    sound.btnSound.play();
                }
            });
            charactersBtns[i].setOnMouseExited(event -> {
                charactersPane.getChildren().get(finalI).setVisible(false);
                charactersBtns[finalI].setTextFill(Color.CYAN);
                charactersBtns[finalI].setScaleX(1);
                charactersBtns[finalI].setScaleY(1);
            });

            //add the button to the vbox
            charactersBtnsVbox.getChildren().add(charactersBtns[i]);
        }

        // Setting the Back Button
        charactersBtns[3] = new Button(btnsText[3]);
        charactersBtns[3].setFont(Font.font("Comic Sans MS", 30));
        charactersBtns[3].setBackground(Background.EMPTY);
        charactersBtns[3].setTextFill(Color.DARKRED);
        addButtonEffect(charactersBtns[3], Color.DARKRED, Color.RED);
        charactersBtns[3].setOnAction(event -> stage.setScene(mainScene));
        charactersBtns[3].setOnMouseEntered(e -> {
            sound.start_sound.stop();
            if (sound.btnSound.getStatus() == MediaPlayer.Status.PLAYING) {
                sound.btnSound.stop();
                sound.btnSound.play();
            } else {
                sound.btnSound.play();
            }
        });
        charactersBtnsVbox.getChildren().add(charactersBtns[3]);
        return charactersBtnsVbox;
    }

    // Returning pane of characters' gifs at [Character Scene]
    private StackPane charactersGifsPane() {
        //pane for characters' gifs
        StackPane charactersPane = new StackPane();
        charactersPane.setBackground(Background.EMPTY);
        charactersPane.setLayoutX(120);
        charactersPane.setLayoutY(120);

        // Adjusting characters pane (VBox)
        VBox charactersBtnsVbox = new VBox(25);
        charactersBtnsVbox.setAlignment(Pos.CENTER_LEFT);
        charactersBtnsVbox.setLayoutX(10);
        charactersBtnsVbox.setLayoutY(150);
        charactersBtnsVbox.setPadding(new Insets(10));

        //set the gif for every character
        String[] gifsUrls = {"PacmanEye.gif", "pacwoman.gif", "pacboy.gif"};
        ImageView[] charactersGifs = new ImageView[3];
        for (int i = 0; i < 3; i++) {
            charactersGifs[i] = new ImageView(gifsUrls[i]);
            setImgDimensions(charactersGifs[i], 150, 150);
            charactersGifs[i].setTranslateY(160);
            charactersGifs[i].setTranslateX(130);
            charactersGifs[i].setVisible(false);
            charactersPane.getChildren().add(charactersGifs[i]);
        }

        return charactersPane;
    }

    /*------------------------------------------------------*/
    // Establishing The Scene of Info (Switched by Info Bt)

    private void setInfoScene() {
        Pane infoPane = new Pane();

        int sceneWidth = (int) (WIDTH/1.15);
        int sceneHeight = (int) (HEIGHT/1.25);

        Button backBtn = new Button("Back");
        backBtn.setBackground(Background.EMPTY);
        backBtn.setLayoutX(sceneWidth * 0.8);
        backBtn.setLayoutY(sceneHeight * 0.85);
        backBtn.setFont(Font.font(30)); // Adjusted font size
        backBtn.setTextFill(Color.DARKMAGENTA);
        backBtn.setOnAction(e -> stage.setScene(mainScene));
        backBtn.setOnMouseEntered(e -> {
            if (sound.btnSound.getStatus() == MediaPlayer.Status.PLAYING) {
                sound.btnSound.stop();
                sound.btnSound.play();
            } else {
                sound.btnSound.play();
            }
        });
        addButtonEffect(backBtn, Color.DARKMAGENTA, Color.rgb(139, 139, 139));

        // Background
        ImageView infoBackground = new ImageView("InfoBackground.jpg");
        setImgDimensions(infoBackground, sceneWidth, sceneHeight);
        infoPane.getChildren().addAll(infoBackground, backBtn);

        // Members Nodes
        String[] membersStrs = {"Chloe Khoury", "Carl Ghanem", "Wissam Estephan"};
        Text[] membersTxt = new Text[3];
        ImageView[] membersQrs = {new ImageView("1.jpg"), new ImageView("2.jpg"), new ImageView("3.jpg")};

        // Adjust positions and sizes for smaller scene
        for (int i = 0; i < 3; i++) {
            membersTxt[i] = new Text(membersStrs[i]);
            membersTxt[i].setStyle("-fx-font-family: 'Trebuchet MS'; -fx-font-size: 24"); // Adjusted font size
            membersTxt[i].setFill(Color.web("#f5f5dc"));
            membersTxt[i].setStroke(Color.GRAY);
            membersTxt[i].setStrokeWidth(.2);
            setTxtPosition(membersTxt[i], 80, 50 + 60 * i); // Adjusted positions
            adjustImg(membersQrs[i], 50, 50, 20, 20 + 60 * i); // Adjusted sizes and positions
            infoPane.getChildren().addAll(membersTxt[i], membersQrs[i]);
        }

        infoScene = new Scene(infoPane, sceneWidth, sceneHeight);
    }


    /*----------------Helper Methods (Generic)--------------------*/

    private void setTxtPosition(Text txt, double x, double y){
        txt.setX(x);
        txt.setY(y);
    }
    private void setImgDimensions(ImageView img, double width, double height){
        img.setFitWidth(width);
        img.setFitHeight(height);
    }
    private void adjustImg(ImageView img, double width, double height,double x, double y){
        img.setFitWidth(width);
        img.setFitHeight(height);
        img.setX(x);
        img.setY(y);
    }
    // Adding Scaling Effect To Button
    private void addButtonEffect(Button button, Color before, Color after) {
        button.setOnMouseEntered(event -> {
            button.setTextFill(after);
            button.setScaleX(1.2);
            button.setScaleY(1.2);
            if (sound.btnSound.getStatus() == MediaPlayer.Status.PLAYING) {
                sound.btnSound.stop();
                sound.btnSound.play();
            } else {
                sound.btnSound.play();
            }
        });
        button.setOnMouseExited(event -> {
            button.setTextFill(before);
            button.setScaleX(1);
            button.setScaleY(1);
        });
    }
}