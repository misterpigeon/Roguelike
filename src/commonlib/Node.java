package commonlib;

/**
 * A node contains a value and a reference to the "next" node
 */
public class Node<T>{
    public Node<T> next;
    public T value;
    public Node(T _value, Node<T> _next){
        next = _next;
        value = _value;
    }
}
