package com.example.nestedvalidation;

import com.example.nestedvalidation.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService.clear();
    }

    @Test
    void 配送先郵便番号が空なら400で拒否し注文を作成しない() throws Exception {
        MvcResult result = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "customerId": "customer-1",
                      "shippingAddress": {
                        "postalCode": "",
                        "city": "Tokyo"
                      }
                    }
                    """))
            .andReturn();

        assertAll(
            () -> assertThat(result.getResponse().getStatus()).isEqualTo(400),
            () -> assertThat(result.getResponse().getContentAsString()).contains("\"code\":\"VALIDATION_ERROR\""),
            () -> assertThat(result.getResponse().getContentAsString()).contains("\"field\":\"shippingAddress.postalCode\""),
            () -> assertThat(orderService.count()).isZero()
        );
    }

    @Test
    void 正しい配送先なら201で注文を作成する() throws Exception {
        MvcResult result = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "customerId": "customer-1",
                      "shippingAddress": {
                        "postalCode": "100-0001",
                        "city": "Tokyo"
                      }
                    }
                    """))
            .andReturn();

        assertAll(
            () -> assertThat(result.getResponse().getStatus()).isEqualTo(201),
            () -> assertThat(result.getResponse().getContentAsString()).contains("\"postalCode\":\"100-0001\""),
            () -> assertThat(orderService.count()).isEqualTo(1)
        );
    }
}
