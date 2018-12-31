package Domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class TestStep extends BaseRowModel{
	
	@ExcelProperty(index = 0)
	private String seriesNo;    // 步骤ID
	
	@ExcelProperty(index = 1)
	private String description; // 步骤描述
	
	@ExcelProperty(index = 2)
	private String operate;     // 操作
	
	@ExcelProperty(index = 3)
	private String param1;      // 参数1
	
	@ExcelProperty(index = 4)
	private String param2;      // 参数2
	
	@ExcelProperty(index = 5)
	private String param3;      // 参数3
	
	
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
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
		map.put("步骤ID", seriesNo);
		map.put("步骤描述", description);
		map.put("操作", operate);
		map.put("参数1", param1);
		map.put("参数2", param2);
		map.put("参数3", param3);
		return map.toString();
	}
}
