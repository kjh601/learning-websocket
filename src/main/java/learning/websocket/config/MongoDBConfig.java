package learning.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("learning.websocket.repository")
@EnableMongoAuditing
public class MongoDBConfig {
}
