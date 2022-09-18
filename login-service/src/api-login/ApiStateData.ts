import { LoginUser } from "src/model/postgres/LoginUser";

export interface ApiStateData {
    loggedInUser: LoginUser;
}
