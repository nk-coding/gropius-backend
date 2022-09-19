import {
    All,
    Controller,
    Get,
    HttpException,
    HttpStatus,
    Param,
} from "@nestjs/common";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { StrategiesService } from "src/model/services/strategies.service";
import { Strategy } from "../../strategies/Strategy";

@Controller()
export class StrategiesController {
    constructor(
        private readonly strategiesService: StrategiesService,
        private readonly strategyInstanceService: StrategyInstanceService,
    ) {}

    @Get("strategy")
    getAllStrategyTypes(): Strategy[] {
        return this.strategiesService.getAllStrategies();
    }

    @Get("strategy/:type")
    getStrategyType(@Param("type") type: string): Strategy {
        if (!this.strategiesService.hasStrategy(type)) {
            throw new HttpException(
                "Strategy type does not exist",
                HttpStatus.NOT_FOUND,
            );
        }
        return this.strategiesService.getStrategyByName(type);
    }

    @Get(":type/instance")
    async getInstancesForType(
        @Param("type") type: string,
    ): Promise<StrategyInstance[]> {
        if (!this.strategiesService.hasStrategy(type)) {
            throw new HttpException(
                "Strategy type does not exist",
                HttpStatus.NOT_FOUND,
            );
        }
        return await this.strategyInstanceService.findBy({ type: type });
    }
}
