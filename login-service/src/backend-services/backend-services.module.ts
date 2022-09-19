import { Module } from "@nestjs/common";
import { JwtModule, JwtService } from "@nestjs/jwt";
import { ModelModule } from "src/model/model.module";
import { TokenService } from "./token.service";
import { BackendUserService } from "./backend-user.service";
import { ImsUserFindingService } from "./ims-user-finding.service";
import { StrategiesModule } from "src/strategies/strategies.module";

@Module({
    imports: [
        JwtModule.registerAsync({
            useFactory(...args) {
                return {
                    secret: process.env.GROPIUS_INTERNAL_BACKEND_JWT_SECRET,
                    signOptions: {
                        issuer: process.env.GROPIUS_JWT_ISSUER,
                        audience: ["backend", "login"],
                    },
                    verifyOptions: {
                        issuer: process.env.GROPIUS_JWT_ISSUER,
                        audience: "login",
                    },
                };
            },
        }),
        ModelModule,
        StrategiesModule,
    ],
    providers: [
        { provide: "BackendJwtService", useExisting: JwtService },
        TokenService,
        BackendUserService,
        ImsUserFindingService,
    ],
    exports: [TokenService, BackendUserService, ImsUserFindingService],
})
export class BackendServicesModule {}
