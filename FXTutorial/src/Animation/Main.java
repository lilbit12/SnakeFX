package Animation;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	
	
	Rectangle rec;
	DoubleProperty snakeX = new SimpleDoubleProperty();
	DoubleProperty snakeY = new SimpleDoubleProperty();
	
	Group snakeComponents;
	
	
	private boolean movingRight = true;
	private boolean movingDown = false;
	
	Button startButton;
	BooleanProperty startVisible = new SimpleBooleanProperty(true);
	
	
	public static void main(String[] args) {
        Application.launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		rec = new Rectangle(30, 30, Color.RED);
		Timeline snakeAnimation = new Timeline(new KeyFrame(new Duration(10.0), t ->  {
			int horzPixels = movingRight ? 1 : -1;
            //int vertPixels = movingDown ? 1 : -1;
            snakeX.setValue(snakeX.getValue() + horzPixels);
            //snakeY.setValue(snakeY.getValue() + vertPixels);
			
		}));
		
		snakeAnimation.setCycleCount(Timeline.INDEFINITE);
        
        
		startButton = new Button("Start!");
        startButton.setLayoutX(225);
        startButton.setLayoutY(470);
        startButton.setOnAction(e -> {
            startVisible.set(false);
            snakeAnimation.playFromStart();
            snakeComponents.requestFocus();
        });
        
        
        
        
        Rectangle topWall = new Rectangle(0, 0, 500, 1);
        Rectangle leftWall = new Rectangle(0, 0, 1, 500);
        Rectangle rightWall = new Rectangle(500, 0, 1, 500);
        Rectangle bottomWall = new Rectangle(0, 500, 500, 1);
        
        
        snakeComponents = new Group(rec,topWall,leftWall,bottomWall,rightWall,startButton);
        
        rec.xProperty().bind(snakeX);
        rec.yProperty().bind(snakeY);
        
        
        Scene scene = new Scene(snakeComponents, 600, 600);
        scene.setFill(Color.GRAY);
        
        
        
        
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Custom animation");
        primaryStage.show();
        
        
        
	}
	
	void initialize() {
		snakeX.setValue(250);
		snakeY.setValue(250);
		snakeComponents.requestFocus();
	}
	
	

}
