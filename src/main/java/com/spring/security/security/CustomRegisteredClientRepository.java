package com.spring.security.security;

import com.spring.security.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

//uma classe de implementação da interface RegisteredClientRepository que é uma interface do authorization server
//que serve para gerenciar os clientes registrados que integarem com o servidor de autorização
// ela busca um client registrado no banco e se existir esse client ele retorna um RegisteredClient

//RegisteredClientRepository essa interface tem 3 metodos para serem implementado
//o metodo save() para salvar o client que foi enocontrado do banco, porem a gente tem uma api propria para isso que salva no banco

//metodo findById() para buscar um client atraves do id dele

//e o metodo findByClientId para buscar atraves do client id do client

@Component //como registrei como um component eu não preciso colocar dentro de uma configuration, o autorization server vai pegar automaticamente essa implementação
@RequiredArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientService clientService;
    private final TokenSettings tokenSettings; //bean para informar o tempo de execussão do token de acesso
    private final ClientSettings clientSettings;

    @Override
    public void save(RegisteredClient registeredClient) {}

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {

        var client = clientService.obterClientPorId(clientId); //busco o client pelo clientId

        if(client == null){ //se estiver nulo quer dizer que não o client não se cadastrou ai eu retorno nulo
            return null;
        }

        //se ele fez o cadastro eu pego esse client e crio um RegisteredClient para informar que esse client esta registrado no servidor de autorização
        return RegisteredClient
                .withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .redirectUri(client.getRedirectURI())
                .scope(client.getScope())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) //para informar qual a forma que o clietn vai se autenticar, atraves do metodo basico que informa client e secret
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) //informo o tipo de concessao para eutenticação, que é o autorization code
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) //e o tipo de concessao client_credentials que é de api para api, ex: postman
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) //adicionario o grant typr de refresh token para informar, que essa aplicação ira usar o fluxo do refresh token
                .tokenSettings(tokenSettings) //configuração de token para informar o tempo de duração de um token
                .clientSettings(clientSettings) //para informar que não vamos trabalhar com tela de concentimento
                .build();
    }
}
