import { Module } from "@nestjs/common";
import { ModelModule } from "src/model/model.module";
import { StrategiesModule } from "src/strategies/strategies.module";
import { SyncImsUserController } from "./sync-ims-user.controller";

@Module({
    imports: [ModelModule, StrategiesModule],
    controllers: [SyncImsUserController],
})
export class ApiSyncModule {}
