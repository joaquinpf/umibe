import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ImageSelectionPanel extends JPanel
{
	private static final long serialVersionUID = 3543129833148371751L;
	private Point start = new Point();
    private Point end   = new Point();
    private Rectangle rect = new Rectangle();

    public ImageSelectionPanel()
    {
        addMouseMotionListener( new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent ev)
            {
                end = ev.getPoint();
                rect.setFrameFromDiagonal(start, end);
                repaint();
            }//end mouse drag
        });
        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                start = e.getPoint();
            }

            public void mouseReleased(MouseEvent ev)
            {
                rect.setFrameFromDiagonal(start, start);
                repaint();
            }
        });
    }
    
    public ImageSelectionPanel(ImageIcon img)
    {
        JLabel image = new javax.swing.JLabel();
        image.setLocation(0, 0); 
        image.setSize(img.getIconWidth(),img.getIconHeight());
        image.setIcon(img);
        image.setVisible(true);
        
        add(image);
        setSize(img.getIconWidth(),img.getIconHeight());
        addMouseMotionListener( new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent ev)
            {
                end = ev.getPoint();
                rect.setFrameFromDiagonal(start, end);
                repaint();
            }//end mouse drag
        });
        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                start = e.getPoint();
            }

            public void mouseReleased(MouseEvent ev)
            {
                rect.setFrameFromDiagonal(start, start);
                repaint();
            }
        });
    }

    public void setImage(ImageIcon img){
        JLabel image = new javax.swing.JLabel();
        image.setLocation(0, 0); 
        image.setSize(img.getIconWidth(),img.getIconHeight());
        image.setIcon(img);
        image.setVisible(true);
        
        this.add(image);
        this.setSize(img.getIconWidth(),img.getIconHeight());
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        // Draw line being dragged.
        g2.setPaint(Color.red);
        g2.draw(rect);
    }
	
    public static void main(String[] args)
    {
        ImageIcon img = new ImageIcon("c:/pampita.jpg"); 
        ImageSelectionPanel test = new ImageSelectionPanel(img);
        test.setVisible(true);
        JDialog f = new JDialog();
        f.setSize(img.getIconWidth()+20,img.getIconHeight()+40);
        //f.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
        f.add(test);
        f.setVisible(true);
    }
}