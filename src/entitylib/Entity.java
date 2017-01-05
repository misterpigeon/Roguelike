package entitylib;
import SpriteCtrl.*;

import java.awt.*;
import java.awt.Point;
import java.io.*;

/**
 * This is the base abstract entity class, it extends the functionality of the SpriteSheet class
 */
public abstract class Entity extends SpriteSheet {
    protected final String name;
    protected Point location; // the Point class already contains a lot of methods needed (thank god), we will not set these values until the entity has been generated
    public abstract void generate(Graphics _gc);
    public Entity(String _name, String _path, int _columns, int _rows, int _height, int _width){
        super(_path, _columns, _rows, _height, _width);
        name = _name;
    };
}

