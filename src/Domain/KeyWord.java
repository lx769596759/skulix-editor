package Domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class KeyWord extends BaseRowModel{
	
	@ExcelProperty(index = 0)
	private String function;    // ����
	
	@ExcelProperty(index = 1)
	private String description; // ʹ��˵��
	
	@ExcelProperty(index = 2)
	private String param1;      // ����1
	
	@ExcelProperty(index = 3)
	private String param2;      // ����2
	
	@ExcelProperty(index = 4)
	private String param3;      // ����3
	
		
	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String toString() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("����", function);
		map.put("ʹ��˵��", description);
		map.put("����1", param1);
		map.put("����2", param2);
		map.put("����3", param3);
		return map.toString();
	}
}
