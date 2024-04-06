package com.lty.controller.user;

import com.lty.dto.OrdersDTO;
import com.lty.dto.OrdersSubmitDTO;
import com.lty.result.Result;
import com.lty.service.OrderService;
import com.lty.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lty
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "订单相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO submitDTO){
        OrderSubmitVO vo = orderService.submitOrder(submitDTO);
        return Result.success(vo);
    }
}
