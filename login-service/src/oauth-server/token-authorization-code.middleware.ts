import {
    HttpException,
    HttpStatus,
    Injectable,
    NestMiddleware,
} from "@nestjs/common";
import { Request, Response } from "express";
import { TokenService } from "src/backend-services/token.service";
import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { ActiveLoginService } from "src/model/services/active-login.service";
import { AuthStateData } from "src/strategies/AuthResult";
import { ensureState } from "src/strategies/utils";
import { OauthServerStateData } from "./oauth-autorize.middleware";

@Injectable()
export class TokenAuthorizationCodeMiddleware implements NestMiddleware {
    constructor(
        private readonly activeLoginService: ActiveLoginService,
        private readonly tokenService: TokenService,
    ) {}

    private throwGenericCodeError(res: Response, next: () => void) {
        (res.locals.state as AuthStateData).authErrorMessage =
            "Given code was invalid or expired";
        (res.locals.state as AuthStateData).authErrorType = "invalid_request";
        return next();
    }

    async use(req: Request, res: Response, next: () => void) {
        ensureState(res);
        let tokenData: {
            activeLoginId: string;
            clientId: string;
            uniqueId: string;
        };
        const currentClient = (res.locals.state as OauthServerStateData).client;
        if (!currentClient) {
            console.error("No client logged in");
            (res.locals.state as AuthStateData).authErrorMessage =
                "Client unknown or unauthorized";
            (res.locals.state as AuthStateData).authErrorType =
                "invalid_client";
            return;
        }
        try {
            tokenData = await this.tokenService.verifyActiveLoginToken(
                req.body.code,
                currentClient.id,
            );
        } catch (err) {
            console.error(err);
            return this.throwGenericCodeError(res, next);
        }
        const activeLogin = await this.activeLoginService.findOneBy({
            id: tokenData.activeLoginId,
        });
        if (!activeLogin) {
            console.error("No active login with id", tokenData.activeLoginId);
            return this.throwGenericCodeError(res, next);
        }
        const activeLoginClient = await activeLogin.createdByClient;
        if (activeLoginClient.id !== currentClient.id) {
            console.error(
                "Active login was not created by current client",
                tokenData.activeLoginId,
            );
            return this.throwGenericCodeError(res, next);
        }
        if (activeLogin.expires > new Date()) {
            console.error("Active login is expired", tokenData.activeLoginId);
            return this.throwGenericCodeError(res, next);
        }
        const codeUniqueId = parseInt(tokenData.uniqueId, 10);
        if (
            !isFinite(codeUniqueId) ||
            codeUniqueId !== activeLogin.nextExpectedRefreshTokenNumber
        ) {
            //todo: maybe make login and all tokens invalid on reuse of code as it has the same consequence as reusing refresh tokens
            // or rather: merge code and refresh token as it is essentially the same functionality
            console.error(
                "Code is no longer valid as it was already used and a token was already generated",
                tokenData.activeLoginId,
            );
            return this.throwGenericCodeError(res, next);
        }
        (res.locals.state as AuthStateData).activeLogin = activeLogin;
        next();
    }
}
