package com.dc.seckill.mapper;

import com.dc.commons.model.pojo.VoucherOrders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 代金券訂單 Mapper
 */
public interface VoucherOrdersMapper {

    // 根據食客 ID 和秒殺 ID 查詢代金券訂單
    @Select("select id, order_no, fk_voucher_id, fk_diner_id, qrcode, payment," +
            " status, fk_seckill_id, order_type, create_date, update_date, " +
            " is_valid from t_voucher_orders where fk_diner_id = #{dinerId} " +
            " and fk_voucher_id = #{voucherId} and is_valid = 1 and status between 0 and 1 ")
    VoucherOrders findDinerOrder(@Param("dinerId") Integer dinerId,
                                 @Param("voucherId") Integer voucherId);

    // 新增代金券訂單
    @Insert("insert into t_voucher_orders (order_no, fk_voucher_id, fk_diner_id, " +
            " status, fk_seckill_id, order_type, create_date, update_date,  is_valid)" +
            " values (#{orderNo}, #{fkVoucherId}, #{fkDinerId}, #{status}, #{fkSeckillId}, " +
            " #{orderType}, now(), now(), 1)")
    int save(VoucherOrders voucherOrders);

}