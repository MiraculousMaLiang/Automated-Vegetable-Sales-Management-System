package com.vegetable.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vegetable.common.exception.BusinessException;
import com.vegetable.entity.Address;
import com.vegetable.mapper.AddressMapper;
import com.vegetable.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 收货地址服务实现类
 *
 * @author vegetable-system
 * @since 2025-11-14
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    private final AddressMapper addressMapper;

    @Override
    public List<Address> getMyAddresses() {
        int userId = StpUtil.getLoginIdAsInt();
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getCreateTime);
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address addAddress(Address address) {
        int userId = StpUtil.getLoginIdAsInt();
        address.setUserId(userId);

        // 如果设置为默认地址，先将其他地址设为非默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        } else {
            // 检查是否是第一个地址，如果是则自动设为默认
            LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Address::getUserId, userId);
            long count = count(wrapper);
            if (count == 0) {
                address.setIsDefault(1);
            } else {
                address.setIsDefault(0);
            }
        }

        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        save(address);
        return address;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAddress(Address address) {
        int userId = StpUtil.getLoginIdAsInt();

        // 验证地址是否属于当前用户
        Address existAddress = getById(address.getAddressId());
        if (existAddress == null) {
            throw new BusinessException(404, "地址不存在");
        }
        if (!existAddress.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此地址");
        }

        // 如果设置为默认地址，先将其他地址设为非默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }

        address.setUserId(userId);
        address.setUpdateTime(LocalDateTime.now());
        return updateById(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAddress(Integer addressId) {
        int userId = StpUtil.getLoginIdAsInt();

        // 验证地址是否属于当前用户
        Address address = getById(addressId);
        if (address == null) {
            throw new BusinessException(404, "地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此地址");
        }

        boolean isDefault = address.getIsDefault() == 1;
        boolean result = removeById(addressId);

        // 如果删除的是默认地址，将第一个地址设为默认
        if (result && isDefault) {
            LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Address::getUserId, userId)
                    .orderByDesc(Address::getCreateTime)
                    .last("LIMIT 1");
            Address firstAddress = getOne(wrapper);
            if (firstAddress != null) {
                firstAddress.setIsDefault(1);
                updateById(firstAddress);
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultAddress(Integer addressId) {
        int userId = StpUtil.getLoginIdAsInt();

        // 验证地址是否属于当前用户
        Address address = getById(addressId);
        if (address == null) {
            throw new BusinessException(404, "地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此地址");
        }

        // 先将所有地址设为非默认
        clearDefaultAddress(userId);

        // 将指定地址设为默认
        address.setIsDefault(1);
        address.setUpdateTime(LocalDateTime.now());
        return updateById(address);
    }

    @Override
    public Address getDefaultAddress() {
        int userId = StpUtil.getLoginIdAsInt();
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .eq(Address::getIsDefault, 1)
                .last("LIMIT 1");
        return getOne(wrapper);
    }

    /**
     * 清除用户的所有默认地址
     *
     * @param userId 用户ID
     */
    private void clearDefaultAddress(int userId) {
        LambdaUpdateWrapper<Address> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Address::getUserId, userId)
                .set(Address::getIsDefault, 0);
        update(updateWrapper);
    }
}
