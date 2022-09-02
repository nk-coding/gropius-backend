import { Module } from "@nestjs/common";
import { ConfigModule } from "@nestjs/config";
import { RouterModule } from "@nestjs/core";
import { TypeOrmModule } from "@nestjs/typeorm";
import { ApiLoginModule } from "./api-login/api-login.module";
import { ApiSyncModule } from "./api-sync/api-sync.module";
import { AppController } from "./app.controller";
import { ModelModule } from "./model/model.module";
import { StrategiesModule } from "./strategies/strategies.module";

@Module({
    imports: [
        ConfigModule.forRoot({
            envFilePath:
                process.env.NODE_ENV === "production"
                    ? [".env.prod.local", ".env.prod"]
                    : [".env.dev.local", ".env.dev"],
        }),
        TypeOrmModule.forRoot({
            type: "postgres",
            host: process.env.GROPIUS_LOGIN_DATABASE_HOST ?? "localhost",
            port: parseInt(process.env.GROPIUS_LOGIN_DATABASE_PORT, 10) || 5432, // note: using || instead of ?? to catch NaN
            username: process.env.GROPIUS_LOGIN_DATABASE_USER ?? "gropius",
            password: process.env.GROPIUS_LOGIN_DATABASE_PASSWORD ?? "gropius",
            database: process.env.GROPIUS_LOGIN_DATABASE_DATABASE ?? "gropius",
            synchronize: process.env.NODE_ENV !== "production",
            autoLoadEntities: true,
        }),
        ModelModule,
        ApiLoginModule,
        ApiSyncModule,
        StrategiesModule,
        RouterModule.register([
            { path: "login", module: ApiLoginModule },
            { path: "syncApi", module: ApiSyncModule },
            { path: "strategy", module: StrategiesModule },
        ]),
    ],
    controllers: [AppController],
    providers: [],
})
export class AppModule {}
