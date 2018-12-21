package xenex.modem.view;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Main extends Application {
        
    
    
    
    private static final Main main = new Main();
     
    public static Main getInstance() {
        return main;
    }
    
    @Override
    public void start(Stage primaryStage) {
        //System.out.println(Font.getFamilies());
         
        final Menu modemMenu = new Menu("Modem");
        MenuItem nanoItem = new MenuItem("Nano");
        MenuItem p900Item = new MenuItem("Pico 900");
        modemMenu.getItems().addAll(nanoItem, p900Item);        
        final MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(modemMenu);
        VBox box = new VBox(menuBar);       
        
        nanoItem.setOnAction(value -> {
            System.out.println("Loading nano...");            
            
            NanoView view = new NanoView();                
            NanoPresenter presenter = new NanoPresenter(view);
            box.getChildren().clear();
            box.getChildren().addAll(menuBar, view);
        });
        
        p900Item.setOnAction(value -> {
            System.out.println("Loading p900...");            
            P900View view = new P900View();                
            P900Presenter presenter = new P900Presenter(view);
            box.getChildren().clear();
            box.getChildren().addAll(menuBar, view);                        
        });
                
              
        StackPane root = new StackPane();
        root.getChildren().add(box);        
        Scene scene = new Scene(root, 500, 800);        
        scene.getStylesheets().add(getClass().getResource("/resources/bordered.css").toExternalForm());
          
        primaryStage.setTitle("Modem Config");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);
        
        primaryStage.setOnCloseRequest(value -> {
            System.out.println("Closing");           
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
