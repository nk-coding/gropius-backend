import {
    CanActivate,
    ExecutionContext,
    HttpException,
    HttpStatus,
    Injectable,
    UnauthorizedException,
} from "@nestjs/common";
import { Request, Response } from "express";
import { Observable } from "rxjs";
import { TokenService } from "src/backend-services/token.service";
import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";
import { LoginState, UserLoginData } from "src/model/postgres/UserLoginData";
import { ActiveLoginService } from "src/model/services/active-login.service";
import { UserLoginDataService } from "src/model/services/user-login-data.service";

@Injectable()
export class CheckRegistrationTokenService {
    constructor(
        private readonly tokenService: TokenService,
        private readonly loginDataService: UserLoginDataService,
        private readonly activeLoginService: ActiveLoginService,
    ) {}

    async getActiveLoginAndLoginDataForToken(
        token: string | undefined | null,
        userMustBe?: LoginUser | undefined,
    ): Promise<{ loginData: UserLoginData; activeLogin: ActiveLogin }> {
        if (!token) {
            throw new UnauthorizedException(
                undefined,
                "No registration token given",
            );
        }
        let activeLoginId: string;
        try {
            activeLoginId = await this.tokenService.verifyRegistrationToken(
                token,
            );
        } catch (err) {
            console.error("Invalid registration token: ", err);
            throw new UnauthorizedException(
                undefined,
                "Invalid registration token: " + (err.message ?? err),
            );
        }
        const activeLogin = await this.activeLoginService.findOneBy({
            id: activeLoginId,
        });
        if (!activeLogin) {
            console.error(
                `No active login with id from token; id:`,
                activeLoginId,
            );
            throw new UnauthorizedException(
                undefined,
                "Register token is (no longer) valid.",
            );
        }
        const loginData = await activeLogin.loginInstanceFor;
        if (!loginData) {
            console.error(`No login data for active login; id:`, activeLoginId);
            throw new UnauthorizedException(
                undefined,
                "Register token is (no longer) valid.",
            );
        }
        if (
            (loginData.expires != null && loginData.expires <= new Date()) ||
            (activeLogin.expires != null &&
                activeLogin.expires <= new Date()) ||
            !activeLogin.isValid
        ) {
            throw new UnauthorizedException(
                undefined,
                "Login has expired. Registration did not happen time",
            );
        }
        if (loginData.state !== LoginState.WAITING_FOR_REGISTER) {
            console.error(
                "State is not waiting for register of login data",
                loginData.id,
            );
            throw new UnauthorizedException(
                undefined,
                "A user is already registered for this login",
            );
        }
        const loginDataUser = await loginData.user;
        if (!!loginDataUser) {
            if (userMustBe) {
                if (loginDataUser.id !== userMustBe.id) {
                    console.error(
                        "User already esists and is not the user that currently tries to link for login data, user",
                        loginData.id,
                        userMustBe.id,
                    );
                    throw new UnauthorizedException(
                        undefined,
                        "This account is already registered and linked to a Gropius account",
                    );
                } else {
                    // ok. required user matches login data user
                }
            } else {
                console.error(
                    "User already esists for login data",
                    loginData.id,
                );
                throw new UnauthorizedException(
                    undefined,
                    "A user is already registered for this login",
                );
            }
        }

        return { loginData, activeLogin };
    }
}
