package gropius.repository.architecture

import gropius.model.architecture.IntraComponentDependencyParticipant
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IntraComponentDependencyParticipant]
 */
@Repository
interface IntraComponentDependencyParticipantRepository :
    GropiusRepository<IntraComponentDependencyParticipant, String>