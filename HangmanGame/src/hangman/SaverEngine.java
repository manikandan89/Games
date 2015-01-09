package hangman;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaverEngine {
	
	//The class variables are obtained from the requirement definition.
	// On triggering the primary URL the JSON data gives the states of the Prisoner
	//Also a two states for successful and wrong characters are maintained in a Individual Set method.
	public static final String GAME_STATUS_ALIVE = "ALIVE";
	public static final String GAME_STATUS_DEAD = "DEAD";
	public static final String GAME_STATUS_FREE = "FREE";

	private static final String HULU_URL = "http://gallows.hulu.com/play?code=nagarajan.m@husky.neu.edu";
	private Set<Character> wrongCharacters = new HashSet<Character>();
	private Set<Character> correctCharacters = new HashSet<Character>();
	private FrequencyHandler frequencyHandler;
	private WordHandler wordHandler;

	SaverEngine() {
		frequencyHandler = new FrequencyHandler();
		wordHandler = new WordHandler();
	}

	//The Next Guess iterates through the successful and wrong character and intelligently picks the
	// next data for the guess, also this data is picked after logically referencing the Word List
	// and the frequency dataset
	public char NextCharacter(String state) {
		List<String> wordList = new ArrayList<String>(Arrays.asList(state
				.split("[^a-z_']+")));
		StringBuilder excludeString = new StringBuilder();
		Iterator<Character> it = wrongCharacters.iterator();
		while (it.hasNext())
			excludeString.append(it.next());
		double[] freq = new double[26];
		for (String s : wordList) {
			if (!s.contains("_"))
				continue;
			Pattern regex = Pattern.compile(s.replace(
					"_",
					(excludeString.length() > 0) ? String.format(
							"[a-z&&[^%s]]", excludeString) : "[a-z]"));
			List<String> cands = new ArrayList<String>();
			//this condition picks the data from the WordList
			if (wordHandler.getWorldMap().containsKey(s.length())) {
				for (String cand : wordHandler.getWorldMap().get(s.length())) {
					Matcher m = regex.matcher(cand);
					if (m.find())
						cands.add(cand);
				}
			}
			double[] thisfreq = new double[26];
			for (String cand : cands) {
				//This loop picks the data from the Frequency dataset
				int freqValue = (frequencyHandler.getFrequencyMap()
						.containsKey(cand)) ? frequencyHandler
						.getFrequencyMap().get(cand) : 1;
				for (int i = 0; i < cand.length(); ++i) {
					int index = cand.charAt(i) - 'a';
					if (index < 0 || index >= freq.length)
						continue;
					thisfreq[index] += freqValue;
				}
			}
			for (int i = 0; i < thisfreq.length; ++i) {
				thisfreq[i] /= cands.size();
				freq[i] += thisfreq[i];
			}
		}

		char maxc = 'a';
		double maxi = 0;
		for (int i = 0; i < freq.length; ++i) {
			if (freq[i] > maxi) {
				//This loop checks value in both the  correctcharter list as well as the wrongcharacter list
				char newc = (char) ((int) 'a' + i);
				if (correctCharacters.contains(newc)
						|| wrongCharacters.contains(newc))
					continue;
				maxi = freq[i];
				maxc = newc;
			}
		}

		if (maxi == 0) {
			for (char c = 'a'; c <= 'z'; ++c) {
				if (!(correctCharacters.contains(c) || wrongCharacters
						.contains(c))) {
					return c;
				}
			}
		}

		return maxc;
	}

	public void predictState(char guess, boolean success) {
		(success ? correctCharacters : wrongCharacters).add(guess);
	}

	public GameResponse urlHandler(GameResponse state, char guess)
			throws MalformedURLException, IOException {
		return JSONHandler.makeJsonHangmanRequest(HULU_URL,
				String.format("&token=%s&guess=%s", state.token, guess));
	}

	public GameResponse newGame() {
		return JSONHandler.newGame(HULU_URL);
	}
}
