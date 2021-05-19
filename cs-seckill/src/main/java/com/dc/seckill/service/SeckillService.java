package com.dc.seckill.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.dc.commons.constant.ApiConstant;
import com.dc.commons.constant.RedisKeyConstant;
import com.dc.commons.exception.ParameterException;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.pojo.SeckillVouchers;
import com.dc.commons.model.pojo.VoucherOrders;
import com.dc.commons.model.vo.SignInDinerInfo;
import com.dc.commons.utils.AssertUtil;
import com.dc.commons.utils.ResultInfoUtil;
import com.dc.seckill.mapper.SeckillVouchersMapper;
import com.dc.seckill.mapper.VoucherOrdersMapper;
//import com.dc.seckill.model.RedisLock;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
import com.dc.seckill.model.RedisLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 秒殺業務邏輯層
 */
@Service
public class SeckillService {

    @Resource
    private SeckillVouchersMapper seckillVouchersMapper;
    @Resource
    private VoucherOrdersMapper voucherOrdersMapper;
    @Value("${service.name.cs-oauth-server}")
    private String oauthServerName;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private DefaultRedisScript defaultRedisScript;
    @Resource
    private RedisLock redisLock;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 搶購代金券
     *
     * @param voucherId   代金券 ID
     * @param accessToken 登錄token
     * @Para path 訪問路徑
     */
    // @Transactional(rollbackFor = Exception.class)
    public ResultInfo doSeckill(Integer voucherId, String accessToken, String path) {
        // 基本參數校驗
        AssertUtil.isTrue(voucherId == null || voucherId < 0, "請選擇需要搶購的代金券");
        AssertUtil.isNotEmpty(accessToken, "請登錄");

        // 註解原始走關係型資料庫流程
        // 判斷此代金券是否加入搶購
        // SeckillVouchers seckillVouchers = seckillVouchersMapper.selectVoucher(voucherId);
        // AssertUtil.isTrue(seckillVouchers == null, "該代金券並未有搶購活動");
        // 判斷是否有效
        //AssertUtil.isTrue(seckillVouchers.getIsValid() == 0, "該活動已結束");

        // 採用Redis
        String key = RedisKeyConstant.seckill_vouchers.getKey() + voucherId;
        Map<String, Object> map = redisTemplate.opsForHash().entries(key);
        SeckillVouchers seckillVouchers = BeanUtil.mapToBean(map, SeckillVouchers.class, true, null);


        // 判斷是否開始、結束
        Date now = new Date();
        AssertUtil.isTrue(now.before(seckillVouchers.getStartTime()), "該搶購還未開始");
        AssertUtil.isTrue(now.after(seckillVouchers.getEndTime()), "該搶購已結束");
        // 判斷是否賣完
        AssertUtil.isTrue(seckillVouchers.getAmount() < 1, "該券已經賣完了");
        // 獲取登錄用戶信息
        String url = oauthServerName + "user/me?access_token={accessToken}";
        ResultInfo resultInfo = restTemplate.getForObject(url, ResultInfo.class, accessToken);
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            resultInfo.setPath(path);
            return resultInfo;
        }
        // 這里的data是一個LinkedHashMap，SignInDinerInfo
        SignInDinerInfo dinerInfo = BeanUtil.fillBeanWithMap((LinkedHashMap) resultInfo.getData(),
                new SignInDinerInfo(), false);
        System.out.println("========== dinerInfo.getId() ========= " + dinerInfo.getId() + ", ========== seckillVouchers.getId() ======== " + seckillVouchers.getId());
        // 判斷登錄用戶是否已搶到(一個用戶針對這次活動只能買一次)
        VoucherOrders order = voucherOrdersMapper.findDinerOrder(dinerInfo.getId(),
                seckillVouchers.getFkVoucherId());

        AssertUtil.isTrue(order != null, "該使用者已搶到該代金券，無需再搶");

        // 註解原始走關係型資料庫流程
        // 扣庫存
        //int count = seckillVouchersMapper.stockDecrease(seckillVouchers.getId());
        //AssertUtil.isTrue(count == 0, "該券已經賣完了");

        //使用Redis鎖，一個帳號只能購買一次
        String lockName = RedisKeyConstant.lock_key.getKey()
                + dinerInfo.getId() + ":" + voucherId;
        long expireTime = seckillVouchers.getEndTime().getTime() - now.getTime();

        //使用自定義的Redis分布式鎖
        //String lockKey = redisLock.tryLock(lockName, expireTime);

