package org.nexus.valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xieningjun
 * @date 2024/7/3 13:44
 */
@RestController
public class ValidController {

    @Autowired
    private ValidComponent validComponent;

    @PostMapping("/valid")
    public void valid(@RequestBody ValidReq validReq) {
        validComponent.valid(validReq);
    }

}
