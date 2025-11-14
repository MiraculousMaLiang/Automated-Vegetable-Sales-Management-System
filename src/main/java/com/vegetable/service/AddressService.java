package com.vegetable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vegetable.entity.Address;

import java.util.List;

/**
 * 收货地址服务接口
 *
 * @author vegetable-system
 * @since 2025-11-14
 */
public interface AddressService extends IService<Address> {

    /**
     * 获取当前用户的地址列表
     *
     * @return 地址列表
     */
    List<Address> getMyAddresses();

    /**
     * 添加收货地址
     *
     * @param address 地址信息
     * @return 添加后的地址
     */
    Address addAddress(Address address);

    /**
     * 更新收货地址
     *
     * @param address 地址信息
     * @return 是否成功
     */
    boolean updateAddress(Address address);

    /**
     * 删除收货地址
     *
     * @param addressId 地址ID
     * @return 是否成功
     */
    boolean deleteAddress(Integer addressId);

    /**
     * 设置默认地址
     *
     * @param addressId 地址ID
     * @return 是否成功
     */
    boolean setDefaultAddress(Integer addressId);

    /**
     * 获取默认地址
     *
     * @return 默认地址，没有则返回null
     */
    Address getDefaultAddress();
}
