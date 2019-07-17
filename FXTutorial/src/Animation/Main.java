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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	
	
	Rectangle rec;
	DoubleProperty snakeX = new SimpleDoubleProperty(250);
	DoubleProperty snakeY = new SimpleDoubleProperty(250);
	
	Group snakeComponents;
	
	public enum Direction{
		UP,DOWN,LEFT,RIGHT
	}
	
	private Direction direction = Direction.RIGHT;
	
	
	
	Button startButton;
	BooleanProperty startVisible = new SimpleBooleanProperty(true);
	
	
	
	private Rectangle topWall;
	private Rectangle leftWall;
	private Rectangle rightWall;
	private Rectangle bottomWall;
	
	
	public static void main(String[] args) {
        Application.launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		rec = new Rectangle(30, 30, Color.RED);
		Timeline snakeAnimation = new Timeline(new KeyFrame(new Duration(5), t ->  {
			
			checkForCollision();
			
			switch (direction) {
			case RIGHT:
				snakeX.setValue(snakeX.getValue() + 1);
				break;
			case LEFT:
				snakeX.setValue(snakeX.getValue() - 1);
				break;
			case UP:
				snakeY.setValue(snakeY.getValue() - 1);
				break;
			case DOWN:
				snakeY.setValue(snakeY.getValue() +1);
			default:
				break;
			}
				
		}));
		
		snakeAnimation.setCycleCount(Timeline.INDEFINITE);
        
        
		startButton = new Button("Start!");
        startButton.setLayoutX(225);
        startButton.setLayoutY(520);
        startButton.setOnAction(e -> {
            startVisible.set(false);
            snakeAnimation.playFromStart();
            snakeComponents.requestFocus();
        });
        
        
        
        
        topWall = new Rectangle(0, 0, 500, 1);
        leftWall = new Rectangle(0, 0, 1, 500);
        rightWall = new Rectangle(500, 0, 1, 500);
        bottomWall = new Rectangle(0, 500, 500, 1);
        
        
        snakeComponents = new Group(rec,topWall,leftWall,bottomWall,rightWall,startButton);
        
        rec.xProperty().bind(snakeX);
        rec.yProperty().bind(snakeY);
        
        
        Scene scene = new Scene(snakeComponents, 600, 600);
        scene.setFill(Color.GRAY);
        
        
        snakeComponents.setOnKeyPressed(k -> {
        	if(k.getCode()== KeyCode.RIGHT) {
        		direction = Direction.RIGHT;
        	} else if (k.getCode()== KeyCode.DOWN) {
        		direction = Direction.DOWN;
        	} else if(k.getCode() == KeyCode.UP) {
        		direction = Direction.UP;
        	} else if(k.getCode() == KeyCode.LEFT){
        		direction = Direction.LEFT;
        }
        });
        
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Custom animation");
        primaryStage.show();
        
	}
	
	private void checkForCollision() {
		if ((rec.intersects(rightWall.getBoundsInLocal()) && direction == Direction.RIGHT)) {
			snakeX.set(0-30);
		} else if (rec.intersects(rightWall.getBoundsInLocal()) && direction == Direction.LEFT) {
			snakeX.set(499);
		} else if (rec.intersects(leftWall.getBoundsInLocal()) && direction == Direction.LEFT) {
			snakeX.set(500);
		} else if (rec.intersects(leftWall.getBoundsInLocal()) && direction == Direction.RIGHT) {
			snakeX.set(1);
		}
	}

	void initialize() {
		snakeX.setValue(250);
		snakeY.setValue(250);
		snakeComponents.requestFocus();
	}
	
	

}
