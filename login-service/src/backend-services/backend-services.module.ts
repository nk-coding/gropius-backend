import { Module } from "@nestjs/common";
import { JwtModule, JwtService } from "@nestjs/jwt";
import { TokenService } from "./token.service";

@Module({
    imports: [
        JwtModule.register({
            secret: process.env.GROPIUS_PASSPORT_STATE_JWT_SECRET,
            signOptions: {
                issuer: process.env.GROPIUS_PASSPORT_STATE_JWT_ISSUER,
            },
            verifyOptions: {
                issuer: process.env.GROPIUS_PASSPORT_STATE_JWT_ISSUER,
            },
        }),
    ],
    providers: [
        TokenService,
        { provide: "BackendJwtService", useExisting: JwtService },
    ],
})
export class BackendServicesModule {}
