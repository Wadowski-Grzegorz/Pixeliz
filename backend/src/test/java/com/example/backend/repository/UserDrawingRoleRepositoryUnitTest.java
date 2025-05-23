package com.example.backend.repository;

import com.example.backend.model.Drawing;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.model.UserDrawingRole;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserDrawingRoleRepositoryUnitTest {

    @Autowired
    private UserDrawingRoleRepository userDrawingRoleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DrawingRepository drawingRepository;
    @Autowired
    RoleRepository roleRepository;


//    @Test
    // jsonb (column in drawing) doesn't work with h2 database
//    public void findUsersAndRoles_UserDrawingRoleExists_ListOfUserDrawingRole() {
//        userDrawingRoleRepository.deleteAll();
//
//        userRepository.deleteAll();
//        User user = User
//                .builder()
//                .name("Jamal")
//                .login("Jamal445")
//                .password("StrongPassword")
//                .email("jamal@email.com")
//                .build();
//        userRepository.save(user);
//
//        roleRepository.deleteAll();
//        Role role = Role
//                .builder()
//                .name("admin")
//                .admin(true)
//                .read(true)
//                .write(true)
//                .delete(true)
//                .build();
//        roleRepository.save(role);
//
//        drawingRepository.deleteAll();
//        Drawing drawing = Drawing
//                .builder()
//                .pixels(List.of("#FFFFFF", "#FFFFFF"))
//                .name("kitty")
//                .size_x(1)
//                .size_y(4)
//                .build();
//        drawingRepository.save(drawing);
//
//        UserDrawingRole udr = new UserDrawingRole(user, drawing, role);
//        userDrawingRoleRepository.save(udr);
//
//        Long drawingId = 1L;
//        List<Object[]> expected = userDrawingRoleRepository.findUsersAndRoles(drawingId);
//
//        assertEquals(1, expected.size());
//        User savedUser = (User) expected.get(0)[0];
//        Role savedRole = (Role) expected.get(0)[1];
//        assertEquals("Jamal", savedUser.getName());
//        assertEquals("admin", savedRole.getName());
//    }
}
