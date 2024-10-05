package org.nexus.virtual;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xieningjun
 * @date 2024/9/24 15:55
 * @description
 */
@RestController
@RequestMapping("/virtual")
public class VirtualController {

    public int doVirtual() {
        return 1;
    }

}
