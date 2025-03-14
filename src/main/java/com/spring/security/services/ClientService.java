package com.spring.security.services;

import com.spring.security.models.Client;
import com.spring.security.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    public Client salvar(Client client){
        return repository.save(client);
    }

    public Client obterClientPorId(String clientId){
         return repository.findByClientId(clientId);
    }
}
