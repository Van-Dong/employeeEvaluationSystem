package com.dongnv.employee_evaluation_system.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import com.dongnv.employee_evaluation_system.dto.mapper.EmployeeMapper;
import com.dongnv.employee_evaluation_system.dto.mapper.EmployeeMapperImpl;
import com.dongnv.employee_evaluation_system.dto.request.EmployeeDTO;
import com.dongnv.employee_evaluation_system.exception.AppException;
import com.dongnv.employee_evaluation_system.exception.ErrorCode;
import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.model.Employee;
import com.dongnv.employee_evaluation_system.repository.DepartmentRepository;
import com.dongnv.employee_evaluation_system.repository.EmployeeRepository;

class EmployeeServiceTest {
    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    DepartmentRepository departmentRepository;

    @Spy
    EmployeeMapper employeeMapper;

    @Mock
    FileStorageService fileStorageService;

    @InjectMocks
    EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeMapper = new EmployeeMapperImpl();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEmployeesByPage() {
        // GIVEN
        String searchName = "Nguyen Van";
        int page = 0;

        // WHEN
        employeeService.getEmployeesByPage(searchName, page);

        // THEN
        Mockito.verify(employeeRepository, Mockito.times(1))
                .findAllByFullNameLike("%" + searchName + "%", PageRequest.of(page, 10));
    }

    @Test
    void getEmployeesByDepartmentId() {
        // GIVEN
        int departmentId = 1;
        int page = 0;

        // WHEN
        employeeService.getEmployeesByDepartmentId(departmentId, page);

        // THEN
        Mockito.verify(employeeRepository, Mockito.times(1)).findByDepartmentId(departmentId, PageRequest.of(page, 10));
    }

    @Test
    void getEmployeeById_hasDepartment_success() {
        // GIVEN
        long employeeId = 1;
        Department department =
                Department.builder().id(1).code("HR001").name("Human Resource").build();
        Employee employee = Employee.builder()
                .id(employeeId)
                .fullName("Nguyen Van A")
                .department(department)
                .build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // WHEN
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeId);

