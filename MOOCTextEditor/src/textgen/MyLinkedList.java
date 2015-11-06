package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		// TODO: Implement this method
		// DONE:
		head = new LLNode<E>(null);
		tail = new LLNode<E>(null);
		head.next = tail;
		tail.prev = head;
		size = 0;
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element ) 
	{
		// TODO: Implement this method
		// DONE:
		if(element != null){
			LLNode<E> current = head;
			while((current.next != null) && (!current.next.equals(tail))){
				current = current.next;
			}
			new LLNode<E>(element,current);
			size++;
			return true;
		}else{
			return false;
		}
	}

	/** Get the element at position index 
	 * @throws IndexOutOfBoundsException if the index is out of bounds. */
	public E get(int index) throws IndexOutOfBoundsException
	{
		// TODO: Implement this method.
		// DONE:
		if(index<0 || index>=size){
			throw new IndexOutOfBoundsException();
		}else{
			LLNode<E> current = head.next;
			for(int i=0;i<size;i++){
				if(i == index){
					return current.data;
				}
				current = current.next;
			}
		}
		return null;
	}

	/**
	 * Add an element to the list at the specified index
	 * @param The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) 
	{
		// TODO: Implement this method
		// DONE:
		if(index<0 || index>size){
			throw new IndexOutOfBoundsException();
		}else{
			int mIndex = 0;
			if(element != null){
				LLNode<E> current = head;
				while((current.next != null) && (!current.next.equals(tail)) && 
						(mIndex != index)){
					current = current.next;
					mIndex++;
				}
				new LLNode<E>(element,current);
				size++;
			}else{
				throw new NullPointerException();
			}
		}
	}


	/** Return the size of the list */
	public int size() 
	{
		// TODO: Implement this method
		// DONE:
		return size;
	}

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 * 
	 */
	public E remove(int index) 
	{
		// TODO: Implement this method
		// DONE:
		if(index<0 || index>size){
			throw new IndexOutOfBoundsException();
		}else{
			int mIndex = 0;
			LLNode<E> current = head;
			LLNode<E> nodeToBeRemoved = null;
			while((current.next != null) && (!current.next.equals(tail)) && 
					(mIndex != index)){
				current = current.next;
				mIndex++;
			}
			nodeToBeRemoved = current.next;
			adjustAfterRemoval(current,nodeToBeRemoved);
			size--;
			return nodeToBeRemoved.data;
		}
	}
	
	private void adjustAfterRemoval(LLNode<E> current,LLNode<E> nodeToBeRemoved){
		current.next = nodeToBeRemoved.next;
		nodeToBeRemoved.next.prev = current;
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element) 
	{
		// TODO: Implement this method
		if(index<0 || index>size){
			throw new IndexOutOfBoundsException();
		}else{
			int mIndex = 0;
			LLNode<E> current = head;
			LLNode<E> nodeToBeRemoved = null;
			while((current.next != null) && (!current.next.equals(tail)) && 
					(mIndex != index)){
				current = current.next;
				mIndex++;
			}
			nodeToBeRemoved = current.next;
			new LLNode<E>(element,current);
			return nodeToBeRemoved.data;
		}
	}   
}

class LLNode<E> 
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	// TODO: Add any other methods you think are useful here
	// E.g. you might want to add another constructor
	// DONE:

	public LLNode(E e) 
	{
		this.data = e;
		this.prev = null;
		this.next = null;
	}
	
	public LLNode(E e,LLNode<E> prev) 
	{
		this.data = e;
		
		this.next = prev.next;
		prev.next.prev = this;
		prev.next = this;
		this.prev = prev;
	}

}
