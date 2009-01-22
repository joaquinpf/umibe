/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VideoInfoDialog.java
 *
 * Created on 14/01/2009, 18:33:14
 */

package ar.com.umibe.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import ar.com.umibe.util.GuiUtils;
import ar.com.umibe.util.UmibeFileUtils;
import ar.com.umibe.util.VideoUtils;

import com.golden.gamedev.util.ImageUtil;

/**
 *
 * @author Kireta
 */
public class VideoInfoDialog extends javax.swing.JDialog {

    /** Creates new form VideoInfoDialog */
    public VideoInfoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
		GuiUtils.setIcon(this);
        GuiUtils.centerOnScreen(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.setMaximumSize(new java.awt.Dimension(320, 240));
        jLabel1.setMinimumSize(new java.awt.Dimension(320, 240));
        jLabel1.setName("jLabel1"); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(320, 240));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        UmibeFileUtils.delete(scs);
        UmibeFileUtils.delete(mediainfo);
    }//GEN-LAST:event_formWindowClosing

    public void loadInfo(String file) {
    	File f = new File(file);
    	if(f.exists()){
    		scs = VideoUtils.takeScreenshot(file);
    		mediainfo = VideoUtils.getMediaInfo(file);
    		try {
    			if(scs != null){
    				BufferedImage b = ImageIO.read(new File(scs));
    				b = ImageUtil.resize(b, 361, 240);
    				Image i = Toolkit.getDefaultToolkit().createImage(b.getSource());
    				ImageIcon icon = new ImageIcon(i);
    				jLabel1.setIcon(icon);
    				// setMargin(new Insets(0,0,0,0));
    				jLabel1.setIconTextGap(0);
    				// setBorderPainted(false);
    				jLabel1.setText(null);
    				jLabel1.setSize(320, 240);
    			}
			    FileReader fileStream = new FileReader(mediainfo);
			    jTextArea1.read( fileStream,mediainfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VideoInfoDialog dialog = new VideoInfoDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

	private String scs = null;
	private String mediainfo = null;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}
