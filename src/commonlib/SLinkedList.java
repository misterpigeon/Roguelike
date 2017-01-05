package commonlib;

/**
 * A linked list contains references to the head and tail node and allows someone
 * to traverse the list by using "next". This is a Singly Linked List meaning it doesn't
 * contain a reference to the previous node
 */
public class SLinkedList<T>{
    private Node<T> head;
    private Node<T> tail;
    private int size;
    public SLinkedList(){
        head = null;
        tail = null;
        size = 0;
    }
    public void addHead(T value){
        if(size == 0){
            head = new Node(value, null);
            tail = head;
            size++;
        }else{
            head = new Node(value, head);
            size++;
        }
        //System.out.println("Added Head : "+value);
    }
    public void addTail(T value){
        if(size == 0){
            addHead(value);
        }else{
            tail = tail.next = new Node(value, null);
            size++;
        }
        //System.out.println("Added Tail : "+value);
    }
    public Node<T> removeHead(){
        Node<T> remove = head;
        head = head.next;
        size--;
        //System.out.println("Removed Head : "+remove.value);
        return remove;
    }
    public Node<T> removeTail(){
        Node<T> temp = head;
        for(int x = 1; x < size - 1; x++){
            temp = temp.next;
        }
        Node<T> remove = temp.next;
        tail = temp;
        size--;
        //System.out.println("Removed Tail : "+remove.value);
        return remove;
    }
    public Node<T> getHead(){
        return head;
    }
    public Node<T> getTail(){
        return tail;
    }
    public int getSize(){
        return size;
    }
}
