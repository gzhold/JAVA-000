package com.xa.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@TableName("t_order_info")
public class OrderInfo extends Model<OrderInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private String order_id;

    /**
     * 唯一订单号
     */
    private String order_number;

    /**
     * 买家id
     */
    private String user_id;

    /**
     * 商品id
     */
    private String good_id;

    /**
     * 订单状态 0未支付 1已支付 2已发出 3已送达 4已评价
     */
    private Integer order_status;

    /**
     * 付款金额
     */
    private BigDecimal pay_amount;

    /**
     * 真实付款金额
     */
    private BigDecimal real_amount;

    /**
     * 缴费账号(卡号)
     */
    private String payaccno;

    /**
     * 快递号
     */
    private String track_no;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 付款时间
     */
    private LocalDateTime pay_time;

    /**
     * 创建时间
     */
    private LocalDateTime create_time;

    /**
     * 更新时间
     */
    private LocalDateTime update_time;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getGood_id() {
        return good_id;
    }

    public void setGood_id(String good_id) {
        this.good_id = good_id;
    }
    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }
    public BigDecimal getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(BigDecimal pay_amount) {
        this.pay_amount = pay_amount;
    }
    public BigDecimal getReal_amount() {
        return real_amount;
    }

    public void setReal_amount(BigDecimal real_amount) {
        this.real_amount = real_amount;
    }
    public String getPayaccno() {
        return payaccno;
    }

    public void setPayaccno(String payaccno) {
        this.payaccno = payaccno;
    }
    public String getTrack_no() {
        return track_no;
    }

    public void setTrack_no(String track_no) {
        this.track_no = track_no;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public LocalDateTime getPay_time() {
        return pay_time;
    }

    public void setPay_time(LocalDateTime pay_time) {
        this.pay_time = pay_time;
    }
    public LocalDateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }
    public LocalDateTime getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(LocalDateTime update_time) {
        this.update_time = update_time;
    }

    @Override
    protected Serializable pkVal() {
        return this.good_id;
    }

    @Override
    public String toString() {
        return "Order_info{" +
                    "order_id=" + order_id +
                    ", order_number=" + order_number +
                    ", user_id=" + user_id +
                    ", good_id=" + good_id +
                    ", order_status=" + order_status +
                    ", pay_amount=" + pay_amount +
                    ", real_amount=" + real_amount +
                    ", payaccno=" + payaccno +
                    ", track_no=" + track_no +
                    ", remark=" + remark +
                    ", pay_time=" + pay_time +
                    ", create_time=" + create_time +
                    ", update_time=" + update_time +
                    "}";
    }

}
