import {
    All,
    Body,
    Controller,
    Get,
    HttpException,
    HttpStatus,
    Post,
    Res,
    SetMetadata,
    UseGuards,
} from "@nestjs/common";
import { Response } from "express";
import { BackendUserService } from "src/backend-services/backend-user.service";
import { TokenService } from "src/backend-services/token.service";
import { defaultReturn } from "src/defaultReturn";
import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";
import { LoginState, UserLoginData } from "src/model/postgres/UserLoginData";
import { ActiveLoginService } from "src/model/services/active-login.service";
import { LoginUserService } from "src/model/services/login-user.service";
import { UserLoginDataService } from "src/model/services/user-login-data.service";
import { ApiStateData } from "./ApiStateData";
import { CheckAccessTokenGuard, NeedsAdmin } from "./check-access-token.guard";
import { CheckRegistrationTokenService } from "./check-registration-token.service";
import {
    AdminLinkUserIdInput,
    adminLinkUserIdInputCheck,
    RegisterTokenInput,
} from "./dto/RegisterTokenInput";
import {
    RegisterUserInput,
    registerUserInputCheck,
} from "./dto/RegisterUserInput";

@Controller("registration")
export class RegisterController {
    constructor(
        private readonly checkRegistrationTokenService: CheckRegistrationTokenService,
        private readonly loginDataService: UserLoginDataService,
        private readonly userService: LoginUserService,
        private readonly activeLoginService: ActiveLoginService,
        private readonly backendUserSerivce: BackendUserService,
    ) {}

    @Get()
    async getTest(@Body() body) {
        console.log(body);
    }

    @Post("self-register")
    async register(@Body() input: RegisterUserInput & RegisterTokenInput) {
        registerUserInputCheck(input);
        const { loginData, activeLogin } =
            await this.checkRegistrationTokenService.getActiveLoginAndLoginDataForToken(
                input.register_token,
            );
        if (
            (await this.userService.countBy({ username: input.username })) > 0
        ) {
            throw new HttpException(
                "Username is not available anymore",
                HttpStatus.BAD_REQUEST,
            );
        }
        const newUser = await this.backendUserSerivce.createNewUser(
            input,
            false,
        );
        const { loggedInUser } = await this.linkAccountToUser(
            newUser,
            loginData,
            activeLogin,
        );
        return defaultReturn("self-register");
    }

    @Post("self-link")
    @UseGuards(CheckAccessTokenGuard)
    async selfLink(
        @Body() input: RegisterTokenInput,
        @Res({ passthrough: true }) res: Response,
    ) {
        //todo: potentially move to POST user/:id/loginData
        if (!(res.locals.state as ApiStateData).loggedInUser) {
            throw new HttpException("Not logged in.", HttpStatus.UNAUTHORIZED);
        }
        const { loginData, activeLogin } =
            await this.checkRegistrationTokenService.getActiveLoginAndLoginDataForToken(
                input.register_token,
                (res.locals.state as ApiStateData).loggedInUser,
            );
        const { loggedInUser } = await this.linkAccountToUser(
            (res.locals.state as ApiStateData).loggedInUser,
            loginData,
            activeLogin,
        );
        (res.locals.state as ApiStateData).loggedInUser = loggedInUser;
        return defaultReturn("self-link");
    }

    @Post("admin-link")
    @UseGuards(CheckAccessTokenGuard)
    @NeedsAdmin()
    async adminLink(
        @Body() input: RegisterTokenInput & AdminLinkUserIdInput,
        @Res({ passthrough: true }) res: Response,
    ) {
        // requires: admin and specification of user id to link with
        //todo: potentially move to POST user/:id/loginData
        adminLinkUserIdInputCheck(input);
        const linkToUser = await this.userService.findOneBy({
            id: input.user_to_link_to_id,
        });
        if (!linkToUser) {
            throw new HttpException(
                "No user with given user_to_link_to_id",
                HttpStatus.BAD_REQUEST,
            );
        }
        const { loginData, activeLogin } =
            await this.checkRegistrationTokenService.getActiveLoginAndLoginDataForToken(
                input.register_token,
                linkToUser,
            );
        await this.linkAccountToUser(linkToUser, loginData, activeLogin);
        return defaultReturn("admin-link");
    }

    private async linkAccountToUser(
        userToLinkTo: LoginUser,
        loginData: UserLoginData,
        activeLogin: ActiveLogin,
    ): Promise<{ loggedInUser: LoginUser; loginData: UserLoginData }> {
        if (!userToLinkTo) {
            throw new HttpException(
                "Not logged in to any account. Linking not possible. Try logging in or registering",
                HttpStatus.BAD_REQUEST,
            );
        }
        if (loginData.state == LoginState.WAITING_FOR_REGISTER) {
            loginData.state = LoginState.VALID;
        }
        const currentLoginDataUser = await loginData.user;
        if (currentLoginDataUser == null) {
            loginData.user = Promise.resolve(userToLinkTo);
        } else {
            if (currentLoginDataUser.id !== userToLinkTo.id) {
                // Shoud not be rachable as this is already checked in token check
                throw new HttpException(
                    "Login data user did not match logged in user. Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                );
            }
        }
        loginData.expires = null;
        loginData = await this.loginDataService.save(loginData);

        await this.activeLoginService.setActiveLoginExpiration(activeLogin);

        userToLinkTo = await this.userService.findOneBy({
            id: userToLinkTo.id,
        });

        await this.backendUserSerivce.linkAllImsUsersToGropiusUser(
            userToLinkTo,
            loginData,
        );
        return { loggedInUser: userToLinkTo, loginData };
    }
}
