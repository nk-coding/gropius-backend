package gropius

import gropius.sync.github.Incoming
import gropius.sync.github.SyncSelector
import io.github.graphglue.data.repositories.EnableGraphglueRepositories
import kotlinx.coroutines.runBlocking
import org.neo4j.driver.Driver
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.data.neo4j.core.ReactiveDatabaseSelectionProvider
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager
import kotlin.system.exitProcess

/**
 * @param incoming Reference for the spring instance of Incoming
 * @param configurableApplicationContext Reference for the spring instance of ConfigurableApplicationContext
 */
@Configuration
class SyncApplication(
    val syncSelector: SyncSelector, val configurableApplicationContext: ConfigurableApplicationContext
) {
    /**
     * Necessary transaction manager
     *
     * @param driver used Neo4j driver
     * @param databaseNameProvider Neo4j database provider
     * @return the generated transaction manager
     */
    @Bean
    fun reactiveTransactionManager(
        driver: Driver, databaseNameProvider: ReactiveDatabaseSelectionProvider
    ): ReactiveNeo4jTransactionManager {
        return ReactiveNeo4jTransactionManager(driver, databaseNameProvider)
    }

    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {
        configurableApplicationContext.use {
            runBlocking {
                syncSelector.sync()
            }
        }
        exitProcess(0)
    }
}

@SpringBootApplication
@EnableGraphglueRepositories
@EnableReactiveMongoRepositories
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
