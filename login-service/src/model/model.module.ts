import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { GraphqlService } from "./graphql/graphql.service";
import { LoginUser } from "./postgres/LoginUser";
import { StrategyInstance } from "./postgres/StrategyInstance";
import { UserLoginData } from "./postgres/UserLoginData";
import { UserLoginDataImsUser } from "./postgres/UserLoginDataImsUser";
import { LoginUserService } from "./services/login-user.service";
import { StrategyInstanceService } from "./services/strategy-instance.service";
import { UserLoginDataImsUserService } from "./services/user-login-data-ims-user";
import { UserLoginDataService } from "./services/user-login-data.service";

const modelModuleExportedServices = [
    GraphqlService,
    LoginUserService,
    StrategyInstanceService,
    UserLoginDataService,
    UserLoginDataImsUserService,
];

@Module({
    imports: [
        TypeOrmModule.forFeature([
            LoginUser,
            StrategyInstance,
            UserLoginData,
            UserLoginDataImsUser,
        ]),
    ],
    providers: modelModuleExportedServices,
    exports: modelModuleExportedServices,
})
export class ModelModule {}
