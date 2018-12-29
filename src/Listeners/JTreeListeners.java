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
		// 下面这行由TreeModelEvent取得的DefaultMutableTreeNode为节点的父节点，而不是用户点选
		// 的节点，这点读者要特别注意。要取得真正的节点需要再加写下面6行代码.
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath
				.getLastPathComponent();
		try {
			// getChildIndices()方法会返回目前修改节点的索引值。由于我们只修改一个节点，因此节点索引值就放在index[0]
			// 的位置，若点选的节点为root
			// node,则getChildIndices()的返回值为null,程序下面的第二行就在处理点选root
			// node产生的NullPointerException问题.
			int[] index = e.getChildIndices();
			// 由DefaultMutableTreeNode类的getChildAt()方法取得修改的节点对象.
			node = (DefaultMutableTreeNode) node.getChildAt(index[0]);
		} catch (NullPointerException exc) {
			
		}
		// 由DefaultMutableTreeNode类getUserObject()方法取得节点的内容，或是写成node.toString()亦相同.		
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
	
	// 处理Mouse点选事件
    public class MouseHandle extends MouseAdapter {
			public void mousePressed(MouseEvent e) {
				try {
					JTree tree = (JTree) e.getSource();
					/*
					 * JTree的getSelectionPath()方法会取得从root
					 * node到点选节点的一条path,此path为一条直线，如程序运行的图示若你点选“本机磁盘(E:)”,则Tree
					 * Path为"资源管理器"-->"我的电脑"-->"本机磁盘(E:)",因此利用TreePath
					 * 的getLastPathComponent()方法就可以取得所点选的节点.
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
			if (ae.getActionCommand().equals("新增节点")) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("新节点");
				newNode.setAllowsChildren(true);
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
				
//				TreePath parentPath = tree.getSelectionPath();
//
//				// 取得新节点的父节点
//				parentNode = (DefaultMutableTreeNode) (parentPath
//						.getLastPathComponent());

				// 由DefaultTreeModel的insertNodeInto（）方法增加新节点
				treeModel.insertNodeInto(newNode, root, root
						.getChildCount());

				// tree的scrollPathToVisible()方法在使Tree会自动展开文件夹以便显示所加入的新节点。若没加这行则加入的新节点
				// 会被 包在文件夹中，你必须自行展开文件夹才看得到。
				tree.scrollPathToVisible(new TreePath(newNode.getPath()));
			}
			if (ae.getActionCommand().equals("删除节点")) {
				TreePath treepath = tree.getSelectionPath();
				if (treepath != null) {
					// 下面两行取得选取节点的父节点.
					DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath
							.getLastPathComponent();
					TreeNode parent = (TreeNode) selectionNode.getParent();
					if (parent != null) {
						// 由DefaultTreeModel的removeNodeFromParent()方法删除节点，包含它的子节点。
						treeModel.removeNodeFromParent(selectionNode);
					}
				}
			}
			if (ae.getActionCommand().equals("清除所有节点")) {

				// 下面一行，由DefaultTreeModel的getRoot()方法取得根节点.
				DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel
						.getRoot();

				// 下面一行删除所有子节点.
				rootNode.removeAllChildren();

				// 删除完后务必运行DefaultTreeModel的reload()操作，整个Tree的节点才会真正被删除.
				treeModel.reload();
			}
			
			
		}

}
