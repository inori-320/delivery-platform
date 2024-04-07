package com.lty.controller.admin;

import com.lty.dto.*;
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
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrdersConfirmDTO confirmDTO){
        orderService.confirm(confirmDTO);
        return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO rejectionDTO) throws Exception {
        orderService.reject(rejectionDTO);
        return Result.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }
}
