package br.com.mapfood.config;


import br.com.mapfood.domain.Cliente;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    //Abrir a conection com o Redis
    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }

    //Template para salvar cliente no redis
    @Bean
    public RedisTemplate<String, Cliente> clienteRedisTemplate(RedisConnectionFactory connectionFactory) {
        return redisTemplate(connectionFactory,Cliente.class);
    }

    //Instanciando template
    public <T>RedisTemplate<String, T> redisTemplate(RedisConnectionFactory connectionFactory, Class<T> type) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<T>(type	));
        return template;

    }
}
