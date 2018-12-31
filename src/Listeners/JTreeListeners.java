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
	private String currentNode; // 当前选中的节点名 
	
	    // 处理Mouse点选事件
	    public void mousePressed(MouseEvent e) {	    	
			try {
				JTree tree = (JTree) e.getSource();
				TreePath treepath = tree.getSelectionPath();
				TreeNode treenode = (TreeNode) treepath.getLastPathComponent();
				String nodeName = treenode.toString();
				currentNode = nodeName;
			} catch (NullPointerException ne) {
				// 没有选中节点时会有NPE 不做任何动作
			}
		}
		

		@Override
		public void actionPerformed(ActionEvent ae) {
			JTree tree = Editor.tree;
			DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
			if (ae.getActionCommand().equals("新增模块")) {
				String moduleName = JOptionPane.showInputDialog(Editor.frame, "请输入模块名", "新增", JOptionPane.INFORMATION_MESSAGE);
				if (moduleName == null || moduleName.equals("")) {
					return;
				}
				if (!addNewModule(moduleName)) { // 校验模块名是否重复，未重复写入XML
					JOptionPane.showMessageDialog(Editor.frame, "该模块已存在！", "提示", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// 新增节点
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(moduleName);
				newNode.setAllowsChildren(true);
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
				treeModel.insertNodeInto(newNode, root, root.getChildCount()); // 由DefaultTreeModel的insertNodeInto()方法增加新节点
				tree.scrollPathToVisible(new TreePath(newNode.getPath())); // 展开节点
				
				// 新增模块文件夹				
				File moduleFolder = new File(testCasePath, moduleName);
				if (!moduleFolder.exists()) {
					moduleFolder.mkdirs();
				}
			}
			if (ae.getActionCommand().equals("新增用例")) {
				TreePath treepath = tree.getSelectionPath();
				if (treepath == null || treepath.getPath().length != 2) { // 为null说明未选任何节点，不为2说明没有选中模块节点
					JOptionPane.showMessageDialog(Editor.frame, "请指定模块！", "提示", JOptionPane.WARNING_MESSAGE);
					return;
				}							
				String caseName = JOptionPane.showInputDialog(Editor.frame, "请输入用例名", "新增", JOptionPane.INFORMATION_MESSAGE);
				if (caseName == null || caseName.equals("")) {
					return;
				}
				if (!addNewCase(treepath.getPath()[1].toString(),caseName)) { // 校验模块下用例是否重复，未重复写入XML
					JOptionPane.showMessageDialog(Editor.frame, "该模块下已存在相同用例！", "提示", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// 新增节点
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(caseName);
				newNode.setAllowsChildren(false);
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent(); // 获取选中的节点
				treeModel.insertNodeInto(newNode, selectionNode, selectionNode.getChildCount());
				tree.scrollPathToVisible(new TreePath(newNode.getPath()));
						
				// 新建用例文件
				File srcFile = new File (templetePath + "CaseTemplate.xls");
				File destFile = new File (testCasePath + selectionNode.toString() + File.separator + caseName + ".xls");
				try {
					FileUtils.copyFile(srcFile, destFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ae.getActionCommand().equals("删除")) {
				TreePath treepath = tree.getSelectionPath();
				if(treepath == null || treepath.getPath().length == 1) { //没有选节点或者选中根节点
					JOptionPane.showMessageDialog(Editor.frame, "请选择要删除的模块或用例！", "提示", JOptionPane.WARNING_MESSAGE);
					return;
				}				
				int level = treepath.getPath().length; // 用长度来判断选中的节点类型  2-模块节点 3-用例节点
				// 获取选中的节点
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent();
				switch (level) {
				case 2 :
					if (JOptionPane.showConfirmDialog(Editor.frame, "确定删除该模块？", "提示", JOptionPane.YES_NO_OPTION) == 1) {
						return;
					}
					if(this.clearModuleDir(selectionNode.toString())) {					
						treeModel.removeNodeFromParent(selectionNode); // 删除节点
						this.removeModule(selectionNode.toString()); // 写入XML
						return;
					}					
					break;
			    case 3 :
				    if (JOptionPane.showConfirmDialog(Editor.frame, "确定删除该用例？", "提示", JOptionPane.YES_NO_OPTION) == 1) {
						return;
					}
				    String moduleName = treepath.getPath()[1].toString();
				    String caseName = selectionNode.toString();
				    if (this.clearCaseFile(moduleName,caseName)) {
				    	this.removeCase(moduleName, caseName); 	// 写入XML
				    	treeModel.removeNodeFromParent(selectionNode); // Tree中删除节点
				    	return;
				    }		
				    break;
			    }
			}
			if (ae.getActionCommand().equals("修改")) {
				TreePath treepath = tree.getSelectionPath();
				if(treepath == null || treepath.getPath().length == 1) { //没有选节点或者选中根节点
					JOptionPane.showMessageDialog(Editor.frame, "请选择要修改的模块或用例！", "提示", JOptionPane.WARNING_MESSAGE);
					return;
				}				
				int level = treepath.getPath().length; // 用长度来判断选中的节点类型  2-模块节点 3-用例节点
				// 获取选中的节点
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent();
				switch (level) {
				case 2 :
					String newModuleName = JOptionPane.showInputDialog(Editor.frame, "请输入新的模块名", "修改", JOptionPane.INFORMATION_MESSAGE);
					if (newModuleName == null || newModuleName.equals("")) return;
					if (!modifyModule(currentNode, newModuleName)) {
						JOptionPane.showMessageDialog(Editor.frame, "该模块已存在！", "提示", JOptionPane.WARNING_MESSAGE);
						return;						
					}
					// 修改节点名
					selectionNode.setUserObject(newModuleName);
					tree.updateUI();
					
					// 修改目录名
					File moduleFolder = new File(testCasePath, currentNode);
					moduleFolder.renameTo(new File(testCasePath, newModuleName));		
					
					break;
			    case 3 :
					String newCaseName = JOptionPane.showInputDialog(Editor.frame, "请输入新的用例名", "修改", JOptionPane.INFORMATION_MESSAGE);
					if (newCaseName == null || newCaseName.equals("")) return;
					String moduleName = treepath.getPath()[1].toString();
					if (!modifyCase(moduleName, currentNode, newCaseName)) {
						JOptionPane.showMessageDialog(Editor.frame, "该模块下已存在相同用例！", "提示", JOptionPane.WARNING_MESSAGE);
						return;						
					}
					// 修改节点名
					selectionNode.setUserObject(newCaseName);
					tree.updateUI();
					
					// 修改用例文件名
					File caseFile= new File(testCasePath + moduleName + File.separator + currentNode + ".xls");
					caseFile.renameTo(new File(testCasePath + moduleName + File.separator + newCaseName + ".xls"));
				    break;
			    }				
			}
		}

		private boolean addNewModule(String moduleName) {
			Document doc = readFromXml();
			Element root = doc.getRootElement();
			String xPath = String.format("//module[@name='%s']", moduleName); // 用Xpath找到对应的模块节点
			Element moduleNode = (Element)doc.selectSingleNode(xPath);
			if (moduleNode != null ) { //已经存在
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
			Attribute attrDate = moduleNode.attribute("name");//获取此节点的指定属性
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
			// 删除模块目录
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
			// 删除用例文件
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
			    OutputFormat format = OutputFormat.createPrettyPrint(); // 精美模式
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
			// 利用JTree的getLastSelectedPathComponent()方法取得目前选取的节点.
			DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();
			String nodeName = selectionNode.toString();
			// 判断是否为树叶节点，若是则显示文件内容，若不是则不做任何事
			if (selectionNode.isLeaf()) {
				TreePath treepath = tree.getSelectionPath();
				String moduleName = treepath.getPath()[1].toString();
				System.out.println(moduleName + nodeName);
				String path = testCasePath + moduleName + File.separator + nodeName + ".xls";
				List<Object> data = ExcelHelper.simpleReadJavaModel(path, 1, 1, TestStep.class);
				System.out.println(data.size());
				System.out.println(data);
				String[][] tableData = new String[1000][6];
				if (data.size() == 0) { // 没有数据
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
				repaintTable(Editor.table, tableData);	// 生成列表数据
			}			
		}
		
		private void repaintTable(JTable table, String[][] data) {
			table.setModel(Editor.generateTableModel(data));
	        TableColumn column = Editor.table.getColumnModel().getColumn(0);
	        column.setMaxWidth(60); // 第一列宽度
			JComboBox<String> comboBox = new JComboBox<String>();
			comboBox.setFont(new Font("微软雅黑",Font.PLAIN,12));
			ConfigReader.keyWordsList.forEach(el -> {
				KeyWord keyWord = (KeyWord)el;
				comboBox.addItem(keyWord.getFunction());
			});
			table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBox)); // 第三列为CheckBox类型		
		} 

}
