package gropius

import io.github.graphglue.data.repositories.EnableGraphglueRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableGraphglueRepositories
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}