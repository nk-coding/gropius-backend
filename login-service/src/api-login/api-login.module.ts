import { Module } from "@nestjs/common";
import { BackendServicesModule } from "src/backend-services/backend-services.module";
import { ModelModule } from "src/model/model.module";
import { StrategiesModule } from "src/strategies/strategies.module";
import { AuthClientController } from "./auth-clients.controller";
import { CheckRegistrationTokenService } from "./check-registration-token.service";
import { RegisterController } from "./register.controller";
import { StrategiesController } from "./strategy/strategies.controller";
import { StrategyInstancesController } from "./strategy/strategy-instances.controller";
import { UsersController } from "./users.controller";

@Module({
    imports: [ModelModule, BackendServicesModule, StrategiesModule],
    controllers: [
        RegisterController,
        UsersController,
        StrategiesController,
        StrategyInstancesController,
        AuthClientController,
    ],
    providers: [CheckRegistrationTokenService],
})
export class ApiLoginModule {}
