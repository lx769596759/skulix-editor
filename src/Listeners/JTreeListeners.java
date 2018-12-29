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
			if (ae.getActionCommand().equals("�����ڵ�")) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("�½ڵ�");
				newNode.setAllowsChildren(true);
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
				
//				TreePath parentPath = tree.getSelectionPath();
//
//				// ȡ���½ڵ�ĸ��ڵ�
//				parentNode = (DefaultMutableTreeNode) (parentPath
//						.getLastPathComponent());

				// ��DefaultTreeModel��insertNodeInto�������������½ڵ�
				treeModel.insertNodeInto(newNode, root, root
						.getChildCount());

				// tree��scrollPathToVisible()������ʹTree���Զ�չ���ļ����Ա���ʾ��������½ڵ㡣��û�������������½ڵ�
				// �ᱻ �����ļ����У����������չ���ļ��вſ��õ���
				tree.scrollPathToVisible(new TreePath(newNode.getPath()));
			}
			if (ae.getActionCommand().equals("ɾ���ڵ�")) {
				TreePath treepath = tree.getSelectionPath();
				if (treepath != null) {
					// ��������ȡ��ѡȡ�ڵ�ĸ��ڵ�.
					DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath
							.getLastPathComponent();
					TreeNode parent = (TreeNode) selectionNode.getParent();
					if (parent != null) {
						// ��DefaultTreeModel��removeNodeFromParent()����ɾ���ڵ㣬���������ӽڵ㡣
						treeModel.removeNodeFromParent(selectionNode);
					}
				}
			}
			if (ae.getActionCommand().equals("������нڵ�")) {

				// ����һ�У���DefaultTreeModel��getRoot()����ȡ�ø��ڵ�.
				DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel
						.getRoot();

				// ����һ��ɾ�������ӽڵ�.
				rootNode.removeAllChildren();

				// ɾ������������DefaultTreeModel��reload()����������Tree�Ľڵ�Ż�������ɾ��.
				treeModel.reload();
			}
			
			
		}

}
