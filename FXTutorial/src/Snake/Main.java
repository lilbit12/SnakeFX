package Snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{
	
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	private Button pauseButton;
	private HBox menuPanel;
	
	public static final int BLOCK_SIZE = 20;
	public static final int APP_W = 30*BLOCK_SIZE;
	public static final int APP_H = 20*BLOCK_SIZE;
	
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

		
		Pane root = new Pane();
		root.setPrefSize(APP_W + (5*BLOCK_SIZE),  APP_H + (3 * BLOCK_SIZE));
		
		
		HBox buttonPanel = new HBox();
		buttonPanel.setPadding(new Insets(15, 12, 15, 12));
		buttonPanel.setSpacing(10);
		
		Button pauseButton = new Button("Pause");
		
		createMenuPanel();
		
		Pane gameArea = new Pane();
		
		gameArea.setPrefSize(APP_W, APP_H);
		gameArea.setBorder(new Border(new BorderStroke(Color.BLACK, 
				BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
	
		Group snakeBody = new Group();
		snake = snakeBody.getChildren();
		
		Rectangle food = new Rectangle(BLOCK_SIZE,BLOCK_SIZE,Color.RED);
		
		setNewFoodPosition(snake, food);
		
		KeyFrame frame = new KeyFrame(Duration.seconds(0.1), event -> {
			if(!running) 
				return;
			
			
			boolean toRemove = snake.size() > 1;
			
			Node tail = toRemove ? snake.remove(snake.size()-1) : snake.get(0);
			
			
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
				
				Rectangle rect = new Rectangle(BLOCK_SIZE,BLOCK_SIZE);
				rect.setTranslateX(tailX);
				rect.setTranslateY(tailY);
				
				snake.add(rect);
				
				setNewFoodPosition(snake,food);
			}
		});
		
		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		gameArea.getChildren().addAll(food,snakeBody);
		root.getChildren().addAll(gameArea,menuPanel);
		
		return root;
	}
	
	private void createMenuPanel() {
		menuPanel = new HBox();
		menuPanel.setPadding(new Insets(10));
		menuPanel.setLayoutY(APP_H + (BLOCK_SIZE/2));
		menuPanel.setSpacing(10);
		
		pauseButton = new Button("Pause");
		menuPanel.getChildren().add(pauseButton);
		
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
