package com.weilai9.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.*;
import com.weilai9.common.utils.redis.RedisUtil;
import com.weilai9.dao.entity.CustomerRole;
import com.weilai9.dao.entity.MsgInfo;
import com.weilai9.dao.mapper.MsgInfoMapper;
import com.weilai9.dao.vo.admin.AddMsgVO;
import com.weilai9.service.base.CustomerRoleService;
import com.weilai9.service.base.MsgInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author china.fuyao@outlook.com
 * @date 2020-04-03 16:24
 */
@Service("msgInfoService")
public class MsgInfoServiceImpl extends ServiceImpl<MsgInfoMapper, MsgInfo> implements MsgInfoService {
    @Resource
    RedisUtil redisUtil;
    @Resource
    CustomerRoleService customerRoleService;

    /**
     * 我的消息列表
     *
     * @param customerId
     * @param current
     * @param size
     * @return
     */
    @Override
    public Result myMsgList(Long customerId, Integer current, Integer size) {
        IPage<MsgInfo> page = new Page<>(null == current ? 1 : current, null == size ? 10 : size);
        IPage<MsgInfo> iPage = this.page(page, new QueryWrapper<MsgInfo>()
                .in("receive_uid", customerId, MsgConstants.MsgType.MSG_SYSTEM.getCode())
                .eq("msg_status", EnableConstants.ENABLE)
                .orderByAsc("is_read"));
        List<MsgInfo> records = iPage.getRecords();
        for (MsgInfo msg : records) {
            //系统消息阅读状态
            if (msg.getMsgType() == MsgConstants.MsgType.MSG_SYSTEM.getCode()) {
                boolean key = redisUtil.existsKey(RedisKey.MSG_SYS_MID_UID + msg.getMsgId());
                if (key) {
                    //存在，消息未读
                    msg.setIsRead(MsgConstants.MSG_NOT_READ);
                } else {
                    //不存在，消息已读
                    msg.setIsRead(MsgConstants.MSG_IS_READ);
                }
            }
        }
        iPage.setRecords(records);
        return new Result(iPage);
    }

    /**
     * 消息已读
     *
     * @param msgId
     * @param customerId
     * @return
     */
    @Override
    public Result readMsg(Long msgId, Long customerId) {
        MsgInfo msgInfo = getById(msgId);
        if (null == msgInfo || msgInfo.getMsgStatus() == EnableConstants.DISABLE) {
            return Result.Error("消息不存在或已删除");
        }
        if (msgInfo.getMsgType() == MsgConstants.MsgType.MSG_SYSTEM.getCode()) {
            //系统消息
            if (redisUtil.existsKey(RedisKey.MSG_SYS_MID_UID + msgId + ":" + customerId)) {
                redisUtil.deleteKey(RedisKey.MSG_SYS_MID_UID + msgId + ":" + customerId);
                Integer readNum = msgInfo.getIsRead() + 1;
                update(new UpdateWrapper<MsgInfo>().eq("msg_id", msgId).set("is_read", readNum));
            }
        } else {
            update(new UpdateWrapper<MsgInfo>().eq("msg_id", msgId).set("read_time", new Date()).set("is_read", MsgConstants.MSG_IS_READ));
        }
        return Result.OK("操作成功");
    }

    /**
     * 系统消息列表
     *
     * @param current
     * @param size
     * @param key
     * @return
     */
    @Override
    public Result sysMsgList(Integer current, Integer size, String key) {
        IPage page = new Page(null == current ? 1 : current, null == size ? 10 : size);
        IPage<Map> customerIPage = baseMapper.getSysMsgList(page, MsgConstants.MsgType.MSG_SYSTEM.getCode(), key);
        return new Result(customerIPage);
    }

    /**
     * 发送消息
     *
     * @param addMsgVO
     * @param addUid
     * @return
     */
    @Override
    public Result publishSysMsg(AddMsgVO addMsgVO, Long addUid) {
        boolean publishMsg = publishMsg(MsgConstants.MsgType.MSG_SYSTEM.getCode(), addMsgVO.getMsgTitle(),
                addMsgVO.getMsgSubhead(), addMsgVO.getMsgContent(), 0L, addMsgVO.getReceiveRoleId(), addUid);
        if (publishMsg) {
            return Result.OK("操作成功");
        }
        return Result.Error("操作失败");
    }

    /**
     * 发送普通消息
     *
     * @param addMsgVO
     * @param addUid
     * @return
     */
    @Override
    public Result publishNormalMsg(AddMsgVO addMsgVO, Long receiveUid, Long addUid) {
        boolean publishMsg = publishMsg(MsgConstants.MsgType.MSG_NORMAL.getCode(), addMsgVO.getMsgTitle(),
                addMsgVO.getMsgSubhead(), addMsgVO.getMsgContent(), receiveUid, addMsgVO.getReceiveRoleId(), addUid);
        if (publishMsg) {
            return Result.OK("操作成功");
        }
        return Result.Error("操作失败");
    }

    /**
     * 消息发送公共方法
     *
     * @param msgType
     * @param msgTitle
     * @param msgSubhead
     * @param msgContent
     * @param receiveUid
     * @param receiveRoleId
     * @param addUid
     * @return
     */
    private boolean publishMsg(Integer msgType, String msgTitle, String msgSubhead, String msgContent, Long receiveUid,
                               Long receiveRoleId, Long addUid) {
        MsgInfo msg = new MsgInfo();
        msg.setMsgTitle(msgTitle);
        msg.setMsgSubhead(msgSubhead);
        msg.setMsgContent(msgContent);
        msg.setIsRead(MsgConstants.MSG_NOT_READ);
        msg.setMsgStatus(EnableConstants.ENABLE);
        msg.setAddTime(new Date());
        msg.setAddUid(addUid);
        msg.setMsgType(msgType);
        msg.setReceiveRoleId(receiveRoleId);
        msg.setReceiveUid(receiveUid);
        if (msgType == MsgConstants.MsgType.MSG_SYSTEM.getCode()) {
            //系统消息
            List<CustomerRole> customerRoleList = customerRoleService.list(new QueryWrapper<CustomerRole>().eq("role_id", receiveRoleId));
            if (null != customerRoleList && customerRoleList.size() > 0) {
                msg.setReceiveNum(customerRoleList.size());
            } else {
                //接收者为0
                msg.setReceiveNum(0);
            }
            boolean save = this.save(msg);
            if (save) {
                for (CustomerRole cr : customerRoleList) {
                    redisUtil.set(RedisKey.MSG_SYS_MID_UID + msg.getMsgId() + ":" + cr.getCustomerId(), "", TokenConstants.MSG_SYS_READ_EXPIRE);
                }
                return true;
            }
        } else {
            //非系统消息
            msg.setReceiveNum(1);
            boolean save = this.save(msg);
            if (save) {
                return true;
            }
        }
        return false;
    }
}
