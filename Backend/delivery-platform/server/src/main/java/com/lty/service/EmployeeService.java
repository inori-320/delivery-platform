package com.lty.service;

import com.lty.dto.EmployeeDTO;
import com.lty.dto.EmployeeLoginDTO;
import com.lty.dto.EmployeePageQueryDTO;
import com.lty.entity.Employee;
import com.lty.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employee);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(Integer status, Long id);

    Employee selectById(Long id);

    void updateEmployee(EmployeeDTO employeeDTO);
}
