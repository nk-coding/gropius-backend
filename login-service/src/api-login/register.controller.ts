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
import { ApiBearerAuth } from "@nestjs/swagger";
import { Response } from "express";
import { BackendUserService } from "src/backend-services/backend-user.service";
import { TokenService } from "src/backend-services/token.service";
import { DefaultReturn } from "src/defaultReturn";
import { ActiveLogin } from "src/model/postgres/ActiveLogin.entity";
import { LoginUser } from "src/model/postgres/LoginUser.entity";
import {
    LoginState,
    UserLoginData,
} from "src/model/postgres/UserLoginData.entity";
import { ActiveLoginService } from "src/model/services/active-login.service";
import { LoginUserService } from "src/model/services/login-user.service";
import { UserLoginDataService } from "src/model/services/user-login-data.service";
import { ApiStateData } from "./ApiStateData";
import { CheckAccessTokenGuard, NeedsAdmin } from "./check-access-token.guard";
import { CheckRegistrationTokenService } from "./check-registration-token.service";
import {
    AdminLinkUserInput,
    RegistrationTokenInput,
} from "./dto/link-user.dto";
import { SelfRegisterUserInput } from "./dto/user-inputs.dto";

/**
 * Controller for handling slef registration of new users as well as linking of existing users to new loginData
 */
@Controller("registration")
export class RegisterController {
    constructor(
        private readonly checkRegistrationTokenService: CheckRegistrationTokenService,
        private readonly loginDataService: UserLoginDataService,
        private readonly userService: LoginUserService,
        private readonly activeLoginService: ActiveLoginService,
        private readonly backendUserSerivce: BackendUserService,
    ) {}

    /**
     * Given user data and a registration token, this will create a new user for the registration.
     * The user will also be created in the backend.
     *
     * For the creation to succeed, the registration token and the registration may not be expired yet.
     *
     * @param input The input data for creating a new user
     * @returns The Default Return.
     */
    @Post("self-register")
    async register(
        @Body() input: SelfRegisterUserInput,
    ): Promise<DefaultReturn> {
        SelfRegisterUserInput.check(input);
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
        return new DefaultReturn("self-register");
    }

    @Post("self-link")
    @UseGuards(CheckAccessTokenGuard)
    @ApiBearerAuth()
    async selfLink(
        @Body() input: RegistrationTokenInput,
        @Res({ passthrough: true }) res: Response,
    ): Promise<DefaultReturn> {
        RegistrationTokenInput.check(input);
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
        return new DefaultReturn("self-link");
    }

    @Post("admin-link")
    @UseGuards(CheckAccessTokenGuard)
    @NeedsAdmin()
    @ApiBearerAuth()
    async adminLink(
        @Body() input: AdminLinkUserInput,
        @Res({ passthrough: true }) res: Response,
    ): Promise<DefaultReturn> {
        // requires: admin and specification of user id to link with
        //todo: potentially move to POST user/:id/loginData
        AdminLinkUserInput.check(input);
        const linkToUser = await this.userService.findOneBy({
            id: input.userIdToLink,
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
        return new DefaultReturn("admin-link");
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
