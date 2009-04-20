package ar.com.umibe.core.filter;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;

import ar.com.umibe.commons.util.GuiUtils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DefaultFilterWindow.java
 *
 * Created on 19/01/2009, 11:00:01
 */


/**
 * @author Joaqu�n Alejandro P�rez Fuentes
 */
public class DefaultFilterWindow extends javax.swing.JDialog implements IFilterWindow {

	private static final long serialVersionUID = -3762405095883290337L;
	/** Creates new form DefaultFilterWindow */
    public DefaultFilterWindow(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
		GuiUtils.centerOnScreen(this);
		GuiUtils.setIcon(this);
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
        jTable1 = new FilterDefaultTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter", "Description", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Accept");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(306, 306, 306)
                .addComponent(jButton1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    	this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

	@Override
	public Filter getFilter() {
		if(filter!=null){
			DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
			for(int i=0;i<model.getRowCount();i++){
				filter.setParameter(((String)model.getValueAt(i, 0)), ((String)model.getValueAt(i, 2)));
			}
			return filter;
		}
		return null;
	}
	@Override
	public void setFilter(Filter f) {
		this.filter = f;
		HashMap<String, String> keys = f.getParameters();
		HashMap<String, String> desc = f.getParameterDescriptions();
		Iterator<String> it = keys.keySet().iterator();
		DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
		
		while(it.hasNext()){
			String key = it.next();
			model.addRow(new String [] {key, desc.get(key), keys.get(key)});
		}
	}
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DefaultFilterWindow dialog = new DefaultFilterWindow(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private Filter filter;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
