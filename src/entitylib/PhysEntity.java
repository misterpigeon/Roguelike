package entitylib;

import com.sun.javafx.scene.layout.region.Margins;
import commonlib.Node;
import commonlib.SLinkedList;
import javafx.animation.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * This is the child class of Entity and makes it interactable and more dynamic, there is a moveset that corresponds to
 * directions left and right. This is still in progress and I will update whenever I need more functionality.
 */
public class PhysEntity extends Entity{

    protected boolean invincibility; // is it interactable?
    protected SLinkedList<Integer> moveQueue; // queuing moves
    protected Node<Integer> current;
    protected int AnimationSpeed = 3;
    protected BufferedImage[][] moveset;
    protected int RandomMoves = 40;
    protected double hpcontainer; // how much hp in total
    protected double hp; // current hp
    protected int cycle; // what animation loop to run through atm (0(walking), 1(set as death anim), 2, 3, 4, 5 (these are specials, every character has up to 4 available specials))
    protected int prevcycle; // useful for managing cool downs and making animation smoother and seem like they are flowing
    // ***(-1) is the neutral cycle, the animation stops and is frozen on the first frame of the waking animation
    protected int speccycles; // number of special cycles
    private int cyclelength; // cycle length
    protected int flip = 1; // left and right animations, 0 = left, 1 = right
    private int stall = 0; // used to slow down animations to 1/2 of the current refresh rate
    private int framenum = 0; // what frame the loop is currently on
    protected int speed;
    private Random rand;
    protected Point restriction; // restricts where the entity can move
    private boolean AISwitch = false;
    private boolean Dead = false;
    private boolean Symmetrical = false; // is the SpriteSheet's left side the same as the right? this way we just load one side and flip the coordinates instead of the whole thing

    /*
    So how the SpriteSheet works is that there is a left animation, a right animation, and 5 available specials,
    when the PhysEntity is constructed, it will require the length of a cycle meaning all cycles must be the same length.

    There are two partitions, the left and right sprites, if they do not differ then you may select tick a parameter,
    if they do then you must make a copy of the opposite moveset that corresponds to what you want.

    Death and Walking are required!

     */

    public PhysEntity(String _name, String _path, int _columns, int _rows, int _height, int _width, int _hpcontainer,
                      boolean isLive, int _speed, int x, int y, int gc_width, int gc_height, int _specycles,
                      boolean isSymmetrical){

        super(_name, _path, _columns, _rows, _height, _width);
        hpcontainer = _hpcontainer;
        hp = _hpcontainer;
        invincibility = !isLive;
        speed = _speed;
        location = new Point(x, y);
        rand = new Random();
        moveQueue = new SLinkedList();
        for(int i = 1; i <= 12; i++) moveQueue.addHead(0); // first twelve moves are do nothings
        current = moveQueue.getHead();
        restriction = new Point(gc_width, gc_height);
        Symmetrical = isSymmetrical;
        speccycles = _specycles;
        cyclelength = columns;

        // A moveset is generated for the right, and then the left
        if(Symmetrical) {
            moveset = new BufferedImage[speccycles + 2][cyclelength];
        }else{
            moveset = new BufferedImage[(speccycles + 2) * 2][cyclelength];
        }
        // Basically @0 walking @1 death @2 spec 1 @3 spec 2 @4 spec 3 @5 spec 4 // there is a separate sprite list for left and right animations
        // The index works like so: if it is symmetrical then the code handles death and things but only reads half as much, it then just flips the images
        for(int i = 0; i < rows; i++){
            for(int s = 0; s < columns; s++){
                moveset[i][s] = sprites[(i*cyclelength)+s];
            }
        }
    }

    // Overload for advanced speed setting and random generator
    public PhysEntity(String _name, String _path, int _columns, int _rows, int _height, int _width,
                      int _hpcontainer, boolean isLive, int _speed, int x, int y, int gc_width,
                      int gc_height, int _specycles, boolean isSymmetrical, int randommoves, int animspeed){

        super(_name, _path, _columns, _rows, _height, _width);
        hpcontainer = _hpcontainer;
        hp = _hpcontainer;
        invincibility = !isLive;
        speed = _speed;
        location = new Point(x, y);
        rand = new Random();
        moveQueue = new SLinkedList();
        for(int i = 1; i <= 6; i++) moveQueue.addHead(0); // first 6 moves are do nothings
        current = moveQueue.getHead();
        restriction = new Point(gc_width, gc_height);
        Symmetrical = isSymmetrical;
        speccycles = _specycles;
        cyclelength = columns;

        AnimationSpeed = animspeed;
        RandomMoves = randommoves;

        // A move set is generated for the right, and then the left
        if(Symmetrical) {
            moveset = new BufferedImage[speccycles + 2][cyclelength];
        }else{
            moveset = new BufferedImage[(speccycles + 2) * 2][cyclelength];
        }
        // Basically @0 walking @1 death @2 spec 1 @3 spec 2 @4 spec 3 @5 spec 4 // there is a separate sprite list for left and right animations
        // The index works like so: if it is symmetrical then the code handles death and things but only reads half as much, it then just flips the images
        for(int i = 0; i < rows; i++){
            for(int s = 0; s < columns; s++){
                moveset[i][s] = sprites[(i*cyclelength)+s];
            }
        }
    }

