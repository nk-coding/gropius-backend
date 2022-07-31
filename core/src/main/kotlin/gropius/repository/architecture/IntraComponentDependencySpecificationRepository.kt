package gropius.repository.architecture

import gropius.model.architecture.IntraComponentDependencySpecification
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IntraComponentDependencySpecification]
 */
@Repository
interface IntraComponentDependencySpecificationRepository :
    ReactiveNeo4jRepository<IntraComponentDependencySpecification, String>