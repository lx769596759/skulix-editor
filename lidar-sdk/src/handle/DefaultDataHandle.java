package handle;

import java.util.List;

public class DefaultDataHandle implements DataHandle {
    @Override
    public void doHandle(List<String> list) {
        System.out.println(list);
    }
}
