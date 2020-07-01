package com.weilai9.common.utils.wechat;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weilai9.dao.entity.Customerqinzuan;
import com.weilai9.dao.entity.Qzlog;
import com.weilai9.dao.mapper.CustomerqinzuanMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 新增亲钻
 * @author xjh
 */
public class QzUtil {
    /**
     *
     * @param
     * @param cid 用户ID
     * @param qzv 亲钻值
     * @param type 亲钻值类型  1 冻结亲钻， 2 正常亲钻
     * @param osn 订单ID
     * @param orderType 订单类型，1 活动 ，2 乐园 3 商品
     * @return Boolean
     */
    public static Boolean addQz(CustomerqinzuanMapper customerqinzuanMapper, Integer cid, BigDecimal qzv, Integer type, Integer osn, Integer orderType) {
        QueryWrapper<Customerqinzuan> wrapper = new QueryWrapper();
        wrapper.eq("customerid",cid);
        Customerqinzuan customerqinzuan = customerqinzuanMapper.selectOne(wrapper);
        if (customerqinzuan == null) {
            return false;
        }
        //冻结值
        if (type == 1){
            customerqinzuanMapper.addQZbyType1(cid,qzv);
            customerqinzuanMapper.calculationTeamQZBytpye1(cid,qzv);
        }
        //正常值
        if (type == 2){
            customerqinzuanMapper.addQZbyType2(cid,qzv);
            customerqinzuanMapper.calculationTeamQZBytpye2(cid,qzv);
        }
        //记录到log
        Qzlog qzlog = new Qzlog();
        qzlog.setCustomerid(cid)
                .setBeforepersoncheck(customerqinzuan.getPersoncheckvalue())
                .setSubdate(LocalDateTime.now())
                .setBeforepersonwait(customerqinzuan.getPersonwaitvalue())
                .setActionvalue(qzv)
                .setActiontype(type)
                .setOrdersn(osn)
                .setOrderType(orderType)
                .insert();
        return true;
    }

    /**
     *
     * @param cid 用户ID
     * @param qzv  亲钻数量
     * @param osn  订单号
     */
    public static void editQz(CustomerqinzuanMapper customerqinzuanMapper,Integer cid, BigDecimal qzv,  String osn){
            //todo 退单，扣除亲钻
    }
}