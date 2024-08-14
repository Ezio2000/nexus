package org.nexus.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author Xieningjun
 * @date 2024/7/15 17:29
 */
@RestController
@RequestMapping("/mybatis")
public class MybatisController {

    @Autowired
    private MybatisService mybatisService;

    @GetMapping("/insert")
    public boolean insert(@RequestParam int id1, @RequestParam int id2) {
        return mybatisService.newUsers(
                Arrays.asList(
                        new NexusUser() {{setId(id1);setUsername("谢宁筠");setPassword("123456");}},
                        new NexusUser() {{setId(id2);setUsername("吴德鹏");setPassword("123456");}}
                )
        );
    }

    @GetMapping("/update")
    public boolean update(@RequestParam int id1, @RequestParam int id2) {
        return mybatisService.upUsers(
                Arrays.asList(
                        new NexusUser() {{setId(id1);setUsername("谢宁筠");setPassword("123456");}},
                        new NexusUser() {{setId(id2);setUsername("吴德鹏");setPassword("123456");}}
                )
        );
    }

}
