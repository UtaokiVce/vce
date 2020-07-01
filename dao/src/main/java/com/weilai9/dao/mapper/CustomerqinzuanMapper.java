package com.weilai9.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.Customerqinzuan;
import com.weilai9.dao.vo.wechat.QzMonthVo;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xjh
 * @since 2020-06-03
 */
@Transactional(rollbackFor = Exception.class)
public interface CustomerqinzuanMapper extends BaseMapper<Customerqinzuan> {

    List<Customerqinzuan> teamList(String cid, int pageno, Integer pagesize);

    QzMonthVo pIncome(String cid);

    QzMonthVo tIncome(String cid);

    /**
     * 计算所有用户收益
     */
    void allProfit();

    /**
     * 新增冻结亲钻
     * @param cid
     * @param qzv
     */
    void addQZbyType1(Integer cid, BigDecimal qzv);

    /**
     * 新增灵活亲钻
     * @param cid
     * @param qzv
     */
    void addQZbyType2(Integer cid, BigDecimal qzv);

    /**
     * 计算团队亲钻
     * @param cid
     * @param qzv
     */
    void calculationTeamQZ(Integer cid, BigDecimal qzv);

    /**
     * 计算团队收益，变更灵活亲钻
     * @param cid
     * @param qzv
     */
    void calculationTeamQZBytpye2(Integer cid, BigDecimal qzv);
    /**
     * 计算团队收益，变更冻结亲钻
     * @param cid
     * @param qzv
     */
    void calculationTeamQZBytpye1(Integer cid, BigDecimal qzv);
    /**
     * 计算团队收益，冻结亲钻转换灵活亲钻
     * @param cid
     * @param qzv
     */
    void calculationTeamQZBytpye3(Integer cid, BigDecimal qzv);


}
