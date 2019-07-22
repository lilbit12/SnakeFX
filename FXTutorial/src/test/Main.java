package test;

public class Main {
	
	public static final int BLOCK_SIZE = 30;
	public static final int APP_W = 20*BLOCK_SIZE;
	public static final int APP_H = 20 * BLOCK_SIZE;

	public static void main(String[] args) {
		
		int counter = 0;
		for (int i = 0; i < 100; i++) {
			
			
			int x = (int)(Math.random() * (APP_W))/BLOCK_SIZE * BLOCK_SIZE;
			int y = (int)(Math.random() * (APP_H))/BLOCK_SIZE * BLOCK_SIZE;
			
			if (x == 570)
				counter++;
			System.out.println(x + " - " + y);
		}
		System.out.println(counter);
		

	}

}
