import javax.swing.event.ChangeEvent;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{
	
	
	DoubleProperty fillVals = new SimpleDoubleProperty(255.0);

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		Slider slider = new Slider(0, 255, 255);
		slider.setOrientation(Orientation.HORIZONTAL);
		
		VBox root = new VBox(slider);
		root.setPadding(new Insets(0, 20, 40, 0));
		root.setLayoutX(20);
        root.setLayoutY(40);
		
		fillVals.bind(slider.valueProperty());
		
		Scene scene = new Scene(root,400,300);
		primaryStage.setScene(scene);
		
		// When fillVals changes, use that value as the RGB to fill the scene
        fillVals.addListener((ov, oldValue, newValue) -> {
            Double fillValue = fillVals.getValue() / 256.0;
            
            scene.setFill(new Color(fillValue, fillValue, fillValue, 1.0));
        });
        
     // Define an unmanaged node that will display Text 
        Text addedTextRef = new Text(0, 0, " ");
        addedTextRef.setTextOrigin(VPos.TOP);
        addedTextRef.setFill(Color.BLUE);
        addedTextRef.setFont(Font.font("Sans Serif", FontWeight.BOLD, 16));
        addedTextRef.setManaged(false);
        
     // Bind the text of the added Text node to the fill property of the Scene
        addedTextRef.textProperty().bind(new SimpleStringProperty("Scene fill: ").
                concat(scene.fillProperty()));
        
        root.getChildren().add(addedTextRef);
		
		
		
		primaryStage.show();
	}

}
