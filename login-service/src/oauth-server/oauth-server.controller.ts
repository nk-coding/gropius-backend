import {
    All,
    Body,
    Controller,
    Get,
    HttpException,
    HttpStatus,
    Param,
    Post,
    Res,
} from "@nestjs/common";
import { Response } from "express";
import { TokenService } from "src/backend-services/token.service";
import { ActiveLoginService } from "src/model/services/active-login.service";
import { AuthClientService } from "src/model/services/auth-client.service";
import { AuthStateData } from "src/strategies/AuthResult";
import { ensureState } from "src/strategies/utils";

@Controller("oauth")
export class OauthServerController {
    constructor(
        private readonly authClientService: AuthClientService,
        private readonly activeLoginService: ActiveLoginService,
        private readonly tokenService: TokenService,
    ) {}

    @All(":id?/token/:mode?")
    async token(
        @Param("id") id: string,
        @Res({ passthrough: true }) res: Response,
    ) {
        ensureState(res);
        let activeLogin = (res.locals.state as AuthStateData)?.activeLogin;
        if (typeof activeLogin == "string") {
            activeLogin = await this.activeLoginService.findOneByOrFail({
                id: activeLogin,
            });
        }
        const loginData = await activeLogin.loginInstanceFor;
        if (!loginData) {
            (res.locals.state as AuthStateData).authErrorType =
                "invalid_request";
            (res.locals.state as AuthStateData).authErrorMessage =
                "No Login data for this code. Internal server error";
        }
        const user = await loginData?.user;
        if (!user) {
            //todo: user is not registered, create temporary token and require to register
        } else {
            //todo: user known, issue regular access + refresh token pair
            const accessToken = this.tokenService.signBackendToken(
                user.neo4jId,
                parseInt(
                    process.env.GROPIUS_INTERNAL_BACKEND_JWT_EXPIRATION_TIME_MS,
                    10,
                ),
            );
        }
        return `Token Endpoint for ${id}, active login: ${
            (res.locals.state as AuthStateData)?.activeLogin
        }`;
    }
}
