package Snake;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.Mnemonic;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{
	
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	private Canvas scorePanel;
	private GraphicsContext gc;

	public static final int BLOCK_SIZE = 40;
	public static final int APP_W = 30*BLOCK_SIZE;
	public static final int APP_H = 20*BLOCK_SIZE;
	
	private String pointsText;
	private IntegerProperty points;
	
	private Image apple  = new Image("/Snake/apple.png");
	
	private Direction direction = Direction.RIGHT;
	private boolean moved = false;
	private boolean running = false;
	
	private Timeline timeline = new Timeline();
	
	private ObservableList<Node> snake;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Scene scene = new Scene(createContent());
		
		scene.setOnKeyPressed( k-> {
			if(!moved) {
				return;
			}
			
			switch(k.getCode()) {
			case UP:
				if(direction != Direction.DOWN) 
					direction = Direction.UP;
				break;
			case RIGHT:
				if(direction != Direction.LEFT) 
					direction = Direction.RIGHT;
				break;
			case LEFT:
				if(direction != Direction.RIGHT) 
					direction = Direction.LEFT;
				break;
			case DOWN:
				if(direction != Direction.UP) 
					direction = Direction.DOWN;
				break;
			}
			moved = false;
		});
		
		primaryStage.setTitle("Snake");
		primaryStage.setScene(scene);
		primaryStage.show();
		startGame();
	}

	private Parent createContent() {

		//score frame
		scorePanel = new Canvas(APP_W, 100);
		gc = scorePanel.getGraphicsContext2D();
		
		createScoreFrame();
		
		
		Pane gameArea = new Pane();
		gameArea.setStyle("-fx-background-color: #F0F8FF");
		
		gameArea.setPrefSize(APP_W, APP_H);
		

		Group snakeBody = new Group();
		snake = snakeBody.getChildren();
		
		Rectangle food = new Rectangle(BLOCK_SIZE,BLOCK_SIZE,Color.RED);
		food.setFill(new ImagePattern(apple));
		
		setNewFoodPosition(snake, food);
		
		KeyFrame frame = new KeyFrame(Duration.seconds(0.06), event -> {
			if(!running) 
				return;
			

			boolean toRemove = snake.size() > 1;
			
			Node tail = toRemove ? snake.remove(snake.size()-1) : snake.get(0);
			((Shape) tail).setFill(Color.MEDIUMSEAGREEN);
			((Rectangle) tail).setArcHeight(50);
			((Rectangle) tail).setArcWidth(50);
			
			double tailX = tail.getTranslateX();
			double tailY = tail.getTranslateY();
			
			
			switch (direction) {
			case UP:
				tail.setTranslateX(snake.get(0).getTranslateX());
				tail.setTranslateY(snake.get(0).getTranslateY()- BLOCK_SIZE);
				break;
			case DOWN:
				tail.setTranslateX(snake.get(0).getTranslateX());
				tail.setTranslateY(snake.get(0).getTranslateY() + BLOCK_SIZE);
				break;
			case RIGHT:
				tail.setTranslateX(snake.get(0).getTranslateX() + BLOCK_SIZE);
				tail.setTranslateY(snake.get(0).getTranslateY());
				break;
			case LEFT:
				tail.setTranslateX(snake.get(0).getTranslateX() - BLOCK_SIZE);
				tail.setTranslateY(snake.get(0).getTranslateY());
				break;
			}
			
			moved = true;
			
			if(toRemove)
				snake.add(0,tail);
			
			for (Node rect: snake) {
				if(rect != tail && tail.getTranslateX() == rect.getTranslateX()
						&& tail.getTranslateY() == rect.getTranslateY()) {
					restartGame();
					break;
				}
			}
			
			if (tail.getTranslateX() < 0 || tail.getTranslateX() >= APP_W || tail.getTranslateY() < 0 
					|| tail.getTranslateY() >= APP_H) {
				restartGame();
			}
			
			if(tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
				
				Rectangle rect = new Rectangle(BLOCK_SIZE,BLOCK_SIZE,Color.MEDIUMSEAGREEN);
				rect.setTranslateX(tailX);
				rect.setTranslateY(tailY);
				rect.setArcHeight(50);
				rect.setArcWidth(50);
				
				snake.add(rect);
				
				updateScoreFrame();
				
				setNewFoodPosition(snake,food);
			}
		});
		
		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		gameArea.getChildren().addAll(food,snakeBody,scorePanel);
		
		
		return gameArea;
	}
	
	private void createScoreFrame() {
		
		gc.fillRect(APP_W,0, 80,50);
		gc.setFill(Color.ORANGE);
		Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 30);
		gc.setFont(theFont);
		gc.setStroke(Color.BLACK );
        pointsText = "Points: " + 0;
		gc.fillText(pointsText,APP_W - 100,0);
		gc.setLineWidth(1);
		
	}
	
	private void updateScoreFrame() {
		gc.clearRect(0, 0, APP_W , 100);
		points.set(points.get()+1);
		System.out.println(points);
		
		pointsText = "Points: " + points.get();
		gc.fillText(pointsText, APP_W - 200, 40);
		gc.strokeText( pointsText, APP_W - 200, 40 );
	}

	private int[] getRandomCoordinates() {
		
		int[] result = new int[2];
		int x = (int)(Math.random() * (APP_W - BLOCK_SIZE))/BLOCK_SIZE * BLOCK_SIZE;
		int y = (int)(Math.random() * (APP_H - BLOCK_SIZE))/BLOCK_SIZE * BLOCK_SIZE;
		result[0] = x;
		result[1] = y;
		
		return result;
	}
	
	private void setNewFoodPosition(ObservableList<Node> snake, Rectangle food) {
		
		boolean keepSearching = true;
		int [] coordinatesProposal = null;
		
		while (keepSearching) {
			coordinatesProposal = getRandomCoordinates();
		
			boolean isOccupied = false;
			for (Node rect : snake) {
				if(rect.getTranslateX() == coordinatesProposal[0] && 
						rect.getTranslateY() == coordinatesProposal[1]) {
					System.out.println("rect.getTranslateX()" + rect.getTranslateX() + " x " + coordinatesProposal[0]);
					isOccupied = true;
					break;
				}
			}
			if(!isOccupied)
				keepSearching = false;
		}
		
		food.setTranslateX(coordinatesProposal[0]);
		food.setTranslateY(coordinatesProposal[1]);
	}

	private void restartGame() {
		stopGame();
		startGame();
	}

	private void startGame() {
		
		createScoreFrame();
		points = new SimpleIntegerProperty(0);
		direction = Direction.RIGHT;
		Rectangle head = new Rectangle(BLOCK_SIZE,BLOCK_SIZE);
		snake.add(head);
		timeline.play();
		running = true;
	}

	private void stopGame() {
		running = false;
		timeline.stop();
		snake.clear();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
