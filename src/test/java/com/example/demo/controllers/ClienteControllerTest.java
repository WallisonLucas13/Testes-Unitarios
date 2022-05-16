package com.example.demo.controllers;

import com.example.demo.models.Cliente;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.services.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;


    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
         mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
       }

    @Test
    public void deveRetornarStatusOK_AoSalvarCliente() throws Exception {
        //Cenario

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Gaby");

        //Acao

        ResultActions response = mockMvc.perform(post("/")
                .content(objectMapper.writeValueAsString(cliente))
                .contentType(MediaType.APPLICATION_JSON_UTF8));
        response.andDo(print());

        MvcResult mvcResult = response.andReturn();
        String mvcResponse = mvcResult.getResponse().getContentAsString();

        //Rules

        Boolean contentNome = mvcResponse.contains(cliente.getNome());
        Boolean contentId = mvcResponse.contains(String.valueOf(cliente.getId()));

        //Verification

        response.andExpect(status().isOk());
        Assert.assertTrue(contentNome && contentId);
    }

    @Test
    public void deveRetornarBadRequest_AoSalvarClienteSemCliente() throws Exception {

        //Acao
        ResultActions response = mockMvc.perform(post("/"));
        response.andDo(print());
        MvcResult mvcResult = response.andReturn();
        String res = mvcResult.getResponse().getContentAsString();
        Boolean empty = res.isEmpty();

        //Verification
        response.andExpect(status().isBadRequest());
        Assert.assertTrue(empty);
    }

    @Test
    public void deveRetornarStatusOK_AoPedirLista() throws Exception {

        //Cenario

        Cliente cliente1 = new Cliente();
        cliente1.setNome("Wallison");

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Jenny");

        Cliente cliente3 = new Cliente();
        cliente3.setNome("Felipe");

        List<Cliente> clientes = Arrays.asList(cliente1, cliente2, cliente3);

        Mockito.when(clienteService.listar()).thenReturn(clientes);

        //Acao

        ResultActions response = mockMvc.perform(get("/"));
        response.andDo(print());

        MvcResult mvcRes = response.andReturn();

        String res = mvcRes.getResponse().getContentAsString();

        //Rules
        Boolean contains1 = res.contains(cliente1.getNome());
        Boolean contains2 = res.contains(cliente2.getNome());
        Boolean contains3 = res.contains(cliente3.getNome());

        //Verification

        response.andExpect(status().isOk());
        Assert.assertTrue(contains1 && contains2 && contains3);
        Mockito.verify(clienteService, Mockito.times(1)).listar();

    }

    @Test
    public void deveRetornarStatusOK_AoBuscarClientePorId() throws Exception {

        //Cenario

        Cliente cliente1 = new Cliente();
        cliente1.setNome("Gaby");
        cliente1.setId(1L);

        Mockito.when(clienteService.pegarPorId(1L)).thenReturn(cliente1);

        //Acao

        ResultActions response = mockMvc.perform(get("/1"));
        response.andDo(print());
        MvcResult mvcResult = response.andReturn();
        String mvcResponse = mvcResult.getResponse().getContentAsString();

        //Rules

        Boolean contentNome = mvcResponse.contains(cliente1.getNome());
        String id = String.valueOf(cliente1.getId());
        Boolean contentId = mvcResponse.contains(id);

        //Verification

        response.andExpect(status().isOk());
        Assert.assertTrue(contentNome && contentId);
        Mockito.verify(clienteService, Mockito.times(1)).pegarPorId(1L);

    }

    @Test
    public void deveRetornarBadRequest_AoBuscarPorIdInexistente() throws Exception {

        //Cenario
        Mockito.when(clienteService.pegarPorId(1L)).thenReturn(null);

        //Acao
        ResultActions response = mockMvc.perform(get("/1"));
        response.andDo(print());
        MvcResult mvcResult = response.andReturn();
        String mvcResponse = mvcResult.getResponse().getContentAsString();

        //Rules
        Boolean empty = mvcResponse.isEmpty();

        //Verification
        response.andExpect(status().isBadRequest());
        Assert.assertTrue(empty);
        Mockito.verify(clienteService, Mockito.times(1)).pegarPorId(1L);
    }

    @Test
    public void deveRetornarStatusOK_AoDeletarCliente() throws Exception {

        //Cenario
        Mockito.when(clienteService.deletar(1L)).thenReturn(true);

        //Acao
        ResultActions response = mockMvc.perform(delete("/1"));
        response.andDo(print());
        MvcResult mvcResult = response.andReturn();
        String mvcString = mvcResult.getResponse().getContentAsString();
        Boolean empty = mvcString.isEmpty();

        //Verification
        response.andExpect(status().isOk());
        Assert.assertTrue(empty);
        Mockito.verify(clienteService, Mockito.times(1)).deletar(1L);
    }

    @Test
    public void deveRetornarBadRequest_AoDeletarClienteInexistente() throws Exception {

        //Cenario
        Mockito.when(clienteService.deletar(1L)).thenReturn(false);

        //Acao
        ResultActions response = mockMvc.perform(delete("/1"));
        response.andDo(print());
        MvcResult mvcResult = response.andReturn();
        String mvcString = mvcResult.getResponse().getContentAsString();
        Boolean empty = mvcString.isEmpty();

        //Verification
        response.andExpect(status().isBadRequest());
        Assert.assertTrue(empty);
        Mockito.verify(clienteService, Mockito.times(1)).deletar(1L);
    }

}
