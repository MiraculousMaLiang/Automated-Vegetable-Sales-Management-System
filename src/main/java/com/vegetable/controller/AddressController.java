package com.vegetable.controller;

import com.vegetable.common.Result;
import com.vegetable.entity.Address;
import com.vegetable.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址控制器
 *
 * @author vegetable-system
 * @since 2025-11-14
 */
@Tag(name = "收货地址管理", description = "收货地址增删改查接口")
@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * 获取我的地址列表
     */
    @Operation(summary = "获取我的地址列表")
    @GetMapping("/my")
    public Result<List<Address>> getMyAddresses() {
        List<Address> addresses = addressService.getMyAddresses();
        return Result.success(addresses);
    }

    /**
     * 获取默认地址
     */
    @Operation(summary = "获取默认地址")
    @GetMapping("/default")
    public Result<Address> getDefaultAddress() {
        Address address = addressService.getDefaultAddress();
        return Result.success(address);
    }

    /**
     * 获取地址详情
     */
    @Operation(summary = "获取地址详情")
    @GetMapping("/{addressId}")
    public Result<Address> getAddressById(@PathVariable Integer addressId) {
        Address address = addressService.getById(addressId);
        if (address == null) {
            return Result.error("地址不存在");
        }
        return Result.success(address);
    }

    /**
     * 添加收货地址
     */
    @Operation(summary = "添加收货地址")
    @PostMapping
    public Result<Address> addAddress(@RequestBody Address address) {
        Address result = addressService.addAddress(address);
        return Result.success(result);
    }

    /**
     * 更新收货地址
     */
    @Operation(summary = "更新收货地址")
    @PutMapping("/{addressId}")
    public Result<Void> updateAddress(@PathVariable Integer addressId,
                                       @RequestBody Address address) {
        address.setAddressId(addressId);
        addressService.updateAddress(address);
        return Result.success();
    }

    /**
     * 删除收货地址
     */
    @Operation(summary = "删除收货地址")
    @DeleteMapping("/{addressId}")
    public Result<Void> deleteAddress(@PathVariable Integer addressId) {
        addressService.deleteAddress(addressId);
        return Result.success();
    }

    /**
     * 设置默认地址
     */
    @Operation(summary = "设置默认地址")
    @PutMapping("/{addressId}/default")
    public Result<Void> setDefaultAddress(@PathVariable Integer addressId) {
        addressService.setDefaultAddress(addressId);
        return Result.success();
    }
}
