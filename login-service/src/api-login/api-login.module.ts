import { Module } from "@nestjs/common";
import { BackendServicesModule } from "src/backend-services/backend-services.module";
import { ModelModule } from "src/model/model.module";
import { CheckRegistrationTokenService } from "./check-registration-token.service";
import { RegisterController } from "./register.controller";

@Module({
    imports: [ModelModule, BackendServicesModule],
    controllers: [RegisterController],
    providers: [CheckRegistrationTokenService],
})
export class ApiLoginModule {}