    public double getHp() {return hp;}
    public void setNeutral(){
        cycle = -1;
        prevcycle = -1;
    }

    public void setNeutralBlank(){
        cycle = -2;
        prevcycle = -2;
    }
    public int getSpeed(){return speed;}
    public double getHpcontainer() {return hpcontainer;}
    public boolean getAIStatus(){return AISwitch;}

    public void moveLeft(){
        flip = 0;
        if(!Symmetrical) {
            cycle = 0;
        }
        else {
            cycle = 1;
        }
        if(location.getX() >= speed) {
            if(prevcycle == -1 || prevcycle == -2) framenum = 0;
            location.setLocation(location.getX() - speed, location.getY());
        }
        prevcycle = 1;
    }
    public void moveRight(){
        flip = 1;
        if(!Symmetrical) cycle = speccycles + 2;
        else cycle = 1;
        if(location.getX() <= restriction.getX() - speed - width - 4) {
            if(prevcycle == -1 || prevcycle == -2) framenum = 0;
            location.setLocation(location.getX() + speed, location.getY());
        }
        prevcycle = 1;
    }
    public void moveUp(){
        cycle = 1;
        if(location.getY() >= speed + 24) {
            if(prevcycle == -1 || prevcycle == -2) framenum = 0;
            location.setLocation(location.getX(), location.getY() - speed);
        }
        prevcycle = 1;
    }
    public void moveDown(){
        cycle = 1;
        if(location.getY() <= restriction.getY() - speed - height) {
            if(prevcycle == -1 || prevcycle == -2) framenum = 0;
            location.setLocation(location.getX(), location.getY() + speed);
        }
        prevcycle = 1;
    }

    // Generate will print an hp bar as well
    @Override
    public void generate(Graphics _gc) { // generate deals with the actual printing of the graphic, the default generate generates to what the current location is
        if(Dead) return;
        if(stall++ % AnimationSpeed == 0) framenum++;
        // if the character's hp reaches zero
        if(hp == 0) {
            if(prevcycle != 420 ){
                prevcycle = 420;
                framenum = 0;
            }
            if(Symmetrical) {
                if (flip == 0) { // if facing left
                    _gc.drawImage(moveset[1][framenum], location.x, location.y, null);
                }else {
                    _gc.drawImage(moveset[1][framenum], location.x + width, location.y, -width, height, null);
                }
            }
            else{
                _gc.drawImage(moveset[(2 + flip * rows/2) - 1][framenum], location.x, location.y, null);
            }
            if(framenum == columns - 1) {
                framenum = 0;
                Dead = true;
            }

        }
        else {

            if (Symmetrical) {
                if (flip == 0) { // if facing left
                    if (cycle == -1) _gc.drawImage(moveset[0][0], location.x, location.y, null);
                    else if (cycle == -2) ;
                    else _gc.drawImage(moveset[0][framenum], location.x, location.y, null);
                } else {
                    if (cycle == -1) _gc.drawImage(moveset[0][0], location.x + width, location.y, -width, height, null);
                    else if (cycle == -2) ;
                    else
                        _gc.drawImage(moveset[0][framenum], location.x + width, location.y, -width, height, null); // horizontal flip
                }
            } else {
                if (cycle == -1) {
                    if (flip == 0) {
                        _gc.drawImage(moveset[0][0], location.x, location.y, null);
                    } else {
                        _gc.drawImage(moveset[2 + speccycles][0], location.x, location.y, null);
                    }
                } else if (cycle == -2) ;
                else
                    _gc.drawImage(moveset[cycle][framenum], location.x, location.y, width, height, null); // horizontal flip
            }
            if (framenum == cyclelength - 1) framenum = 0;
            // hp bar code
            _gc.setColor(Color.BLACK);
            _gc.fillRect((int) (location.x + (width - (60 + (20 - hpcontainer))) / 2), location.y - 21, (int) (60 + (20 - hpcontainer)), 18);
            _gc.setColor(Color.RED);
            _gc.fillRect((int) (4 + location.x + (width - (60 + (20 - hpcontainer))) / 2), 4 + location.y - 21, (int) ((60 + (20 - hpcontainer) - 8) * (hp / hpcontainer)), 10);
            _gc.setColor(Color.WHITE);

            // AI CONTROL
            int move = rand.nextInt(5);
            if (AISwitch == true) {
                if (current.value == 0) { // neutral
                    setNeutral();
                } else if (current.value == 1) { // w
                    moveUp();
                } else if (current.value == 2) { // a
                    moveLeft();
                } else if (current.value == 3) { // s
                    moveDown();
                } else if (current.value == 4) { // d
                    moveRight();
                } else if (current.value == -3) { // print blank
                    setNeutralBlank();
                }
                if (current.next != null) { // if the next move has been set then move on to the next move
                    current = current.next;
                } else { // if not then generate the next move set
                    for (int i = 1; i <= RandomMoves; i++)
                        moveQueue.addTail(move); // generates 40 identical moves each time
                    current = current.next;
                }
            }
        }
    }

