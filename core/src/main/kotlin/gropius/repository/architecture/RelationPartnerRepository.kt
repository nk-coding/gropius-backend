package gropius.repository.architecture

import gropius.model.architecture.RelationPartner
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RelationPartner]
 */
@Repository
interface RelationPartnerRepository : GropiusRepository<RelationPartner, String>