        // THEN
        Assertions.assertEquals(employeeId, employeeDTO.getId());
        Assertions.assertEquals(employee.getFullName(), employeeDTO.getFullName());
        Assertions.assertEquals(department.getId(), employeeDTO.getDepartmentId());
    }

    @Test
    void getEmployeeById_notHasDepartment_success() {
        // GIVEN
        long employeeId = 1;
        Employee employee =
                Employee.builder().id(employeeId).fullName("Nguyen Van A").build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // WHEN
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeId);

        // THEN
        Assertions.assertEquals(employeeId, employeeDTO.getId());
        Assertions.assertEquals(employee.getFullName(), employeeDTO.getFullName());
        Assertions.assertNull(employeeDTO.getDepartmentId());
    }

    @Test
    void getEmployeeById_employeeNotExist_fail() {
        // GIVEN
        long employeeId = 1;
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.ofNullable(null));

        // WHEN, THEN
        var exception = Assertions.assertThrows(AppException.class, () -> employeeService.getEmployeeById(employeeId));
        Assertions.assertEquals(ErrorCode.EMPLOYEE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void save_hasFile_hasDepartmentId_success() {
        // GIVEN
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "image1.jpg", "image/jpeg", "Image content".getBytes());
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .fullName("Nguyen Van A")
                .isMale(true)
                .departmentId(1)
                .imageFile(mockFile)
                .build();
        Department department =
                Department.builder().id(1).code("HR001").name("Human Resource").build();
        Mockito.when(fileStorageService.storeFile(employeeDTO.getImageFile())).thenReturn("images/image1.jpg");
        Mockito.when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));

        // WHEN
        employeeService.save(employeeDTO);

        // THEN
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        Mockito.verify(employeeRepository, Mockito.times(1)).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();
        Assertions.assertEquals(employeeDTO.getId(), savedEmployee.getId());
        Assertions.assertEquals(employeeDTO.getFullName(), savedEmployee.getFullName());
        Assertions.assertEquals(
                employeeDTO.getDepartmentId(), savedEmployee.getDepartment().getId());
    }

    @Test
    void save_noFile_hasDepartmentId_departmentNotFound_fail() {
        // GIVEN
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .fullName("Nguyen Van A")
                .isMale(true)
                .departmentId(1)
                .build();
        Mockito.when(departmentRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        // WHEN, THEN
        var exception = Assertions.assertThrows(AppException.class, () -> employeeService.save(employeeDTO));
        Assertions.assertEquals(ErrorCode.DEPARTMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void save_noFile_noDepartmentId_success() {
        // GIVEN
        EmployeeDTO employeeDTO =
                EmployeeDTO.builder().fullName("Nguyen Van A").isMale(true).build();

        // WHEN
        employeeService.save(employeeDTO);

        // THEN
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        Mockito.verify(employeeRepository, Mockito.times(1)).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();
        Assertions.assertEquals(employeeDTO.getId(), savedEmployee.getId());
        Assertions.assertEquals(employeeDTO.getFullName(), savedEmployee.getFullName());
        Assertions.assertEquals(employeeDTO.getIsMale(), savedEmployee.getIsMale());
    }

    @Test
    void updateEmployee_hasFile_hasImageUrl_hasDepartmentId_success() {
        // GIVEN
        long employeeId = 1;
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "anh2.jpg", "image/jpeg", "Image content".getBytes());
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .fullName("Nguyen Van A1")
                .isMale(true)
                .imageFile(mockFile)
                .departmentId(1)
                .build();
        Department department =
                Department.builder().id(1).code("HR001").name("Human Resource").build();
        Employee employee = Employee.builder()
                .id(employeeId)
                .fullName("Nguyen Van A")
                .isMale(true)
                .imageUrl("images/anh1.jpg")
                .build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        Mockito.when(fileStorageService.storeFile(employeeDTO.getImageFile())).thenReturn("images/anh2.jpg");
        Mockito.when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));

        // WHEN
        employeeService.updateEmployee(employeeDTO, employeeId);

        // THEN
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        Mockito.verify(fileStorageService, Mockito.times(1)).deleteFile(any());
        Mockito.verify(fileStorageService, Mockito.times(1)).storeFile(mockFile);
        Mockito.verify(employeeRepository, Mockito.times(1)).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();
        Assertions.assertEquals(employeeDTO.getFullName(), savedEmployee.getFullName());
        Assertions.assertEquals(
                employeeDTO.getDepartmentId(), savedEmployee.getDepartment().getId());
        Assertions.assertEquals("images/anh2.jpg", savedEmployee.getImageUrl());
    }

    @Test
    void updateEmployee_employeeNotExist_fail() {
        // GIVEN
        long employeeId = 1;
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .fullName("Nguyen Van A1")
                .isMale(true)
                .departmentId(1)
                .build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.ofNullable(null));

        // WHEN, THEN
        var exception = Assertions.assertThrows(
                AppException.class, () -> employeeService.updateEmployee(employeeDTO, employeeId));
        Assertions.assertEquals(ErrorCode.EMPLOYEE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateEmployee_noFile_hasDepartmentId_departmentNotFound_fail() {
        // GIVEN
        long employeeId = 1;
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .fullName("Nguyen Van A1")
                .isMale(true)
                .departmentId(1)
                .build();
        Employee employee = Employee.builder()
                .id(employeeId)
                .fullName("Nguyen Van A")
                .isMale(true)
                .imageUrl("images/anh1.jpg")
                .build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        Mockito.when(departmentRepository.findById(employeeDTO.getDepartmentId()))
                .thenReturn(Optional.ofNullable(null));

        // WHEN, THEN
        var exception = Assertions.assertThrows(
                AppException.class, () -> employeeService.updateEmployee(employeeDTO, employeeId));
        Assertions.assertEquals(ErrorCode.DEPARTMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void deleteById() {
        // GIVEN
        long employeeId = 1;

        // WHEN
        employeeService.deleteById(employeeId);

        // THEN
        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(employeeId);
    }
}
