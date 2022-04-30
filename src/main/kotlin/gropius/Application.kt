package gropius

import graphql.scalars.`object`.JsonScalar
import io.github.graphglue.data.repositories.EnableGraphglueRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
@EnableGraphglueRepositories
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}