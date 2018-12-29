package Client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ConfigReader {
	
	public static String workPath;
	public static String testCasePath;
	public static String templetePath;
	public static String configFilesPath;
	
	static {
		workPath = System.getProperty("user.dir") + File.separator; // 工作路径
		testCasePath = workPath + "TestCases\\";  // 用例路径
		templetePath = workPath + "Templete\\";   // 模板路径
		configFilesPath = workPath + "Resources\\"; // 配置文件路径
	}
	
	public void initializeTree(JTree tree) {
		this.parseCaseXml(tree); // 解析用户用例配置文件
		
	
	}
	
	private void parseCaseXml(JTree tree) {
		File caseSets = new File(configFilesPath + "TestCase.xml");
		if (!caseSets.exists()) {
			System.out.println("获取配置文件失败！");
			return;
		}
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(caseSets);
			Element root = doc.getRootElement();
			List<Element> modules = root.elements("module"); // 所有的模块
			Map<String,List<String>> resultMap = new HashMap<String,List<String>>();
			modules.forEach(module -> {
				List<String> caseList = new ArrayList<String>();
				List<Element> cases = module.elements("case"); // 模块下所有的用例
				cases.forEach(testCase -> {					
					caseList.add(testCase.getText());
				});
				resultMap.put(module.attribute("name").getValue(), caseList);
			});
			Set<String> set = resultMap.keySet();
			DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode)tree.getModel().getRoot();
			set.forEach(el -> {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(el);				
				List<String> caseList = resultMap.get(el);
				caseList.forEach(element -> {
					DefaultMutableTreeNode leafnode  = new DefaultMutableTreeNode(element);
					node.add(leafnode);
				});
				treeRoot.add(node);	
			});
		} catch (DocumentException e) {
			
		}
		
		
	}

}