    public void generate(Graphics _gc, int x, int y){ // generate at specific position, used only to initialize and to test
        if(stall++ % AnimationSpeed == 0) framenum++;

        if(Symmetrical) {
            if (flip == 0) { // if facing left
                if(cycle == -1) _gc.drawImage(moveset[0][0], x, y, null);
                else if(cycle == -2);
                else _gc.drawImage(moveset[0][framenum], x, y, null);
            }else {
                if(cycle == -1) _gc.drawImage(moveset[0][0], x + width, y, -width, height, null);
                else _gc.drawImage(moveset[0][framenum], x + width, y, -width, height, null); // horizontal flip
            }
        }
        else{
            if(cycle == -1){
                if(flip == 0){
                    _gc.drawImage(moveset[0][0], x, y, null);
                }else{
                    _gc.drawImage(moveset[2 + speccycles][0], x, y, null);
                }
            }
            else if(cycle == -2);
            else _gc.drawImage(moveset[cycle][framenum], x, y, width, height, null); // horizontal flip
        }
        if(framenum == cyclelength - 1) framenum = 0;
        // hp bar code
        _gc.setColor(Color.BLACK);
        _gc.fillRect((int)(x + (width - (60 + (20 - hpcontainer)))/2), y - 21, (int)(60 + (20 - hpcontainer)), 18);
        _gc.setColor(Color.RED);
        _gc.fillRect((int)(4 + x + (width - (60 + (20 - hpcontainer)))/2), 4 + y - 21, (int)(60 + (20 - hpcontainer) - 8), 10);
        _gc.setColor(Color.WHITE);

        // AI CONTROL
        int move = rand.nextInt(5);
        if(AISwitch == true){
            if(current.value == 0){ // neutral
                setNeutral();
            }
            else if(current.value == 1){ // w
                moveUp();
            }
            else if(current.value == 2){ // a
                moveLeft();
            }
            else if(current.value == 3){ // s
                moveDown();
            }
            else if(current.value == 4){ // d
                moveRight();
            }
            else if(current.value == -3){ // print blank
                setNeutralBlank();
            }

            if(current.next != null){ // if the next move has been set then move on to the next move
                current = current.next;
            }else{ // if not then generate the next move set
                for(int i = 1; i <= RandomMoves; i++) moveQueue.addTail(move); // generates 40 identical moves each time
                current = current.next;
            }
        }
    }

    public void startAI(){ // this starts the AI algo for randomly moving
        AISwitch = true; // that's it!
    }

    public void stopAI(){ // this stops the AI algo for randomly moving
        AISwitch = false; // that's it!
    }

    public boolean isDead(){
        return Dead;
    }

    public void damage(int hit, int side){
        Node<Integer> temp = current;
        startAI();
        if(hit >= hp) {
            hp = 0;
            for(int i = 0; i <= 2; i++){
                for(int x = 0; x <= 2; x++){
                    temp.next = new Node(0, temp.next);
                    temp = temp.next;
                }
                for(int x = 0; x <= 2; x++){
                    temp.next = new Node(-3, temp.next);
                    temp = temp.next;
                }
            }
        }
        else {
            hp = hp - hit;
            for(int i = 0; i <= 2; i++){
                for(int x = 0; x <= 2; x++){
                    temp.next = new Node(0, temp.next);
                    temp = temp.next;
                }
                for(int x = 0; x <= 2; x++){
                    temp.next = new Node(-3, temp.next);
                    temp = temp.next;
                }
            }
        }

        System.out.println("HIT: "+hit);
        System.out.println("HEALTH: "+hp);

    }
}
