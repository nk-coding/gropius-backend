import { Inject, Injectable } from "@nestjs/common";
import { JwtService } from "@nestjs/jwt";
import { JsonWebTokenError } from "jsonwebtoken";
import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { ActiveLoginService } from "src/model/services/active-login.service";

@Injectable()
export class TokenService {
    constructor(
        @Inject("BackendJwtService")
        private readonly backendJwtService: JwtService,
        private readonly activeLoginService: ActiveLoginService,
    ) {}

    signBackendToken(neo4JUserId: string, expiresIn: number): string {
        return this.backendJwtService.sign(
            {},
            { subject: neo4JUserId, expiresIn, audience: ["login", "backend"] },
        );
    }

    async signActiveLoginCode(
        activeLoginId: string,
        clientId: string,
        expiresIn: number,
        uniqueId: string | number,
    ): Promise<string> {
        const activeLogin = await this.activeLoginService.findOneBy({
            id: activeLoginId,
        });
        if (!activeLogin) {
            throw new Error("Login doesn't exist");
        }
        if (
            activeLogin.nextExpectedRefreshTokenNumber !=
            ActiveLogin.LOGGED_IN_BUT_TOKEN_NOT_YET_RETRIVED
        ) {
            throw new Error(
                "A login code can't be retrieved for a login for which a token was already created",
            );
        }
        return await this.backendJwtService.signAsync(
            {
                loginId: activeLogin.id,
            },
            {
                subject: clientId,
                expiresIn,
                jwtid: uniqueId.toString(),
                secret: process.env.GROPIUS_LOGIN_SPECIFIC_JWT_SECRET,
                audience: ["token"], // = token retrieval (valid for getting a (new) access token, e.g. oauth code or refresh token)
            },
        );
    }

    async verifyActiveLoginToken(
        token: string,
        requiredClientId: string,
    ): Promise<{ activeLoginId: string; clientId: string; uniqueId: string }> {
        const payload = await this.backendJwtService.verifyAsync(token, {
            secret: process.env.GROPIUS_LOGIN_SPECIFIC_JWT_SECRET,
            audience: ["token"],
            subject: requiredClientId,
        });
        if (!payload.loginId) {
            throw new JsonWebTokenError(
                "Active login token (code) doesn't contain an id",
            );
        }
        const activeLogin = await this.activeLoginService.findOneBy({
            id: payload.loginId,
        });
        if (!activeLogin) {
            throw new JsonWebTokenError("Invalid login id");
        }
        const tokenJti = parseInt(payload.jti, 10);
        if (
            !isFinite(tokenJti) ||
            tokenJti !== activeLogin.nextExpectedRefreshTokenNumber
        ) {
            throw new JsonWebTokenError("Code has already been used");
        }
        return {
            activeLoginId: payload.loginId,
            clientId: payload.sub,
            uniqueId: payload.jti,
        };
    }
}
