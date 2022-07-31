package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.IntraComponentDependencyParticipant

/**
 * Repository for [IntraComponentDependencyParticipant]
 */
@Repository
interface IntraComponentDependencyParticipantRepository :
    ReactiveNeo4jRepository<IntraComponentDependencyParticipant, String>