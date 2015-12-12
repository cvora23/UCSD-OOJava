package spelling;

import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author You
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;
    

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
	}
		
	private boolean addIfWord(TrieNode nextNode,String lcWord){
		boolean retVal = false;
		if(nextNode.getText().equals(lcWord) &&	
				nextNode.endsWord() == false){
			nextNode.setEndsWord(true);
			retVal = true;
		}
		return retVal;
	}
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should ignore the word's case.
	 * That is, you should convert the string to all lower case as you insert it. */
	public boolean addWord(String word)
	{
	    // TODO: Implement this method.
		//  DONE:
		boolean retVal = false;
		String lcWord = word.toLowerCase();
		TrieNode nextNode = null,currNode = root;
		for (char c : lcWord.toCharArray()){
			nextNode = currNode.insert(c);
			if(nextNode != null ){
				if(addIfWord(nextNode,lcWord)){
					retVal = true;
					break;
				}else{
					currNode = nextNode;
				}
			}else{
				currNode = currNode.getChild(c);
				if(addIfWord(currNode,lcWord)){
					retVal = true;
					break;
				}
			}
		}
	    return retVal;
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
	    // TODO: Implement this method
		// DONE:
 		if (root == null) 
 			return 0;
 		
 		return size(root);

	}
	
	private int size(TrieNode curr){
 		TrieNode next = null;
 		int count = 0;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			if(next.endsWord() == true){
 				count++;
 			}
 			count += size(next);
 		}
 		return count;
	}
	
	/** Returns whether the string is a word in the trie */
	@Override
	public boolean isWord(String s) 
	{
	    // TODO: Implement this method
		// DONE:
 		if (root == null) 
 			return false;
 		else
 			return isWord(root,s.toLowerCase());
	}
	
	private boolean isWord(TrieNode curr,String string){
 		TrieNode next = null;
 		boolean retVal = false;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			if(next.getText().equals(string)){
 				if(next.endsWord()){
 	 				return true;
 				}
 			}else{
 				retVal = isWord(next,string);
 				if(retVal == true){
 					break;
 				}
 			}
 		}
 		return retVal;
	}
	
	private TrieNode isStem(String prefix){
 		if (root == null) 
 			return null;
 		else
 			return isStem(root,prefix.toLowerCase());
	}
	
	private TrieNode isStem(TrieNode curr,String string){
 		TrieNode next = null;
 		TrieNode retVal = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			if(next.getText().equals(string)){
 	 			return next;
 			}else{
 				retVal = isStem(next,string);
 				if(retVal != null){
 					break;
 				}
 			}
 		}
 		return retVal;
	}

	/** 
	 *  * Returns up to the n "best" predictions, including the word itself,
     * in terms of length
     * If this string is not in the trie, it returns null.
     * @param text The text to use at the word stem
     * @param n The maximum number of predictions desired.
     * @return A list containing the up to n best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 // DONE:
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    	 //    empty list
    	 // 2. Once the stem is found, perform a breadth first search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList) and add the node that completes the stem to the back
    	 //       of the list.
    	 //    Create a list of completions to return (initially empty)
    	 //    While the queue is not empty and you don't have enough completions:
    	 //       remove the first Node from the queue
    	 //       If it is a word, add it to the completions list
    	 //       Add all of its child nodes to the back of the queue
    	 // Return the list of completions
    	 List<String> completionList = new LinkedList<String>();
    	 TrieNode stemNode = null,curr = null,next = null;
    	 
    	 if(prefix.isEmpty()){
    		 stemNode = root;
    	 }else{
    		 stemNode = isStem(prefix);
    	 }
    	 
    	 if(stemNode != null && numCompletions > 0){
    		 List<TrieNode> queuePredictions = new LinkedList<TrieNode>();
    		 queuePredictions.add(stemNode);
    		 while((completionList.size() != numCompletions) && !queuePredictions.isEmpty()){
    			 curr = queuePredictions.remove(0);
    			 if(curr.endsWord()){
    				 completionList.add(curr.getText());
    			 }
    		 	for (Character c : curr.getValidNextCharacters()) {
    		 			next = curr.getChild(c);
    		 			queuePredictions.add(next);
    		 	}
    		 }
    	 }
         return completionList;
     }

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		if(curr.endsWord())
 			System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}