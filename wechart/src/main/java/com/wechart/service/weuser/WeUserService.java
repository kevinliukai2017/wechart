package com.wechart.service.weuser;

import com.wechart.model.weuser.WeUserModel;

public interface WeUserService {

    /**
     * 添加微信用户
     * @return
     */
    int addWeUser(WeUserModel weUserModel);
    
    /**
     * 通过openid查询微信用户
     * @param openid
     * @return
     */
    WeUserModel getWeUserByOpenid(String openid);
    
    /**
     * 修改微信用户信息
     * @param weUserModel
     * @return
     */
    int updateWeUser(WeUserModel weUserModel);
}
