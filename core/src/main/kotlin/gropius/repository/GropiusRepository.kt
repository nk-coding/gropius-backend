package gropius.repository

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.data.neo4j.repository.support.ReactiveCypherdslConditionExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface GropiusRepository<T, ID> : ReactiveNeo4jRepository<T, ID>, ReactiveCypherdslConditionExecutor<T>