package Animation;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;

public class MyListener implements ListChangeListener<Node> {

	
	public void onChanged(Change<? extends Node> c) {
		
		System.out.println("change getList" + c.getList().size());
		Node tail = c.getList().get(c.getList().size()-1);
		tail.bou
		
	}

}
