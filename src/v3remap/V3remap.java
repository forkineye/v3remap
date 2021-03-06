/*
 * Project: V3remap
 * Copyright (c) 2014 Shelby Merrick
 * http://www.forkineye.com
 *
 *  This program is provided free for you to use in any way that you wish,
 *  subject to the laws and regulations where you are using it.  Due diligence
 *  is strongly suggested before using this code.  Please give credit where due.
 *
 *  The Author makes no warranty of any kind, express or implied, with regard
 *  to this program or the documentation contained in this document.  The
 *  Author shall not be liable in any event for incidental or consequential
 *  damages in connection with, or arising out of, the furnishing, performance
 *  or use of these programs.
 *
 */ 

package v3remap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author sporadic
 */
public class V3remap extends javax.swing.JFrame {
    private static final String VERSION = "0.1";
    
    private File    fSourceConfig;
    private File    fTargetConfig;
    private File    fSourceSequence;
    private File    fTargetSequence;
    
    private DefaultTableModel   mdlMain;
    
    private final JComboBox cboxTargetNode = new JComboBox();
    
    private final SortedSet<VixenNode>  nlSource = new TreeSet<>();
    private final SortedSet<VixenNode>  nlTarget = new TreeSet<>();

    private final FileNameExtensionFilter ffXML = new FileNameExtensionFilter("XML Files", "xml");
    private final FileNameExtensionFilter ffTIM = new FileNameExtensionFilter("Vixen 3 Sequence Files", "tim");
    
    private class VixenNode implements Comparable<VixenNode> {
        private String id;
        private String name;
                
        public VixenNode(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int compareTo(VixenNode vn) {
            return this.name.compareTo(vn.getName());
        }
    };
    
    /**
     * Creates new form frmMain
     */
    public V3remap() {
        initComponents();

        this.setTitle("V3Remap - v" + VERSION);
        
        // Center up UI
        setSize(getPreferredSize());
        setLocationRelativeTo(null);
    }

       
    private void parseNodeMap(File file, SortedSet<VixenNode> set) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        set.clear();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file.getPath());
            
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            
            XPathExpression expr = xpath.compile("/SystemConfig/Nodes//Node");
            NodeList nlist = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            
            for (int i = 0; i < nlist.getLength(); i++) {
                Node item = nlist.item(i);
                VixenNode vn = new VixenNode(
                        item.getAttributes().getNamedItem("id").getNodeValue(),
                        item.getAttributes().getNamedItem("name").getNodeValue()
                );
                set.add(vn);
            }
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private void exportRemap(File file) {
        try {
            String content = new String(Files.readAllBytes(fSourceSequence.toPath()));
            for (int i = 0; i < mdlMain.getRowCount(); i++) {
                VixenNode find = (VixenNode)(mdlMain.getValueAt(i, 0));
                VixenNode replace = (VixenNode)(mdlMain.getValueAt(i, 1));
                if (replace != null) 
                    content = content.replaceAll(find.getId(), replace.getId());
            }
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void buildTable() {
        mdlMain = new DefaultTableModel()  {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        tblMain.setModel(mdlMain);
        mdlMain.addColumn("Source Node");
        mdlMain.addColumn("Target Node");

        tblMain.getColumnModel().getColumn(1).setCellEditor(
                new DefaultCellEditor(cboxTargetNode));
        tblMain.setRowSorter(new TableRowSorter<>(mdlMain));

        for (VixenNode node : nlSource)
            mdlMain.addRow(new Object[] {
                node
            });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dlgFile = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMain = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSourceConfig = new javax.swing.JTextField();
        txtTargetConfig = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtSequence = new javax.swing.JTextField();
        btnReMap = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("V3remap");
        setName("v3remap"); // NOI18N

        tblMain.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblMain);

        jLabel1.setText("Source Configuration");

        jLabel2.setText("Target Configuration");

        txtSourceConfig.setEditable(false);
        txtSourceConfig.setToolTipText("Click to select file");
        txtSourceConfig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSourceConfigMouseClicked(evt);
            }
        });

        txtTargetConfig.setEditable(false);
        txtTargetConfig.setToolTipText("Click to select file");
        txtTargetConfig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTargetConfigMouseClicked(evt);
            }
        });

        jLabel3.setText("Current Sequence");

        txtSequence.setEditable(false);
        txtSequence.setToolTipText("Click to select file");
        txtSequence.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSequenceMouseClicked(evt);
            }
        });

        btnReMap.setText("ReMap");
        btnReMap.setEnabled(false);
        btnReMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReMapActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSourceConfig)
                            .addComponent(txtTargetConfig)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtSequence)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnReMap)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSourceConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTargetConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSequence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btnReMap))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSourceConfigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSourceConfigMouseClicked
        dlgFile.setFileFilter(ffXML);
        dlgFile.setSelectedFile(fSourceConfig);
        dlgFile.setDialogTitle("Select Source SystemConfig.xml");
        
        if (dlgFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fSourceConfig = dlgFile.getSelectedFile();
            txtSourceConfig.setText(fSourceConfig.getAbsolutePath());
            parseNodeMap(fSourceConfig, nlSource);
            buildTable();
        }
    }//GEN-LAST:event_txtSourceConfigMouseClicked

    private void txtTargetConfigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTargetConfigMouseClicked
        dlgFile.setFileFilter(ffXML);
        dlgFile.setSelectedFile(fTargetConfig);
        dlgFile.setDialogTitle("Select Target SystemConfig.xml");
        if (dlgFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fTargetConfig = dlgFile.getSelectedFile();
            txtTargetConfig.setText(fTargetConfig.getAbsolutePath());
            parseNodeMap(fTargetConfig, nlTarget);
            cboxTargetNode.removeAllItems();
            for (VixenNode node : nlTarget) {
                cboxTargetNode.addItem(node);
            }
        }
    }//GEN-LAST:event_txtTargetConfigMouseClicked

    private void txtSequenceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSequenceMouseClicked
        dlgFile.setFileFilter(ffTIM);
        dlgFile.setSelectedFile(fSourceSequence);
        dlgFile.setDialogTitle("Select Sequence");
        if (dlgFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fSourceSequence = dlgFile.getSelectedFile();
            txtSequence.setText(fSourceSequence.getAbsolutePath());
            btnReMap.setEnabled(true);
        }
    }//GEN-LAST:event_txtSequenceMouseClicked

    private void btnReMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReMapActionPerformed
        String path = fTargetConfig.getParentFile().getPath();
        File dir = new File(path);
        dlgFile.setSelectedFile(dir);
        dlgFile.setDialogTitle("Save Sequence");
        if (dlgFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
            exportRemap(dlgFile.getSelectedFile());
    }//GEN-LAST:event_btnReMapActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            /*
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }*/
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(V3remap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V3remap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V3remap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V3remap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V3remap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReMap;
    private javax.swing.JFileChooser dlgFile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblMain;
    private javax.swing.JTextField txtSequence;
    private javax.swing.JTextField txtSourceConfig;
    private javax.swing.JTextField txtTargetConfig;
    // End of variables declaration//GEN-END:variables
}
