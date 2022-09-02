import { Injectable } from "@nestjs/common";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { StrategiesService } from "../strategies.service";
import { Strategy } from "../Strategy";

@Injectable()
export class UserpassStrategyService extends Strategy {
    constructor(
        strategiesService: StrategiesService,
        strategyInstanceService: StrategyInstanceService,
    ) {
        super("userpass", strategyInstanceService, true, false);
        strategiesService.addStrategy("userpass", this);
    }

    protected checkInstanceConfig(instanceConfig: object): boolean {
        return Object.keys(instanceConfig).length === 0;
    }
}
