package gropius.repository.architecture

import gropius.model.architecture.IntraComponentDependencyParticipant
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IntraComponentDependencyParticipant]
 */
@Repository
interface IntraComponentDependencyParticipantRepository :
    ReactiveNeo4jRepository<IntraComponentDependencyParticipant, String>