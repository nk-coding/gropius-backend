package gropius.repository.architecture

import gropius.model.architecture.IMS
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMS]
 */
@Repository
interface IMSRepository : GropiusRepository<IMS, String>