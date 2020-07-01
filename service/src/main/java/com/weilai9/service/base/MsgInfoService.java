package com.weilai9.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.MsgInfo;
import com.weilai9.dao.vo.admin.AddMsgVO;

/**
 * api消息服务类
 *
 * @author china.fuyao@outlook.com
 * @date 2020-04-03 16:27:51
 */
public interface MsgInfoService extends IService<MsgInfo> {
    /**
     * 我的消息列表
     *
     * @param customerId
     * @param current
     * @param size
     * @return
     */
    Result myMsgList(Long customerId, Integer current, Integer size);

    /**
     * 已读消息
     *
     * @param msgId
     * @param customerId
     * @return
     */
    Result readMsg(Long msgId, Long customerId);

    /**
     * 系统消息列表
     *
     * @param current
     * @param size
     * @param key
     * @return
     */
    Result sysMsgList(Integer current, Integer size, String key);

    /**
     * 发送消息
     *
     * @param addMsgVO
     * @param addUid
     * @return
     */
    Result publishSysMsg(AddMsgVO addMsgVO,Long addUid);


    /**
     * 发送普通消息
     *
     * @param addMsgVO
     * @param receiveUid
     * @param addUid
     * @return
     */
    Result publishNormalMsg(AddMsgVO addMsgVO,Long receiveUid,Long addUid);
}
