/* ----------------------------------------------------------------------------
 * Copyright (C) 2015      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : ESA NanoSat MO Framework
 * ----------------------------------------------------------------------------
 * Licensed under the European Space Agency Public License, Version 2.0
 * You may not use this file except in compliance with the License.
 *
 * Except as expressly set forth in this License, the Software is provided to
 * You on an "as is" basis and without warranties of any kind, including without
 * limitation merchantability, fitness for a particular purpose, absence of
 * defects or errors, accuracy or non-infringement of intellectual property rights.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * ----------------------------------------------------------------------------
 */
package esa.mo.nmf.ctt.services.mc;

import esa.mo.com.impl.provider.ArchivePersistenceObject;
import esa.mo.com.impl.util.HelperArchive;
import esa.mo.helpertools.helpers.HelperMisc;
import esa.mo.mc.impl.consumer.CheckConsumerServiceImpl;
import esa.mo.tools.mowindow.MOWindow;
import java.io.InterruptedIOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.ccsds.moims.mo.com.structures.InstanceBooleanPair;
import org.ccsds.moims.mo.com.structures.InstanceBooleanPairList;
import org.ccsds.moims.mo.com.structures.ObjectDetails;
import org.ccsds.moims.mo.com.structures.ObjectDetailsList;
import org.ccsds.moims.mo.com.structures.ObjectId;
import org.ccsds.moims.mo.com.structures.ObjectKey;
import org.ccsds.moims.mo.com.structures.ObjectType;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.MALInteractionException;
import org.ccsds.moims.mo.mal.structures.Duration;
import org.ccsds.moims.mo.mal.structures.Identifier;
import org.ccsds.moims.mo.mal.structures.IdentifierList;
import org.ccsds.moims.mo.mal.structures.LongList;
import org.ccsds.moims.mo.mal.structures.StringList;
import org.ccsds.moims.mo.mc.check.CheckHelper;
import org.ccsds.moims.mo.mc.check.structures.CheckDefinitionDetails;
import org.ccsds.moims.mo.mc.check.structures.CheckDefinitionDetailsList;
import org.ccsds.moims.mo.mc.check.structures.CheckLinkDetails;
import org.ccsds.moims.mo.mc.check.structures.CheckLinkDetailsList;
import org.ccsds.moims.mo.mc.check.structures.CheckTypedInstance;
import org.ccsds.moims.mo.mc.check.structures.CheckTypedInstanceList;
import org.ccsds.moims.mo.mc.check.structures.CompoundCheckDefinition;
import org.ccsds.moims.mo.mc.check.structures.ConstantCheckDefinition;
import org.ccsds.moims.mo.mc.check.structures.DeltaCheckDefinition;
import org.ccsds.moims.mo.mc.check.structures.LimitCheckDefinition;
import org.ccsds.moims.mo.mc.check.structures.ReferenceCheckDefinition;
import org.ccsds.moims.mo.mc.parameter.ParameterHelper;
import org.ccsds.moims.mo.mc.structures.ObjectInstancePairList;

/**
 *
 * @author Cesar Coelho
 */
public class CheckConsumerPanel extends javax.swing.JPanel {

    private CheckConsumerServiceImpl serviceMCCheck;
//    private ParameterConsumerServiceImpl serviceMCParameter;
    private CheckDefinitionsTablePanel checkDefsTable;
    private CheckLinksTablePanel checkLinksTable;
    private ObjectType objTypeCheckDefinition;

    /**
     *
     * @param serviceMCCheck
     */
    public CheckConsumerPanel(CheckConsumerServiceImpl serviceMCCheck) {
        initComponents();

        checkDefsTable = new CheckDefinitionsTablePanel(serviceMCCheck.getCOMServices().getArchiveService());
        checkLinksTable = new CheckLinksTablePanel(serviceMCCheck.getCOMServices().getArchiveService());
        jScrollPane2.setViewportView(checkDefsTable);
        jScrollPane3.setViewportView(checkLinksTable);

        this.serviceMCCheck = serviceMCCheck;
        objTypeCheckDefinition = CheckHelper.CHECKIDENTITY_OBJECT_TYPE;
        
        this.listDefinitionAllButtonActionPerformed(null);
    
    }

