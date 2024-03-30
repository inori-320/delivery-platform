package com.lty.service.impl;

import com.lty.constant.MessageConstant;
import com.lty.constant.PasswordConstant;
import com.lty.constant.StatusConstant;
import com.lty.context.BaseContext;
import com.lty.dto.EmployeeDTO;
import com.lty.dto.EmployeeLoginDTO;
import com.lty.entity.Employee;
import com.lty.exception.AccountLockedException;
import com.lty.exception.AccountNotFoundException;
import com.lty.exception.PasswordErrorException;
import com.lty.mapper.EmployeeMapper;
import com.lty.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 登录类
     * @return 员工类
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);
        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //密码比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        // 将DTO对象拷贝到实体对象中
        BeanUtils.copyProperties(employeeDTO, employee);
        // 设置账号状态为可用
        employee.setStatus(StatusConstant.ENABLE);
        // 设置默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        // 设置当前记录时间的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        // 记录该员工的创建人与修改人
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }

}