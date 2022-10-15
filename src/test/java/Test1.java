import cn.hutool.json.JSONUtil;
import com.meituan.order.HelloWorldApplication;
import com.meituan.order.controller.MenuController;
import com.meituan.order.controller.OrderController;
import com.meituan.order.controller.OrderDetailController;
import com.meituan.order.entity.dto.MenuIncomeDto;
import com.meituan.order.service.IMenuService;
import com.meituan.order.service.IOrderDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

@SpringBootTest(classes = HelloWorldApplication.class)
//@SpringBootTest
public class Test1 {


    @Resource
    public OrderController orderController;
    @Resource
    public MenuController menuController;
    @Resource
    public OrderDetailController orderDetailController;
    @Resource
    public IOrderDetailService orderDetailService;


    @Resource
    public IMenuService menuService;

    @Test
    public void test1() {

//        menuController.getMenuList();

//        menuController.getAllMenu(cookie);

//        orderDetailService.OrderItemList("1552977053480120400", Constant.cookie);


//        menuC.syncMenus();

//        JSONObject orderListJson = orderController.getOrderList("2022-07-31 00:00:00","2022-07-31 23:59:59", cookie);

//        MenuIncomeDto incomeDto = orderDetailController.acountMenuIncome("1552977053480120400");
//        System.out.println(JSONUtil.toJsonStr(incomeDto));

//        orderDetailController.typeIncome("2022-07-31 00:00:00","2022-07-31 23:59:59");

        orderDetailController.TypeIncomeFile("2021-10-01 00:00:00","2021-10-31 23:59:59", "income202110.xlsx", null);
        orderDetailController.TypeIncomeFile("2021-11-01 00:00:00","2021-11-30 23:59:59", "income202111.xlsx", null);
        orderDetailController.TypeIncomeFile("2021-12-01 00:00:00","2021-12-31 23:59:59", "income202112.xlsx", null);

    }




    @Test
    public void test2() {

        MenuIncomeDto incomeDto = orderDetailController.acountMenuIncome("1544996977685778573");
        System.out.println(JSONUtil.toJsonStr(incomeDto));


    }

    @Test
    public void test3() {
//        menuController.syncMenus();
        menuController.syncMenus("2022-09-01 00:00:00", "2022-10-31 23:59:59");

    }


    @Test
    public void test4() throws FileNotFoundException {
//        System.out.println(System.getProperty("user.dir"));
//        System.out.println(ResourceUtils.getURL("classpath:").getPath());

        float a = 33554431f;
        System.out.println(a);

        BigDecimal bigDecimal = new BigDecimal(555);
        System.out.println(bigDecimal);
        bigDecimal.divide(new BigDecimal(10));
        System.out.println(bigDecimal);
    }
}
