package hangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordHandler {
	private Map<Integer, List<String>> wordMap;

	public Map<Integer, List<String>> getWorldMap() {
		return wordMap;
	}

	public WordHandler() {
		wordMap = new HashMap<Integer, List<String>>();
		for (File f : new File("res/words/").listFiles()) {
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(f));
				for (String str; (str = in.readLine()) != null;) {
					str = str.trim();
					if (!wordMap.containsKey(str.length())) {
						wordMap.put(str.length(), new ArrayList<String>());
					}
					wordMap.get(str.length()).add(str);
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
