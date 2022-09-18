import { Injectable } from "@nestjs/common";
import { GraphqlService } from "src/model/graphql/graphql.service";
import { LoginUser } from "src/model/postgres/LoginUser";

@Injectable()
export class BackendUserService {
    constructor(private readonly graphqlService: GraphqlService) {}

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
}
