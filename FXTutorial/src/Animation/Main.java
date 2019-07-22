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
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	
	
	
	
	Rectangle apple;
	
	DoubleProperty appleX = new SimpleDoubleProperty((int) Math.random()*470);
	DoubleProperty appleY = new SimpleDoubleProperty((int) Math.random()*470);
	
	
	Rectangle snakeHead;
	DoubleProperty snakeHeadX = new SimpleDoubleProperty(250);
	DoubleProperty snakeHeadY = new SimpleDoubleProperty(250);
	
	Rectangle snakeTail;
	
	
	
	
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
	private Button pauseButton;
	private Timeline snakeAnimation;
	private ObservableList<Node> snake;
	Group snakeBody;
	
	
	public static void main(String[] args) {
        Application.launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		snakeTail = new Rectangle(30,30,Color.BLACK);
		snakeHead = new Rectangle(30, 30, Color.BLUE);
		apple = new Rectangle(30,30,Color.RED);
		
		snakeBody = new Group();
		
		snake = snakeBody.getChildren();
		snake.add(snakeHead);
		
		snake.addListener(new MyListener());
		
		
		
		snakeAnimation = new Timeline(new KeyFrame(new Duration(500), t ->  {
			
			checkForCollision();
			
			switch (direction) {
			case RIGHT:
				snakeHeadX.setValue(snakeHeadX.getValue() + 30);
				
				break;
			case LEFT:
				snakeHeadX.setValue(snakeHeadX.getValue() - 30);
				break;
			case UP:
				snakeHeadY.setValue(snakeHeadY.getValue() - 30);
				break;
			case DOWN:
				snakeHeadY.setValue(snakeHeadY.getValue() +30);
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
        
        pauseButton = new Button("Pause");
        pauseButton.setLayoutX(300);
        pauseButton.setLayoutY(520);
        pauseButton.setOnAction(e -> {
        	snakeAnimation.pause();
        });
        
             
        topWall = new Rectangle(0, 0, 500, 1);
        leftWall = new Rectangle(0, 0, 1, 500);
        rightWall = new Rectangle(500, 0, 1, 500);
        bottomWall = new Rectangle(0, 500, 500, 1);
        
        
        snakeComponents = new Group(snakeBody,topWall,leftWall,bottomWall,rightWall,startButton,pauseButton,apple);
        
        snakeHead.xProperty().bind(snakeHeadX);
        snakeHead.yProperty().bind(snakeHeadY);
        
        apple.xProperty().bind(appleX);
        apple.yProperty().bind(appleY);
        
        
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
		
		if ((snakeHead.intersects(rightWall.getBoundsInLocal())) ||
				snakeHead.intersects(leftWall.getBoundsInLocal()) ||
				snakeHead.intersects(topWall.getBoundsInLocal())||
				snakeHead.intersects(bottomWall.getBoundsInLocal())){
				snakeAnimation.stop();
				initialize();
		}
		
		if(snakeHead.intersects(apple.getBoundsInLocal())) {
			appleX.setValue((int) (Math.random()*470));
			appleY.setValue((int) (Math.random()*470));
			snakeTail = new Rectangle(30,30,Color.BLACK);
			
			snake.add(snakeTail);
		}
		
		
	}

	void initialize() {
		snakeHeadX.setValue(250);
		snakeHeadY.setValue(250);
		
		appleX.setValue((int) (Math.random()*470) / 30 * 30);
		appleY.setValue((int) (Math.random()*470) / 30 * 30);
		
		snakeComponents.requestFocus();
	}
	
	

}
