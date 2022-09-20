import {
    Body,
    Controller,
    Delete,
    Get,
    HttpException,
    HttpStatus,
    Param,
    Post,
    Put,
    Res,
    UseGuards,
} from "@nestjs/common";
import { Response } from "express";
import { BackendUserService } from "src/backend-services/backend-user.service";
import { defaultReturn } from "src/defaultReturn";
import { LoginUser } from "src/model/postgres/LoginUser.entity";
import { UserLoginData } from "src/model/postgres/UserLoginData.entity";
import { LoginUserService } from "src/model/services/login-user.service";
import { UserLoginDataService } from "src/model/services/user-login-data.service";
import { ApiStateData } from "./ApiStateData";
import { CheckAccessTokenGuard, NeedsAdmin } from "./check-access-token.guard";
import { IsAdminInput, isAdminInputCheck } from "./dto/IsAdminInput";
import {
    RegisterUserInput,
    registerUserInputCheck,
} from "./dto/self-register-user.dto";

@Controller("user")
@UseGuards(CheckAccessTokenGuard)
export class UsersController {
    constructor(
        private readonly userService: LoginUserService,
        private readonly backendUserSerice: BackendUserService,
        private readonly loginDataSerive: UserLoginDataService,
    ) {}

    @Get()
    async listAllUsers(): Promise<LoginUser[]> {
        return this.userService.find();
    }

    @Get("self")
    async getOwnUser(
        @Res({ passthrough: true }) res: Response,
    ): Promise<LoginUser> {
        return (res.locals.state as ApiStateData).loggedInUser;
    }

    @Get(":id")
    async getOneUser(@Param("id") id: string): Promise<LoginUser> {
        const user = await this.userService.findOneBy({ id });
        if (!user) {
            throw new HttpException(
                "User with given id not found",
                HttpStatus.NOT_FOUND,
            );
        }
        return user;
    }

    @Post()
    @NeedsAdmin()
    async createNewUser(
        @Body() input: RegisterUserInput & IsAdminInput,
    ): Promise<LoginUser> {
        registerUserInputCheck(input);
        isAdminInputCheck(input);
        return this.backendUserSerice.createNewUser(input, input.isAdmin);
    }

    @Put(":id")
    async editUser(
        @Param("id") id: string,
        @Body() input: Partial<RegisterUserInput & IsAdminInput>,
    ): Promise<LoginUser> {
        throw new HttpException(
            "Needs to be discussed with backend who stores what and what changes where",
            HttpStatus.NOT_IMPLEMENTED,
        );
        const user = await this.userService.findOneBy({ id });
        if (!user) {
            throw new HttpException(
                "User with given id not found",
                HttpStatus.NOT_FOUND,
            );
        }
    }

    @Delete(":id")
    async deleteUser(
        @Param("id") id: string,
    ): Promise<ReturnType<typeof defaultReturn>> {
        throw new HttpException(
            "Yeah, we are not even gonna talk about this.",
            HttpStatus.NOT_IMPLEMENTED,
        );
        return defaultReturn("delete-user");
    }

    @Get(":id/loginData")
    async getLoginDataForUser(
        @Param("id") id: string,
        @Res({ passthrough: true }) res: Response,
    ): Promise<UserLoginData[]> {
        if (!id) {
            throw new HttpException("Id must be given", HttpStatus.BAD_REQUEST);
        }
        const loggedInUser = (res.locals.state as ApiStateData).loggedInUser;
        if (id == "self") {
            id = loggedInUser.id;
        }
        if (
            id != loggedInUser.id &&
            !this.backendUserSerice.checkIsUserAdmin(loggedInUser)
        ) {
            throw new HttpException(
                "No permission to access others login data if not admin",
                HttpStatus.UNAUTHORIZED,
            );
        }
        return this.loginDataSerive.findBy({
            user: {
                id: loggedInUser.id,
            },
        });
    }
}
