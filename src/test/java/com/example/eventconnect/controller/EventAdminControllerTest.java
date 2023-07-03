package com.example.eventconnect.controller;

import com.example.eventconnect.TestContainerTest;
import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.dto.event.registration.ParticipantEventParamDto;
import com.example.eventconnect.model.dto.event.registration.ParticipantRegistrationResponse;
import com.example.eventconnect.model.entity.contract.EventContract;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.model.entity.event.EventStatus;
import com.example.eventconnect.model.entity.participant.Participant;
import com.example.eventconnect.model.entity.participant.ParticipationStatus;
import com.example.eventconnect.model.entity.user.Role;
import com.example.eventconnect.model.entity.user.RoleEnum;
import com.example.eventconnect.model.entity.user.User;
import com.example.eventconnect.repository.EventContractRepository;
import com.example.eventconnect.repository.EventParticipantRepository;
import com.example.eventconnect.repository.EventRepository;
import com.example.eventconnect.repository.UserRepository;
import com.example.eventconnect.security.JwtTokenProvider;
import com.example.eventconnect.service.admin.AdminService;
import com.example.eventconnect.service.event.participant.ParticipantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class EventAdminControllerTest extends TestContainerTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventContractRepository eventContractRepository;
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private AdminService adminService;

    private final String eventAdminLogin = "enet@admin.com";
    private final String eventAdminPassword = "eventAdminPassword";
    private String eventAdminAccessToken;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Long eventId = 1L;

    @Test
    @Order(1)
    public void createEvent() throws Exception {
        saveUser(eventAdminLogin, eventAdminPassword, RoleEnum.EVENT_ADMIN);
        eventAdminAccessToken = generateJwtAccessToken();
        mvc.perform(post("/events")
                .header("Authorization", "Bearer " + eventAdminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson)).andDo(
                print()
        ).andExpect(status().isOk());
        List<Event> events = eventRepository.findAll();
        Assertions.assertEquals(1, events.size());
        Event event = events.get(0);
        Assertions.assertEquals(EventStatus.CREATED, event.getEventStatus());
        Assertions.assertEquals(1, event.getEventRegistrationParams().size());
        List<EventContract> contracts = eventContractRepository.findAll();
        Assertions.assertEquals(1, contracts.size());
        EventContract contract = contracts.get(0);
        Assertions.assertEquals(contract.getEvent().getId(), event.getId());
        Assertions.assertEquals(EventContractStatus.CREATED, contract.getStatus());
        eventId = event.getId();
    }

    @Test
    @Order(2)
    public void getContracts() throws Exception {
        eventAdminAccessToken = generateJwtAccessToken();
        MockHttpServletResponse response = mvc.perform(get("/event-admin/contracts")
                        .header("Authorization", "Bearer " + eventAdminAccessToken)).andDo(
                        print())
                .andExpect(status().isOk()).andReturn().getResponse();
        String contentAsString = response.getContentAsString();

        EventContractResponse[] contracts = objectMapper.readValue(contentAsString, EventContractResponse[].class);
        Assertions.assertEquals(1, contracts.length);
        EventContractResponse contract = contracts[0];
        Assertions.assertEquals(eventId, contract.getEvent().getId());
        Assertions.assertEquals(EventContractStatus.CREATED, contract.getStatus());
    }

    @Test
    @Order(3)
    public void signContract() throws Exception {
        eventAdminAccessToken = generateJwtAccessToken();
        mvc.perform(put("/event-admin/contracts/1/sign")
                        .header("Authorization", "Bearer " + eventAdminAccessToken)).andDo(
                        print())
                .andExpect(status().isOk());
        EventContract byId = eventContractRepository.findById(1L).get();
        Assertions.assertEquals(EventContractStatus.SIGNED, byId.getStatus());
    }


    @Test
    @Order(4)
    public void getEventParticipantsAnswers() throws Exception {
        adminService.updateContractStatus(1L, EventContractStatus.APPROVED);
        saveUser("participant@event.com", "password", RoleEnum.PARTICIPANT);
        participantService.registerParticipant(1L, List.of(new ParticipantEventParamDto(1L, "answer")), "participant@event.com");

        eventAdminAccessToken = generateJwtAccessToken();
        MockHttpServletResponse response = mvc.perform(get("/events/1/participants/answers")
                        .header("Authorization", "Bearer " + eventAdminAccessToken)).andDo(
                        print())
                .andExpect(status().isOk()).andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        ParticipantRegistrationResponse[] contracts = objectMapper.readValue(contentAsString, ParticipantRegistrationResponse[].class);
        Assertions.assertEquals(1, contracts.length);
    }

    @Test
    @Order(5)
    public void updateParticipantStatus() throws Exception {
        eventAdminAccessToken = generateJwtAccessToken();
        mvc.perform(put("/events/1/participants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"APPROVED\"")
                        .header("Authorization", "Bearer " + eventAdminAccessToken)).andDo(
                        print())
                .andExpect(status().isOk());
        Participant participant = eventParticipantRepository.findById(1L).get();
        Assertions.assertEquals(ParticipationStatus.APPROVED,participant.getParticipationStatus());
    }


    private final String eventJson = """
            {
              "name": "testName",
              "description": "testDescription",
              "eventRegistrationParams": [
                {
                  "name": "testEventRegistrationParamName",
                  "description": "testEventRegistrationParamDescription",
                  "checkRequire": true
                }
              ]
            }""";
    @Autowired
    private EventParticipantRepository eventParticipantRepository;


    private void saveUser(String login, String password, RoleEnum roleEnum) {
        userRepository.save(User.builder()
                .login(login)
                .password(passwordEncoder.encode(password))
                .roles(Set.of(new Role(roleEnum))).build());
    }

    private String generateJwtAccessToken() {
        return jwtTokenProvider.createAccessToken(eventAdminLogin, RoleEnum.EVENT_ADMIN.name());
    }


}