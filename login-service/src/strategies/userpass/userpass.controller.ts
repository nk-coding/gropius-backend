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
    Query,
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
import { UserpassLoginInput } from "../userpass/UserpassLoginInput";
import { AuthClientService } from "src/model/services/auth-client.service";
import { AuthFunction } from "../AuthResult";

@Controller("userpass")
export class StrategyUserpassController extends GenericStrategyController {
    constructor(
        strategiesService: StrategiesService,
        private readonly userService: LoginUserService,
        private readonly authClientService: AuthClientService,
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

    @Get(":id/authorize/login")
    async userpassLogin(
        @Param("id") id: string,
        @Query() query,
        @Body() body,
        @Req() req,
        @Res() res,
        @Next() next,
    ): Promise<string> {
        //this.checkUserpassLoginInput(body);
        const instance = await this.idToStrategyInstance(id);
        const strategy = await this.strategiesService.getStrategyByName(
            this.strategyName,
        );
        const params = { ...query, ...body };
        const clientId = params.client_id;
        const client = await this.authClientService.findOneBy({ id: clientId });
        if (!clientId || !client) {
            throw new HttpException(
                "client_id not given or not a valid client id",
                HttpStatus.BAD_REQUEST,
            );
        }
        if (params.response_type !== "code") {
            throw new HttpException(
                "response_type must be set to 'code'. Other flow types not supported",
                HttpStatus.NOT_IMPLEMENTED,
            );
        }
        if (
            !!params.redirect_uri &&
            !client.redirectUrls.includes(params.redirect_uri)
        ) {
            throw new HttpException(
                "Given redirect URI not valid for this client.",
                HttpStatus.BAD_REQUEST,
            );
        }
        const redirect = params.redirect_uri || client.redirectUrls[0];
        const state = params.state;

        const result = await strategy.performAuth(
            instance,
            { function: AuthFunction.LOGIN, state },
            req,
            res,
        );

        if (result.result == null) {
            console.log("Result null, returning info");
            throw new HttpException(
                result.info.message,
                HttpStatus.BAD_REQUEST,
            );
        } else {
            console.log("Auth successfull");
            return "Logged in as " + result.result.user.displayName;
        }

        /*const passportStrategy =
            this.strategiesService.getPassportStrategyInstanceFor(instance);
        console.log("Login witn userpass " + id);
        passport.authenticate(
            passportStrategy,
            { session: false },
            (err, user, info) => {
                console.log("passport callback", err, user, info);
            },
        )(req, res, (a) => {
            console.log("next", a);
            return next(a);
        });
        return "Login";*/
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
