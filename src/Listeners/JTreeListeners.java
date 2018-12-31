package Listeners;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.io.FileUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import Client.ConfigReader;
import Client.Editor;
import Domain.KeyWord;
import Domain.TestStep;
import utils.ExcelHelper;

public class JTreeListeners extends MouseAdapter implements TreeSelectionListener ,ActionListener{
	
	private String testCasePath = ConfigReader.testCasePath;
	private String templetePath = ConfigReader.templetePath;
	private String configFilesPath = ConfigReader.configFilesPath;
	private SAXReader reader = new SAXReader();
	private String currentNode; // ��ǰѡ�еĽڵ��� 
	
	    // ����Mouse��ѡ�¼�
	    public void mousePressed(MouseEvent e) {	    	
			try {
				JTree tree = (JTree) e.getSource();
				TreePath treepath = tree.getSelectionPath();
				TreeNode treenode = (TreeNode) treepath.getLastPathComponent();
				String nodeName = treenode.toString();
				currentNode = nodeName;
			} catch (NullPointerException ne) {
				// û��ѡ�нڵ�ʱ����NPE �����κζ���
			}
		}
		

		@Override
		public void actionPerformed(ActionEvent ae) {
			JTree tree = Editor.tree;
			DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
			if (ae.getActionCommand().equals("����ģ��")) {
				String moduleName = JOptionPane.showInputDialog(Editor.frame, "������ģ����", "����", JOptionPane.INFORMATION_MESSAGE);
				if (moduleName == null || moduleName.equals("")) {
					return;
				}
				if (!addNewModule(moduleName)) { // У��ģ�����Ƿ��ظ���δ�ظ�д��XML
					JOptionPane.showMessageDialog(Editor.frame, "��ģ���Ѵ��ڣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// �����ڵ�
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(moduleName);
				newNode.setAllowsChildren(true);
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
				treeModel.insertNodeInto(newNode, root, root.getChildCount()); // ��DefaultTreeModel��insertNodeInto()���������½ڵ�
				tree.scrollPathToVisible(new TreePath(newNode.getPath())); // չ���ڵ�
				
				// ����ģ���ļ���				
				File moduleFolder = new File(testCasePath, moduleName);
				if (!moduleFolder.exists()) {
					moduleFolder.mkdirs();
				}
			}
			if (ae.getActionCommand().equals("��������")) {
				TreePath treepath = tree.getSelectionPath();
				if (treepath == null || treepath.getPath().length != 2) { // Ϊnull˵��δѡ�κνڵ㣬��Ϊ2˵��û��ѡ��ģ��ڵ�
					JOptionPane.showMessageDialog(Editor.frame, "��ָ��ģ�飡", "��ʾ", JOptionPane.WARNING_MESSAGE);
					return;
				}							
				String caseName = JOptionPane.showInputDialog(Editor.frame, "������������", "����", JOptionPane.INFORMATION_MESSAGE);
				if (caseName == null || caseName.equals("")) {
					return;
				}
				if (!addNewCase(treepath.getPath()[1].toString(),caseName)) { // У��ģ���������Ƿ��ظ���δ�ظ�д��XML
					JOptionPane.showMessageDialog(Editor.frame, "��ģ�����Ѵ�����ͬ������", "��ʾ", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// �����ڵ�
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(caseName);
				newNode.setAllowsChildren(false);
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent(); // ��ȡѡ�еĽڵ�
				treeModel.insertNodeInto(newNode, selectionNode, selectionNode.getChildCount());
				tree.scrollPathToVisible(new TreePath(newNode.getPath()));
						
				// �½������ļ�
				File srcFile = new File (templetePath + "CaseTemplate.xls");
				File destFile = new File (testCasePath + selectionNode.toString() + File.separator + caseName + ".xls");
				try {
					FileUtils.copyFile(srcFile, destFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ae.getActionCommand().equals("ɾ��")) {
				TreePath treepath = tree.getSelectionPath();
				if(treepath == null || treepath.getPath().length == 1) { //û��ѡ�ڵ����ѡ�и��ڵ�
					JOptionPane.showMessageDialog(Editor.frame, "��ѡ��Ҫɾ����ģ���������", "��ʾ", JOptionPane.WARNING_MESSAGE);
					return;
				}				
				int level = treepath.getPath().length; // �ó������ж�ѡ�еĽڵ�����  2-ģ��ڵ� 3-�����ڵ�
				// ��ȡѡ�еĽڵ�
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent();
				switch (level) {
				case 2 :
					if (JOptionPane.showConfirmDialog(Editor.frame, "ȷ��ɾ����ģ�飿", "��ʾ", JOptionPane.YES_NO_OPTION) == 1) {
						return;
					}
					if(this.clearModuleDir(selectionNode.toString())) {					
						treeModel.removeNodeFromParent(selectionNode); // ɾ���ڵ�
						this.removeModule(selectionNode.toString()); // д��XML
						return;
					}					
					break;
			    case 3 :
				    if (JOptionPane.showConfirmDialog(Editor.frame, "ȷ��ɾ����������", "��ʾ", JOptionPane.YES_NO_OPTION) == 1) {
						return;
					}
				    String moduleName = treepath.getPath()[1].toString();
				    String caseName = selectionNode.toString();
				    if (this.clearCaseFile(moduleName,caseName)) {
				    	this.removeCase(moduleName, caseName); 	// д��XML
				    	treeModel.removeNodeFromParent(selectionNode); // Tree��ɾ���ڵ�
				    	return;
				    }		
				    break;
			    }
			}
			if (ae.getActionCommand().equals("�޸�")) {
				TreePath treepath = tree.getSelectionPath();
				if(treepath == null || treepath.getPath().length == 1) { //û��ѡ�ڵ����ѡ�и��ڵ�
					JOptionPane.showMessageDialog(Editor.frame, "��ѡ��Ҫ�޸ĵ�ģ���������", "��ʾ", JOptionPane.WARNING_MESSAGE);
					return;
				}				
				int level = treepath.getPath().length; // �ó������ж�ѡ�еĽڵ�����  2-ģ��ڵ� 3-�����ڵ�
				// ��ȡѡ�еĽڵ�
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent();
				switch (level) {
				case 2 :
					String newModuleName = JOptionPane.showInputDialog(Editor.frame, "�������µ�ģ����", "�޸�", JOptionPane.INFORMATION_MESSAGE);
					if (newModuleName == null || newModuleName.equals("")) return;
					if (!modifyModule(currentNode, newModuleName)) {
						JOptionPane.showMessageDialog(Editor.frame, "��ģ���Ѵ��ڣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
						return;						
					}
					// �޸Ľڵ���
					selectionNode.setUserObject(newModuleName);
					tree.updateUI();
					
					// �޸�Ŀ¼��
					File moduleFolder = new File(testCasePath, currentNode);
					moduleFolder.renameTo(new File(testCasePath, newModuleName));		
					
					break;
			    case 3 :
					String newCaseName = JOptionPane.showInputDialog(Editor.frame, "�������µ�������", "�޸�", JOptionPane.INFORMATION_MESSAGE);
					if (newCaseName == null || newCaseName.equals("")) return;
					String moduleName = treepath.getPath()[1].toString();
					if (!modifyCase(moduleName, currentNode, newCaseName)) {
						JOptionPane.showMessageDialog(Editor.frame, "��ģ�����Ѵ�����ͬ������", "��ʾ", JOptionPane.WARNING_MESSAGE);
						return;						
					}
					// �޸Ľڵ���
					selectionNode.setUserObject(newCaseName);
					tree.updateUI();
					
					// �޸������ļ���
					File caseFile= new File(testCasePath + moduleName + File.separator + currentNode + ".xls");
					caseFile.renameTo(new File(testCasePath + moduleName + File.separator + newCaseName + ".xls"));
				    break;
			    }				
			}
		}

		private boolean addNewModule(String moduleName) {
			Document doc = readFromXml();
			Element root = doc.getRootElement();
			String xPath = String.format("//module[@name='%s']", moduleName); // ��Xpath�ҵ���Ӧ��ģ��ڵ�
			Element moduleNode = (Element)doc.selectSingleNode(xPath);
			if (moduleNode != null ) { //�Ѿ�����
				return false;
			} else {
				Element newModule = root.addElement("module");
				newModule.addAttribute("name", moduleName);
				writeToXml(doc);
			}
			return true;			
		}
		
		private void removeModule(String moduleName) {
			Document doc = readFromXml();
			Element root = doc.getRootElement();
			String xPath = String.format("//module[@name='%s']", moduleName);
			Element moduleNode = (Element)doc.selectSingleNode(xPath);
			root.remove(moduleNode);
			writeToXml(doc);
		}
		
		private boolean modifyModule(String oldName, String newName) {
			Document doc = readFromXml();
			String xPath = String.format("//module[@name='%s']", newName);
			Element moduleNode = (Element)doc.selectSingleNode(xPath);
			if (moduleNode != null) {
				return false;
			} 
			xPath = String.format("//module[@name='%s']", oldName);
			moduleNode = (Element)doc.selectSingleNode(xPath);
			Attribute attrDate = moduleNode.attribute("name");//��ȡ�˽ڵ��ָ������
			attrDate.setValue(newName);
			writeToXml(doc);
			return true;
		}		
		
        private boolean addNewCase(String moduleName, String caseName) {
        	Document doc = readFromXml();
        	String xPath = String.format("//module[@name='%s']", moduleName);
			Element moduleNode = (Element)doc.selectSingleNode(xPath);
			List<Element> caseList = moduleNode.elements();
			for (Element el : caseList)	{
				if (caseName.equals(el.getText())) {
					return false;
				}
			}
			Element newCase = moduleNode.addElement("case");
			newCase.setText(caseName);
			writeToXml(doc);
        	return true;
        }
        	
		private void removeCase(String moduleName, String caseName) {
			Document doc = readFromXml();
			String xPath = String.format("//module[@name='%s']", moduleName);
			Element moduleNode = (Element)doc.selectSingleNode(xPath);
			List<Element> caseList = moduleNode.elements();
			caseList.forEach(el -> {
				if (caseName.equals(el.getText())) {
					moduleNode.remove(el);
				}
			});			
			writeToXml(doc);
		}
		
		private boolean modifyCase(String moduleName, String oldCaseName, String newCaseName) {
			Document doc = readFromXml();
			String xPath = String.format("//module[@name='%s']", moduleName);
			Element moduleNode = (Element)doc.selectSingleNode(xPath);
			List<Element> caseList = moduleNode.elements();
			Element oldCase = null;
			for (Element el : caseList)	{
				if (newCaseName.equals(el.getText())) {
					return false;
				}
				if (oldCaseName.equals(el.getText())) {
					oldCase = el;
				}
			}
			oldCase.setText(newCaseName);
			writeToXml(doc);
			return true;
		}

		private boolean clearModuleDir(String moduleName) {
			// ɾ��ģ��Ŀ¼
			try {
				FileUtils.deleteDirectory(new File(testCasePath + moduleName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		private boolean clearCaseFile(String moduleName, String caseName) {
			// ɾ�������ļ�
			String filePath = testCasePath + moduleName + File.separator + caseName + ".xls";
			try {
				new File(filePath).delete();
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		


			
		private Document readFromXml() {
			File caseSets = new File(configFilesPath);
			Document doc = null;
			try {
				doc = reader.read(caseSets);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return doc;
		}
		
		private void writeToXml(Document doc) {
		    try {
			    File caseSets = new File(configFilesPath);
			    OutputStream os = new FileOutputStream(caseSets);
			    OutputFormat format = OutputFormat.createPrettyPrint(); // ����ģʽ
			    format.setEncoding("utf-8"); 
			    XMLWriter xw = new XMLWriter(os,format);
			    xw.write(doc);
			    xw.flush();
			    xw.close();
			} catch (IOException e) {
				
			}
		}


		@Override
		public void valueChanged(TreeSelectionEvent e) {
			JTree tree = (JTree) e.getSource();
			// ����JTree��getLastSelectedPathComponent()����ȡ��Ŀǰѡȡ�Ľڵ�.
			DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();
			String nodeName = selectionNode.toString();
			// �ж��Ƿ�Ϊ��Ҷ�ڵ㣬��������ʾ�ļ����ݣ������������κ���
			if (selectionNode.isLeaf()) {
				TreePath treepath = tree.getSelectionPath();
				String moduleName = treepath.getPath()[1].toString();
				System.out.println(moduleName + nodeName);
				String path = testCasePath + moduleName + File.separator + nodeName + ".xls";
				List<Object> data = ExcelHelper.simpleReadJavaModel(path, 1, 1, TestStep.class);
				System.out.println(data.size());
				System.out.println(data);
				String[][] tableData = new String[1000][6];
				if (data.size() == 0) { // û������
					for (int i = 0; i < tableData.length; i++) {
						tableData[i][0] = String.valueOf(i + 1);
					}					
				} else {
					for (int i = 0; i < data.size(); i++) {
						tableData[i][0] = String.valueOf(i + 1);
						tableData[i][1] = ((TestStep)data.get(i)).getDescription();
						tableData[i][2] = ((TestStep)data.get(i)).getOperate();
						tableData[i][3] = ((TestStep)data.get(i)).getParam1();
						tableData[i][4] = ((TestStep)data.get(i)).getParam2();
						tableData[i][5] = ((TestStep)data.get(i)).getParam3();
					}					
				}
				repaintTable(Editor.table, tableData);	// �����б�����
			}			
		}
		
		private void repaintTable(JTable table, String[][] data) {
			table.setModel(Editor.generateTableModel(data));
	        TableColumn column = Editor.table.getColumnModel().getColumn(0);
	        column.setMaxWidth(60); // ��һ�п��
			JComboBox<String> comboBox = new JComboBox<String>();
			comboBox.setFont(new Font("΢���ź�",Font.PLAIN,12));
			ConfigReader.keyWordsList.forEach(el -> {
				KeyWord keyWord = (KeyWord)el;
				comboBox.addItem(keyWord.getFunction());
			});
			table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBox)); // ������ΪCheckBox����		
		} 

}
