package org.nexus.valid;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Xieningjun
 * @date 2024/7/3 13:46
 */
public class ValidReq {

    @NotEmpty(message = "Code can't be empty.")
    public String code;

    @Size(min = 6, message = "Password must be at least 6 characters.")
    public String password;

}
