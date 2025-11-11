package com.vegetable.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vegetable.common.Result;
import com.vegetable.entity.Refund;
import com.vegetable.service.RefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;

/**
 * 退款/售后控制器
 *
 * @author vegetable-system
 * @since 2025-11-11
 */
@Tag(name = "退款/售后管理", description = "退款申请、审核等接口")
@RestController
@RequestMapping("/refund")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    /**
     * 申请退款
     */
    @Operation(summary = "申请退款")
    @PostMapping("/apply")
    public Result<Refund> applyRefund(@RequestParam @NotBlank(message = "订单号不能为空") String orderId,
                                      @RequestParam @NotBlank(message = "退款类型不能为空") String refundType,
                                      @RequestParam @NotBlank(message = "退款原因不能为空") String refundReason,
                                      @RequestParam(required = false) String refundImages) {
        Refund refund = refundService.applyRefund(orderId, refundType, refundReason, refundImages);
        return Result.success("退款申请已提交", refund);
    }

    /**
     * 商家审核-同意退款
     */
    @Operation(summary = "同意退款")
    @PostMapping("/approve/{refundId}")
    @SaCheckRole("merchant")
    public Result<Void> approveRefund(@PathVariable String refundId,
                                       @RequestParam(required = false) String merchantReply) {
        refundService.approveRefund(refundId, merchantReply);
        return Result.success("已同意退款");
    }

    /**
     * 商家审核-拒绝退款
     */
    @Operation(summary = "拒绝退款")
    @PostMapping("/reject/{refundId}")
    @SaCheckRole("merchant")
    public Result<Void> rejectRefund(@PathVariable String refundId,
                                      @RequestParam @NotBlank(message = "拒绝原因不能为空") String rejectReason) {
        refundService.rejectRefund(refundId, rejectReason);
        return Result.success("已拒绝退款");
    }

    /**
     * 我的退款列表
     */
    @Operation(summary = "我的退款列表")
    @GetMapping("/my")
    public Result<IPage<Refund>> getMyRefunds(@RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               @RequestParam(required = false) String status) {
        Page<Refund> pageParam = new Page<>(page, size);
        IPage<Refund> result = refundService.getMyRefunds(pageParam, status);
        return Result.success(result);
    }

    /**
     * 商家待处理退款列表
     */
    @Operation(summary = "待处理退款列表")
    @GetMapping("/pending")
    @SaCheckRole("merchant")
    public Result<IPage<Refund>> getPendingRefunds(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        Page<Refund> pageParam = new Page<>(page, size);
        IPage<Refund> result = refundService.getPendingRefunds(pageParam);
        return Result.success(result);
    }

    /**
     * 获取退款详情
     */
    @Operation(summary = "获取退款详情")
    @GetMapping("/{refundId}")
    public Result<Refund> getRefundDetail(@PathVariable String refundId) {
        Refund refund = refundService.getById(refundId);
        if (refund == null) {
            return Result.notFound("退款单不存在");
        }
        return Result.success(refund);
    }
}
