package gropius.repository.architecture

import gropius.model.architecture.RelationPartner
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RelationPartner]
 */
@Repository
interface RelationPartnerRepository : ReactiveNeo4jRepository<RelationPartner, String>