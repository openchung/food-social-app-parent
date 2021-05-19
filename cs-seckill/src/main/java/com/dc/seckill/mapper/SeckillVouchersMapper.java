package com.dc.seckill.mapper;

import com.dc.commons.model.pojo.SeckillVouchers;
import org.apache.ibatis.annotations.*;

/**
 * 秒殺代金券 Mapper
 */
public interface SeckillVouchersMapper {

    // 新增秒殺活動
    @Insert("insert into t_seckill_vouchers (fk_voucher_id, amount, start_time, end_time, is_valid, create_date, update_date) " +
            " values (#{fkVoucherId}, #{amount}, #{startTime}, #{endTime}, 1, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(SeckillVouchers seckillVouchers);

    // 根據代金券 ID 查詢該代金券是否參與搶購活動
    @Select("select id, fk_voucher_id, amount, start_time, end_time, is_valid " +
            " from t_seckill_vouchers where fk_voucher_id = #{voucherId}")
    SeckillVouchers selectVoucher(Integer voucherId);

    // 減庫存
    @Update("update t_seckill_vouchers set amount = amount - 1 " +
            " where id = #{seckillId}")
    int stockDecrease(@Param("seckillId") int seckillId);

}