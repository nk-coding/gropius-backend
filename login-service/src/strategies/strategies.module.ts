import { Module } from "@nestjs/common";
import { JwtModule, JwtService } from "@nestjs/jwt";
import { ModelModule } from "src/model/model.module";
import { ErrorHandlerMiddleware } from "./error-handler.middleware";
import { ModeExtractorMiddleware } from "./mode-extractor.middleware";
import { OauthStrategyService } from "./oauth/oauth.service";
import { PerformAuthFunctionService } from "./perform-auth-function.service";
import { StrategiesController } from "./strategies.controller";
import { StrategiesMiddleware } from "./strategies.middleware";
import { StrategiesService } from "./strategies.service";
import { StrategyUserpassController } from "./userpass/userpass.controller";
import { UserpassStrategyService } from "./userpass/userpass.service";

@Module({
    imports: [
        ModelModule,
        JwtModule.registerAsync({
            useFactory(...args) {
                return {
                    secret:
                        process.env.GROPIUS_LOGIN_SPECIFIC_JWT_SECRET ||
                        "blabla",
                    signOptions: {
                        issuer: process.env.GROPIUS_PASSPORT_STATE_JWT_ISSUER,
                    },
                    verifyOptions: {
                        issuer: process.env.GROPIUS_PASSPORT_STATE_JWT_ISSUER,
                    },
                };
            },
        }),
    ],
    controllers: [StrategiesController, StrategyUserpassController],
    providers: [
        StrategiesService,
        PerformAuthFunctionService,
        UserpassStrategyService,
        OauthStrategyService,
        { provide: "PassportStateJwt", useExisting: JwtService },
        ModeExtractorMiddleware,
        StrategiesMiddleware,
        ErrorHandlerMiddleware,
    ],
    exports: [
        ModeExtractorMiddleware,
        StrategiesMiddleware,
        ErrorHandlerMiddleware,
    ],
})
export class StrategiesModule {}
