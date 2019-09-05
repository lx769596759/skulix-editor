package handle;

import utils.JdbcUtil;
import javafx.util.Pair;
import utils.LidarUtil;

import java.util.List;

public class LidarDataHandle implements Runnable{

    private List<String> list;

    private static final String INSERT_SQL = "INSERT INTO HISTORY (angle, distance) VALUES (?, ?) ";

    public LidarDataHandle (List<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (String data : list) {
            Pair p = LidarUtil.parse(data);
            String angle = p.getKey().toString();
            String distance = p.getKey().toString();
            JdbcUtil.insertOrUpdate(INSERT_SQL, angle, distance);
            p = null;
        }
    }
}
