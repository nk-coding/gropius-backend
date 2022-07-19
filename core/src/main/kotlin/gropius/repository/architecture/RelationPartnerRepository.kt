package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.RelationPartner

/**
 * Repository for [RelationPartner]
 */
@Repository
interface RelationPartnerRepository : ReactiveNeo4jRepository<RelationPartner, String>