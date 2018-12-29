package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.io.FileUtils;
import Client.Editor;

public class JTreeListeners implements TreeModelListener,ActionListener{
	
	private String workPath;
	private String testCasePath;
	private String templetePath;
	
	{
		workPath = System.getProperty("user.dir") + File.separator; // ����·��
		testCasePath = workPath + "TestCases\\"; // ����·��
		templetePath = workPath + "Templete\\"; // ģ��·��
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		TreePath treePath = e.getTreePath();
		System.out.println(treePath);
		// ����������TreeModelEventȡ�õ�DefaultMutableTreeNodeΪ�ڵ�ĸ��ڵ㣬�������û���ѡ
		// �Ľڵ㣬������Ҫ�ر�ע�⡣Ҫȡ�������Ľڵ���Ҫ�ټ�д����6�д���.
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath
				.getLastPathComponent();
		try {
			// getChildIndices()�����᷵��Ŀǰ�޸Ľڵ������ֵ����������ֻ�޸�һ���ڵ㣬��˽ڵ�����ֵ�ͷ���index[0]
			// ��λ�ã�����ѡ�Ľڵ�Ϊroot
			// node,��getChildIndices()�ķ���ֵΪnull,��������ĵڶ��о��ڴ����ѡroot
			// node������NullPointerException����.
			int[] index = e.getChildIndices();
			// ��DefaultMutableTreeNode���getChildAt()����ȡ���޸ĵĽڵ����.
			node = (DefaultMutableTreeNode) node.getChildAt(index[0]);
		} catch (NullPointerException exc) {
			
		}
		// ��DefaultMutableTreeNode��getUserObject()����ȡ�ýڵ�����ݣ�����д��node.toString()����ͬ.		
	}

	@Override
	public void treeNodesInserted(TreeModelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeStructureChanged(TreeModelEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	// ����Mouse��ѡ�¼�
    public class MouseHandle extends MouseAdapter {
			public void mousePressed(MouseEvent e) {
				try {
					JTree tree = (JTree) e.getSource();
					/*
					 * JTree��getSelectionPath()������ȡ�ô�root
					 * node����ѡ�ڵ��һ��path,��pathΪһ��ֱ�ߣ���������е�ͼʾ�����ѡ����������(E:)��,��Tree
					 * PathΪ"��Դ������"-->"�ҵĵ���"-->"��������(E:)",�������TreePath
					 * ��getLastPathComponent()�����Ϳ���ȡ������ѡ�Ľڵ�.
					 */

					TreePath treepath=tree.getSelectionPath();
					TreeNode treenode = (TreeNode) treepath.getLastPathComponent();
					String nodeName = treenode.toString();
					System.out.println(nodeName);
				} catch (NullPointerException ne) {
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			JTree tree = Editor.tree;
			DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
			if (ae.getActionCommand().equals("����ģ��")) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("��ģ��");
				newNode.setAllowsChildren(true);
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
				// ��DefaultTreeModel��insertNodeInto()���������½ڵ�
				treeModel.insertNodeInto(newNode, root, root.getChildCount());
				// չ���ڵ�
				tree.scrollPathToVisible(new TreePath(newNode.getPath()));
				
				// ����ģ���ļ���				
				File moduleFolder = new File(testCasePath,"��ģ��");
				if (!moduleFolder.exists()) {
					moduleFolder.mkdirs();
				}
			}
			if (ae.getActionCommand().equals("��������")) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("������");
				newNode.setAllowsChildren(false);
				TreePath treepath = tree.getSelectionPath();
				if (treepath != null && treepath.getPath().length == 2) { // �㼶����2��˵��Ϊģ��
					// ��ȡѡ�еĽڵ�
					DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent();
					treeModel.insertNodeInto(newNode, selectionNode, selectionNode.getChildCount());
					tree.scrollPathToVisible(new TreePath(newNode.getPath()));
						
					// �½������ļ�
					File srcFile = new File (templetePath + "CaseTemplate.xls");
					File destFile = new File (testCasePath + selectionNode.toString() + "\\������.xls");
					try {
						FileUtils.copyFile(srcFile, destFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				// �������
				System.out.println("��ָ��ģ�飡");
			}
			if (ae.getActionCommand().equals("ɾ��")) {
				TreePath treepath = tree.getSelectionPath();
				if(treepath == null) { //û��ѡ�ڵ�
					System.out.println("��ѡ��Ҫɾ���Ľڵ㣡");
					return;
				}				
				int level = treepath.getPath().length;
				// ��ȡѡ�еĽڵ�
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent();
				switch (level) {
				case 1 :
					System.out.println("ȷ��ɾ������ģ���������");
					if (this.clearAllModules()){
						selectionNode.removeAllChildren();
						treeModel.reload(); // ���¼�����
						return;
					}										
					break;
				case 2 :
					System.out.println("ȷ��ɾ����ģ�飿");
					if(this.clearUniqueModule(selectionNode.toString())) {
						treeModel.removeNodeFromParent(selectionNode); // ɾ���ڵ�
						return;
					}					
					break;
			    case 3 :
				    System.out.println("ȷ��ɾ����������");
				    String moduleName = treepath.getPath()[1].toString();
				    String caseName = selectionNode.toString();
				    if (this.clearUniqueCase(moduleName,caseName)) {
				    	treeModel.removeNodeFromParent(selectionNode); // ɾ���ڵ�
				    	return;
				    }		
				    break;
			    }
				System.out.println("ɾ��ʧ�ܣ����Ժ����ԣ�");
			}			
		}
		
		private boolean clearAllModules() {
			// ɾ������ģ���ļ���
			File[] files = new File (testCasePath).listFiles();
			for (File file : files) {
				try {
					FileUtils.deleteDirectory(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
		
		private boolean clearUniqueModule(String moduleName) {
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
		
		private boolean clearUniqueCase(String moduleName, String caseName) {
			// ɾ�������ļ�
			String filePath = testCasePath + moduleName + File.separator + caseName + ".xls";
			try {
				new File(filePath).delete();
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		
		

}
