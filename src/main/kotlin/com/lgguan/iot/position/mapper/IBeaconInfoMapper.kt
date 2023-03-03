package com.lgguan.iot.position.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.lgguan.iot.position.entity.BeaconInfo

/**
 *
 *
 * @author N.Liu
 **/
interface IBeaconInfoMapper: BaseMapper<BeaconInfo> {
    fun getBoundNameByDeviceId(deviceId: String): String?
}