    /**
     * This method is called from within the constructor to initialize the
     * formAddModifyParameter. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        actionDefinitionsTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        actionDefinitionsTable1 = new javax.swing.JTable();
        parameterTab = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        getCurrentTransitionListButton = new javax.swing.JButton();
        getSummaryReportButton = new javax.swing.JButton();
        enableServiceButton = new javax.swing.JButton();
        getServiceStatusButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        enableCheckButton = new javax.swing.JButton();
        enableCheckAllButton = new javax.swing.JButton();
        triggerCheckButton = new javax.swing.JButton();
        listDefinitionAllButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        addDefButton = new javax.swing.JButton();
        updateDefButton = new javax.swing.JButton();
        removeDefButton = new javax.swing.JButton();
        removeDefAllButton = new javax.swing.JButton();
        addParameterCheck = new javax.swing.JButton();
        removeParameterCheck = new javax.swing.JButton();

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Check Service");
        jLabel6.setToolTipText("");

        jScrollPane2.setHorizontalScrollBar(null);
        jScrollPane2.setName(""); // NOI18N
        jScrollPane2.setPreferredSize(new java.awt.Dimension(796, 80));

        actionDefinitionsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null,  new Boolean(true), null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Obj Inst Id", "name", "description", "rawType", "rawUnit", "generationEnabled", "updateInterval"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        actionDefinitionsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        actionDefinitionsTable.setAutoscrolls(false);
        actionDefinitionsTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        actionDefinitionsTable.setMinimumSize(new java.awt.Dimension(525, 32));
        actionDefinitionsTable.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                actionDefinitionsTableComponentAdded(evt);
            }
        });
        jScrollPane2.setViewportView(actionDefinitionsTable);

        jScrollPane3.setHorizontalScrollBar(null);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(796, 80));
        jScrollPane3.setRequestFocusEnabled(false);

        actionDefinitionsTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null,  new Boolean(true), null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Obj Inst Id", "name", "description", "rawType", "rawUnit", "generationEnabled", "updateInterval"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        actionDefinitionsTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        actionDefinitionsTable1.setAutoscrolls(false);
        actionDefinitionsTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        actionDefinitionsTable1.setMaximumSize(null);
        actionDefinitionsTable1.setMinimumSize(null);
        actionDefinitionsTable1.setPreferredSize(null);
        actionDefinitionsTable1.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                actionDefinitionsTable1ComponentAdded(evt);
            }
        });
        jScrollPane3.setViewportView(actionDefinitionsTable1);

        parameterTab.setLayout(new java.awt.GridLayout(3, 1));

        getCurrentTransitionListButton.setText("getCurrentTransitionList");
        getCurrentTransitionListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getCurrentTransitionListButtonActionPerformed(evt);
            }
        });
        jPanel2.add(getCurrentTransitionListButton);

        getSummaryReportButton.setText("getSummaryReport");
        getSummaryReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getSummaryReportButtonActionPerformed(evt);
            }
        });
        jPanel2.add(getSummaryReportButton);

        enableServiceButton.setText("enableService");
        enableServiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableServiceButtonActionPerformed(evt);
            }
        });
        jPanel2.add(enableServiceButton);

        getServiceStatusButton.setText("getServiceStatus");
        getServiceStatusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getServiceStatusButtonActionPerformed(evt);
            }
        });
        jPanel2.add(getServiceStatusButton);

        jLabel1.setText("Unk");
        jPanel2.add(jLabel1);

        parameterTab.add(jPanel2);

        enableCheckButton.setText("enableCheck");
        enableCheckButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableCheckButtonActionPerformed(evt);
            }
        });
        jPanel1.add(enableCheckButton);

        enableCheckAllButton.setText("enableCheck(group=false, 0)");
        enableCheckAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableCheckAllButtonActionPerformed(evt);
            }
        });
        jPanel1.add(enableCheckAllButton);

        triggerCheckButton.setText("triggerCheck");
        triggerCheckButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerCheckButtonActionPerformed(evt);
            }
        });
        jPanel1.add(triggerCheckButton);

        listDefinitionAllButton.setText("listDefinition(\"*\")");
        listDefinitionAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listDefinitionAllButtonActionPerformed(evt);
            }
        });
        jPanel1.add(listDefinitionAllButton);

        parameterTab.add(jPanel1);

        addDefButton.setText("addDefinition");
        addDefButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDefButtonActionPerformed(evt);
            }
        });
        jPanel5.add(addDefButton);

        updateDefButton.setText("updateDefinition");
        updateDefButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateDefButtonActionPerformed(evt);
            }
        });
        jPanel5.add(updateDefButton);

        removeDefButton.setText("removeDefinition");
        removeDefButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeDefButtonActionPerformed(evt);
            }
        });
        jPanel5.add(removeDefButton);

        removeDefAllButton.setText("removeDefinition(0)");
        removeDefAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeDefAllButtonActionPerformed(evt);
            }
        });
        jPanel5.add(removeDefAllButton);

        addParameterCheck.setText("addParameterCheck");
        addParameterCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addParameterCheckActionPerformed(evt);
            }
        });
        jPanel5.add(addParameterCheck);

        removeParameterCheck.setText("removeParameterCheck");
        removeParameterCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeParameterCheckActionPerformed(evt);
            }
        });
        jPanel5.add(removeParameterCheck);

        parameterTab.add(jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(parameterTab, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 939, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(parameterTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addDefButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDefButtonActionPerformed

        // User must pick a type of check...
        Object[] possibilities = {"ConstantCheckDefinition", "ReferenceCheckDefinition", 
            "DeltaCheckDefinition", "LimitCheckDefinition", "CompoundCheckDefinition" };
        
        String s = (String)JOptionPane.showInputDialog(
                    null,
                    "Type of Check:\n",
                    "Check Type",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "ham");        
        
        if (s == null){
            return;
        }
        
        CheckDefinitionDetails checkDefinition = null;
        
        if ("ConstantCheckDefinition".equals(s.toString())){
            checkDefinition = new ConstantCheckDefinition();
        }

        if ("ReferenceCheckDefinition".equals(s.toString())){
            checkDefinition = new ReferenceCheckDefinition();
        }

        if ("DeltaCheckDefinition".equals(s.toString())){
            checkDefinition = new DeltaCheckDefinition();
        }

        if ("LimitCheckDefinition".equals(s.toString())){
            checkDefinition = new LimitCheckDefinition();
        }

        if ("CompoundCheckDefinition".equals(s.toString())){
            checkDefinition = new CompoundCheckDefinition();
         }

        // Display in a window
        MOWindow checkDefinitionWindow = new MOWindow(checkDefinition, true);
        CheckDefinitionDetailsList checkDefinitionList;

        try {
            checkDefinitionList = (CheckDefinitionDetailsList) HelperMisc.element2elementList(checkDefinition);
            checkDefinitionList.add((CheckDefinitionDetails) checkDefinitionWindow.getObject());
            StringList checkNames = new StringList();
            checkNames.add("AcheckDefinition");

            ObjectInstancePairList objIds = this.serviceMCCheck.getCheckStub().addCheck(checkNames, checkDefinitionList);

            if (objIds.isEmpty()) {
                return;
            }
            
            Thread.sleep(500);
            // Get the stored Action Definition from the Archive
            ArchivePersistenceObject comObject = HelperArchive.getArchiveCOMObject(
                    this.serviceMCCheck.getCOMServices().getArchiveService().getArchiveStub(),
                    objTypeCheckDefinition, 
                    serviceMCCheck.getConnectionDetails().getDomain(), 
                    objIds.get(0).getObjDefInstanceId());
/*
            ObjectId source = comObject.getArchiveDetails().getDetails().getSource();
            
            if (source == null){
                return;
            }
            
            // Get the specific Action Definition from the Archive
            ArchivePersistenceObject comObject2 = HelperArchive.getArchiveCOMObject(this.serviceMCCheck.getCOMServices().getArchiveService().getArchiveService(),
                    source.getType(), source.getKey().getDomain(), source.getKey().getInstId());
*/            
            // Add the Statistic Link to the table
            checkDefsTable.addEntry(new Identifier(checkNames.get(0)), comObject);
        } catch (InterruptedIOException ex) {
            return;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "There was an error with the submitted check definition.", "Error", JOptionPane.PLAIN_MESSAGE);
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addDefButtonActionPerformed

    private void updateDefButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDefButtonActionPerformed
        if (checkDefsTable.getSelectedRow() == -1) { // The row is not selected?
            return;  // Well, then nothing to be done here folks!
        }

        ArchivePersistenceObject obj = checkDefsTable.getSelectedCOMObject();
        MOWindow moObject = new MOWindow(obj.getObject(), true);

        LongList objIds = new LongList();
        objIds.add(checkDefsTable.getSelectedIdentityObjId());
        CheckDefinitionDetailsList checkDefinitionList;

        try {
            checkDefinitionList = (CheckDefinitionDetailsList) HelperMisc.element2elementList(moObject.getObject());
            checkDefinitionList.add((CheckDefinitionDetails) moObject.getObject());
            this.serviceMCCheck.getCheckStub().updateDefinition(objIds, checkDefinitionList);
            this.listDefinitionAllButtonActionPerformed(null);
        } catch (InterruptedIOException ex) {
            return;
        } catch (Exception ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_updateDefButtonActionPerformed

    private void removeDefButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeDefButtonActionPerformed
        if (checkDefsTable.getSelectedRow() == -1) { // The row is not selected?
            return;  // Well, then nothing to be done here folks!
        }
        
        Long objId = checkDefsTable.getSelectedIdentityObjId();
        LongList longlist = new LongList();
        longlist.add(objId);

        try {
            this.serviceMCCheck.getCheckStub().removeCheck(longlist);
            checkDefsTable.removeSelectedEntry();
        } catch (MALInteractionException ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MALException ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_removeDefButtonActionPerformed

    private void listDefinitionAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listDefinitionAllButtonActionPerformed
        IdentifierList idList = new IdentifierList();
        idList.add(new Identifier("*"));

        CheckTypedInstanceList outputs;
        try {
            outputs = this.serviceMCCheck.getCheckStub().listDefinition(idList);
            ObjectInstancePairList objIds = new ObjectInstancePairList();

            for(CheckTypedInstance output : outputs){
                objIds.add(output.getObjInstIds());
            }
                    
            checkDefsTable.refreshTableWithIds(objIds, serviceMCCheck.getConnectionDetails().getDomain(), objTypeCheckDefinition);
        } catch (MALInteractionException ex) {
            JOptionPane.showMessageDialog(null, "There was an error during the listDefinition operation.", "Error", JOptionPane.PLAIN_MESSAGE);
            Logger.getLogger(ParameterConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (MALException ex) {
            JOptionPane.showMessageDialog(null, "There was an error during the listDefinition operation.", "Error", JOptionPane.PLAIN_MESSAGE);
            Logger.getLogger(ParameterConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.INFO, "listDefinition(\"*\") returned {0} object instance identifiers.", outputs.size());
    }//GEN-LAST:event_listDefinitionAllButtonActionPerformed

    private void removeDefAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeDefAllButtonActionPerformed
        Long objId = (long) 0;
        LongList longlist = new LongList();
        longlist.add(objId);

        try {
            this.serviceMCCheck.getCheckStub().removeCheck(longlist);
            checkDefsTable.removeAllEntries();
        } catch (MALInteractionException ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MALException ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_removeDefAllButtonActionPerformed

    private void getServiceStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getServiceStatusButtonActionPerformed


    }//GEN-LAST:event_getServiceStatusButtonActionPerformed

    private void getCurrentTransitionListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getCurrentTransitionListButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_getCurrentTransitionListButtonActionPerformed

    private void getSummaryReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getSummaryReportButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_getSummaryReportButtonActionPerformed

    private void enableServiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableServiceButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_enableServiceButtonActionPerformed

    private void enableCheckAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableCheckAllButtonActionPerformed

        Boolean curState;

        if (checkLinksTable.getSelectedRow() == -1) {  // Used to avoid problems if no row is selected
            CheckLinkDetails checkLink = (CheckLinkDetails) checkLinksTable.getSelectedCOMObject().getObject();
            if (checkLink != null) {
                curState = checkLink.getCheckEnabled();
            } else {
                curState = true;
            }
        } else {
            curState = ((CheckLinkDetails) checkLinksTable.getSelectedCOMObject().getObject()).getCheckEnabled();
        }

        InstanceBooleanPairList BoolPairList = new InstanceBooleanPairList();
        BoolPairList.add(new InstanceBooleanPair((long) 0, !curState));  // Zero is the wildcard

        try {
            this.serviceMCCheck.getCheckStub().enableCheck(false, BoolPairList);
            checkLinksTable.removeSelectedEntry();
        } catch (MALInteractionException ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MALException ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_enableCheckAllButtonActionPerformed

    private void enableCheckButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableCheckButtonActionPerformed
        Boolean curState;

        if (checkLinksTable.getSelectedRow() == -1) {  // Used to avoid problems if no row is selected
            CheckLinkDetails checkLink = (CheckLinkDetails) checkLinksTable.getSelectedCOMObject().getObject();
            if (checkLink != null) {
                curState = checkLink.getCheckEnabled();
            } else {
                curState = true;
            }
        } else {
            curState = ((CheckLinkDetails) checkLinksTable.getSelectedCOMObject().getObject()).getCheckEnabled();
        }

        InstanceBooleanPairList BoolPairList = new InstanceBooleanPairList();
        BoolPairList.add(new InstanceBooleanPair(checkLinksTable.getSelectedCOMObject().getObjectId(), !curState));  // Zero is the wildcard

        try {
            this.serviceMCCheck.getCheckStub().enableCheck(false, BoolPairList);
            checkLinksTable.removeSelectedEntry();
        } catch (MALInteractionException ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MALException ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_enableCheckButtonActionPerformed

    private void triggerCheckButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerCheckButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_triggerCheckButtonActionPerformed

    private void addParameterCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addParameterCheckActionPerformed
        
        CheckLinkDetails checkLink = new CheckLinkDetails();
        checkLink.setCheckEnabled(true);
        checkLink.setCheckInterval(new Duration(4));
        checkLink.setCheckOnChange(true);
        checkLink.setCondition(null);
        checkLink.setUseConverted(false);
        
        // Display in a window
        try {
            MOWindow checkLinkWindow = new MOWindow(checkLink, true);
            CheckLinkDetailsList checkLinkList = new CheckLinkDetailsList();
            checkLinkList.add((CheckLinkDetails) checkLinkWindow.getObject());

            ObjectDetails details = new ObjectDetails();

            if (checkDefsTable.getSelectedCOMObject() == null){
                JOptionPane.showMessageDialog(null, "Please select the check definition to be linked.", "Error", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            
            details.setRelated(checkDefsTable.getSelectedCOMObject().getObjectId());
            details.setSource(new ObjectId(ParameterHelper.PARAMETERDEFINITION_OBJECT_TYPE, 
                    new ObjectKey(serviceMCCheck.getConnectionDetails().getDomain(), new Long (1))));

            MOWindow linksWindow = new MOWindow(details, true);
            ObjectDetailsList detailsList = new ObjectDetailsList();
            detailsList.add((ObjectDetails) linksWindow.getObject());
            
            ObjectInstancePairList objIds = this.serviceMCCheck.getCheckStub().addParameterCheck(checkLinkList, detailsList);

            if (objIds.isEmpty()) {
                return;
            }
            
            Thread.sleep(500);
            // Get the stored Action Definition from the Archive
            ArchivePersistenceObject comObject = HelperArchive.getArchiveCOMObject(this.serviceMCCheck.getCOMServices().getArchiveService().getArchiveStub(),
                    CheckHelper.CHECKLINK_OBJECT_TYPE, serviceMCCheck.getConnectionDetails().getDomain(), objIds.get(0).getObjDefInstanceId());

            // Add the Check Link to the table
            checkLinksTable.addEntry(new Identifier("A check!!"), comObject);

        } catch (InterruptedIOException ex) {
            return;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "There was an error with the submitted check definition.", "Error", JOptionPane.PLAIN_MESSAGE);
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addParameterCheckActionPerformed

    private void removeParameterCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeParameterCheckActionPerformed

        if (checkLinksTable.getSelectedRow() == -1) { // The row is not selected?
            return;  // Well, then nothing to be done here folks!
        }
        
        Long objId = checkLinksTable.getSelectedDefinitionObjId();
        LongList longlist = new LongList();
        longlist.add(objId);

        try {
            this.serviceMCCheck.getCheckStub().removeParameterCheck(longlist);
            checkLinksTable.removeSelectedEntry();
        } catch (MALInteractionException ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MALException ex) {
            Logger.getLogger(CheckConsumerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_removeParameterCheckActionPerformed

    private void actionDefinitionsTable1ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_actionDefinitionsTable1ComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_actionDefinitionsTable1ComponentAdded

    private void actionDefinitionsTableComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_actionDefinitionsTableComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_actionDefinitionsTableComponentAdded


    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable actionDefinitionsTable;
    private javax.swing.JTable actionDefinitionsTable1;
    private javax.swing.JButton addDefButton;
    private javax.swing.JButton addParameterCheck;
    private javax.swing.JButton enableCheckAllButton;
    private javax.swing.JButton enableCheckButton;
    private javax.swing.JButton enableServiceButton;
    private javax.swing.JButton getCurrentTransitionListButton;
    private javax.swing.JButton getServiceStatusButton;
    private javax.swing.JButton getSummaryReportButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton listDefinitionAllButton;
    private javax.swing.JPanel parameterTab;
    private javax.swing.JButton removeDefAllButton;
    private javax.swing.JButton removeDefButton;
    private javax.swing.JButton removeParameterCheck;
    private javax.swing.JButton triggerCheckButton;
    private javax.swing.JButton updateDefButton;
    // End of variables declaration//GEN-END:variables
    
    
}
