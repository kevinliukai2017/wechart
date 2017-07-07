package com.wechart.service.weuser.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wechart.dao.weuser.WeUserMapper;
import com.wechart.model.weuser.WeUserModel;
import com.wechart.service.weuser.WeUserService;

import br.com.objectos.core.lang.Preconditions;
import tk.mybatis.mapper.entity.Example;

@Service
public class WeUserServiceImpl implements WeUserService {

    @Autowired
    private WeUserMapper weUserMapper;

    @Override
    public int addWeUser(WeUserModel record) {
        // TODO Auto-generated method stub
        Preconditions.checkNotNull(record, "插入weuser微信对象为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(record.getOpenid()), "微信号不能为空");
        return weUserMapper.insertSelective(record);
    }

    @Override
    public WeUserModel getWeUserByOpenid(String openid) {
        // TODO Auto-generated method stub
        Preconditions.checkArgument(StringUtils.isNotEmpty(openid), "openid不能为空");
        Example example = new Example(WeUserModel.class);
        example.createCriteria().andEqualTo("openid", openid);
        List<WeUserModel> results = weUserMapper.selectByExample(example);
        return CollectionUtils.isEmpty(results) ? null : results.get(0);
    }

    @Override
    public int updateWeUser(WeUserModel record) {
        // TODO Auto-generated method stub
        Preconditions.checkNotNull(record, "更新weuser微信对象为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(record.getOpenid()), "openid不能为空");
        Example example = new Example(WeUserModel.class);
        example.createCriteria().andEqualTo("openid", record.getOpenid());
        return weUserMapper.updateByExampleSelective(record, example);
    }

}
