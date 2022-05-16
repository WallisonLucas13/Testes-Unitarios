package com.example.demo.controllers;

import com.example.demo.models.Cliente;
import com.example.demo.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity save(@RequestBody Cliente cliente){

        if(cliente == null){
            return ResponseEntity.badRequest().build();
        }

        clienteService.salvar(cliente);

        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity list(){

        List<Cliente> clientes = clienteService.listar();

        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity pegarCliente(@PathVariable("id") Long id){

        Cliente cliente = clienteService.pegarPorId(id);

        if(cliente == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){

        Boolean response = clienteService.deletar(id);

        if(response == false){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
