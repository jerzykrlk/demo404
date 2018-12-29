package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class DemoApplicationTests {

    @Autowired
    private MockMvc mvc;

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void shouldGet404StatusCode() throws Exception {
        mvc.perform(get("/example"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldGet404StatusMessage() {
        String uri = "http://localhost:" + port + "/example";

        HttpStatusCodeException e = getHttpError(uri);

        assertThat(e).isInstanceOf(NotFound.class).hasMessage("404 Not Found");
    }

    private HttpStatusCodeException getHttpError(String uri) {
        try {
            restTemplate.getForObject(uri, Object.class);
            fail("Should fail");
        } catch (HttpStatusCodeException e) {
            return e;
        }
        return null;
    }

}

