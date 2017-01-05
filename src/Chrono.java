import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Chrono implements ActionListener {
    Heft gc;

    Chrono(Heft _gc){ // gc stands for gamecanvas
        this.gc = _gc;
    }

    public void actionPerformed(ActionEvent arg0) {
        gc.HeftRepaint();
    }
}
