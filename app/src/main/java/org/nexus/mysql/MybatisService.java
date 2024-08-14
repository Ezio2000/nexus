package org.nexus.mysql;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Xieningjun
 */
public interface MybatisService extends IService<NexusUser> {

    boolean newUsers(List<NexusUser> users);

    boolean upUsers(List<NexusUser> users);

}
