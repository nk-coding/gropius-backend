package gropius

import gropius.sync.github.Incoming
import io.github.graphglue.data.repositories.EnableGraphglueRepositories
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

/**
 * @param incoming Reference for the spring instance of Incoming
 * @param configurableApplicationContext Reference for the spring instance of ConfigurableApplicationContext
 */
@Configuration
class SyncApplication(
    val incoming: Incoming, val configurableApplicationContext: ConfigurableApplicationContext
) {
    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {
        configurableApplicationContext.use {
            runBlocking {
                incoming.sync()
            }
        }
    }
}

@SpringBootApplication
@EnableGraphglueRepositories
@EnableReactiveMongoRepositories
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
