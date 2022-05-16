package com.example.demo.services;

import com.example.demo.models.Cliente;
import com.example.demo.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente){

        return clienteRepository.save(cliente);


    }

    @Transactional
    public List<Cliente> listar(){

        List<Cliente> response = clienteRepository.findAll();

        if(response == null){
            return null;
        }

        return response;
    }

    @Transactional
    public Cliente pegarPorId(Long id){

        Optional<Cliente> clienteOptional = clienteRepository.findById(id);

        if(clienteOptional.isPresent()){
            ResponseEntity<Cliente> clienteResponseEntity = ResponseEntity.of(clienteOptional);
            Cliente cliente = clienteResponseEntity.getBody();

            return cliente;
        }

        return null;

    }

    @Transactional
    public Boolean deletar(Long id){

        Boolean exist = clienteRepository.existsById(id);

        if(exist == false){
            return false;
        }

        clienteRepository.deleteById(id);

        return true;
    }

}
