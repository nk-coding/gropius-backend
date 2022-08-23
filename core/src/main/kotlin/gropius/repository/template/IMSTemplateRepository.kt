package gropius.repository.template

import gropius.model.template.IMSTemplate
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMSTemplate]
 */
@Repository
interface IMSTemplateRepository : GropiusRepository<IMSTemplate, String>