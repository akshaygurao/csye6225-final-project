package spring2018;

import com.csye6225.spring2018.controller.IndexController;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import spring2018.controller.IndexControllerTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IndexControllerTest.class)
public class SpringBootWebApplicationTest {

  @Ignore
  @Test
  public void contextLoads() {
  }

}
