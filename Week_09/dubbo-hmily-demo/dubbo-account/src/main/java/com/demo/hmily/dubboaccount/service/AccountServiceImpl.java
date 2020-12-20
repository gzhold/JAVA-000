package com.demo.hmily.dubboaccount.service;

import com.demo.hmily.dubboaccount.mapper.AccountMapper;
import com.demo.common.dubboapi.service.AccountService;
import com.demo.common.dubboapi.service.FreezeAssetService;
import com.demo.common.dubboapi.vo.AccountDTO;
import com.demo.common.dubboapi.vo.FreezeAssetDTO;
import org.springframework.stereotype.Service;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@DubboService(version = "1.0.0")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper mapper;

    @DubboReference(version = "1.0.0", url = "dubbo://127.0.0.1:12345")
    private FreezeAssetService freezeAssetService;

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean exchange(AccountDTO accountDTO) {
        AccountDTO newAccountDTO = trans(accountDTO);

        FreezeAssetDTO freezeAssetDTO = new FreezeAssetDTO();
        freezeAssetDTO.setAccountId(1L);

        if (newAccountDTO != null) {
            freezeAssetDTO.setAmount(newAccountDTO.getAmount());
            freezeAssetDTO.setUserId(newAccountDTO.getUserId());
            //todo
            freezeAssetService.updateTempConfirm(freezeAssetDTO);
            mapper.update(newAccountDTO);
            freezeAssetService.updateTempRollback(freezeAssetDTO);

        }
        return false;
    }


    private AccountDTO trans(AccountDTO accountDTO) {
        AccountDTO dto = new AccountDTO();
        switch (accountDTO.getUnit()) {
            case "RMB":
                return accountDTO;
            case "DOLLAR":
                BigDecimal amount = accountDTO.getAmount();
                amount = amount.multiply(new BigDecimal(7));
                dto.setUserId(accountDTO.getUserId());
                dto.setAmount(amount);
                return dto;
            default:
                System.out.println("unit input error!");
                return null;
        }
    }


    /**
     * Confirm boolean.
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(AccountDTO accountDTO) {
        AccountDTO newAccountDTO = trans(accountDTO);
        if (newAccountDTO != null) {
            mapper.confirm(newAccountDTO);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Cancel boolean.
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(AccountDTO accountDTO) {
        AccountDTO newAccountDTO = trans(accountDTO);
        if (newAccountDTO != null) {
            mapper.cancel(newAccountDTO);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
