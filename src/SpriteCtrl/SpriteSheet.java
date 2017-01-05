package SpriteCtrl;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This is the SpriteSheet object that loads the .png file that contains all the frames
 * The SpriteSheet has different functionality than the Sprite object itself as the SpriteSheet is
 * aggregated by the Sprite object, the Sprite interacts with the game and controls the SpriteSheet
 * while the SpriteSheet interacts with the image input and output.
 */
public class SpriteSheet{
    protected BufferedImage sheetImg; // I do not make this final as reading may throw an IO Exception
    protected BufferedImage[] sprites;

    protected final String path;
    protected final int columns;
    protected final int rows;
    protected final File file;
    // protected final int frames; // this allows for incomplete tables of sprites
    // These are the height and width of a sprite in the SpriteSheet
    // ASSUMES THAT ALL SPRITES IN SHEET ARE THE SAME SIZE
    protected final int height;
    protected final int width;

    /**
     * This loads the BufferedImage and sets up the SpriteSheet for use (using other methods laid out in this class)
     * @param _path this is the path of the SpriteSheet.png
     * @param _columns columns in SpriteSheet
     * @param _rows rows in SpriteSheet
     * @param _height height of sprite in px
     * @param _width width of sprite in px
     */
    public SpriteSheet(String _path, int _columns, int _rows, int _height, int _width){
        path = _path;
        columns = _columns;
        rows = _rows;
        height = _height;
        width = _width;
        file = new File(path);
        try {
            sheetImg = ImageIO.read(file);
        }
        catch(IOException e) {
            System.out.println("IMAGE FAILED TO LOAD");
        }

        sprites = new BufferedImage[columns * rows];

        // This assumes that the SpriteSheet image is in a specific format, mainly all animation on one row with each animation being the same length/number of frames
        for(int i = 0; i < rows; i++){
            for(int x = 0; x < columns; x++){
                    sprites[i*columns + x] = sheetImg.getSubimage(x * width, i * height, width, height);
            }
        }
    }

    public BufferedImage getSprite(int pos){
        return sprites[pos];
    }

    public int getSpriteHeight(){
        return height;
    }

    public int getSpriteWidth(){
        return width;
    }
}

