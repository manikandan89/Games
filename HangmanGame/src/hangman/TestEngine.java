package hangman;

import java.io.IOException;
import java.net.MalformedURLException;

public class TestEngine {
	public static void main(String[] args) {
		try {
			SaverEngine saverEngine = new SaverEngine();
			GameResponse gameState;
			gameState = saverEngine.newGame();
			int errors = 0;
			while (gameState.status.equals(saverEngine.GAME_STATUS_ALIVE)) {
				System.out.println(gameState.state);
				char g = saverEngine.NextCharacter(gameState.state);
				GameResponse newState = saverEngine.urlHandler(gameState, g);
				if (!gameState.state.equals(newState.state)) {
					saverEngine.predictState(g, true);
					System.out.println(g + ", " + true);
				} else {
					saverEngine.predictState(g, false);
					System.out.println(g + ", " + false);
					++errors;
				}
				gameState = newState;
			}
			System.out.println("GAME STATE " + gameState.status);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
