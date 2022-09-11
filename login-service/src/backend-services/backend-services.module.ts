import { Module } from "@nestjs/common";
import { JwtModule, JwtService } from "@nestjs/jwt";
import { ModelModule } from "src/model/model.module";
import { TokenService } from "./token.service";

@Module({
    imports: [
        JwtModule.register({
            secret: process.env.GROPIUS_INTERNAL_BACKEND_JWT_SECRET,
            signOptions: {
                issuer: process.env.GROPIUS_JWT_ISSUER,
                audience: ["backend", "login"],
            },
            verifyOptions: {
                issuer: process.env.GROPIUS_JWT_ISSUER,
                audience: "login",
            },
        }),
        ModelModule,
    ],
    providers: [
        { provide: "BackendJwtService", useExisting: JwtService },
        TokenService,
    ],
    exports: [TokenService],
})
export class BackendServicesModule {}
