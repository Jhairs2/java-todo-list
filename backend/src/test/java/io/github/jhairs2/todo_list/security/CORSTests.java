package io.github.jhairs2.todo_list.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CORSTests {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("Should return 200 when recieving requests from accepted Origins")
    @ParameterizedTest
    @CsvSource({ "http://localhost:5500,POST", "http://127.0.0.1:5500,POST", "http://localhost:5500,GET",
            "http://127.0.0.1:5500,GET" })
    void preFlightRequest_IfAcceptedOrigin_ReturnStatus200(String origin, String method) throws Exception {
        mockMvc.perform(options("/api/v1/projects").header("Access-Control-Request-Method", method)
                .header("Origin", origin))
                .andDo(print())
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", origin))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS"))
                .andExpect(status().isOk());
    }

    @DisplayName("Sh0uld return Forbidden 403 status when recieving requests from bad Origins")
    @ParameterizedTest
    @CsvSource({ "badOrigin.com,POST", "badOrigin.com,GET" })
    void preFlightRequest_IfBadOrigin_ReturnStatusForbidden(String origin, String method) throws Exception {
        mockMvc.perform(options("/api/v1/projects").header("Access-Control-Request-Method", method)
                .header("Origin", origin))
                .andDo(print())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"))
                .andExpect(status().isForbidden());
    }
}
