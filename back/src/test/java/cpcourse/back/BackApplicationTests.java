package cpcourse.back;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackApplicationTests {

    @Test
    public void contextLoads() {
        Integer test1 = 0;
        test(test1);
        System.out.println(test1);
    }
    private void test(Integer a){
        a = a+1;
    }

}
