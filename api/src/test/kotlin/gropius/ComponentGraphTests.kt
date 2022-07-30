package gropius

import gropius.service.architecture.ComponentService
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
class ComponentGraphTests {

    @Autowired
    lateinit var componentService: ComponentService

    @Test
    fun getComponentsTest(): Unit = runBlocking {
        val components = componentService.repository.findAll().collectList().awaitSingle()
        throw IllegalStateException()
    }

    @Test
    fun workYouPieceOfShit() {
        throw IllegalArgumentException()
    }

}