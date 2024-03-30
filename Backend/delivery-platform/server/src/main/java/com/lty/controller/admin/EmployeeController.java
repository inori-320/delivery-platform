package com.lty.controller.admin;

import com.lty.constant.JwtClaimsConstant;
import com.lty.dto.EmployeeDTO;
import com.lty.dto.EmployeeLoginDTO;
import com.lty.dto.EmployeePageQueryDTO;
import com.lty.entity.Employee;
import com.lty.properties.JwtProperties;
import com.lty.result.PageResult;
import com.lty.result.Result;
import com.lty.service.EmployeeService;
import com.lty.utils.JwtUtil;
import com.lty.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("退出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @ApiOperation("新增员工")
    @PostMapping()
    public Result<String> save(@RequestBody EmployeeDTO employee){
        log.info("新增员工：{}", employee);
        employeeService.save(employee);
        return Result.success();
    }

    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数为：{}", employeePageQueryDTO);
        PageResult pageQuery = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageQuery);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result startOrStop(@PathVariable("status") Integer status, Long id){
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    @ApiOperation("查询员工信息")
    @GetMapping("/{id}")
    public Result selectById(@PathVariable("id") Long id){
        Employee employee = employeeService.selectById(id);
        return  Result.success(employee);
    }

    @ApiOperation("修改员工信息")
    @PutMapping
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }
}
