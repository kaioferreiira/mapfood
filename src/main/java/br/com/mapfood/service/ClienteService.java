package br.com.mapfood.service;

import br.com.mapfood.domain.Cliente;
import br.com.mapfood.domain.ClienteRedis;
import br.com.mapfood.processors.ClienteProcessor;
import br.com.mapfood.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteProcessor clienteProcessor;

    @Autowired
    private RedisTemplate<String, Cliente> redisTemplates;

    Cliente cliente = new Cliente();
    Cliente clienteRedis;

    public void processarFileCliente(){
        List<Cliente> listCliente = clienteProcessor.lerArquivo();
        clienteRepository.saveAll(listCliente);
    }


    public List<Cliente> findAll(){
        return clienteRepository.findAll();
    }

    public Cliente findById(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);

        cliente.setId(clienteOptional.get().getId());
        cliente.setLatitude(clienteOptional.get().getLatitude());
        cliente.setLongitude(clienteOptional.get().getLongitude());

        if(cliente == null) {
            System.out.println("não encontrado!");
        } else {
            clienteRedis = redisTemplates.opsForValue().get("bairro:" + cliente.getId());

            if(clienteRedis == null ) {
                System.out.println("Salvando no redis. ");

                redisTemplates.opsForValue().set("bairro:" + cliente.getId(), clienteRedis);

                System.out.println("Bairro salvo!");
            }else {
                System.out.println("Bairro já se encontra no Redis.");
            }
        }
        return cliente;
    }


    public ClienteRedis getInstance(Optional<Cliente> clienteRepozitorio) {
        ClienteRedis redisCliente = new ClienteRedis();
        redisCliente.setId(clienteRepozitorio.get().getId());
        redisCliente.setLongitude(clienteRepozitorio.get().getLongitude());
        redisCliente.setLatitude(clienteRepozitorio.get().getLatitude());
        return redisCliente;
    }

}
