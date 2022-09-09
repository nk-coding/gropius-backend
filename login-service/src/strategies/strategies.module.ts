import { MiddlewareConsumer, Module } from "@nestjs/common";
import { ModelModule } from "src/model/model.module";
import { ErrorHandlerMiddleware } from "./error-handler.middleware";
import { ModeExtractorMiddleware } from "./mode-extractor.middleware";
import { OauthAutorizeMiddleware } from "./oauth-server/oauth-autorize.middleware";
import { OauthRedirectMiddleware } from "./oauth-server/oauth-redirect.middleware";
import { OauthServerController } from "./oauth-server/oauth-server.controller";
import { OauthStrategyService } from "./oauth/oauth.service";
import { PerformAuthFunctionService } from "./perform-auth-function.service";
import { StrategiesController } from "./strategies.controller";
import { StrategiesMiddleware } from "./strategies.middleware";
import { StrategiesService } from "./strategies.service";
import { StrategyUserpassController } from "./userpass/userpass.controller";
import { UserpassStrategyService } from "./userpass/userpass.service";

@Module({
    imports: [ModelModule],
    controllers: [
        StrategiesController,
        StrategyUserpassController,
        OauthServerController,
    ],
    providers: [
        StrategiesService,
        UserpassStrategyService,
        OauthStrategyService,
        PerformAuthFunctionService,
    ],
})
export class StrategiesModule {
    configure(consumer: MiddlewareConsumer) {
        //consumer.apply(StrategiesMiddleware).forRoutes("*/login");
        //consumer.apply(StrategiesMiddleware).forRoutes("*/register");
        consumer
            .apply(
                ModeExtractorMiddleware,
                OauthAutorizeMiddleware,
                StrategiesMiddleware,
                OauthRedirectMiddleware,
                ErrorHandlerMiddleware, // This middleware should never be reached as the oauth middleware should already care about it, its just to make absolutely sure, no unauthorized request gets through
            )
            .forRoutes("strategy/oauth/:id/authorize/:mode?");
        consumer
            .apply(
                StrategiesMiddleware,
                OauthRedirectMiddleware,
                ErrorHandlerMiddleware,
            )
            .forRoutes("strategy/oauth/:id/callback");
        consumer
            .apply(
                ModeExtractorMiddleware,
                StrategiesMiddleware,
                ErrorHandlerMiddleware, // This middleware should never be reached as the oauth middleware should already care about it, its just to make absolutely sure, no unauthorized request gets through
            )
            .forRoutes("strategy/post/:id/:mode?");
    }
}