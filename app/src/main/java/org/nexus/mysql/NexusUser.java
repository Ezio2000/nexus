package org.nexus.mysql;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Xieningjun
 * @date 2024/7/15 17:10
 */
@Data
@TableName("nexus_user")
public class NexusUser {

    @TableId
    private int id;

    private String username;

    private String password;

}
