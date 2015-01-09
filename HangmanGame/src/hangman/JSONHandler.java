package hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

public class JSONHandler {
	public static GameResponse makeJsonHangmanRequest(String huluURL,
			String params) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new URL(huluURL + params).openStream()));
			StringBuilder sb = new StringBuilder();
			for (String str; ((str = in.readLine()) != null); sb.append(str))
				;
			in.close();
			GameResponse state = new Gson().fromJson(sb.toString(),
					GameResponse.class);
			state.state = state.state.toLowerCase();
			return state;
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	public static GameResponse newGame(String huluURL) {
		return makeJsonHangmanRequest(huluURL, "");
	}
}
