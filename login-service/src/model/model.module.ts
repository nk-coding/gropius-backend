import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { GraphqlService } from "./graphql/graphql.service";
import { ActiveLogin } from "./postgres/ActiveLogin";
import { AuthClient } from "./postgres/AuthClient";
import { LoginUser } from "./postgres/LoginUser";
import { StrategyInstance } from "./postgres/StrategyInstance";
import { UserLoginData } from "./postgres/UserLoginData";
import { UserLoginDataImsUser } from "./postgres/UserLoginDataImsUser";
import { ActiveLoginService } from "./services/active-login.service";
import { AuthClientService } from "./services/auth-client.service";
import { LoginUserService } from "./services/login-user.service";
import { StrategiesService } from "./services/strategies.service";
import { StrategyInstanceService } from "./services/strategy-instance.service";
import { UserLoginDataImsUserService } from "./services/user-login-data-ims-user";
import { UserLoginDataService } from "./services/user-login-data.service";

const modelModuleExportedServices = [
    StrategiesService,
    LoginUserService,
    StrategyInstanceService,
    UserLoginDataService,
    UserLoginDataImsUserService,
    ActiveLoginService,
    AuthClientService,
    GraphqlService,
];

@Module({
    imports: [
        TypeOrmModule.forFeature([
            LoginUser,
            ActiveLogin,
            StrategyInstance,
            UserLoginData,
            UserLoginDataImsUser,
            AuthClient,
        ]),
    ],
    providers: modelModuleExportedServices,
    exports: modelModuleExportedServices,
})
export class ModelModule {}
