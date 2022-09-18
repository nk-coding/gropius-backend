import { Injectable } from "@nestjs/common";
import { RegisterUserInput } from "src/api-login/dto/RegisterUserInput";
import { GraphqlService } from "src/model/graphql/graphql.service";
import { LoginUser } from "src/model/postgres/LoginUser";
import { UserLoginData } from "src/model/postgres/UserLoginData";
import { LoginUserService } from "src/model/services/login-user.service";

@Injectable()
export class BackendUserService {
    constructor(
        private readonly graphqlService: GraphqlService,
        private readonly loginUserService: LoginUserService,
    ) {}

    async checkIsUserAdmin(user: LoginUser): Promise<boolean> {
        // todo: adapt once actual query is available
        return (
            true ||
            (
                await this.graphqlService.sdk.checkUserIsAdmin({
                    id: user.neo4jId,
                })
            ).node.id == ""
        );
    }

    async createNewUser(
        input: RegisterUserInput,
        isAdmin: boolean,
    ): Promise<LoginUser> {
        let loginUser = new LoginUser();
        loginUser.username = input.username;
        loginUser = await this.loginUserService.save(loginUser);
        try {
            const backendUser = await this.graphqlService.sdk.createNewUser({
                input: {
                    username: input.username,
                    displayName: input.displayName,
                    email: input.email,
                    isAdmin,
                },
            });
            if (!backendUser.createGropiusUser.gropiusUser.id) {
                throw new Error("No Id returned on mutation. Assuming error");
            }
            loginUser.neo4jId = backendUser.createGropiusUser.gropiusUser.id;
            loginUser = await this.loginUserService.save(loginUser);
        } catch (err) {
            this.loginUserService.delete(loginUser.id);
            throw err;
        }
        return loginUser;
    }

    async linkImsUserToGropiusUser(
        loginUser: LoginUser,
        loginData: UserLoginData,
    ) {
        const gropiusUserId = loginUser.neo4jId;
        if (!gropiusUserId) {
            throw new Error("Login user has no gropius user associated");
        }
        const imsUsers = await loginData.imsUsers;
        const linkResults = await Promise.allSettled(
            imsUsers.map((user) =>
                this.graphqlService.sdk.setImsUserLink({
                    gropiusUserId,
                    imsUserId: user.neo4jId,
                }),
            ),
        );
        const failedLinks = linkResults
            .filter(
                (result) =>
                    result.status == "rejected" ||
                    (result.status == "fulfilled" &&
                        !result.value.updateIMSUser.imsuser.id),
            )
            .map((result) =>
                result.status == "fulfilled" ? result.value : result.reason,
            );
        console.warn(
            "Failures during linking ims user and gropius user:",
            failedLinks,
        );
    }
}
