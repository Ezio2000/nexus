package org.nexus.valid;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * @author Xieningjun
 * @date 2024/7/3 13:45
 */
@Component
@Validated
public class ValidComponent {

    public void valid(@Valid ValidReq req) {
        System.out.println("Code is : " + req.code);
        System.out.println("Password is : " + req.password);
    }

}
