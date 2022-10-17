package de.angelasensio.tariff.controller;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import de.angelasensio.tariff.domain.Module;
import de.angelasensio.tariff.domain.ModuleType;
import de.angelasensio.tariff.domain.Policy;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TariffControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters)
                .stream()
                .filter(messageConverter -> messageConverter instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("no messageConverter was found"));

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void calculateTariffWithSuccess() throws Exception {

        String transactionAsJson = toJson(new Policy(new HashSet<>(asList(new Module(1000, ModuleType.BIKE),
                new Module(1000, ModuleType.JEWELRY)))));

        this.mockMvc.perform(post("/tariff")
                .contentType(contentType)
                .content(transactionAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(new BigDecimal("350.0")));
    }

    @Test
    public void calculateTariffForCoverageOutOfRangeReturnsBadRequest() throws Exception {

        String transactionAsJson = toJson(new Policy(new HashSet<>(asList(new Module(100000, ModuleType.BIKE),
                new Module(1000, ModuleType.JEWELRY)))));

        this.mockMvc.perform(post("/tariff")
                .contentType(contentType)
                .content(transactionAsJson))
                .andExpect(status().isBadRequest());
    }

    private String toJson(final Object object) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(object, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}