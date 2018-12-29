package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRelation;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import Listeners.JTreeListeners;


public class Editor extends JFrame {
	public static JTable table;
	public static JTree tree;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BeautyEyeLNFHelper.launchBeautyEyeLNF();					
					UIManager.put("RootPane.setupButtonVisible", false);
					Editor frame = new Editor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Editor() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(1000, 700));
		this.setLocationRelativeTo(null);
		this.setTitle("Editor");
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnMenumeration = new JMenu("Menu1");
		menuBar.add(mnMenumeration);
		JMenu mnNewMenu = new JMenu("Menu2");
		menuBar.add(mnNewMenu);
		this.setLayout(new GridBagLayout());
		
		 //上侧的工具选择面板  
        JPanel toolSelectPanel = new JPanel();        
        this.add(toolSelectPanel, new GBC(0,0,2,1).  
                     setFill(GBC.BOTH).setIpad(500, 10).setWeight(100, 0));
        toolSelectPanel.setLayout(new BoxLayout(toolSelectPanel,BoxLayout.X_AXIS));  
        //toolSelectPanel.setLayout(new FlowLayout());
        //toolSelectPanel.setLayout(new GridLayout(0,2));
        toolSelectPanel.add(Box.createHorizontalStrut(5));
        
        JButton scratchButton = new JButton("屏幕截图");
        scratchButton.setFont(new Font("微软雅黑",Font.PLAIN,12));
        scratchButton.setIcon(new ImageIcon("pics/1.png"));
        toolSelectPanel.add(scratchButton);
        toolSelectPanel.add(Box.createHorizontalStrut(10)); 
        JButton insertButton = new JButton("插入图片");
        insertButton.setFont(new Font("微软雅黑",Font.PLAIN,12));
        insertButton.setIcon(new ImageIcon("pics/2.png"));
        toolSelectPanel.add(insertButton);   
        toolSelectPanel.add(Box.createHorizontalStrut(10)); 
        
        JButton locateButton = new JButton("定位");
        locateButton.setIcon(new ImageIcon("pics/3.png"));
        locateButton.setFont(new Font("微软雅黑",Font.PLAIN,12));
        toolSelectPanel.add(locateButton);


        //左侧的树形结构  
        JPanel treePanel = new JPanel();  
        this.add(treePanel,new GBC(0,1).  
                     setFill(GBC.BOTH).setIpad(200, 90).setWeight(0, 100));
        treePanel.setLayout(new GridLayout(0,1));
        JScrollPane scrollPane = new JScrollPane();
        
        //树相关
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("资源管理器");
		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("文件夹");
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("我的电脑");
		DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("收藏夹");
		DefaultMutableTreeNode node4 = new DefaultMutableTreeNode("Readme");
		root.add(node1);
		root.add(node2);
		root.add(node3);
		root.add(node4);

		DefaultMutableTreeNode leafnode = new DefaultMutableTreeNode("公司文件");
		node1.add(leafnode);
		leafnode = new DefaultMutableTreeNode("个人信件");
		node1.add(leafnode);
		leafnode = new DefaultMutableTreeNode("私人文件");
		node1.add(leafnode);

		leafnode = new DefaultMutableTreeNode("本机磁盘(C:)");
		node2.add(leafnode);
		leafnode = new DefaultMutableTreeNode("本机磁盘(D:)");
		node2.add(leafnode);
		leafnode = new DefaultMutableTreeNode("本机磁盘(E:)");
		node2.add(leafnode);

		DefaultMutableTreeNode node31 = new DefaultMutableTreeNode("网站列表");
		node3.add(node31);

		leafnode = new DefaultMutableTreeNode("天勤网站");
		node31.add(leafnode);
		leafnode = new DefaultMutableTreeNode("足球消息");
		node31.add(leafnode);
		leafnode = new DefaultMutableTreeNode("网络书店");
		node31.add(leafnode);

		tree = new JTree(root);
		tree.setFont(new Font("微软雅黑",Font.PLAIN,12));
		tree.setEditable(true);// 设置JTree为可编辑的
		JTreeListeners treeListeners = new JTreeListeners();
		tree.addMouseListener(treeListeners.new MouseHandle());// 使Tree加入检测Mouse事件，以便取得节点名称
		// 下面两行取得DefaultTreeModel,并检测是否有TreeModelEvent事件.
		DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
		treeModel.addTreeModelListener(treeListeners);
        
		
		
        scrollPane.setViewportView(tree);
        treePanel.add(scrollPane);
        JPopupMenu popup = new JPopupMenu(); // 右键弹出菜单
        JMenuItem jmi1 = new JMenuItem("新增模块");
        JMenuItem jmi2 = new JMenuItem("新增用例");
        JMenuItem jmi3 = new JMenuItem("删除");
        jmi1.addActionListener(treeListeners);
        jmi2.addActionListener(treeListeners);
        jmi3.addActionListener(treeListeners);
        tree.setComponentPopupMenu(popup);
        popup.add(jmi1);
        popup.add(jmi2);
        popup.add(jmi3);
        
        
        
        
        //右侧的表格编辑面板  
        JPanel tablePanel = new JPanel();  
        tablePanel.setBackground(Color.WHITE);  
        this.add(tablePanel,new GBC(1,1).setFill(GBC.BOTH)); 
        tablePanel.setLayout(new GridLayout(0,1));
        JScrollPane scrollPane1 = new JScrollPane();
        table = new JTable();
        scrollPane1.setViewportView(table);
        tablePanel.add(scrollPane1);
       
        //下侧的输出面板  
        JPanel consolePanel = new JPanel();  
        consolePanel.setBackground(Color.LIGHT_GRAY);  
        this.add(consolePanel,new GBC(0,2,2,1).  
                     setFill(GBC.BOTH).setIpad(200,50).setWeight(100, 0));  
        
        //低侧的状态面板  
        JPanel statePanel = new JPanel();  
        statePanel.setBackground(Color.CYAN);  
        this.add(statePanel,new GBC(0,3,2,1).  
                      setFill(GBC.BOTH).setIpad(200, 20).setWeight(100, 0));  
        		
	}
	
	class GBC extends GridBagConstraints  
	{  
	   //初始化左上角位置  
	   public GBC(int gridx, int gridy)  
	   {  
	      this.gridx = gridx;  
	      this.gridy = gridy;  
	   }  
	  
	   //初始化左上角位置和所占行数和列数  
	   public GBC(int gridx, int gridy, int gridwidth, int gridheight)  
	   {  
	      this.gridx = gridx;  
	      this.gridy = gridy;  
	      this.gridwidth = gridwidth;  
	      this.gridheight = gridheight;  
	   }  
	  
	   //对齐方式  
	   public GBC setAnchor(int anchor)  
	   {  
	      this.anchor = anchor;  
	      return this;  
	   }  
	  
	   //是否拉伸及拉伸方向  
	   public GBC setFill(int fill)  
	   {  
	      this.fill = fill;  
	      return this;  
	   }  
	  
	   //x和y方向上的增量  
	   public GBC setWeight(double weightx, double weighty)  
	   {  
	      this.weightx = weightx;  
	      this.weighty = weighty;  
	      return this;  
	   }  
	  
	   //外部填充  
	   public GBC setInsets(int distance)  
	   {  
	      this.insets = new Insets(distance, distance, distance, distance);  
	      return this;  
	   }  
	  
	   //外填充  
	   public GBC setInsets(int top, int left, int bottom, int right)  
	   {  
	      this.insets = new Insets(top, left, bottom, right);  
	      return this;  
	   }  
	  
	   //内填充  
	   public GBC setIpad(int ipadx, int ipady)  
	   {  
	      this.ipadx = ipadx;  
	      this.ipady = ipady;  
	      return this;  
	   }  
	}
}
