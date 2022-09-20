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
import { ApiOkResponse } from "@nestjs/swagger";
import { Response } from "express";
import { BackendUserService } from "src/backend-services/backend-user.service";
import { defaultReturn } from "src/defaultReturn";
import { AuthClient } from "src/model/postgres/AuthClient.entity";
import { LoginUser } from "src/model/postgres/LoginUser.entity";
import { UserLoginData } from "src/model/postgres/UserLoginData.entity";
import { AuthClientService } from "src/model/services/auth-client.service";
import { LoginUserService } from "src/model/services/login-user.service";
import { UserLoginDataService } from "src/model/services/user-login-data.service";
import { ApiStateData } from "./ApiStateData";
import { CheckAccessTokenGuard, NeedsAdmin } from "./check-access-token.guard";
import { CreateOrUpdateAuthClientInput } from "./dto/create-update-auth-client.dto";
import { IsAdminInput, isAdminInputCheck } from "./dto/IsAdminInput";
import {
    RegisterUserInput,
    registerUserInputCheck,
} from "./dto/self-register-user.dto";

@Controller("client")
@UseGuards(CheckAccessTokenGuard)
export class AuthClientController {
    constructor(
        private readonly userService: LoginUserService,
        private readonly backendUserSerice: BackendUserService,
        private readonly loginDataSerive: UserLoginDataService,
        private readonly authClientService: AuthClientService,
    ) {}

    @Get()
    @NeedsAdmin()
    async listAllAuthClients(): Promise<AuthClient[]> {
        return this.authClientService.find();
    }

    @Get(":id")
    @NeedsAdmin()
    async getOneAuthClient(@Param("id") id: string) {
        const authClient = await this.authClientService.findOneBy({ id });
        if (!authClient) {
            throw new HttpException(
                "Auth client with given id not found",
                HttpStatus.NOT_FOUND,
            );
        }
        return {
            ...authClient.toJSON(),
            clientSecrets: authClient.getSecretsShortedAndFingerprint(),
        };
    }

    @Post()
    @NeedsAdmin()
    async createNewAuthClient(
        @Body() input: CreateOrUpdateAuthClientInput,
    ): Promise<AuthClient> {
        CreateOrUpdateAuthClientInput.check(input);
        const newClient = new AuthClient();
        newClient.redirectUrls = [];
        if (input.redirectUrls) {
            for (const url of input.redirectUrls) {
                newClient.redirectUrls.push(url);
            }
        }
        if (input.isValid != undefined) {
            newClient.isValid = input.isValid;
        } else {
            newClient.isValid = true;
        }
        if (input.requiresSecret != undefined) {
            newClient.requiresSecret = input.requiresSecret;
        } else {
            newClient.requiresSecret = true;
        }

        return this.authClientService.save(newClient);
    }

    /**
     * Updates the auth client with the given ID.
     * Only parameter given in the input will be changed.
     *
     * @param id The uuid string of a valid auth client
     * @param input The input data dto
     * @returns The auth client with the updated data
     */
    @Put(":id")
    @NeedsAdmin()
    @ApiOkResponse({
        description: "The auth client was successfully updated",
        type: AuthClient,
    })
    async editAuthClient(
        @Param("id") id: string,
        @Body() input: CreateOrUpdateAuthClientInput,
    ): Promise<AuthClient> {
        CreateOrUpdateAuthClientInput.check(input);

        const authClient = await this.authClientService.findOneBy({ id });
        if (!authClient) {
            throw new HttpException(
                "Auth client with given id not found",
                HttpStatus.NOT_FOUND,
            );
        }

        if (input.redirectUrls) {
            authClient.redirectUrls = [];
            for (const url of input.redirectUrls) {
                authClient.redirectUrls.push(url);
            }
        }
        if (input.isValid != undefined) {
            authClient.isValid = input.isValid;
        }
        if (input.requiresSecret != undefined) {
            authClient.requiresSecret = input.requiresSecret;
        }

        return this.authClientService.save(authClient);
    }

    @Delete(":id")
    @NeedsAdmin()
    async deleteAuthClient(
        @Param("id") id: string,
    ): Promise<ReturnType<typeof defaultReturn>> {
        const authClient = await this.authClientService.findOneBy({ id });
        if (!authClient) {
            throw new HttpException(
                "Auth client with given id not found",
                HttpStatus.NOT_FOUND,
            );
        }

        await this.authClientService.remove(authClient);

        return defaultReturn("delete-client");
    }

    @Get(":id/clientSecret")
    @NeedsAdmin()
    async getClientSecrets(
        @Param("id") id: string,
    ): Promise<
        ReturnType<typeof AuthClient.prototype.getSecretsShortedAndFingerprint>
    > {
        const authClient = await this.authClientService.findOneBy({ id });
        if (!authClient) {
            throw new HttpException(
                "Auth client with given id not found",
                HttpStatus.NOT_FOUND,
            );
        }

        return authClient.getSecretsShortedAndFingerprint();
    }

    @Post(":id/clientSecret")
    @NeedsAdmin()
    async createClientSecret(
        @Param("id") id: string,
    ): Promise<ReturnType<typeof AuthClient.prototype.addSecret>> {
        const authClient = await this.authClientService.findOneBy({ id });
        if (!authClient) {
            throw new HttpException(
                "Auth client with given id not found",
                HttpStatus.NOT_FOUND,
            );
        }

        const result = await authClient.addSecret();

        await this.authClientService.save(authClient);

        return result;
    }

    @Delete(":id/clientSecret/:fingerprint")
    @NeedsAdmin()
    async deleteClientSecret(
        @Param("id") id: string,
        @Param("fingerprint") fingerprint: string,
    ): Promise<ReturnType<typeof defaultReturn>> {
        if (!fingerprint) {
            throw new HttpException(
                "Fingerprint of secret to delete expected",
                HttpStatus.BAD_REQUEST,
            );
        }

        const authClient = await this.authClientService.findOneBy({ id });
        if (!authClient) {
            throw new HttpException(
                "Auth client with given id not found",
                HttpStatus.NOT_FOUND,
            );
        }

        const allSecrets = authClient.getFullHashesPlusCensoredAndFingerprint();
        const secretsToKeep = allSecrets
            .filter((entry) => entry.fingerprint != fingerprint)
            .map((entry) => entry.secret);
        if (allSecrets.length <= secretsToKeep.length) {
            throw new HttpException(
                "No secret with given fingerprint",
                HttpStatus.NOT_FOUND,
            );
        }

        authClient.clientSecrets = secretsToKeep;
        await this.authClientService.save(authClient);
        return defaultReturn("delete-clientSecret");
    }
}
