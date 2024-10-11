package com.dongnv.employee_evaluation_system.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.dongnv.employee_evaluation_system.dto.mapper.DepartmentMapper;
import com.dongnv.employee_evaluation_system.dto.request.DepartmentDTO;
import com.dongnv.employee_evaluation_system.exception.AppException;
import com.dongnv.employee_evaluation_system.exception.ErrorCode;
import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.repository.DepartmentRepository;
import com.dongnv.employee_evaluation_system.repository.EmployeeRepository;

// Only Use Mock
class DepartmentServiceTest {
    @Mock
    DepartmentRepository departmentRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    DepartmentMapper departmentMapper;

    @InjectMocks
    DepartmentService departmentService;

    private Department department1;
    private Department department2;
    private Department department3;
    private Department department3_update;

    private DepartmentDTO departmentDTO3;
    private DepartmentDTO departmentDTO3_update;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        department1 =
                Department.builder().id(1).code("D001").name("Department 1").build();
        department2 =
                Department.builder().id(2).code("D002").name("Department 2").build();
        department3 =
                Department.builder().id(3).code("D003").name("Department 3").build();

        departmentDTO3 =
                DepartmentDTO.builder().code("D003").name("Department 3").build();
        departmentDTO3_update =
                DepartmentDTO.builder().code("D003").name("Department 3333").build();
    }

    @Test
    void getAllDepartments() {
        // GIVEN
        List<Department> expectedDepartments = List.of(department1, department2);
        Mockito.when(departmentRepository.findAll()).thenReturn(expectedDepartments);

        // WHEN
        List<Department> actualDepartments = departmentService.getAllDepartments();

        // THEN
        Mockito.verify(departmentRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(expectedDepartments, actualDepartments);
    }

    @Test
    void getDepartmentsByPage() {
        // GIVEN
        department1.setCountEmployee(10);
        department2.setCountEmployee(10);
        List<Department> departments = List.of(department1, department2);
        Page<Department> departmentPage = new PageImpl<>(departments, PageRequest.of(0, 10), departments.size());
        Mockito.when(departmentRepository.findAllByNameLike(anyString(), any(PageRequest.class)))
                .thenReturn(departmentPage);
        Mockito.when(employeeRepository.countByDepartmentId(any())).thenReturn(10);

        // When
        Page<Department> actualDepartmentPage = departmentService.getDepartmentsByPage(0, "Department");

        // THEN
        Assertions.assertEquals(departmentPage, actualDepartmentPage);
        Mockito.verify(employeeRepository, Mockito.times(departments.size())).countByDepartmentId(any());
        Mockito.verify(departmentRepository, Mockito.times(1)).findAllByNameLike(anyString(), any(PageRequest.class));
    }

    @Test
    void getDepartmentById_valid_success() {
        // GIVEN
        Integer id = 1;
        Mockito.when(departmentRepository.findById(id)).thenReturn(Optional.of(department1));
        DepartmentDTO departmentDTO = DepartmentDTO.builder()
                .id(department1.getId())
                .code(department1.getCode())
                .name(department1.getName())
                .build();
        Mockito.when(departmentMapper.toDepartmentDTO(department1)).thenReturn(departmentDTO);

        // WHEN
        DepartmentDTO actualDepartmentDTO = departmentService.getDepartmentById(id);

        // THEN
        Assertions.assertEquals(departmentDTO.getId(), actualDepartmentDTO.getId());
        Assertions.assertEquals(departmentDTO.getCode(), actualDepartmentDTO.getCode());
        Assertions.assertEquals(departmentDTO.getName(), actualDepartmentDTO.getName());
    }

    @Test
    void getDepartmentById_notExist_fail() {
        // GIVEN
        Mockito.when(departmentRepository.findById(3)).thenReturn(Optional.ofNullable(null));

        // WHEN, THEN
        var Exception = Assertions.assertThrows(AppException.class, () -> departmentService.getDepartmentById(3));
        Assertions.assertEquals(Exception.getErrorCode().getCode(), 1001);
    }

    @Test
    void createDepartment_valid_success() {
        // GIVEN
        Department department = Department.builder()
                .code(departmentDTO3.getCode())
                .name(departmentDTO3.getName())
                .build();
        Mockito.when(departmentMapper.toDepartment(departmentDTO3)).thenReturn(department);
        Mockito.when(departmentRepository.save(department)).thenReturn(department3);

        // WHEN
        departmentService.createDepartment(departmentDTO3);

        // THEN
        Mockito.verify(departmentRepository, Mockito.times(1)).save(any(Department.class));
        Mockito.verify(departmentMapper, Mockito.times(1)).toDepartment(departmentDTO3);
    }

    @Test
    void createDepartment_nameExist_fail() {
        // GIVEN
        Department department = Department.builder()
                .code(departmentDTO3.getCode())
                .name(departmentDTO3.getName())
                .build();
        Mockito.when(departmentMapper.toDepartment(departmentDTO3)).thenReturn(department);
        Mockito.doThrow(new DataIntegrityViolationException("Duplicate name department"))
                .when(departmentRepository)
                .save(department);

        // WHEN, THEN
        Assertions.assertThrows(
                DataIntegrityViolationException.class, () -> departmentService.createDepartment(departmentDTO3));
    }

    @Test
    void updateDepartment_valid_success() {
        //      // GIVEN
        Mockito.when(departmentRepository.findById(any())).thenReturn(Optional.of(department3));
        Mockito.doAnswer(invocationOnMock -> {
                    Department department = invocationOnMock.getArgument(0);
                    DepartmentDTO departmentDTO = invocationOnMock.getArgument(1);
                    department.setCode(departmentDTO.getCode());
                    department.setName(departmentDTO.getName());
                    return null;
                })
                .when(departmentMapper)
                .updatedDepartment(department3, departmentDTO3_update);

        Mockito.when(departmentRepository.save(any(Department.class))).thenReturn(department3);

        // Gọi phương thức update
        departmentService.updateDepartment(departmentDTO3_update, department3.getId());

        // Kiểm tra xem mapper và repository đã được gọi chưa
        Mockito.verify(departmentMapper).updatedDepartment(department3, departmentDTO3_update);
        Mockito.verify(departmentRepository).save(department3);
        Assertions.assertEquals(department3.getName(), departmentDTO3_update.getName());
    }

    @Test
    void updateDepartment_notFound_fail() {
        // GIVEN
        Mockito.when(departmentRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        // WHEN
        var exception = Assertions.assertThrows(
                AppException.class,
                () -> departmentService.updateDepartment(departmentDTO3_update, department3.getId()));
        Assertions.assertEquals(exception.getErrorCode().getCode(), ErrorCode.DEPARTMENT_NOT_FOUND.getCode());
    }

    @Test
    void deleteDepartmentById() {
        // GIVEN
        Integer id = 1;

        // WHEN
        departmentService.deleteDepartmentById(id);

        // THEN
        Mockito.verify(departmentRepository, Mockito.times(1)).deleteById(id);
    }
}
