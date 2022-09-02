import { MiddlewareConsumer, Module } from "@nestjs/common";
import { ModelModule } from "src/model/model.module";
import { StrategiesController } from "./strategies.controller";
import { StrategiesService } from "./strategies.service";
import { StrategyUserpassController } from "./userpass/userpass.controller";
import { UserpassStrategyService } from "./userpass/userpass.service";

@Module({
    imports: [ModelModule],
    controllers: [StrategiesController, StrategyUserpassController],
    providers: [StrategiesService, UserpassStrategyService],
})
export class StrategiesModule {}
