package com.example.eventconnect;

import com.example.eventconnect.model.dto.auth.AuthResponse;
import com.example.eventconnect.model.entity.user.Role;
import com.example.eventconnect.model.entity.user.RoleEnum;
import com.example.eventconnect.model.entity.user.User;
import com.example.eventconnect.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIntegrationTest extends TestContainerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;
    private final String userValidLogin = "valid@login.com";
    private final String userValidPassword = "validPassword";
    @Autowired
    private PasswordEncoder passwordEncoder;
    private ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("invalidRegistrationData")
    public void invalidUserRegistrationTest(String invalidRegistrationJsonString) throws Exception {
        mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRegistrationJsonString)).andDo(
                print()
        ).andExpect(status().isBadRequest());
    }


    @ParameterizedTest
    @MethodSource("validRegistrationData")
    public void validUserRegistrationTest(String invalidRegistrationJsonString) throws Exception {
        mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRegistrationJsonString)).andDo(
                print()
        ).andExpect(status().isOk());
    }

    @Test
    public void registerAlreadyExistedUser() throws Exception {
        saveValidUser();
        String userRegisterJson = "{\"login\":\"" + userValidLogin + "\",\"password\":\"" + userValidPassword + "\",\"role\":\"ADMIN\"}";

        mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRegisterJson)).andDo(
                print()
        ).andExpect(status().isConflict());
    }

    @Test
    public void validLogin() throws Exception {
        saveValidUser();
        String userLoginJson = "{\"login\":\"" + userValidLogin + "\",\"password\":\"" + userValidPassword + "\"}";
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginJson)).andDo(
                print()
        ).andExpect(status().isOk());
    }

    @Test
    public void loginNotExistedUser() throws Exception {
        String userLoginJson = "{\"login\":\"" + userValidLogin + "\",\"password\":\"" + userValidPassword + "\"}";
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginJson)).andDo(
                print()
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void loginWithInvalidPassword() throws Exception {
        saveValidUser();
        String userLoginJson = "{\"login\":\"" + userValidLogin + "\",\"password\":\"" + "invalid" + userValidPassword + "\"}";
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginJson)).andDo(
                print()
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void refreshToken() throws Exception {
        saveValidUser();
        String userLoginJson = "{\"login\":\"" + userValidLogin + "\",\"password\":\"" + userValidPassword + "\"}";
        MvcResult result = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginJson)).andDo(
                print()
        ).andExpect(status().isOk()).andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        AuthResponse authResponse = objectMapper.readValue(contentAsString, AuthResponse.class);
        mvc.perform(post("/auth/token/refresh")
                .content(authResponse.getRefreshToken())).andDo(
                print()
        ).andExpect(status().isOk()).andReturn();
    }

    private void saveValidUser() {
        userRepository.save(User.builder()
                .login(userValidLogin)
                .password(passwordEncoder.encode(userValidPassword))
                .roles(Set.of(new Role(RoleEnum.ADMIN)))
                .build());
    }

    static Stream<String> invalidRegistrationData() {
        return Stream.of(
                "{\"login\":\"\",\"password\":\"\",\"role\":\"\"}",
                "{\"login\":\"notEmail\",\"password\":\"password\",\"role\":\"ADMIN\"}",
                "{\"login\":\"nt1dc@gmail.com\",\"password\":\"12345\",\"role\":\"ADMIN\"}",
                "{\"login\":\"nt1dc@gmail.com\",\"password\":\"12345\",\"role\":\"INVALID_ROLE\"}",
                "{\"login\":\"nt1dc@gmail.com\",\"password\":\"tooLongPasswordtooLongPasswordd\",\"role\":\"ADMIN\"}");
    }

    public static Stream<String> validRegistrationData() {
        return Stream.of(
                "{\"login\":\"nt1dc@gmail1.com\",\"password\":\"012345678901234567890123456789\",\"role\":\"ADMIN\"}",
                "{\"login\":\"nt1dc@gmail2.com\",\"password\":\"123456\",\"role\":\"ADMIN\"}",
                "{\"login\":\"nt1dc@gmail3.com\",\"password\":\"password\",\"role\":\"EVENT_ADMIN\"}",
                "{\"login\":\"nt1dc@gmail4.com\",\"password\":\"123456\",\"role\":\"PARTICIPANT\"}"
        );
    }
}
