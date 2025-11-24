package com.tron.main;

import com.tron.gamestate.GameState;
import com.tron.gamestate.GameStateManager;
import com.tron.gamestate.MenuState;
//import com.tron.gamestate.MenuState; // Will create MenuState later
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 560; // 500 for map + 60 for UI below map
    public static final int MAP_DIMENSION = 500; // Map is square 500x500

    private static Scene currentScene; // Static reference to the scene for GameStates to access
    
    private GameStateManager gsm;
    private long lastTime = 0;

    public static Scene getScene() {
        return currentScene;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tron");

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        
        Scene scene = new Scene(root);
        currentScene = scene; // Store the scene
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        gsm = GameStateManager.getInstance();
        // Initially, push a placeholder or the actual MenuState
        // For now, let's push a dummy state until MenuState is created.
        // gsm.push(new MenuState(gsm)); 
        // Will be replaced with MenuState once it's available.
        gsm.push(new MenuState(gsm));
        
        lastTime = System.nanoTime();
        
        new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                double deltaTime = (currentTime - lastTime) / 1000000000.0;
                lastTime = currentTime;

                gsm.update(deltaTime);
                gsm.render(gc);
                gsm.handleInput(); 
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
