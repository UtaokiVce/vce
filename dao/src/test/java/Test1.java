import org.junit.jupiter.api.Test;

import java.util.Date;

public class Test1 {

    @Test
    public void test1(){

        long time = new Date().getTime();
        long millis = System.currentTimeMillis();
        System.out.println(time+"\n"+millis);

    }

}
