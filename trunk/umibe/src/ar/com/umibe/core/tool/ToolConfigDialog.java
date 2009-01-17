package ar.com.umibe.core.tool;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import ar.com.umibe.util.GuiUtils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ToolConfigDialog.java
 *
 * Created on 16/01/2009, 14:41:00
 */

/**
 *
 * @author Usuario
 */
public class ToolConfigDialog extends javax.swing.JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1591150352147988248L;
	/** Creates new form ToolConfigDialog */
    public ToolConfigDialog(java.awt.Frame parent, boolean modal) {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new ToolOptionsTable();
        jToggleButton1 = new javax.swing.JToggleButton();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Option", "Key", "Value", "Enabled"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jToggleButton1.setText("Generate profile");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jTextField1.setText("Default");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jToggleButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    	ToolMode mode = (ToolMode)jComboBox1.getSelectedItem();
        setSteps();
        models = null;
        setTable(mode);
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        tool = (Tool)jComboBox2.getSelectedItem();
    	setModes();
        setSteps();
        models = null;
    	setTable((ToolMode)jComboBox1.getSelectedItem());
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
    	setTable((ToolMode)jComboBox1.getSelectedItem());
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        saveProfile();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    public void addTool(Tool t){
    	jComboBox2.addItem(t);
    }
    
    public void addTool(ArrayList<Tool> t){
    	for(int i=0; i<t.size(); i++)
    		jComboBox2.addItem(t.get(i));
    }
    
    private void setTable(ToolMode mode){
    	int step = jComboBox3.getSelectedIndex();
    	if(models == null) {
    		
    		models = new ArrayList<DefaultTableModel>();
    		
    		Hashtable<String,ToolOption> opt = tool.getOptions();

    		for(int i=1;i<=mode.steps.size();i++){
    			
    			DefaultTableModel model = getModel();
    			
    			Enumeration<String> enu = opt.keys();

    			while(enu.hasMoreElements()){
    				String key = enu.nextElement();
    				ToolOption option = opt.get(key);
    				if(option.modes.contains("ALL") || option.modes.contains(mode.name)){
    					Object row [] = {option, option.key, option.value, true};
    					model.addRow(row);
    				}
    			}
    			models.add(model);
    		}
    	}
    	jTable1.setModel(models.get(step));
    }

    private void setModes(){
        ArrayList<ToolMode> a = tool.getModes();
        int itemsBefore = jComboBox1.getItemCount();
        for(int i=0;i<a.size();i++){
            jComboBox1.addItem(a.get(i));
        }
        for(int i=0; i<itemsBefore; i++){
        	jComboBox1.removeItemAt(0);
        }
    }
    
    private void setSteps(){
    	ToolMode m = (ToolMode)jComboBox1.getSelectedItem();
        int itemsBefore = jComboBox3.getItemCount();
        for(int i=1;i<=m.steps.size();i++){
            jComboBox3.addItem("Pass " + i);
        }
        for(int i=0; i<itemsBefore; i++){
        	jComboBox3.removeItemAt(0);
        }
    }

    private void saveProfile() {
    	Element root = new Element("Profile");
		Element el = new Element("Name");
		el.setText(jTextField1.getText());
		root.addContent(el);               
		
		el = new Element("EncoderName");
		el.setText(((Tool)jComboBox2.getSelectedItem()).getName());
		root.addContent(el);               
		
		el = new Element("EncoderPassNumber");
		int steps = jComboBox3.getItemCount();
		el.setText(Integer.toString(steps));
		root.addContent(el);   

		for (int i = 1; i <= steps; i++) {
			DefaultTableModel m = models.get(i-1);
			String key = "PassOption" + i;
			Element passoptions = new Element(key + "s");
			for(int j=0; j<m.getRowCount(); j++){
				if((Boolean)m.getValueAt(j, 3) == true){
					el = new Element(key);
					el.setText(m.getValueAt(j, 1) + " " + m.getValueAt(j, 2));
					passoptions.addContent(el);     
				}
    		}
    		root.addContent(passoptions);
    	}
    	Document doc = new Document(root);
    	// serialize it onto System.out
    	try {
    		XMLOutputter serializer = new XMLOutputter();
    		Format f = serializer.getFormat();
    		f.setIndent("  ");
    		serializer.setFormat(f);
    		String name = ((Tool)jComboBox2.getSelectedItem()).getName() + "_" + jTextField1.getText() + ".xml";
    		FileWriter fileWriter = new FileWriter("./config/" + name);
    		serializer.output(doc, fileWriter);
    		fileWriter.flush();
    		fileWriter.close();
    	} catch (IOException e) {
    		System.err.println(e);
    	}
    }


    private Document openDocument(final String route) {
    	Document doc = null;
    	SAXBuilder builder = new SAXBuilder();


    	try {
    		return builder.build(route);
    	} catch (JDOMException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return doc;
    }
    
    private DefaultTableModel getModel(){
    	return new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Option", "Key", "Value", "Enabled"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            };
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ToolConfigDialog dialog = new ToolConfigDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                Tool t = new Tool("x264.xml");
                dialog.addTool(t);
                t = new Tool("neroaacenc.xml");
                dialog.addTool(t);
                dialog.setVisible(true);;
            }
        });
    }

    private ArrayList<DefaultTableModel> models = null;
    private Tool tool;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables

}
