import { MiddlewareConsumer, Module } from "@nestjs/common";
import { ModelModule } from "src/model/model.module";
import { StrategiesController } from "./strategies.controller";
import { StrategiesMiddleware } from "./strategies.middleware";
import { StrategiesService } from "./strategies.service";
import { StrategyUserpassController } from "./userpass/userpass.controller";
import { UserpassStrategyService } from "./userpass/userpass.service";

@Module({
    imports: [ModelModule],
    controllers: [StrategiesController, StrategyUserpassController],
    providers: [StrategiesService, UserpassStrategyService],
})
export class StrategiesModule {
    configure(consumer: MiddlewareConsumer) {
        //consumer.apply(StrategiesMiddleware).forRoutes("*/login");
        //consumer.apply(StrategiesMiddleware).forRoutes("*/register");
    }
}
