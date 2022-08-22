package gropius.repository.architecture

import gropius.model.architecture.IMSProject
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMSProject]
 */
@Repository
interface IMSProjectRepository : GropiusRepository<IMSProject, String>