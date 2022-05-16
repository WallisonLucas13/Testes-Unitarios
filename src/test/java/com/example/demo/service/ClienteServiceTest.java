package com.example.demo.service;


import com.example.demo.models.Cliente;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.services.ClienteService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    public void deveRetornarOk_AoSalvarCliente(){
        //Cenario
        Cliente cliente = new Cliente();
        cliente.setNome("Jenny");
        cliente.setId(1L);
        Mockito.when(clienteRepository.save(cliente)).thenReturn(cliente);

        //Acao
        Cliente response = clienteService.salvar(cliente);

        //Verification
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getNome(), cliente.getNome());
        Assert.assertEquals(response.getId(), cliente.getId());
        Mockito.verify(clienteRepository, Mockito.times(1)).save(cliente);
    }

    @Test
    public void deveRetornarListaDeClientes_AoPedirLista(){
        //Cenario
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        Cliente cliente3 = new Cliente();

        List<Cliente> clientes = asList(cliente1, cliente2, cliente3);

        Mockito.when(clienteRepository.findAll()).thenReturn(clientes);

        //Acao
        List<Cliente> response = clienteService.listar();

        //Verification
        Assert.assertNotNull(response);
        Assert.assertEquals(clientes, response);
        Mockito.verify(clienteRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void deveRetornarCliente_AoBuscarPorId(){
        //Cenario
        Cliente cliente = new Cliente();
        cliente.setNome("Gaby");
        cliente.setId(1L);

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        //Acao
        Cliente response = clienteService.pegarPorId(cliente.getId());

        //Verification
        Assert.assertNotNull(response);
        Assert.assertEquals(cliente, response);
        Assert.assertEquals(cliente.getNome(), response.getNome());
        Assert.assertEquals(cliente.getId(), response.getId());
        Mockito.verify(clienteRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void deveRetornarNull_AoBuscarPorClienteInexistente(){
        //Cenario
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        //Acao
        Cliente response = clienteService.pegarPorId(1L);

        //Verification
        Assert.assertNull(response);
        Mockito.verify(clienteRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void deveRetornarTrue_AoDeletarCliente(){
        //Cenario
        Mockito.when(clienteRepository.existsById(1L)).thenReturn(true);

        //Acao
        Boolean response = clienteService.deletar(1L);

        //Verification
        Assert.assertTrue(response);
        Mockito.verify(clienteRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void deveRetornarFalse_AoDeletarClienteInexistente(){
        //Cenario
        Mockito.when(clienteRepository.existsById(1L)).thenReturn(false);

        //Acao
        Boolean response = clienteService.deletar(1L);

        //Verification
        Assert.assertFalse(response);
        Mockito.verify(clienteRepository, Mockito.times(0)).deleteById(1L);
    }
}
