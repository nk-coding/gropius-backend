package gropius.sync.github

import gropius.sync.github.repository.RepositoryInfoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestApp {

    /*@Bean
    fun jkl(
        incoming: Incoming,
        repositoryInfoRepository: RepositoryInfoRepository
    ): String {
        Thread {
            runBlocking {
                delay(1000)
                incoming.sync()
            }
        }.start()
        return "vjdsklfsdjklfjskdl"
    }*/
}