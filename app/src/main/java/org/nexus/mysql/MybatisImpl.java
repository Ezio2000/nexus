package org.nexus.mysql;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xieningjun
 */
@Service
public class MybatisImpl extends ServiceImpl<NexusUserMapper, NexusUser> implements MybatisService {

    public boolean newUsers(List<NexusUser> users) {
        return super.saveBatch(users);
    }

    public boolean upUsers(List<NexusUser> users) {
        String sqlStatement = mapperClass.getName() + StringPool.DOT + "updateById";
        return super.executeBatch(users, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            MapperMethod.ParamMap<NexusUser> param = new MapperMethod.ParamMap<>();
            param.put(com.baomidou.mybatisplus.core.toolkit.Constants.ENTITY, entity);
            sqlSession.update(sqlStatement, param);
        });
    }

}
