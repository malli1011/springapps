package com.azure.springapps.controller;

import com.azure.springapps.exceptions.GlobalExceptionHandler;
import com.azure.springapps.exceptions.ResourceNotFoundException;
import com.azure.springapps.repository.EmployeeRepository;
import com.azure.springapps.repository.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Validated
public class EmployeeController {
    private final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public List <Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity < Employee > getEmployeeById(@PathVariable(value = "id") Long employeeId)
    throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        log.debug(employee.toString());
        return employeeRepository.save(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity < Employee > updateEmployee(@PathVariable(value = "id") Long employeeId,
        @Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public Map < String, Boolean > deleteEmployee(@PathVariable(value = "id") Long employeeId)
    throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employeeRepository.delete(employee);
        Map < String, Boolean > response = new HashMap < > ();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}