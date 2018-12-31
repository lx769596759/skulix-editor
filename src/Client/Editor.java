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
	private final static String[] tableTitle = { "����ID", "��������", "����", "����1", "����2", "����3"};
	private Font normalFont = new Font("΢���ź�",Font.PLAIN,12);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// ����Ч�����
					System.setProperty("sun.java2d.noddraw", "true");
					BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
					BeautyEyeLNFHelper.launchBeautyEyeLNF();				
					UIManager.put("RootPane.setupButtonVisible", false);
					
					// ��������
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
		
		// �˵���
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnMenumeration = new JMenu("Menu1");
		menuBar.add(mnMenumeration);
		JMenu mnNewMenu = new JMenu("Menu2");
		menuBar.add(mnNewMenu);
		this.setLayout(new GridBagLayout());
		
		 //�ϲ�Ĺ���ѡ�����  
        JPanel toolSelectPanel = new JPanel();        
        this.add(toolSelectPanel, new GBC(0,0,2,1).  
                     setFill(GBC.BOTH).setIpad(500, 10).setWeight(100, 0));
        toolSelectPanel.setLayout(new BoxLayout(toolSelectPanel,BoxLayout.X_AXIS));  
        //toolSelectPanel.setLayout(new FlowLayout());
        //toolSelectPanel.setLayout(new GridLayout(0,2));
        toolSelectPanel.add(Box.createHorizontalStrut(5));
        
        JButton scratchButton = new JButton("��Ļ��ͼ");
        scratchButton.setFont(normalFont);
        scratchButton.setIcon(new ImageIcon("pics/1.png"));
        toolSelectPanel.add(scratchButton);
        toolSelectPanel.add(Box.createHorizontalStrut(10)); 
        JButton insertButton = new JButton("����ͼƬ");
        insertButton.setFont(normalFont);
        insertButton.setIcon(new ImageIcon("pics/2.png"));
        toolSelectPanel.add(insertButton);   
        toolSelectPanel.add(Box.createHorizontalStrut(10)); 
        
        JButton locateButton = new JButton("��λ");
        locateButton.setIcon(new ImageIcon("pics/3.png"));
        locateButton.setFont(normalFont);
        toolSelectPanel.add(locateButton);


        //�������νṹ  
        JPanel treePanel = new JPanel();  
        this.add(treePanel,new GBC(0,1).  
                     setFill(GBC.BOTH).setIpad(200, 90).setWeight(0, 100));
        treePanel.setLayout(new GridLayout(0,1));
        JScrollPane scrollPane = new JScrollPane();
        
        //�����
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ģ��");
		tree = new JTree(root);
		ConfigReader configReader = new ConfigReader();
		configReader.initializeTree(tree); // ��ȡ�����ļ���ȡJTree�ĳ�ʼֵ
		tree.setFont(new Font("΢���ź�",Font.BOLD,13));
		tree.setEditable(false);// ����JTreeΪ���ɱ༭��
		JTreeListeners treeListeners = new JTreeListeners();
		tree.addMouseListener(treeListeners); // ʹTree������Mouse�¼����Ա�ȡ�ýڵ�����
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // ����Tree��ѡ��ģʽΪһ��ֻ��ѡ��һ���ڵ�
		tree.addTreeSelectionListener(treeListeners); // ����TreeSelectionEvent�¼�����
        scrollPane.setViewportView(tree);
        treePanel.add(scrollPane);
        JPopupMenu popup = new JPopupMenu(); // �Ҽ������˵�
        JMenuItem addModule = new JMenuItem("����ģ��");
        JMenuItem addCase = new JMenuItem("��������");
        JMenuItem delete = new JMenuItem("ɾ��");
        JMenuItem modify = new JMenuItem("�޸�");
        addModule.addActionListener(treeListeners);
        addCase.addActionListener(treeListeners);
        delete.addActionListener(treeListeners);
        modify.addActionListener(treeListeners);
        tree.setComponentPopupMenu(popup);
        popup.add(addModule);
        popup.add(addCase);
        popup.add(delete);
        popup.add(modify);
        
        
        
        // �Ҳ�ı��༭���  
        JPanel tablePanel = new JPanel();  
        tablePanel.setBackground(Color.WHITE);  
        this.add(tablePanel,new GBC(1,1).setFill(GBC.BOTH)); 
        tablePanel.setLayout(new GridLayout(0,1));
        JScrollPane scrollPane1 = new JScrollPane();
        table = new JTable();
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER); // ��ͷ�������
        table.getTableHeader().setFont(new Font("΢���ź�",Font.BOLD,13)); // ��ͷ������ʽ
        table.setFont(normalFont); // ���������ʽ
        table.setShowHorizontalLines(true); // ˮƽ������
        table.setShowVerticalLines(true); // ��ֱ������
        
        scrollPane1.setViewportView(table);
        tablePanel.add(scrollPane1);
        //�²��������  
        JPanel consolePanel = new JPanel();  
        consolePanel.setBackground(Color.LIGHT_GRAY);  
        this.add(consolePanel,new GBC(0,2,2,1).  
                     setFill(GBC.BOTH).setIpad(200,50).setWeight(100, 0));  
        
        //�Ͳ��״̬���  
        JPanel statePanel = new JPanel();  
        statePanel.setBackground(Color.CYAN);  
        this.add(statePanel,new GBC(0,3,2,1).  
                      setFill(GBC.BOTH).setIpad(200, 20).setWeight(100, 0));  
        		
	}
	
	public static DefaultTableModel generateTableModel(String[][] data) {
		DefaultTableModel defaultModel = new DefaultTableModel(data, tableTitle) {
			public boolean isCellEditable(int row, int col) {
				  if (col == 0) return false; // ��һ�в��ɱ༭
			      return true;
			   }
		};
		return defaultModel;		
	}
	
	class GBC extends GridBagConstraints  
	{  
	   private static final long serialVersionUID = 1L;

	//��ʼ�����Ͻ�λ��  
	   public GBC(int gridx, int gridy)  
	   {  
	      this.gridx = gridx;  
	      this.gridy = gridy;  
	   }  
	  
	   //��ʼ�����Ͻ�λ�ú���ռ����������  
	   public GBC(int gridx, int gridy, int gridwidth, int gridheight)  
	   {  
	      this.gridx = gridx;  
	      this.gridy = gridy;  
	      this.gridwidth = gridwidth;  
	      this.gridheight = gridheight;  
	   }  
	  
	   //���뷽ʽ  
	   public GBC setAnchor(int anchor)  
	   {  
	      this.anchor = anchor;  
	      return this;  
	   }  
	  
	   //�Ƿ����켰���췽��  
	   public GBC setFill(int fill)  
	   {  
	      this.fill = fill;  
	      return this;  
	   }  
	  
	   //x��y�����ϵ�����  
	   public GBC setWeight(double weightx, double weighty)  
	   {  
	      this.weightx = weightx;  
	      this.weighty = weighty;  
	      return this;  
	   }  
	  
	   //�ⲿ���  
	   public GBC setInsets(int distance)  
	   {  
	      this.insets = new Insets(distance, distance, distance, distance);  
	      return this;  
	   }  
	  
	   //�����  
	   public GBC setInsets(int top, int left, int bottom, int right)  
	   {  
	      this.insets = new Insets(top, left, bottom, right);  
	      return this;  
	   }  
	  
	   //�����  
	   public GBC setIpad(int ipadx, int ipady)  
	   {  
	      this.ipadx = ipadx;  
	      this.ipady = ipady;  
	      return this;  
	   }  
	}
}
