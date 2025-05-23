package com.example.backend.service;

import com.example.backend.dto.DrawingDTO;
import com.example.backend.exception.DrawingNotFoundException;
import com.example.backend.model.Drawing;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.model.UserDrawingRole;
import com.example.backend.repository.DrawingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DrawingServiceTest {

    @Mock
    private DrawingRepository drawingRepository;

    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private UserDrawingRoleService userDrawingRoleService;

    @InjectMocks
    private DrawingService drawingService;

    private Drawing drawing;

    @BeforeEach
    void setUp() {
        drawing = Drawing
                .builder()
                .id(1L)
                .pixels(List.of("#FFFFFF", "#FFFFFF"))
                .name("my drawing")
                .size_x(1)
                .size_y(2)
                .build();
    }

    @Test
    void createDrawing_ValidDTO_ReturnsCreatedDrawing() {
        // precondition
        User user = User.builder().id(1L).build();
        DrawingDTO dDto =
                DrawingDTO
                        .builder()
                        .pixels(List.of("#FFFFFF", "#FFFFFF"))
                        .name("my drawing")
                        .size_x(1)
                        .size_y(2)
                        .build();
        Role role = Role.builder().id(1L).name("owner").build();
        UserDrawingRole udr = new UserDrawingRole();

        given(userService.getUserByUsername(anyString())).willReturn(user);
        given(roleService.getRole(anyString())).willReturn(role);
        given(drawingRepository.save(any(Drawing.class))).willReturn(drawing);
        given(userDrawingRoleService.createUserDrawingRole(eq(user), any(Drawing.class), eq(role))).willReturn(udr);

        // action
        Drawing returnedDrawing = drawingService.createDrawing(dDto, "username");

        // verify
        verify(drawingRepository, times(1)).save(any(Drawing.class));
        assertThat(returnedDrawing)
                .isNotNull()
                .isEqualTo(drawing);

    }

    @Test
    void getDrawing_IdExists_ReturnsDrawing() {
        // precondition
        Long id = 1L;
        given(drawingRepository.findById(id)).willReturn(Optional.of(drawing));

        // action
        Drawing returnedDrawing = drawingService.getDrawing(id);

        // verify
        assertThat(returnedDrawing)
                .isNotNull()
                .isEqualTo(drawing);
    }

    @Test
    void getDrawing_IdDoesNotExists_ThrowsDrawingNotFoundException() {
        // precondition
        Long id = 100L;
        given(drawingRepository.findById(id)).willReturn(Optional.empty());

        // action
        DrawingNotFoundException ex = assertThrows(DrawingNotFoundException.class, () -> drawingService.getDrawing(id));

        // verify
        assertThat(ex.getCriteria())
                .isNotNull()
                .isNotEmpty()
                .containsEntry("id", id);
    }

    @Test
    void getDrawings_ReturnsAllDrawings() {
        // precondition
        Drawing drawing2 = Drawing
                .builder()
                .id(2L)
                .pixels(List.of("#FFFFFF", "#FFFFFF"))
                .name("my second drawing")
                .size_x(1)
                .size_y(3)
                .build();
        given(drawingRepository.findAll()).willReturn(List.of(drawing, drawing2));

        // action
        List<Drawing> drawings = drawingService.getDrawings();

        // verify
        assertThat(drawings)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(List.of(drawing, drawing2));
    }

    @Test
    void updateDrawing() {
        // precondition
        Long id = 1L;
        DrawingDTO dDto = DrawingDTO
                .builder()
                .pixels(List.of("#FFFFFF", "#FFFFFF"))
                .name("my updated drawing")
                .build();
        User user = User.builder().id(1L).build();
        Role role = Role.builder().id(1L).build();
        UserDrawingRole udr = new UserDrawingRole(user, drawing, role);
        given(drawingRepository.findById(id)).willReturn(Optional.of(drawing));
        given(drawingRepository.save(any(Drawing.class))).willReturn(drawing);
        given(userService.getUserByUsername(anyString())).willReturn(user);
        given(userDrawingRoleService.getUserDrawingRole(id, user.getId())).willReturn(udr);
        given(roleService.canWrite(any(Role.class))).willReturn(true);

        // action
        Drawing returnedDrawing = drawingService.updateDrawing(id, dDto, "username");

        // verify
        verify(drawingRepository, times(1)).save(any(Drawing.class));
        assertThat(returnedDrawing.getId()).isEqualTo(id);
        assertThat(returnedDrawing.getPixels()).isEqualTo(List.of("#FFFFFF", "#FFFFFF"));
        assertThat(returnedDrawing.getName()).isEqualTo("my updated drawing");
    }

    @Test
    void deleteDrawing_IdExists() {
        // precondition
        Long id = 1L;
        User user = User.builder().id(1L).build();
        Role role = Role.builder().id(1L).build();
        UserDrawingRole udr = new UserDrawingRole(user, drawing, role);

        given(drawingRepository.findById(id)).willReturn(Optional.of(drawing));
        given(userService.getUserByUsername(anyString())).willReturn(user);
        given(userDrawingRoleService.getUserDrawingRole(id, user.getId())).willReturn(udr);
        given(roleService.canDelete(any(Role.class))).willReturn(true);


        // action
        drawingService.deleteDrawing(1L, "username", false);

        // verify
        verify(drawingRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteDrawing_IdDoesNotExists_ThrowsDrawingNotFoundException() {
        // precondition
        Long id = 100L;

        given(drawingRepository.findById(id)).willReturn(Optional.empty());

        // action
        DrawingNotFoundException ex = assertThrows(
                DrawingNotFoundException.class,
                () -> drawingService.deleteDrawing(100L, "username", false)
        );

        // verify
        assertThat(ex.getCriteria())
                .isNotNull()
                .isNotEmpty()
                .containsEntry("id", id);
    }
}