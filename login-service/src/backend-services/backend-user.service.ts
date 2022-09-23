import { Injectable } from "@nestjs/common";
import { GraphqlService } from "src/model/graphql/graphql.service";
import { LoginUser } from "src/model/postgres/LoginUser.entity";
import { UserLoginData } from "src/model/postgres/UserLoginData.entity";
import { UserLoginDataImsUser } from "src/model/postgres/UserLoginDataImsUser.entity";
import { LoginUserService } from "src/model/services/login-user.service";

export interface CreateUserInput {
    username: string;
    displayName: string;
    email?: string;
}

@Injectable()
export class BackendUserService {
    constructor(private readonly graphqlService: GraphqlService, private readonly loginUserService: LoginUserService) {}

    async checkIsUserAdmin(user: LoginUser): Promise<boolean> {
        // todo: adapt once actual query is available
        if (!user.neo4jId) {
            throw new Error("User without neo4jId: " + user.id);
        }
        return (
            true ||
            (
                await this.graphqlService.sdk.checkUserIsAdmin({
                    id: user.neo4jId,
                })
            ).node.id == ""
        );
    }

    async createNewUser(input: CreateUserInput, isAdmin: boolean): Promise<LoginUser> {
        let loginUser = new LoginUser();
        loginUser.username = input.username;
        loginUser.revokeTokensBefore = new Date();
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
            this.loginUserService.remove(loginUser);
            throw err;
        }
        return loginUser;
    }

    async linkOneImsUserToGropiusUser(loginUser: LoginUser, imsUser: UserLoginDataImsUser) {
        if (!loginUser.neo4jId) {
            throw new Error("User without neo4jId: " + loginUser.id);
        }
        const gropiusUserId = loginUser.neo4jId;
        if (!gropiusUserId) {
            throw new Error("Login user has no Gropius user associated");
        }
        const linkResult = await this.graphqlService.sdk.setImsUserLink({
            gropiusUserId,
            imsUserId: imsUser.neo4jId,
        });
    }

    async linkAllImsUsersToGropiusUser(loginUser: LoginUser, loginData: UserLoginData) {
        if (!loginUser.neo4jId) {
            throw new Error("User without neo4jId: " + loginUser.id);
        }
        const gropiusUserId = loginUser.neo4jId;
        if (!gropiusUserId) {
            throw new Error("Login user has no Gropius user associated");
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
                    (result.status == "fulfilled" && !result.value.updateIMSUser.imsuser.id),
            )
            .map((result) => (result.status == "fulfilled" ? result.value : result.reason));
        console.warn("Failures during linking ims user and Gropius user:", failedLinks);
    }
}
