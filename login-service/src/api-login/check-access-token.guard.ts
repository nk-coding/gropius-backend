import {
    CanActivate,
    ExecutionContext,
    Injectable,
    SetMetadata,
    UnauthorizedException,
} from "@nestjs/common";
import { Reflector } from "@nestjs/core";
import { Request, Response } from "express";
import { Observable } from "rxjs";
import { BackendUserService } from "src/backend-services/backend-user.service";
import { TokenService } from "src/backend-services/token.service";
import { GraphqlService } from "src/model/graphql/graphql.service";
import { LoginUser } from "src/model/postgres/LoginUser.entity";
import {
    LoginState,
    UserLoginData,
} from "src/model/postgres/UserLoginData.entity";
import { UserLoginDataService } from "src/model/services/user-login-data.service";
import { ensureState } from "src/strategies/utils";
import { ApiStateData } from "./ApiStateData";

export const NeedsAdmin = () => SetMetadata("needsAdmin", true);

/**
 * Guard for checking the presence of an access token in the request.
 * If needed, checks for admin permissions of the user.
 *
 * Used with `@UseGuards(CheckAccessTokenGuard)`
 *
 * The access token is expected in the "Authorization" header, prefixed with "Bearer ".
 * Not providing a token, a token without prefix or an invalid token will result in a 401 Unauthorized response.
 *
 * Once access token (and admin permission) were verified sucessfully,
 * the logged in user is written to the request state object
 */
@Injectable()
export class CheckAccessTokenGuard implements CanActivate {
    constructor(
        private readonly tokenService: TokenService,
        private readonly reflector: Reflector,
        private readonly backendUserService: BackendUserService,
    ) {}

    async canActivate(context: ExecutionContext): Promise<boolean> {
        const req = context.switchToHttp().getRequest<Request>();
        const authHead = req?.headers?.authorization;
        if (!authHead || authHead.length == 0) {
            throw new UnauthorizedException(
                undefined,
                "Authorization header is empty",
            );
        }
        if (!authHead.toLowerCase().startsWith("bearer ")) {
            throw new UnauthorizedException(
                undefined,
                "Only accepting Bearer authorization",
            );
        }
        const token = authHead.substring(7).trim();
        let user: LoginUser;
        try {
            user = (await this.tokenService.verifyAccessToken(token)).user;
        } catch (err) {
            console.log("Invalid access token:", err);
            throw new UnauthorizedException(
                undefined,
                "Invalid access token: " + (err.message ?? err),
            );
        }
        if (!user) {
            console.error("No user based on token");
            return false;
        }

        const needsAdmin =
            this.reflector.get<boolean>("needsAdmin", context.getHandler()) ??
            false;
        console.log("Request needs admin:", needsAdmin);
        if (needsAdmin) {
            if (!this.backendUserService.checkIsUserAdmin(user)) {
                return false;
            }
        }

        const res = context.switchToHttp().getResponse<Response>();
        ensureState(res);
        (res.locals.state as ApiStateData).loggedInUser = user;
        return true;
    }
}
