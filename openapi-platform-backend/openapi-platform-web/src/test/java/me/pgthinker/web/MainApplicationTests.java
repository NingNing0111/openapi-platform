package me.pgthinker.web;

import me.pgthinker.model.vo.ApiSumCountVO;
import me.pgthinker.web.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Project: com.pgthinker.backend
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/23 09:25
 * @Description:
 */
@SpringBootTest
public class MainApplicationTests {

    @Autowired
    private UserInterfaceInfoService  userInterfaceInfoService;
    @Test
    public void test(){
        ApiSumCountVO apiSumCountVO = userInterfaceInfoService.ApiCount();
        System.out.println(apiSumCountVO);
    }
}
