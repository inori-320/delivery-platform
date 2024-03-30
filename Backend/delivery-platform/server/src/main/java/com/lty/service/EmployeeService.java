package com.lty.service;

import com.lty.dto.EmployeeDTO;
import com.lty.dto.EmployeeLoginDTO;
import com.lty.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employee);
}
