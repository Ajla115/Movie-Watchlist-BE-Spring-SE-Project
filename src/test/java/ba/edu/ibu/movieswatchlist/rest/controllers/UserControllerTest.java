package ba.edu.ibu.movieswatchlist.rest.controllers;

import ba.edu.ibu.movieswatchlist.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testAddUser() throws Exception {
        String email = "test@example.com";
        Long userId = 1L;

        when(userService.addOrGetUserId(email)).thenReturn(userId);

        mockMvc.perform(post("/api/users/login")
                        .content(email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(userId.toString()));

        verify(userService, times(1)).addOrGetUserId(email);
    }

    @Test
    void testChangeNotificationStatus() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).toggleNotificationStatus(userId);

        mockMvc.perform(put("/api/users/change-notification-status/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).toggleNotificationStatus(userId);
    }
}
