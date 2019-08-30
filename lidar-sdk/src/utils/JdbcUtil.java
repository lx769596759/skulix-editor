package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @title JDBC工具类
 * 连接数据库
 * 执行SQL
 * 查询对象
 * 查询集合
 */
public class JdbcUtil {
    /**
     * 驱动名称
     */
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    /**
     * 数据库链接地址
     */
    private static final String url = "jdbc:mysql://localhost:3306/Product?useSSL=false";
    /**
     * 用户名
     */
    private static final String userName = "root";
    /**
     * 密码
     */
    private static final String password = "root";

    /**
     * 定义连接
     */
    private static Connection conn;
    /**
     * 定义STMT
     */
    private static PreparedStatement stmt;
    /**
     * 定义结果集
     */
    private static ResultSet rs;

    /** 初始化加载链接 */
    static {
        try {
            Class.forName(DRIVER_NAME);
            conn = DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException e) {
            System.err.println("驱动加载失败");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("数据库链接异常");
            e.printStackTrace();
        }
    }

    /**
     * 获取链接
     */
    public static Connection getConn() {
        return conn;
    }

    /**
     * 关闭链接,释放资源
     */
    public static void close() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }

            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            System.err.println("资源释放发生异常");
        }
    }

    /**
     * 获取指定数据库下所有的表名
     *
     * @param dbNm
     * @return
     */
    public static List<String> getAllTableName(String dbNm) {
        List<String> result = new ArrayList<String>();
        Statement st = null;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  WHERE TABLE_SCHEMA='" + dbNm + "'");
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            close();
        }
        return result;
    }

    /**
     * 执行SQL返回ResultSet
     */
    public static ResultSet executeSql(String sql, Object... args) {
        try {
            stmt = conn.prepareStatement(sql);
            if (null != args && args.length != 0) {
                for (int i = 0; i < args.length; i++) {
                    stmt.setObject(i + 1, args[i]);
                }
            }

            rs = stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("数据查询异常");
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 执行SQL进行INSERT和UPDATE
     */
    public static void update(String sql, Object... args) {
        try {
            stmt = conn.prepareStatement(sql);
            if (null != args && args.length != 0) {
                for (int i = 0; i < args.length; i++) {
                    stmt.setObject(i + 1, args[i]);
                }
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("数据更新异常");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

}