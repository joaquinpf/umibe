/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ProfileMakerDialog.java
 *
 * Created on Jan 2, 2009, 3:59:21 PM
 */

package ar.com.umibe.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import ar.com.umibe.core.DataModel;
import ar.com.umibe.core.tool.Tool;
import ar.com.umibe.core.tool.ToolConfigDialog;
import ar.com.umibe.util.GuiUtils;
import javax.swing.JFrame;

/**
 *
 * @author Kireta
 */
public class ProfileMakerDialog extends javax.swing.JDialog {

    /** Creates new form ProfileMakerDialog */
    public ProfileMakerDialog(java.awt.Frame parent, boolean modal) {
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

        jPanel1 = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jButton10 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 345));

        jTextField6.setBackground(new java.awt.Color(255, 255, 255));
        jTextField6.setEditable(false);
        jTextField6.setName("jTextField6"); // NOI18N
        jTextField6.setText(DataModel.INSTANCE.getMoveAfterDone());

        jLabel7.setText("Move to directory");
        jLabel7.setName("jLabel7"); // NOI18N

        jSlider1.setMaximum(10);
        jSlider1.setValue(3);
        jSlider1.setName("jSlider1"); // NOI18N
        this.jSlider1.setValue(DataModel.INSTANCE.getPriority());

        jButton10.setText("Open");
        jButton10.setName("jButton10"); // NOI18N

        jLabel6.setText("Default priority");
        jLabel6.setName("jLabel6"); // NOI18N

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setEditable(false);
        jTextField1.setName("jTextField1"); // NOI18N
        jTextField1.setText(DataModel.INSTANCE.getDoneDir());

        jButton2.setText("Open");
        jButton2.setName("jButton2"); // NOI18N

        jLabel1.setText("Output folder");
        jLabel1.setName("jLabel1"); // NOI18N

        jComboBox1.setName("jComboBox1"); // NOI18N
        String[] profiles = DataModel.INSTANCE.loadProfiles("video_");
        if(profiles != null){
            for(int i = 0; i < profiles.length; i++){
                jComboBox1.addItem(profiles[i]);
                if(DataModel.INSTANCE.getVProfile().equals("profiles/" + profiles[i])){
                    jComboBox1.setSelectedIndex(i);
                }
            }
        }

        jLabel4.setText("Video profile");
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel3.setText("Audio profile");
        jLabel3.setName("jLabel3"); // NOI18N

        jComboBox2.setName("jComboBox2"); // NOI18N
        profiles = DataModel.INSTANCE.loadProfiles("audio_");
 if(profiles != null){
     for(int i = 0; i < profiles.length; i++){
         jComboBox2.addItem(profiles[i]);
        if(DataModel.INSTANCE.getAProfile().equals("profiles/" + profiles[i])){
            jComboBox2.setSelectedIndex(i);
         }
     }
 }

        jLabel5.setText("AviSynth profile");
        jLabel5.setName("jLabel5"); // NOI18N

        jComboBox3.setName("jComboBox3"); // NOI18N
        profiles = DataModel.INSTANCE.loadProfiles("avisynth_");
 if(profiles != null){
     for(int i = 0; i < profiles.length; i++){
         jComboBox3.addItem(profiles[i]);
         if(DataModel.INSTANCE.getAviSynthProfile().equals("profiles/" + profiles[i])){
            jComboBox3.setSelectedIndex(i);
         }
     }
 }

        jLabel2.setText("Profile maker");
        jLabel2.setName("jLabel2"); // NOI18N

        jTextField2.setText("Default");
        jTextField2.setName("jTextField2"); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel8.setText("Name");
        jLabel8.setName("jLabel8"); // NOI18N

        jButton4.setText("New...");
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("New...");
        jButton5.setName("jButton5"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField1)
                            .addComponent(jTextField6)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 263, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addComponent(jButton10)
                            .addComponent(jButton5)
                            .addComponent(jButton4))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(158, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(154, 154, 154))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(10, 10, 10)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10))
                .addGap(5, 5, 5)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Save Profile");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Cancel");
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton3))
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		Element root = new Element("Config");
		Element el = new Element("DoneDirectory");
		el.setText(jTextField1.getText());
		root.addContent(el);
		el = new Element("MoveToAfterDone");
		el.setText(jTextField6.getText());
		root.addContent(el);
		el = new Element("DefaultAudioProfile");
		el.setText("profiles/" + (String)jComboBox2.getSelectedItem());
		root.addContent(el);
		el = new Element("DefaultVideoProfile");
		el.setText("profiles/" + (String)jComboBox1.getSelectedItem());
		root.addContent(el);
		el = new Element("DefaultAviSynthProfile");
		el.setText("profiles/" + (String)jComboBox3.getSelectedItem());
		root.addContent(el);
		el = new Element("DefaultPriority");
		el.setText(Integer.toString(jSlider1.getValue()));
		root.addContent(el);

		Document doc = new Document(root);
		// serialize it onto System.out
		try {
			XMLOutputter serializer = new XMLOutputter();
			Format f = serializer.getFormat();
			f.setIndent("  ");
			serializer.setFormat(f);
			String name = jTextField2.getText();
			if(name.equals("")){
				name = "Default";
			}
			File file = new File("./profiles/Profile_" + name + ".xml");
			if (file.exists())
				name += this.hashCode();
			FileWriter fileWriter = new FileWriter("./profiles/Profile_" + name + ".xml");
			serializer.output(doc, fileWriter);
			fileWriter.flush();
			fileWriter.close();
            this.setEnabled(false);
            this.setVisible(false);
		} catch (IOException e) {
			System.err.println(e);
		}

}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.setEnabled(false);
        this.setVisible(false);
}//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        ToolConfigDialog t = new ToolConfigDialog((JFrame)this.getOwner(), true);
        t.addTool(new Tool("x264.xml"));
        t.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ProfileMakerDialog dialog = new ProfileMakerDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables

}
