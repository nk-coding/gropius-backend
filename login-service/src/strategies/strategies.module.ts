import { MiddlewareConsumer, Module } from "@nestjs/common";
import { ModelModule } from "src/model/model.module";
import { OauthAutorizeMiddleware } from "./oauth-server/oauth-autorize.middleware";
import { OauthRedirectMiddleware } from "./oauth-server/oauth-redirect.middleware";
import { OauthServerController } from "./oauth-server/oauth-server.controller";
import { OauthStrategyService } from "./oauth/oauth.service";
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
    ],
})
export class StrategiesModule {
    configure(consumer: MiddlewareConsumer) {
        //consumer.apply(StrategiesMiddleware).forRoutes("*/login");
        //consumer.apply(StrategiesMiddleware).forRoutes("*/register");
        consumer
            .apply(OauthAutorizeMiddleware)
            .forRoutes("strategy/oauth/:id/authorize")
            .apply(StrategiesMiddleware)
            .forRoutes("strategy/oauth/:id/authorize")
            .apply(OauthRedirectMiddleware)
            .forRoutes("strategy/oauth/:id/authorize");
        consumer
            .apply(StrategiesMiddleware)
            .forRoutes("strategy/oauth/:id/callback")
            .apply(OauthRedirectMiddleware)
            .forRoutes("strategy/oauth/:id/authorize");
    }
}
