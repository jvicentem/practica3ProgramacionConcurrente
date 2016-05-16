package practica3PC;

import java.io.IOException;

import practica3PC.modules.Menu;

public class App {
	private App() {}
	
	public static void main(String[] args) throws IOException {
		boolean developer = true;
		
		Menu.execute(developer);
	}
}
