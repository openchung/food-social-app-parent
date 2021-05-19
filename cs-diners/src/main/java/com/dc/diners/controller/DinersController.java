package com.dc.diners.controller;

import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.dto.DinersDTO;
import com.dc.commons.model.vo.ShortDinerInfo;
import com.dc.commons.utils.ResultInfoUtil;
import com.dc.diners.service.DinersService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(tags = "食客相關接口")
public class DinersController {
    @Resource
    private DinersService dinersService;

    @Resource
    private HttpServletRequest request;

    /**
     * 根據 ids 查詢食客信息
     *
     * @param ids
     * @return
     */
    @GetMapping("findByIds")
    public ResultInfo<List<ShortDinerInfo>> findByIds(String ids) {
        List<ShortDinerInfo> dinerInfos = dinersService.findByIds(ids);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), dinerInfos);
    }

    /**
     *
     * @param dinersDTO
     * @return
     */
    @PostMapping("register")
    public ResultInfo register(@RequestBody DinersDTO dinersDTO) {
        return dinersService.register(dinersDTO, request.getServletPath());
    }

    /**
     * 驗證手機號是否已註冊
     * @param phone
     * @return
     */
    @GetMapping("checkPhone")
    public  ResultInfo checkPhone(String phone) {
        dinersService.checkPhoneIsRegistered(phone);
        return ResultInfoUtil.buildSuccess(request.getServletPath());
    }

    /**
     * 登入
     *
     * @param account
     * @param password
     * @return
     */
    @GetMapping("signin")
    public ResultInfo signIn(String account, String password) {
        return  dinersService.signIn(account, password, request.getServletPath());
    }
}
