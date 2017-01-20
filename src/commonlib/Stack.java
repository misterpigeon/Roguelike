package commonlib;

/**
 * Created by steve on 2017-01-12.
 */
public class Stack<T> { // we can think of the head in a SLinkedList as the 'top' of a stack
    private Node<T> top;
    private int size;
    public Stack(){
        top = null;
        size = 0;
    }
    public void push(T temp){ // add to the top of stack
        if(top == null) top = new Node<T>(temp, null);
        else top = new Node<T>(temp, top);
    }
    public T pop(){ // remove top of stack
         if(size <= 0) {
             System.out.println("Empty Stack");
             return null;
         }
         else{
             T remove = top.value;
             top = top.next;
             return remove;
         }
    }
}
