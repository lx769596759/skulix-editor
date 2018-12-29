package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import Client.Editor;

public class JTreeListeners implements TreeModelListener,ActionListener{

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
			}
			if (ae.getActionCommand().equals("��������")) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("������");
				newNode.setAllowsChildren(false);
				TreePath treepath = tree.getSelectionPath();
				if (treepath != null && treepath.getPath().length <= 2) { // �㼶���ܴ���2
					// ��ȡѡ�еĽڵ�
					DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent();
					if (selectionNode != null && selectionNode != treeModel.getRoot()) { // ����Ϊnull���߸��ڵ�
						treeModel.insertNodeInto(newNode, selectionNode, selectionNode.getChildCount());
						tree.scrollPathToVisible(new TreePath(newNode.getPath()));
					}
				}
				// ������Ҫ��
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
					selectionNode.removeAllChildren();
					treeModel.reload(); // ���¼�����
					break;
				case 2 :
					System.out.println("ȷ��ɾ����ģ�飿");					
					treeModel.removeNodeFromParent(selectionNode);
					break;
			    case 3 :
				    System.out.println("ȷ��ɾ����������");
				    treeModel.removeNodeFromParent(selectionNode);
				    break;
			    }
			}
			
			
		}

}
