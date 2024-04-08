package com.lty.controller.user;

import com.lty.dto.OrdersDTO;
import com.lty.dto.OrdersPaymentDTO;
import com.lty.dto.OrdersSubmitDTO;
import com.lty.result.PageResult;
import com.lty.result.Result;
import com.lty.service.OrderService;
import com.lty.vo.OrderPaymentVO;
import com.lty.vo.OrderSubmitVO;
import com.lty.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lty
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO submitDTO){
        OrderSubmitVO vo = orderService.submitOrder(submitDTO);
        return Result.success(vo);
    }

    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> historyOrders(int page, int pageSize, Integer status){
        PageResult pageResult = orderService.pageQuery(page, pageSize, status);
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> orderDetail(@PathVariable("id") Long id){
        OrderVO orderVO = orderService.showDetail(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancelOrder(@PathVariable("id") Long id) throws Exception {
        orderService.cancelOrder(id);
        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result reOrder(@PathVariable("id") Long id){
        orderService.reOrder(id);
        return Result.success();
    }

    @GetMapping("/reminder/{id}")
    @ApiOperation("催单功能")
    public Result remindOrder(@PathVariable("id") Long id){
        orderService.remindOrder(id);
        return Result.success();
    }
}
