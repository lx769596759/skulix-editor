package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import Listeners.JTreeListeners;

// http://www.iteedu.com/plang/java/jtswingchxshj/58.php

public class Editor extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Editor frame;
	public static JTable table;
	public static JTree tree;
	private final static String[] tableTitle = { "步骤ID", "步骤描述", "操作", "参数1", "参数2", "参数3"};
	private Font normalFont = new Font("微软雅黑",Font.PLAIN,12);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// 界面效果相关
					System.setProperty("sun.java2d.noddraw", "true");
					BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
					BeautyEyeLNFHelper.launchBeautyEyeLNF();				
					UIManager.put("RootPane.setupButtonVisible", false);
					
					// 启动界面
					frame = new Editor();
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
		this.setMinimumSize(new Dimension(1200, 900));
		this.setLocationRelativeTo(null);
		this.setTitle("Editor");
		
		// 菜单栏
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
        scratchButton.setFont(normalFont);
        scratchButton.setIcon(new ImageIcon("pics/1.png"));
        toolSelectPanel.add(scratchButton);
        toolSelectPanel.add(Box.createHorizontalStrut(10)); 
        JButton insertButton = new JButton("插入图片");
        insertButton.setFont(normalFont);
        insertButton.setIcon(new ImageIcon("pics/2.png"));
        toolSelectPanel.add(insertButton);   
        toolSelectPanel.add(Box.createHorizontalStrut(10)); 
        
        JButton locateButton = new JButton("定位");
        locateButton.setIcon(new ImageIcon("pics/3.png"));
        locateButton.setFont(normalFont);
        toolSelectPanel.add(locateButton);


        //左侧的树形结构  
        JPanel treePanel = new JPanel();  
        this.add(treePanel,new GBC(0,1).  
                     setFill(GBC.BOTH).setIpad(200, 90).setWeight(0, 100));
        treePanel.setLayout(new GridLayout(0,1));
        JScrollPane scrollPane = new JScrollPane();
        
        //树相关
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("模块");
		tree = new JTree(root);
		ConfigReader configReader = new ConfigReader();
		configReader.initializeTree(tree); // 读取配置文件获取JTree的初始值
		tree.setFont(new Font("微软雅黑",Font.BOLD,13));
		tree.setEditable(false);// 设置JTree为不可编辑的
		JTreeListeners treeListeners = new JTreeListeners();
		tree.addMouseListener(treeListeners); // 使Tree加入检测Mouse事件，以便取得节点名称
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // 设置Tree的选择模式为一次只能选择一个节点
		tree.addTreeSelectionListener(treeListeners); // 增加TreeSelectionEvent事件监听
        scrollPane.setViewportView(tree);
        treePanel.add(scrollPane);
        JPopupMenu popup = new JPopupMenu(); // 右键弹出菜单
        JMenuItem addModule = new JMenuItem("新增模块");
        JMenuItem addCase = new JMenuItem("新增用例");
        JMenuItem delete = new JMenuItem("删除");
        JMenuItem modify = new JMenuItem("修改");
        addModule.addActionListener(treeListeners);
        addCase.addActionListener(treeListeners);
        delete.addActionListener(treeListeners);
        modify.addActionListener(treeListeners);
        tree.setComponentPopupMenu(popup);
        popup.add(addModule);
        popup.add(addCase);
        popup.add(delete);
        popup.add(modify);
        
        
        
        // 右侧的表格编辑面板  
        JPanel tablePanel = new JPanel();  
        tablePanel.setBackground(Color.WHITE);  
        this.add(tablePanel,new GBC(1,1).setFill(GBC.BOTH)); 
        tablePanel.setLayout(new GridLayout(0,1));
        JScrollPane scrollPane1 = new JScrollPane();
        table = new JTable();
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER); // 表头字体居中
        table.getTableHeader().setFont(new Font("微软雅黑",Font.BOLD,13)); // 表头字体样式
        table.setFont(normalFont); // 表格字体样式
        table.setShowHorizontalLines(true); // 水平网格线
        table.setShowVerticalLines(true); // 垂直网格线
        
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
	
	public static DefaultTableModel generateTableModel(String[][] data) {
		DefaultTableModel defaultModel = new DefaultTableModel(data, tableTitle) {
			public boolean isCellEditable(int row, int col) {
				  if (col == 0) return false; // 第一列不可编辑
			      return true;
			   }
		};
		return defaultModel;		
	}
	
	class GBC extends GridBagConstraints  
	{  
	   private static final long serialVersionUID = 1L;

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
