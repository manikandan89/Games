package hangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrequencyHandler {

	private Map<String, Integer> frequencyMap;

	public Map<String, Integer> getFrequencyMap() {
		return frequencyMap;
	}

	//This function is the Frequency handler which is called on every word frequency check. This fuction
	//refers the preloaded dataset which is obtained from BNC online resource	
	public FrequencyHandler() {
		frequencyMap = new HashMap<String, Integer>();
		for (File f : new File("res/freq/").listFiles()) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(f));
				for (String str; (str = in.readLine()) != null;) {
					String[] tokens = str.split("\\s+");
					assert (tokens.length > 2);
					frequencyMap.put(tokens[1], Integer.parseInt(tokens[0])
							+ integrator(frequencyMap.get(tokens[1]), 0));
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//The integrator combines the Ranking data along with the Word Freq list
	public static <T> T integrator(T... items) {
		for (T i : items)
			if (i != null)
				return i;
		return null;
	}
}
