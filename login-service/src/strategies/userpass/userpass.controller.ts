import {
    All,
    Body,
    Controller,
    Get,
    HttpException,
    HttpStatus,
    Next,
    Param,
    Post,
    Req,
    Res,
    UseGuards,
} from "@nestjs/common";
import { AuthGuard, PassportStrategy } from "@nestjs/passport";
import * as passport from "passport";
import { LoginUser } from "src/model/postgres/LoginUser";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { LoginUserService } from "src/model/services/login-user.service";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { GenericStrategyController } from "../generic-strategy.controller";
import { StrategiesService } from "../strategies.service";
import { CreateStrategyInstanceInput } from "../_inputs/CreateStrategyInstanceInput";
import { UserpassLoginInput } from "./UserpassLoginInput";

@Controller("userpass")
export class StrategyUserpassController extends GenericStrategyController {
    constructor(
        strategiesService: StrategiesService,
        private readonly userService: LoginUserService,
    ) {
        super("userpass", strategiesService);
    }

    private checkUserpassLoginInput(input: UserpassLoginInput) {
        if (!input) {
            throw new HttpException(
                "Body expected containing username and password",
                HttpStatus.BAD_REQUEST,
            );
        }
        if (
            typeof input.username !== "string" ||
            typeof input.password !== "string"
        ) {
            throw new HttpException(
                "Username and password must be given as strings",
                HttpStatus.BAD_REQUEST,
            );
        }
    }

    @All(":id/login")
    async userpassLogin(
        @Param("id") id: string,
        @Req() req,
        @Res() res,
        @Next() next,
    ): Promise<string> {
        //this.checkUserpassLoginInput(body);
        const instance = await this.idToStrategyInstance(id);
        const passportStrategy =
            this.strategiesService.getPassportStrategyInstanceFor(instance);
        console.log("Login witn userpass " + id);
        passport.authenticate(passportStrategy)(req, res, (a) => {
            console.log("next", a);
            return next(a);
        });
        return "Login";
    }

    @All(":id/register")
    async userpassRegister(
        @Param("id") id: string,
        @Body() body: UserpassLoginInput,
    ): Promise<string> {
        const instance = await this.idToStrategyInstance(id);
        const newUser = new LoginUser();
        newUser.username = "test-user";
        newUser.displayName = "Test User";
        newUser.neo4jId = "1234";
        this.userService.save(newUser);
        console.log("Register witn userpass " + id);
        return "Registered";
    }
}
