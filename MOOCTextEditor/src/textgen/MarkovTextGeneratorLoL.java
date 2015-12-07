package textgen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * An implementation of the MTG interface that uses a list of lists.
 * @author UC San Diego Intermediate Programming MOOC team 
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

	// The list of words with their next words
	private List<ListNode> wordList; 
	
	// The starting "word"
	private String starter;
	
	// The random number generator
	private Random rnGenerator;
	
	
	public MarkovTextGeneratorLoL(Random generator)
	{
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}
	
	protected List<String> getTokens(String pattern,String text)
	{
		ArrayList<String> tokens = new ArrayList<String>();
		Pattern tokSplitter = Pattern.compile(pattern);
		Matcher m = tokSplitter.matcher(text);
		
		while (m.find()) {
			tokens.add(m.group());
		}
		
		return tokens;
	}
	
	/** Train the generator by adding the sourceText */
	@Override
	public void train(String sourceText)
	{
		// TODO: Implement this method
		// DONE:
		if(!sourceText.isEmpty()){
			
			starter = "";
			ArrayList<String>mWordList =  (ArrayList<String>) getTokens("[a-zA-Z]+",sourceText);
			for(String word:mWordList){
				ListNode listNode = checkIfWordNodeInList(starter);
				validateAndAddToWordList(listNode,starter,word);
				starter = word;
			}
			// For Last node
			ListNode lastNode = checkIfWordNodeInList(starter);
			validateAndAddToWordList(lastNode,starter,"");
		}
		else{
			return;
		}
	}
	
	/** 
	 * Generate the number of words requested.
	 */
	@Override
	public String generateText(int numWords) {
	    // TODO: Implement this method
		// DONE:

		starter = "";
		String output = "";
		if(wordList.size()>0){
			for(int i=0;i<numWords;i++){
				ListNode listNode = checkIfWordNodeInList(starter);
				String randomWord = listNode.getRandomNextWord(rnGenerator);
				if(randomWord.isEmpty()){
					i--;
					starter = "";
				}else{
					output += randomWord;
					output += " ";
					starter = randomWord;
				}
			}
		}
		else{
			
		}
		return output;
	}
	
	// Can be helpful for debugging
	@Override
	public String toString()
	{
		String toReturn = "";
		for (ListNode n : wordList)
		{
			toReturn += n.toString();
		}
		return toReturn;
	}
	
	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText)
	{
		// TODO: Implement this method.
		// DONE:
		cleanUpInternally();
		train(sourceText);
	}
	
	// TODO: Add any private helper methods you need here.
	// DONE:
	private void validateAndAddToWordList(ListNode listNode,String word,String nextWord){
		
		if(listNode!=null){
			listNode.addNextWord(nextWord);
		}else{
			listNode = new ListNode(word);
			listNode.addNextWord(nextWord);
			wordList.add(listNode);
		}
	}
	
	private ListNode checkIfWordNodeInList(String text){
		for(ListNode word:wordList){
			if(word.getWord().equals(text)){
				return word;
			}
		}
		return null;
	}
	
	private void cleanUpInternally(){
		for(ListNode word:wordList){
			word.cleanNextWordsList();
		}
		wordList.clear();
	}
	
	/**
	 * This is a minimal set of tests.  Note that it can be difficult
	 * to test methods/classes with randomized behavior.   
	 * @param args
	 */
	public static void main(String[] args)
	{
		// feed the generator a fixed random value for repeatable behavior
		MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
		String textString = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
		System.out.println(textString);
		gen.train(textString);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
		String textString2 = "You say yes, I say no, "+
				"You say stop, and I say go, go, go, "+
				"Oh no. You say goodbye and I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"I say high, you say low, "+
				"You say why, and I say I don't know. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"Why, why, why, why, why, why, "+
				"Do you say goodbye. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"You say yes, I say no, "+
				"You say stop and I say go, go, go. "+
				"Oh, oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello,";
		System.out.println(textString2);
		gen.retrain(textString2);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
	}
}

/** Links a word to the next words in the list 
 * You should use this class in your implementation. */
class ListNode
{
    // The word that is linking to the next words
	private String word;
	
	// The next words that could follow it
	private List<String> nextWords;
	
	ListNode(String word)
	{
		this.word = word;
		nextWords = new LinkedList<String>();
	}
	
	public String getWord()
	{
		return word;
	}

	public void addNextWord(String nextWord)
	{
		nextWords.add(nextWord);
	}
	
	public String getRandomNextWord(Random generator)
	{
		// TODO: Implement this method
	    // The random number generator should be passed from 
	    // the MarkovTextGeneratorLoL class
		// DONE:
		String randomWord = "";
		randomWord = nextWords.get(generator.nextInt(nextWords.size()));
		return randomWord;
	}

	public String toString()
	{
		String toReturn = word + ": ";
		for (String s : nextWords) {
			toReturn += s + "->";
		}
		toReturn += "\n";
		return toReturn;
	}
	
	public void cleanNextWordsList(){
		nextWords.clear();
	}
	
}


