package com.weilai9.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.Customerrelation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  Mapper 接口
 * </p>
 * @author xjh
 * @since 2020-05-20
 */
@Transactional(rollbackFor = Exception.class)
public interface CustomerrelationMapper extends BaseMapper<Customerrelation> {
    /**
     *  用户绑定邀请码
     * @param customerId
     * @param invitationCustomerId
     */
    void bindCustomerSn(Integer customerId, Integer invitationCustomerId);

    /**
     * 更新下级团队积分到自己帐上
     */
    void updateIntegralByTeam();
    /**
     * 更新自己的积分到账上
     */
    void updateIntegralByMyself();

}
