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
			if (ae.getActionCommand().equals("新增模块")) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("新模块");
				newNode.setAllowsChildren(true);
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
				// 由DefaultTreeModel的insertNodeInto()方法增加新节点
				treeModel.insertNodeInto(newNode, root, root.getChildCount());
				// 展开节点
				tree.scrollPathToVisible(new TreePath(newNode.getPath()));
			}
			if (ae.getActionCommand().equals("新增用例")) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("新用例");
				newNode.setAllowsChildren(false);
				TreePath treepath = tree.getSelectionPath();
				if (treepath != null && treepath.getPath().length <= 2) { // 层级不能大于2
					// 获取选中的节点
					DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent();
					if (selectionNode != null && selectionNode != treeModel.getRoot()) { // 不能为null或者根节点
						treeModel.insertNodeInto(newNode, selectionNode, selectionNode.getChildCount());
						tree.scrollPathToVisible(new TreePath(newNode.getPath()));
					}
				}
				// 不符合要求
				System.out.println("请指定模块！");		
			}
			if (ae.getActionCommand().equals("删除")) {
				TreePath treepath = tree.getSelectionPath();
				if(treepath == null) { //没有选节点
					System.out.println("请选择要删除的节点！");
					return;
				}
				
				int level = treepath.getPath().length;
				// 获取选中的节点
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) treepath.getLastPathComponent();
				switch (level) {
				case 1 :
					System.out.println("确定删除所有模块和用例？");
					selectionNode.removeAllChildren();
					treeModel.reload(); // 重新加载树
					break;
				case 2 :
					System.out.println("确定删除该模块？");					
					treeModel.removeNodeFromParent(selectionNode);
					break;
			    case 3 :
				    System.out.println("确定删除该用例？");
				    treeModel.removeNodeFromParent(selectionNode);
				    break;
			    }
			}
			
			
		}

}
