import { LoginUser } from "src/model/postgres/LoginUser.entity";

export interface ApiStateData {
    loggedInUser: LoginUser;
}
