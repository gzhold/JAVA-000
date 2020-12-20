package com.demo.common.dubboapi.service;

import com.demo.common.dubboapi.vo.FreezeAssetDTO;
import org.dromara.hmily.annotation.Hmily;

public interface FreezeAssetService {

    @Hmily
    boolean updateTempConfirm(FreezeAssetDTO freezeAssetDTO);

    @Hmily
    boolean updateTempRollback(FreezeAssetDTO freezeAssetDTO);
}
