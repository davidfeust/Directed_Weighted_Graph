package trys;

import javax.swing.*;
import java.awt.*;

public class overlay {
    public static class DrawOverTest {

        public static void main(String[] args) {
            JFrame frame = new JFrame("glass pane test");
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(4,1));
            panel.add(new JButton("my button"));
            panel.add(new JLabel("my button"));
            panel.add(new JButton("my button"));
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);


            LabelGlassPane glass = new LabelGlassPane(frame);
            frame.setGlassPane(glass);
            glass.setVisible(true);
        }
    }

    static class LabelGlassPane extends JComponent {
        public LabelGlassPane(JFrame frame) {
            this.frame = frame;
            //this.addMouseListener(new MouseAdapter() {         });
        }
        public JFrame frame;
        public void paint(Graphics g) {
            g.setColor(Color.red);
            Container root = frame.getContentPane();
            g.setColor(new Color(100,100,100,100));
            rPaint(root,g);
        }
        private void rPaint(Container cont, Graphics g) {
            for(int i=0; i<cont.getComponentCount(); i++) {
                Component comp = cont.getComponent(i);
                if(!(comp instanceof JPanel)) {
                    int x = comp.getX();
                    int y = comp.getY();
                    int w = comp.getWidth();
                    int h = comp.getHeight();
                    g.drawRect(x+4,y+4,w-8,h-8);
                    g.drawString(comp.getClass().getName(),x+10,y+20);
                }
                if(comp instanceof Container) {
                    rPaint((Container)comp,g);
                }
            }
        }
    }
}
