package com.wechart.model.weuser;

import java.sql.Timestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="weuser")
public class WeUserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String openid;
    private int lotterycount;
    private Timestamp lotterysharetime;
    private Timestamp createtime;
    private Timestamp modifytime;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public int getLotterycount() {
        return lotterycount;
    }
    public void setLotterycount(int lotterycount) {
        this.lotterycount = lotterycount;
    }
    public Timestamp getCreatetime() {
        return createtime;
    }
    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }
    public Timestamp getModifytime() {
        return modifytime;
    }
    public void setModifytime(Timestamp modifytime) {
        this.modifytime = modifytime;
    }
    public Timestamp getLotterysharetime() {
        return lotterysharetime;
    }
    public void setLotterysharetime(Timestamp lotterysharetime) {
        this.lotterysharetime = lotterysharetime;
    }
    
    
    
}
