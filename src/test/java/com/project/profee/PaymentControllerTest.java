package com.project.profee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.profee.controller.PaymentController;
import com.project.profee.dto.payment.PaymentRequest;
import com.project.profee.dto.payment.PaymentResponse;
import com.project.profee.entity.Payment;
import com.project.profee.exception.PaymentNotFoundException;
import com.project.profee.repository.PaymentRepository;
import com.project.profee.service.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@MockBean({
        PaymentRepository.class
})

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    private final static BigDecimal AMOUNT = BigDecimal.valueOf(111.11);
    private final static Long FROM = 5555444433332222L;
    private final static Long TO = 5555444433332221L;
    private final static String ID = "some-unique-id";
    private final static BigDecimal WRONG_AMOUNT = BigDecimal.valueOf(133.333);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    @Test
    public void createSuccess() throws Exception {

        when(paymentService.create(anyString(), any(PaymentRequest.class))).thenReturn(getTestPaymentResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/payments")
                .accept(MediaType.APPLICATION_JSON)
                .content(toJson(getTestPaymentRequest(FROM, TO, AMOUNT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("payment").isNotEmpty());
    }

    @Test
    public void createErrorEmptyData() throws Exception {

        when(paymentService.create(anyString(), any(PaymentRequest.class))).thenReturn(getTestPaymentResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/payments")
                .accept(MediaType.APPLICATION_JSON)
                .content(toJson(getTestPaymentRequest(null, null, null)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors").isNotEmpty())
                .andExpect(jsonPath("fieldErrors[*]", hasSize(3)));
    }

    @Test
    public void createErrorAmount() throws Exception {

        when(paymentService.create(anyString(), any(PaymentRequest.class))).thenReturn(getTestPaymentResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/payments")
                .accept(MediaType.APPLICATION_JSON)
                .content(toJson(getTestPaymentRequest(FROM, TO, WRONG_AMOUNT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors").isNotEmpty())
                .andExpect(jsonPath("fieldErrors[*]", hasSize(1)))
                .andExpect(jsonPath("fieldErrors[*].field", hasItem("amount")));
    }

    @Test
    public void getOneSuccess() throws Exception {
        when(paymentRepository.getById(anyString())).thenReturn(getTestPayment());

        mockMvc.perform(get("/payments/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("from").isNotEmpty())
                .andExpect(jsonPath("to").isNotEmpty())
                .andExpect(jsonPath("amount").isNotEmpty())
                .andExpect(jsonPath("created").isNotEmpty());
    }

    @Test
    public void getOneNotFound() throws Exception {
        when(paymentRepository.getById(anyString())).thenThrow(new PaymentNotFoundException(any()));

        mockMvc.perform(get("/payments/{id}", ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").isNotEmpty());

        verify(paymentRepository, times(1)).getById(anyString());
    }

    @Test
    public void getAllSuccessful() throws Exception {
        HashMap<String, Payment> container = new HashMap<>();
        container.put(ID, getTestPayment());

        when(paymentRepository.getAll()).thenReturn(container);

        mockMvc.perform(MockMvcRequestBuilders.get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].from").isNotEmpty())
                .andExpect(jsonPath("$[*].to").isNotEmpty())
                .andExpect(jsonPath("$[*].amount").isNotEmpty())
                .andExpect(jsonPath("$[*].created").isNotEmpty());
    }

    @Test
    public void updateOneSuccessful() throws Exception {

        when(paymentRepository.getById(anyString())).thenReturn(getTestPayment());
        when(paymentService.update(anyString(), any(PaymentRequest.class))).thenReturn(getTestPaymentResponse());

        mockMvc.perform(MockMvcRequestBuilders.put("/payments/{id}", ID)
                .accept(MediaType.APPLICATION_JSON)
                .content(toJson(getTestPaymentRequest(FROM, TO, AMOUNT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("payment").isNotEmpty());
    }

    @Test
    public void updateOneNotFound() throws Exception {
        when(paymentRepository.getById(anyString())).thenThrow(new PaymentNotFoundException(any()));

        mockMvc.perform(get("/payments/{id}", ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").isNotEmpty());

        verify(paymentRepository, times(1)).getById(anyString());
        verify(paymentRepository, times(0)).create(anyString(), anyLong(), anyLong(), BigDecimal.valueOf(anyDouble()));
    }

    @Test
    public void deleteOneSuccessful() throws Exception {
        when(paymentRepository.getById(ID)).thenReturn(getTestPayment());

        mockMvc.perform(delete("/payments/{id}", ID))
                .andExpect(status().isOk());

        verify(paymentRepository, times(1)).deleteById(ID);
    }

    @Test
    public void deleteOneNotFound() throws Exception {
        when(paymentRepository.getById(anyString())).thenThrow(new PaymentNotFoundException(any()));

        mockMvc.perform(get("/payments/{id}", ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").isNotEmpty());

        verify(paymentRepository, times(1)).getById(anyString());
        verify(paymentRepository, times(0)).deleteById(ID);
    }


    private Payment getTestPayment() {
        return new Payment(FROM, TO, AMOUNT);
    }

    private PaymentRequest getTestPaymentRequest(Long from, Long to, BigDecimal amount) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(amount);
        paymentRequest.setFrom(from);
        paymentRequest.setTo(to);
        return paymentRequest;
    }

    private PaymentResponse getTestPaymentResponse() {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPayment(getTestPayment());
        paymentResponse.setId(ID);
        return paymentResponse;
    }

    private String toJson(PaymentRequest paymentRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(paymentRequest);
    }
}