        //Redisson 分布式鎖
        RLock lock = redissonClient.getLock(lockName);


        try {
            // 不為空就是拿到鎖了，執行下單
            // 自定義的Redis分布式鎖
            //if(StrUtil.isNotBlank(lockKey)) {

            // Redisson分布式鎖處理
            boolean isLocked = lock.tryLock(expireTime, TimeUnit.MILLISECONDS);
            if(isLocked) {
                // 下單
                VoucherOrders voucherOrders = new VoucherOrders();
                voucherOrders.setFkDinerId(dinerInfo.getId());

                //Redis中不需要維護外鍵資訊
                //voucherOrders.setFkSeckillId(seckillVouchers.getId());

                voucherOrders.setFkVoucherId(seckillVouchers.getFkVoucherId());
                String orderNo = IdUtil.getSnowflake(1, 1).nextIdStr();
                System.out.println("=============" + orderNo + "==============");
                voucherOrders.setOrderNo(orderNo);
                voucherOrders.setOrderType(1);
                voucherOrders.setStatus(0);
                long count = voucherOrdersMapper.save(voucherOrders);
                AssertUtil.isTrue(count == 0, "使用者搶購失敗");


                // 使用Redis扣庫存
                // 扣庫存
                // count = redisTemplate.opsForHash().increment(key, "amount", -1);
                // AssertUtil.isTrue(count < 0, "該券已經賣完了");

                // 採用Redis + Lua 解決問題
                // 扣庫存
                List<String> keys = new ArrayList<>();
                keys.add(key);
                keys.add("amount");
                Long amount = (Long)redisTemplate.execute(defaultRedisScript, keys);
                AssertUtil.isTrue(amount == null || amount < 1, "該券已經賣完了");
            }
        } catch (Exception e) {
            // 手動回滾事務
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // 解鎖
            // 自定義分布式解鎖
            // redisLock.unlock(lockName, lockKey);

            // Redisson解鎖
            lock.unlock();

            if( e instanceof ParameterException ) {
                return ResultInfoUtil.buildError(0, "該券已經賣完了", path);
            }
       }

        return ResultInfoUtil.buildSuccess(path, "搶購成功");
    }


    /**
     * 新增需要搶購的代金券
     *
     * @param seckillVouchers
     */
    @Transactional(rollbackFor = Exception.class)
    public void addSeckillVouchers(SeckillVouchers seckillVouchers) {
        // 非空校驗
        AssertUtil.isTrue(seckillVouchers.getFkVoucherId() == null, "請選擇需要搶購的代金券");
        AssertUtil.isTrue(seckillVouchers.getAmount() == 0, "請輸入搶購總數量");
        Date now = new Date();
        AssertUtil.isNotNull(seckillVouchers.getStartTime(), "請輸入開始時間");
        // 生產環境下面一行代碼需放行，這里注釋方便測試
        // AssertUtil.isTrue(now.after(seckillVouchers.getStartTime()), "開始時間不能早於當前時間");
        AssertUtil.isNotNull(seckillVouchers.getEndTime(), "請輸入結束時間");
        AssertUtil.isTrue(now.after(seckillVouchers.getEndTime()), "結束時間不能早於當前時間");
        AssertUtil.isTrue(seckillVouchers.getStartTime().after(seckillVouchers.getEndTime()), "開始時間不能晚於結束時間");

        // 註解原走關係型資料庫的流程
        // 驗證資料庫中是否已經存在該券的秒殺活動
        // SeckillVouchers seckillVouchersFromDb = seckillVouchersMapper.selectVoucher(seckillVouchers.getFkVoucherId());
        // AssertUtil.isTrue(seckillVouchersFromDb != null, "該券已經擁有了搶購活動");
        // 插入數據庫
        // seckillVouchersMapper.save(seckillVouchers);

        // 採用Redis實現
        String key = RedisKeyConstant.seckill_vouchers.getKey() + seckillVouchers.getFkVoucherId();
        // 驗證Redis中是否已經存在該券的秒殺活動
        Map<String, Object> map = redisTemplate.opsForHash().entries(key);
        AssertUtil.isTrue(!map.isEmpty() && (int)map.get("amount") > 0, "該劵已存在搶購活動中");

        // 插入Redis
        seckillVouchers.setIsValid(1);
        seckillVouchers.setCreateDate(now);
        seckillVouchers.setUpdateDate(now);
        redisTemplate.opsForHash().putAll(key, BeanUtil.beanToMap(seckillVouchers));
    }

}
