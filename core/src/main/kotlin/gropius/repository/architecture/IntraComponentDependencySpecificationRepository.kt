package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.IntraComponentDependencySpecification

/**
 * Repository for [IntraComponentDependencySpecification]
 */
@Repository
interface IntraComponentDependencySpecificationRepository :
    ReactiveNeo4jRepository<IntraComponentDependencySpecification, String>