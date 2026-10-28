package com.assessment.employeemanagement.mapper;
import com.assessment.employeemanagement.dto.request.EmployeeRequest;
import com.assessment.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.assessment.employeemanagement.dto.response.EmployeeResponse;
import com.assessment.employeemanagement.entity.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)

public interface EmployeeMapper {

    Employee toEntity(EmployeeRequest request);

    EmployeeResponse toResponse(Employee employee);

    @Mapping(target = "name", conditionQualifiedByName = "stringCheckForUpdate")
    @Mapping(target = "email", conditionQualifiedByName = "stringCheckForUpdate")
    @Mapping(target = "phoneNumber", conditionQualifiedByName = "stringCheckForUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployee(EmployeeUpdateRequest request,  @MappingTarget Employee employee);

    @Condition
    @Named("stringCheckForUpdate")
    default boolean isStringNotBlank(String stringValue) {
        return  stringValue != null && !stringValue.isBlank();
    }

}
