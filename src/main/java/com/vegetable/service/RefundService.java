package com.vegetable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vegetable.entity.Refund;

/**
 * 退款/售后服务接口
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
public interface RefundService extends IService<Refund> {

    /**
     * 申请退款
     *
     * @param orderId      订单ID
     * @param refundType   退款类型(REFUND_ONLY/RETURN_REFUND)
     * @param refundReason 退款原因
     * @param refundImages 凭证图片
     * @return 退款单
     */
    Refund applyRefund(String orderId, String refundType, String refundReason, String refundImages);

    /**
     * 商家审核退款(同意)
     *
     * @param refundId     退款单号
     * @param merchantReply 商家回复
     * @return 是否成功
     */
    boolean approveRefund(String refundId, String merchantReply);

    /**
     * 商家审核退款(拒绝)
     *
     * @param refundId     退款单号
     * @param rejectReason 拒绝原因
     * @return 是否成功
     */
    boolean rejectRefund(String refundId, String rejectReason);

    /**
     * 完成退款(模拟退款到账)
     *
     * @param refundId 退款单号
     * @return 是否成功
     */
    boolean completeRefund(String refundId);

    /**
     * 获取我的退款列表
     *
     * @param page   分页对象
     * @param status 状态(可选)
     * @return 分页数据
     */
    IPage<Refund> getMyRefunds(Page<Refund> page, String status);

    /**
     * 获取商家待处理退款列表
     *
     * @param page 分页对象
     * @return 分页数据
     */
    IPage<Refund> getPendingRefunds(Page<Refund> page);
}
