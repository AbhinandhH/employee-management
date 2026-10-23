package com.assessment.employeemanagement.mapper;
import com.assessment.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.assessment.employeemanagement.dto.response.EmployeeResponse;
import com.assessment.employeemanagement.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)

public interface EmployeeMapper {

    EmployeeResponse toResponse(Employee employee);

    void updateEmployee(EmployeeUpdateRequest request, @MappingTarget Employee employee);

}
